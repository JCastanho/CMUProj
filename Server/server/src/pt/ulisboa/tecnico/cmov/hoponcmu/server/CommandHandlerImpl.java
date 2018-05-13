package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.*;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.*;

import java.util.List;

public class CommandHandlerImpl implements CommandHandler {

    Session s = new Session();

    @Override
    public Response handle(LoginCommand cmd) {
        int identifier = -1;
		try {
			identifier = s.verifyUser(cmd.getUsername(), cmd.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}

        try {
			return new LoginResponse(identifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

    @Override
    public Response handle(CreateUserCommand cmd){
        String rsp = "NOK";
		try {
			if(cmd.securityCheck()) {
				rsp = s.createUser(cmd.getUsername(), cmd.getCode())? "OK": "NOK";	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

        try {
			return new SignupResponse(rsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public Response handle(SendLocationCommand cmd){
        SendLocationResponse rsp = new SendLocationResponse(cmd.verifyString(cmd.getLocation()));
        System.out.println(rsp.getLocations().get(0));
        return rsp;
    }

    @Override
    public Response handle(GetQuizzesCommand cmd){
    	
    	String question = s.getQuizzQuestion(cmd.getLocation(), cmd.getPage());
    	ArrayList<String> answers = s.getQuizzAnswers(cmd.getLocation(), cmd.getPage());
    	int size = s.getQuizzSize(cmd.getLocation());
    	
        GetQuizzesResponse rsp = new GetQuizzesResponse(question, answers, cmd.getPage(), size);
        return rsp;
    }

    @Override
    public Response handle(GetUsersCommand cmd){
        //verify id from command?
        List<String> users = s.getActiveUsers(cmd.getID());

        GetUsersResponse rsp = new GetUsersResponse(users);
        return rsp;
    }

    @Override
    public Response handle(GetQuizzesAnswersCommand cmd) {
        GetQuizzesAnswersResponse rsp = new GetQuizzesAnswersResponse(cmd.getQuizzAnswers());
        return rsp;
    }
    //Adicionar aqui handle para outros comandos

}