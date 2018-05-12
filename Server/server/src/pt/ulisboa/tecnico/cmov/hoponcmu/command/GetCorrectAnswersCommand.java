package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;


public class GetCorrectAnswersCommand implements Command{

    private static final long serialVersionUID = -8807331723807741905L;
    private String quizzTitle;

    public GetCorrectAnswersCommand(String quizzTitle){
        this.quizzTitle = quizzTitle;
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public String getQuizzTitle() {
        return quizzTitle;
    }
}
