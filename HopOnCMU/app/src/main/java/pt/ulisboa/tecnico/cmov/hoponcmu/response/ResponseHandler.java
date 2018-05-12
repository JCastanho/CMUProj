package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public interface ResponseHandler {
	public void handle(LoginResponse hr);
	public void handle(SignupResponse hr);
	public void handle(GetQuizzesResponse hr);
	public void handle(GetCorrectAnswersResponse hr);
	public void handle(SendQuizzesAnswersResponse hr);

}
