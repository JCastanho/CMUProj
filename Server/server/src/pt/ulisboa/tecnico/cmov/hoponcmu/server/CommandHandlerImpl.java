package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.CommandHandler;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.CreateUserCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SignupResponse;

public class CommandHandlerImpl implements CommandHandler {

    Session s = new Session();

    @Override
    public Response handle(LoginCommand cmd) {
        LoginResponse rsp = null;

        if(s.verifyUser(cmd.getUsername(), cmd.getCode())){
            //Decidir onde por gerador do id
            rsp = new LoginResponse(s.generateID());
        } else {
            rsp = new LoginResponse(-1);
        }

        return rsp;
    }

    @Override
    public Response handle(CreateUserCommand cmd) {
        Boolean rsp = s.createUser(cmd.getUsername(), cmd.getCode());

        return new SignupResponse(rsp);
    }

    //Adicionar aqui handle para outros comandos
}