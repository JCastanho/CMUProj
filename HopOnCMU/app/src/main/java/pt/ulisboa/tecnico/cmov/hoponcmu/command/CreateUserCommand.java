package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class CreateUserCommand implements Command {

	private static final long serialVersionUID = -8807331723807741905L;

	// Data
	private byte[] username;
	private byte[] code;

	// Security
	private byte[] nonce;
	private byte[] signature;
	private transient boolean verified = false;


	public CreateUserCommand(String username, String code) throws UnsupportedEncodingException, SignatureException, ParseException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.username= encryption.encrypt(username.getBytes("UTF-8"));
		this.code=encryption.encrypt(code.getBytes("UTF-8"));

		String pureNonce = "CreateUserCommand" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + username + code;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}
	
	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public String getUsername() throws UnsupportedEncodingException {
		if(this.verified){
			EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

			return new String(encryption.decrypt(this.username),"UTF-8");
		}
		return "NOK";
	}

	public String getCode() throws UnsupportedEncodingException {
		if(this.verified){
			EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

			return new String(encryption.decrypt(this.code),"UTF-8");
		}
		return "NOK";
	}

	public byte[] getNonce (){
		return this.nonce;
	}

	private String getInternalUsername() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.username),"UTF-8");
	}

	private String getInternalCode() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.code),"UTF-8");
	}

	public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
		if(nonce.equals("NOK")){
			this.verified = false;
		}

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String replicateSignature = nonce + this.getInternalUsername() + this.getInternalCode();

		this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
