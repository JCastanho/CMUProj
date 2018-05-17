package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetCorrectAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private byte[] correctAnswers;
    private byte[] time;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public GetCorrectAnswersResponse(int correctAnswers, int time) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.correctAnswers= encryption.encrypt(Integer.toString(correctAnswers).getBytes("UTF-8"));
        this.time= encryption.encrypt(Integer.toString(time).getBytes("UTF-8"));

        String pureNonce = "GetCorrectAnswersResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + correctAnswers + time;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public int getCorrectAnswers() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.correctAnswers),"UTF-8"));
    }

    public int getTime() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
    }

    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce), "UTF-8");

        String replicateSignature = nonce + this.getCorrectAnswers() + this.getTime();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"), signature);
    }
}
