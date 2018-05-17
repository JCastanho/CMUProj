package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class LogoutResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;

	// Data
	private byte[] id;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public LogoutResponse(int id) throws UnsupportedEncodingException, SignatureException {

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));

		String pureNonce = "LogoutResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + id;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public int getID() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
	}


	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getID();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
