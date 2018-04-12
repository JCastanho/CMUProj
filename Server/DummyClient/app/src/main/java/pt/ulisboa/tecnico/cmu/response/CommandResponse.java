package pt.ulisboa.tecnico.cmu.response;

public class CommandResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private String message;
	private boolean loginOrCreate;
	
	public CommandResponse(String message) {
		this.message = message;
	}

	public CommandResponse(boolean loginOrCreate) {
		this.loginOrCreate = loginOrCreate;
	}
	
	public String getMessage() {
		return this.message;
	}

	public boolean getLoginOrCreate() {
	    return loginOrCreate;
    }
}
