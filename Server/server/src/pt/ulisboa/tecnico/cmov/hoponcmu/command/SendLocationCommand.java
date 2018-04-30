package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class SendLocationCommand implements Command{
    
    private List<String> locations;
    private final String location = "location";
    private String v;
    
    public SendLocationCommand(String v){
        locations = new ArrayList<>();
        locations.add("Terreiro do Paço");
        locations.add("Chiado");
        locations.add("Castelo de São Jorge");
        locations.add("Praça da Figueira");
        this.v=v;
    }
    
    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }
    
    public List<String> verifyString(String v){
        if(v.equals(location)){
            return locations;
        }
        return null;
    }
    
    public String getV(){
        return v;
    }
}
