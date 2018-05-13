package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class SignupResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

	// Data
	private byte[] success;

	// Security
	private byte[] nonce;
	private byte[] signature;


	public SignupResponse(String success) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");
    	
		this.success= encryption.encrypt(success.getBytes("UTF-8"));

		String pureNonce = "SignupResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + success;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public Boolean getAuthorization() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");
    	
		String pureText = new String(encryption.decrypt(this.success),"UTF-8");

		return pureText.equals("OK") ? true: false;
	}

	private String getSuccess() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");
    	
		return new String(encryption.decrypt(this.success),"UTF-8");
	}

	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");
    	
		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getSuccess();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
