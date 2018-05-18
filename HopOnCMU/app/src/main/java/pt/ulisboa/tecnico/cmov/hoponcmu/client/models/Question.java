package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

import java.util.ArrayList;

public class Question {
    private String Question;
    private ArrayList<String> Answers;


    public Question(String question, ArrayList<String> answers){
        Question = question;
        Answers = answers;
    }

    public String getQuestion() {
        return Question;
    }

    public ArrayList<String> getAnswers() {
        return Answers;
    }

}
