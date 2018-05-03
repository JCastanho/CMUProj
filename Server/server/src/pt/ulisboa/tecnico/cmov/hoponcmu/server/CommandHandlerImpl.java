package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.*;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.*;

import java.util.ArrayList;
import java.util.List;

public class CommandHandlerImpl implements CommandHandler {

    Session s = new Session();

    @Override
    public Response handle(LoginCommand cmd) {
        int identifier = s.verifyUser(cmd.getUsername(), cmd.getCode());

        return new LoginResponse(identifier);
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
    public Response handle(ShareRsltCommand cmd) {
        Boolean success = false;

        if(s.verifyLogin(cmd.getId())) {
            int friend = cmd.getFriend();

            //WIFI direct shits
            success = true;
        }

        return new ShareRsltResponse(success);
    }

    @Override
    public Response handle(GetUsersCommand cmd){
        //verify id from command?
        List<String> users = s.getActiveUsers(cmd.getID());

        GetUsersResponse rsp = new GetUsersResponse(users);
        return rsp;
    }
}