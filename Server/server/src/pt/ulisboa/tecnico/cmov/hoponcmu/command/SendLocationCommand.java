package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class SendLocationCommand implements Command{
    
    private static final long serialVersionUID = -8807331723807741905L;
    private List<String> locations;
    private final String locationVariable="location";
    private String location;
    
    public SendLocationCommand(String location){
        this.location=location;
    }
    
    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }
    
    public List<String> verifyString(String location){
        if(location.equals(locationVariable)){
            populateList();
            return locations;
        }
        return null;
    }
    
    public void populateList(){
        locations = new ArrayList<>();
        locations.add("Terreiro do Paço");
        locations.add("Chiado");
        locations.add("Castelo de São Jorge");
        locations.add("Praça da Figueira");
    }
    
    public String getLocation(){
        return location;
    }
}
