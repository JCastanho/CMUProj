package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

import java.util.ArrayList;

public class Question {
    private String Question;
    private ArrayList<String> Answers;
    private int Page;
    private int Size;

    public Question(String question, ArrayList<String> answers, int page, int size){
        Question = question;
        Answers = answers;
        Page = page;
        Size = size;
    }

    public String getQuestion() {
        return Question;
    }

    public ArrayList<String> getAnswers() {
        return Answers;
    }

    public int getPage() {
        return Page;
    }

    public int getSize() {
        return Size;
    }
}
