package pt.ulisboa.tecnico.cmu.response;

public class CommandResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private String message;
	private boolean login;
	
	public CommandResponse(String message) {
		this.message = message;
	}

	public CommandResponse(boolean login) {
		this.login=login;
	}
	
	public String getMessage() {
		return this.message;
	}

	public boolean getLogin() {
	    return login;
    }
}
