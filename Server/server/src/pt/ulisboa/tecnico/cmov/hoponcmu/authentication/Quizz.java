package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.util.ArrayList;

public class Quizz {
	private String Question;
	private ArrayList<String> answers;
	private ArrayList<String> Wrong;
	private String Correct;
	
	public Quizz(String question, ArrayList<String> wrong, String correct){
		Question = question;
		Wrong = wrong;
		Correct = correct;
	}

	public ArrayList<String> getAnswers(){
		ArrayList<String> answers = Wrong;
		answers.add(Correct);
		
		return answers;
	}
	
	public String getQuestion(){
            return Question;
	}

	public String getCorrect() {
            return Correct;
	}
	
	public boolean validateAnswer(String answer){
            return answer.equals(Correct);
	}
}
