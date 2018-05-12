package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.util.ArrayList;

public class QuizzAnswers {

    private ArrayList<String> quizzQuestions;
    private ArrayList<String> quizzAnswers;

    public QuizzAnswers(ArrayList<String> quizzQuestions, ArrayList<String> quizzAnswers) {
        this.quizzQuestions = quizzQuestions;
        this.quizzAnswers = quizzAnswers;
    }

    public ArrayList<String> getQuizzQuestions() {
        return quizzQuestions;
    }

    public ArrayList<String> getQuizzAnswers() {
        return quizzAnswers;
    }


}
