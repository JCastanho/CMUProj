package pt.ulisboa.tecnico.cmu.response;

public class LoginResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private String message;
	private boolean loginOrCreate;
	
	public LoginResponse(String message) {
		this.message = message;
	}

	public LoginResponse(boolean loginOrCreate) {
		this.loginOrCreate = loginOrCreate;
	}
	
	public String getMessage() {
		return this.message;
	}

	public boolean getLoginOrCreate() {
	    return loginOrCreate;
    }
}
