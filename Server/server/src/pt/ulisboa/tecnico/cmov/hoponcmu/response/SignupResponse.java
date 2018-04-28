package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class SignupResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private Boolean success;

    public SignupResponse(Boolean success){
        this.success = success;
    }

    public Boolean getAuthorization() {
        return this.success;
    }
}
