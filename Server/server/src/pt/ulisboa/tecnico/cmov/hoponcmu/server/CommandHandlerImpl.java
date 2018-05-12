package pt.ulisboa.tecnico.cmov.hoponcmu.server;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.authentication.Session;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.*;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.*;

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
    public Response handle(LogoutCommand cmd) {
        Integer token = cmd.getToken();

        s.logOutUser(token);

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
<<<<<<< HEAD
    public Response handle(GetUsersCommand cmd){
        //verify id from command?
        List<String> users = s.getActiveUsers(cmd.getID());

        GetUsersResponse rsp = new GetUsersResponse(users);
        return rsp;
    }

    @Override
    public Response handle(SendQuizzesAnswersCommand cmd) {
        s.quizzAnswers(cmd.getQuizzTitle(), cmd.getQuizzQuestions(), cmd.getQuizzAnswers());
        SendQuizzesAnswersResponse rsp = new SendQuizzesAnswersResponse(cmd.getId());
        return rsp;
    }

    @Override
    public Response handle(GetCorrectAnswersCommand cmd) {
        int correctAnswers = s.correctAnswers(cmd.getQuizzTitle());
        GetCorrectAnswersResponse rsp = new GetCorrectAnswersResponse(correctAnswers);
=======
    public Response handle(GetQuizzesAnswersCommand cmd) {
        GetQuizzesAnswersResponse rsp = new GetQuizzesAnswersResponse(cmd.getQuizzAnswers());
>>>>>>> Daniela
        return rsp;
    }
    //Adicionar aqui handle para outros comandos

}