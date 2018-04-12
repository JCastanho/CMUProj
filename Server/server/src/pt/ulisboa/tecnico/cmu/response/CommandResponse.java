package pt.ulisboa.tecnico.cmu.response;

import java.util.List;

public class CommandResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private String message;
    private boolean loginOrSignin;
    private List<String> getUserSignUp;
	
    public CommandResponse(String message) {
	this.message=message;
    }
    
    public CommandResponse(boolean login){
        this.loginOrSignin=login;
    }
    
    public String getMessage() {
	return this.message;
    }
    
    public boolean getLoginOrSignin(){
        return this.loginOrSignin;
    }
}
