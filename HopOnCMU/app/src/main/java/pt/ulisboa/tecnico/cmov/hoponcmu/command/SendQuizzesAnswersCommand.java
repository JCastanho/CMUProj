package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class SendQuizzesAnswersCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    private int id;
    private String quizzTitle;
    private ArrayList<String> quizzQuestions;
    private ArrayList<String> quizzAnswers;

    public SendQuizzesAnswersCommand(int id, String quizzTitle, ArrayList<String> quizzQuestions, ArrayList<String> quizzAnswers){
        this.id = id;
        this.quizzTitle= quizzTitle;
        this.quizzQuestions = quizzQuestions;
        this.quizzAnswers = quizzAnswers;
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public int getId() {
        return id;
    }

    public String getQuizzTitle() {
        return quizzTitle;
    }

    public ArrayList<String> getQuizzQuestions() {
        return quizzQuestions;
    }

    public ArrayList<String> getQuizzAnswers() {
        return quizzAnswers;
    }
}
