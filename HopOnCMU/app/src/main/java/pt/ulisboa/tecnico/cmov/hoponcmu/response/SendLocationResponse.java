package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.List;

public class SendLocationResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private List<String> locations;

    public SendLocationResponse(List<String> locations){
        this.locations = locations;
    }

    public List<String> getLocations() {
        return locations;
    }
}
