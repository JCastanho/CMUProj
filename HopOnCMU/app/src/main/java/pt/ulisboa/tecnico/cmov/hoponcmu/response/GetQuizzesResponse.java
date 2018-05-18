package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetQuizzesResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;

	// Data
	private ArrayList<byte[]> questions;
	private ArrayList<ArrayList<byte[]>> answers;
	private byte[] location;

	// Security
	private byte[] nonce;
	private byte[] signature;

	public GetQuizzesResponse(ArrayList<String> questions, ArrayList<ArrayList<String>> answers, String location) throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		this.location= encryption.encrypt(location.getBytes("UTF-8"));

		ArrayList<byte[]> finalQuestions = new ArrayList<byte[]>();
		for (String question: questions) {
			finalQuestions.add(encryption.encrypt(question.getBytes("UTF-8")));
		}
		this.questions=finalQuestions;

		ArrayList<ArrayList<byte[]>> finalAnswers = new ArrayList<ArrayList<byte[]>>();
		for (ArrayList<String> answerList: answers) {
			ArrayList<byte[]> innerAnswers = new ArrayList<byte[]>();
			for (String answer: answerList) {
				innerAnswers.add(encryption.encrypt(answer.getBytes("UTF-8")));
			}
			finalAnswers.add(innerAnswers);
		}
		this.questions=finalQuestions;

		String pureNonce = "GetQuizzesResponse" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
		this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

		String pureSignature = pureNonce + questions.toString() + answers.toString() + location;

		this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
	}

	public ArrayList<String> getQuestion() throws UnsupportedEncodingException {

		ArrayList<String> pureQuestions = new ArrayList<String>();

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		for (byte[] question: this.questions) {
			pureQuestions.add(new String(encryption.decrypt(question),"UTF-8"));
		}

		return pureQuestions;
	}

	public ArrayList<ArrayList<String>> getAnswers() throws UnsupportedEncodingException {

		ArrayList<ArrayList<String>> pureAnswers = new ArrayList<ArrayList<String>>();

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		for (ArrayList<byte[]> innerAnswer: this.answers) {
			ArrayList<String> pureInnerAnswers = new ArrayList<String>();
			for (byte[] answer: innerAnswer) {
				pureInnerAnswers.add(new String(encryption.decrypt(answer), "UTF-8"));
			}
			pureAnswers.add(pureInnerAnswers);
		}

		return pureAnswers;
	}

	public String getLocation() throws UnsupportedEncodingException {

		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		return new String(encryption.decrypt(this.location),"UTF-8");
	}



	public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
		EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

		String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

		String replicateSignature = nonce + this.getQuestion().toString() + this.getAnswers().toString() + this.getLocation();

		return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
	}

}
