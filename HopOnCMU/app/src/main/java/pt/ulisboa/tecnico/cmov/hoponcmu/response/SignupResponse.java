package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
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
	private transient boolean verified = false;

	public SignupResponse(String success) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.success= encryption.encrypt(success.getBytes("UTF-8"));

		String pureNonce = "SignupResponse" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + success;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public Boolean getAuthorization() throws UnsupportedEncodingException {
		String pureText = "";
		if(verified){
			EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

			pureText = new String(encryption.decrypt(this.success),"UTF-8");

			Log.d("este", pureText);
			return pureText.equals("OK") ? true: false;
		}
		return false;
	}

	public byte[] getNonce() {
		return nonce;
	}

	private String getSuccess() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.success),"UTF-8");

	}

	public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
		if(nonce.equals("NOK")){
			this.verified = false;
		}

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String replicateSignature = nonce + this.getSuccess();
		Log.d("test", replicateSignature);

		this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
