package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class LoginCommand implements Command {
	
	private static final long serialVersionUID = -8807331723807741905L;

	// Data
	private byte[] username;
	private byte[] code;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public LoginCommand(String username, String code) throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.username= encryption.encrypt(username.getBytes("UTF-8"));
		this.code=encryption.encrypt(code.getBytes("UTF-8"));

		String pureNonce = "LoginCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + username + code;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}
	
	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public String getUsername() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.username),"UTF-8");
	}

	public String getCode() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.code),"UTF-8");
	}

	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getUsername() + this.getCode();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"), signature);
	}
}
