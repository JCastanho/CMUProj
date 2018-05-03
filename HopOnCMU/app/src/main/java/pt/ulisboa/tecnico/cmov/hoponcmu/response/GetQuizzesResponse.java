package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.ArrayList;

public class GetQuizzesResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private String question;
	private ArrayList<String> answers;

	private int page;
	private int size;

	public GetQuizzesResponse(String question, ArrayList<String> answers, int page, int size){
		this.question = question;
		this.answers = answers;
		this.page = page;
		this.size = size;
	}

	public String getQuestion() {
		return this.question;
	}

	public ArrayList<String> getAnswers() {
		return this.answers;
	}

	public int getPage() {
		return this.page;
	}

	public int getSize() {
		return this.size;
	}
}
