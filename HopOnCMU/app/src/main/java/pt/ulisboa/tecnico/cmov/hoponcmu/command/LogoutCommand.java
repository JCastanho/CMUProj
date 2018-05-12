package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class LogoutCommand implements Command {

	private static final long serialVersionUID = -8807331723807741905L;
	private int id;
	private Integer token;

	public LogoutCommand(int id) {
		this.token=id;
	}

	@Override
	public Response handle(CommandHandler chi) {
		return chi.handle(this);
	}

	public Integer getToken() {
		return this.token;
	}

}
