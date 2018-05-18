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
    private transient boolean verified = false;

    public GetCorrectAnswersResponse(int correctAnswers, int time) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.correctAnswers= encryption.encrypt(Integer.toString(correctAnswers).getBytes("UTF-8"));
        this.time= encryption.encrypt(Integer.toString(time).getBytes("UTF-8"));

        String pureNonce = "GetCorrectAnswersResponse" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + correctAnswers + time;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public int getCorrectAnswers() throws UnsupportedEncodingException {
        if(this.verified){
        	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.correctAnswers),"UTF-8"));
        }
        return -1;
    }

    public int getTime() throws UnsupportedEncodingException {
        if(this.verified){
        	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
        }
        return -1;
    }

    public byte[] getNonce() {
        return nonce;
    }

    private int getInternalCorrectAnswers() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.correctAnswers),"UTF-8"));
    }

    private int getInternalTime() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String replicateSignature = nonce + this.getInternalCorrectAnswers() + this.getInternalTime();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
