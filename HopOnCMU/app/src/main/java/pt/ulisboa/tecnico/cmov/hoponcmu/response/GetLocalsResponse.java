package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class GetLocalsResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private String location;

    public GetLocalsResponse(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
