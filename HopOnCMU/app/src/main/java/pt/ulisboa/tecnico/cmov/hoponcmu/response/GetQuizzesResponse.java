package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.ArrayList;

public class GetQuizzesResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private ArrayList<String> questions;
	private ArrayList<ArrayList<String>> answers;
	private String location;


	public GetQuizzesResponse(ArrayList<String> questions, ArrayList<ArrayList<String>> answers, String location){
		this.questions = questions;
		this.answers = answers;
		this.location = location;
	}

	public ArrayList<String> getQuestion() {
		return this.questions;
	}

	public ArrayList<ArrayList<String>> getAnswers() {
		return this.answers;
	}

	public String getLocation() {
		return location;
	}

}
