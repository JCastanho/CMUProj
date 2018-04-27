package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class LoginCommand implements Command {
	
    private static final long serialVersionUID = -8807331723807741905L;
    private int id;
    private String username;
    private String code;

    public LoginCommand(int id) {
        this.id=id;
    }
    
    public LoginCommand(int id, String username, String code){
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

    public String getUsername() {
        return this.username;
    }
    
    public String getCode(){
        return this.code;
    }
}
