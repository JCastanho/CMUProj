package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public interface ResponseHandler {
	public void handle(LoginResponse rsp);
	public void handle(SignupResponse rsp);

	//Adicionar aqui handle para outros comandos

}