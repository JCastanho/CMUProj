package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class SendLocationCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    // Data
    private byte[] location;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public SendLocationCommand(String location) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.location= encryption.encrypt(location.getBytes("UTF-8"));

        String pureNonce = "SendLocationCommand" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + location;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public String getLocation() throws UnsupportedEncodingException {
        if(this.verified) {
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return new String(encryption.decrypt(this.location), "UTF-8");
        }
        return "NOK";
    }

    private String getInternalLocation() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return new String(encryption.decrypt(this.location),"UTF-8");
    }

    public byte[] getNonce (){
        return this.nonce;
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String replicateSignature = nonce + this.getInternalLocation();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
