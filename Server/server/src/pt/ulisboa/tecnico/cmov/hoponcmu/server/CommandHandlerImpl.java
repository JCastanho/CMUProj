package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.CommandHandler;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public class CommandHandlerImpl implements CommandHandler {

    Session s = new Session();

    @Override
    public Response handle(LoginCommand hc) {
        LoginResponse resp = null;

        if(s.verifyUser(hc.getUsername(), hc.getCode())){
            //Decidir onde por gerador do id
            resp = new LoginResponse(s.generateID());
        } else {
            resp = new LoginResponse(-1);
        }

        return resp;
    }

    //Adicionar aqui handle para outrors comandos
}