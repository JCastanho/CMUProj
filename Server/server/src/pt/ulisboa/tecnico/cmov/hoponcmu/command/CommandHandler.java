package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public interface CommandHandler {
	public Response handle(LoginCommand cmd);
	public Response handle(CreateUserCommand cmd);
    public Response handle(SendLocationCommand cmd);
    public Response handle(GetQuizzesCommand cmd);

	//Adicionar aqui handle para outros comandos
}
