package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.*;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.*;

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

    @Override
    public Response handle(SendLocationCommand cmd){
        SendLocationResponse rsp = new SendLocationResponse(cmd.verifyString(cmd.getLocation()));
        System.out.println(rsp.getLocations().get(0));
        return rsp;
    }

    @Override
    public Response handle(GetQuizzesCommand cmd){
        GetQuizzesResponse rsp = new GetQuizzesResponse(s.getQuizz(cmd.getLocation()));
        return rsp;
    }

    @Override
    public Response handle(GetQuizzesAnswersCommand cmd) {
        GetQuizzesAnswersResponse rsp = new GetQuizzesAnswersResponse(cmd.getQuizzAnswers());
        return rsp;
    }
    //Adicionar aqui handle para outros comandos
}