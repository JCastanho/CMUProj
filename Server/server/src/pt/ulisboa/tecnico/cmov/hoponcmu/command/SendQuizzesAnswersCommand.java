package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

import java.util.ArrayList;

public class SendQuizzesAnswersCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private String quizzTitle;
    private ArrayList<String> quizzAnswers;
    private ArrayList<String> quizzQuestions;
    private int timeForQuizz;

    public SendQuizzesAnswersCommand(int id, String quizzTitle , ArrayList<String> quizzQuestions , ArrayList<String> quizzAnswers, int timeForQuizz){
        this.id = id;
        this.quizzTitle = quizzTitle;
        this.quizzQuestions = quizzQuestions;
        this.quizzAnswers = quizzAnswers;
        this.timeForQuizz = timeForQuizz;
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

    public ArrayList<String> getQuizzAnswers() {
        return quizzAnswers;
    }

    public ArrayList<String> getQuizzQuestions() {
        return quizzQuestions;
    }
    
    public int getTimeForQuizz(){
        return timeForQuizz;
    }        
}
