package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class LoginResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

	// Data
	private byte[] id;

	// Security
	private byte[] nonce;
	private byte[] signature;
	private transient boolean verified = false;

	public LoginResponse(int id) throws UnsupportedEncodingException, SignatureException {

		String strId = Integer.toString(id);

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		this.id= encryption.encrypt(strId.getBytes("UTF-8"));

		String pureNonce = "LoginResponse" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + id;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public int getID() throws UnsupportedEncodingException {
		if(this.verified){
	    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

			return new Integer(new String(encryption.decrypt(this.id),"UTF-8"));
		}
		return -1;
	}

	private int getInternalID() throws UnsupportedEncodingException {

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		return new Integer(new String(encryption.decrypt(this.id),"UTF-8"));
	}


	public byte[] getNonce (){
		return this.nonce;
	}


	public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
		if(nonce.equals("NOK")){
			this.verified = false;
		}

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		String replicateSignature = nonce + this.getInternalID();

		this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
