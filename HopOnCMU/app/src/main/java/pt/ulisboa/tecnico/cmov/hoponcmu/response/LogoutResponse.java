package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class LogoutResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private int id;

	public LogoutResponse(int id){
		this.id = id;
	}

	public int getID() {
		return this.id;
	}
}
