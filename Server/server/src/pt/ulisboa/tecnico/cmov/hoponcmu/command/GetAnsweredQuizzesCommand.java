package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class GetAnsweredQuizzesCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    private int id;

    public GetAnsweredQuizzesCommand(int id){
        this.id = id;
    }

    @Override
    public Response handle(CommandHandler ch){
        return ch.handle(this);
    }

    public int getId() {
        return id;
    }
}
