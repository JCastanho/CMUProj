package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class PrizesResponse implements Response{
    
    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private byte[] res;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public PrizesResponse(String res) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.res= encryption.encrypt(res.getBytes("UTF-8"));

        String pureNonce = "PrizesResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + res;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public String getRes() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return new String(encryption.decrypt(this.res),"UTF-8");
    }

    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getRes();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
