package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public interface ResponseHandler {
	public void handle(LoginResponse hr);
	public void handle(SignupResponse hr);

}
