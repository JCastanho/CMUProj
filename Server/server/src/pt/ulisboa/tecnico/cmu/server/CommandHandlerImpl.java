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
        String[] resp = hc.getMessage().split("/");
        
        if(resp[0].equals("login")){
            return new CommandResponse(s.verifyUser(resp[1], ""));
        }
        else if(resp[0].equals("signin")){
            return new CommandResponse(s.createUser(resp[1], resp[2], resp[3], resp[4], Integer.parseInt(resp[5])));
        }
        return new CommandResponse("nothing");
    }
}
