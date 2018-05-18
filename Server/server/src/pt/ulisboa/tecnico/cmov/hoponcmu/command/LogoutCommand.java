package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class LogoutCommand implements Command {

	private static final long serialVersionUID = -8807331723807741905L;
	
	// Data
	private byte[] token;

	// Security
	private byte[] nonce;
	private byte[] signature;
	
	public LogoutCommand(int id) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		this.token= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));

		String pureNonce = "LogoutCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + id;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public Integer getToken() throws NumberFormatException, UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.token),"UTF-8"));
	}

	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getToken();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
