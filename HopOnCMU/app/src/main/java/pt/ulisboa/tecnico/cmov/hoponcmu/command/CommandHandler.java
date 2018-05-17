package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public interface CommandHandler {
	public Response handle(LoginCommand hc);

	public Response handle(LogoutCommand hc);

	public Response handle(GetQuizzesCommand hc);

	public Response handle(SendLocationCommand hc);

	public Response handle(SendQuizzesAnswersCommand hc);

	public Response handle(CreateUserCommand createUserCommand);

	public Response handle(GetCorrectAnswersCommand hc);

	public Response handle(RequestPrizesCommand hc);

	public Response handle(GetAnsweredQuizzesCommand hc);

}
