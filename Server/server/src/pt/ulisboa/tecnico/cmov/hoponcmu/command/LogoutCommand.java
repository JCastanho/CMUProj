package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
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
	private transient boolean verified = false;

	public LogoutCommand(int id) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		this.token= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));

		String pureNonce = "LogoutCommand" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + id;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public Integer getToken() throws UnsupportedEncodingException {
		if(this.verified){
	        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

			return Integer.parseInt(new String(encryption.decrypt(this.token),"UTF-8"));
		}
		return -1;
	}

	private Integer getInternalToken() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.token),"UTF-8"));
	}

	public byte[] getNonce (){
		return this.nonce;
	}

	public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
		if(nonce.equals("NOK")){
			this.verified = false;
		}

        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

		String replicateSignature = nonce + this.getInternalToken();

		this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
