package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class GetQuizzesAnswersCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private String quizzAnswers;

    public GetQuizzesAnswersCommand(int id, String quizzAnswers){
        this.id = id;
        this.quizzAnswers = quizzAnswers;
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public int getId() {
        return id;
    }

    public String getQuizzAnswers() {
        return quizzAnswers;
    }
}
