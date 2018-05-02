package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class SendLocationCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private String location;

    public SendLocationCommand(String location) {
        this.location = location;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public String getLocation() {
        return this.location;
    }
}
