package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetQuizzesResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
	private byte[] question;
	private ArrayList<byte[]> answers;

	private byte[] page;
	private byte[] size;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public GetQuizzesResponse(String question, ArrayList<String> answers, int page, int size) throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.question= encryption.encrypt(question.getBytes("UTF-8"));
		ArrayList<byte[]> finalAnswers = new ArrayList<byte[]>();
		for (String answer: answers) {
			finalAnswers.add(encryption.encrypt(answer.getBytes("UTF-8")));
		}
		this.answers=finalAnswers;

		this.size=encryption.encrypt((Integer.toString(size)).getBytes("UTF-8"));
		this.page=encryption.encrypt((Integer.toString(page)).getBytes("UTF-8"));

		String pureNonce = "CreateUserCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + question + answers.toString() + page + size;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public String getQuestion() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.question),"UTF-8");
	}

	public ArrayList<String> getAnswers() throws UnsupportedEncodingException {

		ArrayList<String> pureAnswers = new ArrayList<String>();

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		for (byte[] answer: this.answers) {
			pureAnswers.add(new String(encryption.decrypt(answer),"UTF-8"));
		}

		return pureAnswers;
	}

	public int getPage() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.page),"UTF-8"));
	}

	public int getSize() throws UnsupportedEncodingException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return Integer.parseInt(new String(encryption.decrypt(this.size),"UTF-8"));
	}



	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getQuestion() + this.getAnswers().toString() + this.getPage() + this.getSize();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}
}
