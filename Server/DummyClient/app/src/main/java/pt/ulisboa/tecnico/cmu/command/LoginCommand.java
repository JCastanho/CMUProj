package pt.ulisboa.tecnico.cmu.command;

import pt.ulisboa.tecnico.cmu.response.Response;

public class LoginCommand implements Command {
	
	private static final long serialVersionUID = -8807331723807741905L;
	private int id;
	private String message;
	private String username;
	private String code;

	public LoginCommand(int id, String message) {
		this.id=id;
		this.message = message;
	}

	public LoginCommand(int id, String username, String code){
		this.id=id;
		this.username=username;
		this.code=code;
	}
	
	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}
	
	public String getMessage() {
		return this.message;
	}
}
