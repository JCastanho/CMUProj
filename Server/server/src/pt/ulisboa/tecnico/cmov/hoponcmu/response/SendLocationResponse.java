package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.List;

public class SendLocationResponse implements Response {
    
    List<String> locations;
    
    public SendLocationResponse(List<String> locations){
        this.locations=locations;
    }
    
    public List<String> getLocations(){
        return locations;
    }
}
