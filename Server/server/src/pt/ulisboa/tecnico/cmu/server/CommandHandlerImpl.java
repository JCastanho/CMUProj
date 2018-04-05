package pt.ulisboa.tecnico.cmu.server;

import pt.ulisboa.tecnico.cmu.authentication.Session;
import pt.ulisboa.tecnico.cmu.command.CommandHandler;
import pt.ulisboa.tecnico.cmu.command.ResponseCommand;
import pt.ulisboa.tecnico.cmu.response.CommandResponse;
import pt.ulisboa.tecnico.cmu.response.Response;

public class CommandHandlerImpl implements CommandHandler {
    
    @Override
    public Response handle(ResponseCommand hc) {
        String resp = hc.getMessage();
        switch(resp){
            case "Ola":
                System.out.println("Received: " + hc.getMessage());
                return new CommandResponse("Hi from Server!");
            case "Adeus":
                System.out.println("Received: " + hc.getMessage());
                return new CommandResponse("Goodbye from Server!");
            default:
                System.out.println("Received: " + hc.getMessage());
                return new CommandResponse("Command Invalid");
        }
    }
}
