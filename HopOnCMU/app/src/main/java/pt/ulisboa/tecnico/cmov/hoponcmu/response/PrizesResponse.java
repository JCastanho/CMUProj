package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class PrizesResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private byte[] res;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public PrizesResponse(String res) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.res= encryption.encrypt(res.getBytes("UTF-8"));

        String pureNonce = "PrizesResponse" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + res;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public String getRes() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return new String(encryption.decrypt(this.res),"UTF-8");
        }
        return "NOK";
    }

    private String getInternalRes() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return new String(encryption.decrypt(this.res),"UTF-8");
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String replicateSignature = nonce + this.getInternalRes();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
