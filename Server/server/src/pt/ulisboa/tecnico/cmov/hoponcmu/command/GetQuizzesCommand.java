package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetQuizzesCommand implements Command {
	
    private static final long serialVersionUID = -8807331723807741905L;
    private int id;

	// Data
	private byte[] location;
	private byte[] page;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public GetQuizzesCommand(int id) {
		this.id=id;
	}

	public GetQuizzesCommand(int id, String location, int page) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		this.location= encryption.encrypt(location.getBytes("UTF-8"));
		this.page=encryption.encrypt((Integer.toString(page)).getBytes("UTF-8"));

		String pureNonce = "CreateUserCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + location + page;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public int getId(){
		return this.id;
	}

	public String getLocation() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		return new String(encryption.decrypt(this.location),"UTF-8");
	}

	public int getPage() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.page),"UTF-8"));
	}

	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getLocation() + this.getPage();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
