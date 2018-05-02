package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class GetQuizzesCommand implements Command {
	
    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private String location;

    public GetQuizzesCommand(int id) {
        this.id=id;
    }
    
    public GetQuizzesCommand(int id, String location){
        this.location=location;
    }
	
    @Override
    public Response handle(CommandHandler chi) {
    	return chi.handle(this);
    }
    
    public int getId(){
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }
}
