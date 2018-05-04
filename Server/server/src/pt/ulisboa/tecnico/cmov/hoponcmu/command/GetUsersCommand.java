package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

/**
 * Created by Daniela on 03/05/2018.
 */

public class GetUsersCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;
    private Integer id;

    public GetUsersCommand(Integer id) {
        this.id = id;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public Integer getID() {
        return this.id;
    }
}
