package pt.ulisboa.tecnico.cmu.server;

import pt.ulisboa.tecnico.cmu.authentication.Session;
import pt.ulisboa.tecnico.cmu.command.CommandHandler;
import pt.ulisboa.tecnico.cmu.command.ResponseCommand;
import pt.ulisboa.tecnico.cmu.response.CommandResponse;
import pt.ulisboa.tecnico.cmu.response.Response;

public class CommandHandlerImpl implements CommandHandler {
    
    Session s = new Session();
    
    @Override
    public Response handle(ResponseCommand hc) {
        int id = hc.getId();
        CommandResponse resp=null;
        
        switch(id){
            case 0:
                resp = new CommandResponse(s.verifyUser(hc.getUsername(), ""));
                break;
            case 1:
                resp = new CommandResponse(s.createUser(hc.getUsername(), hc.getCode()));
                break;
            case 2:
                resp = new CommandResponse(s.getQuizz(hc.getMonument()));
        }
        
        return new CommandResponse(hc.getMessage());
    }
}
