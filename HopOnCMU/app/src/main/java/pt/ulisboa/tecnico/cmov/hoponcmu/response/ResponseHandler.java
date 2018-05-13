package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import pt.ulisboa.tecnico.cmov.hoponcmu.command.RequestPrizesCommand;

public interface ResponseHandler {
	public void handle(LoginResponse hr);
	public void handle(SignupResponse hr);
	public void handle(GetQuizzesResponse hr);
	public void handle(GetCorrectAnswersResponse hr);
	public void handle(SendQuizzesAnswersResponse hr);

}
