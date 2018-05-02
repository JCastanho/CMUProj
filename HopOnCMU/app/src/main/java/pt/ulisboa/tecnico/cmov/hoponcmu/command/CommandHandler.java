package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public interface CommandHandler {
	public Response handle(LoginCommand hc);

	public Response handle(CreateUserCommand hc);

	public Response handle(GetQuizzesCommand hc);

	public Response handle(ListLocalsCommand hc);

}
