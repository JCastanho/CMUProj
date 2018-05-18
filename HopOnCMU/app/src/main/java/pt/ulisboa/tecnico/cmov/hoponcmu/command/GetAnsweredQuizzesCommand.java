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
    private transient boolean verified = false;

    public GetAnsweredQuizzesCommand(int id) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));

        String pureNonce = "GetAnsweredQuizzesCommand" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler chi){
        return chi.handle(this);
    }

    public int getId() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
        }
        return -1;
    }

    public byte[] getNonce (){
        return this.nonce;
    }

    private int getInternalId() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String replicateSignature = nonce + this.getInternalId();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
