package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public interface ResponseHandler {
	public void handle(LoginResponse rsp);
	public void handle(SignupResponse rsp);
	public void handle(GetQuizzesResponse rsp);
	public void handle(SendQuizzesAnswersResponse rsp);
        public void handle(PrizesResponse rsp);

}