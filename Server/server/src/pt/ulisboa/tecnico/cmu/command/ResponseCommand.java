package pt.ulisboa.tecnico.cmu.command;

import pt.ulisboa.tecnico.cmu.response.Response;

public class ResponseCommand implements Command {
	
    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private String message;
    private String username;
    private String code;
    private String monument;

    public ResponseCommand(int id, String message) {
        this.id=id;
	this.message = message;
    }
    
    public ResponseCommand(int id, String username, String code){
        this.username=username;
        this.code=code;
    }
	
    @Override
    public Response handle(CommandHandler chi) {
	return chi.handle(this);
    }
    
    public int getId(){
        return this.id;
    }
	
    public String getMessage() {
        return this.message;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getCode(){
        return this.code;
    }
    
    public String getMonument(){
        return this.monument;
    }
}
