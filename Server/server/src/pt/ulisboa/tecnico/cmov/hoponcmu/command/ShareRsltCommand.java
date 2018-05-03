package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

/**
 * Created by Daniela on 02/05/2018.
 */

public class ShareRsltCommand implements Command{

    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private int friend;

    public ShareRsltCommand(int id) {
        this.id=id;
    }

    public ShareRsltCommand(int id, int friend){
        this.friend=friend;
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public int getId(){
        return this.id;
    }

    public int getFriend() {
        return this.friend;
    }
}
