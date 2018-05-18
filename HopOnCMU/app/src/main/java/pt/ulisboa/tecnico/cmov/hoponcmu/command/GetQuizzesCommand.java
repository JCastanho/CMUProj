package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetQuizzesCommand implements Command {

	private static final long serialVersionUID = -8807331723807741905L;

	// Data
	private byte[] id;
	private byte[] location;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public GetQuizzesCommand(int id, String location) throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));
		this.location= encryption.encrypt(location.getBytes("UTF-8"));

		String pureNonce = "GetQuizzesCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + id + location;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public int getId() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
	}

	public String getLocation() throws UnsupportedEncodingException {

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.location),"UTF-8");
	}

	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getId() + this.getLocation();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
