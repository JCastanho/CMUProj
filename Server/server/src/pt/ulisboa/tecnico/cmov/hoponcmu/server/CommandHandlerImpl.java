package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.*;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.*;

import java.util.List;
import java.util.Map;

public class CommandHandlerImpl implements CommandHandler {

    Session s = new Session();

    @Override
    public Response handle(LoginCommand cmd) {
        int identifier = -1;
		try {
	        if(cmd.securityCheck())
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
    public Response handle(LogoutCommand cmd) {
    	
    	
        Integer token;
		try {
			if(cmd.securityCheck()) {
				token = cmd.getToken();
				s.logOutUser(token);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return null;
    }

    @Override
    public Response handle(SendLocationCommand cmd){
        SendLocationResponse rsp = null;
		try {
			if(cmd.securityCheck()) {
				rsp = new SendLocationResponse(cmd.verifyString(cmd.getLocation()));
		        System.out.println(rsp.getLocations().get(0));
		        return rsp;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsp;
    }

    @Override
    public Response handle(GetQuizzesCommand cmd){
		try {
			if(cmd.securityCheck()) {
		    	String question = s.getQuizzQuestion(cmd.getLocation(), cmd.getPage());
		    	ArrayList<String> answers = s.getQuizzAnswers(cmd.getLocation(), cmd.getPage());
		    	int size = s.getQuizzSize(cmd.getLocation());

		        GetQuizzesResponse rsp = new GetQuizzesResponse(question, answers, cmd.getPage(), size);
		        return rsp;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
    }

    @Override
    public Response handle(SendQuizzesAnswersCommand cmd) {
        try {
        	if(cmd.securityCheck()) {
        		s.quizzAnswers(cmd.getId() ,cmd.getQuizzTitle(), cmd.getQuizzQuestions(), cmd.getQuizzAnswers());
                s.correctAnswers(cmd.getId() ,cmd.getQuizzTitle());
                System.out.println("SAVE: " + cmd.getQuizzTitle() + " " + cmd.getTime());
                s.saveTime(cmd.getId(), cmd.getQuizzTitle(), cmd.getTime());
                SendQuizzesAnswersResponse rsp = new SendQuizzesAnswersResponse(cmd.getId());
                return rsp;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
    }

    @Override
    public Response handle(GetCorrectAnswersCommand cmd) {
        int correctAnswers;
		try {
			if(cmd.securityCheck()) {
				correctAnswers = s.correctAnswers(cmd.getId() ,cmd.getQuizzTitle());
		        int timeQuizz = s.getTime(cmd.getId(), cmd.getQuizzTitle());
		        GetCorrectAnswersResponse rsp = new GetCorrectAnswersResponse(correctAnswers, timeQuizz);
		        return rsp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public Response handle(RequestPrizesCommand cmd){
        String res;
		try {
			if(cmd.securityCheck()) {
				res = s.getQuizzesPrizes(cmd.getId());
		        PrizesResponse rsp = new PrizesResponse(res);
		        return rsp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
    }

    @Override
    public Response handle(GetAnsweredQuizzesCommand cmd){
        List<String> answeredQuizzes = s.getAnsweredQuizzes(cmd.getId());
        GetAnsweredQuizzesResponse rsp = new GetAnsweredQuizzesResponse(answeredQuizzes);
        return rsp;
    }
    //Adicionar aqui handle para outros comandos
}
