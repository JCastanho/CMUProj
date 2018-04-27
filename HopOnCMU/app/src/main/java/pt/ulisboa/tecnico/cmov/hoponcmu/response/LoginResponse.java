package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class LoginResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private int id;

	public LoginResponse(int id){
		this.id = id;
	}

	public int getID() {
		return this.id;
	}
}
