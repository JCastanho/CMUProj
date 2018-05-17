package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetCorrectAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private byte[] correct;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public GetCorrectAnswersResponse(int correct) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.correct= encryption.encrypt(Integer.toString(correct).getBytes("UTF-8"));

        String pureNonce = "GetCorrectAnswersResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + correct;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public int getCorrect() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.correct),"UTF-8"));
    }

    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getCorrect();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
