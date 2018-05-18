package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetAnsweredQuizzesCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    // Data
    private byte[] id;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public GetAnsweredQuizzesCommand(int id) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));

        String pureNonce = "GetAnsweredQuizzesCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler chi){
        return chi.handle(this);
    }



    public int getId() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }


    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getId();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
