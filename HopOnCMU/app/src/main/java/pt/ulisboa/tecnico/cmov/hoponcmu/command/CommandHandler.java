package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public interface CommandHandler {
	public Response handle(LoginCommand hc);

	public Response handle(GetUsersCommand hc);

	public Response handle(GetQuizzesCommand hc);

	public Response handle(SendLocationCommand hc);

	public Response handle(ShareRsltCommand hc);

	public Response handle(CreateUserCommand createUserCommand);
}
