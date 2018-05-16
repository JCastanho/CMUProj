package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class RequestPrizesCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private int id;

    public RequestPrizesCommand(int id) {
        this.id = id;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public int getId() {
        return this.id;
    }
}
