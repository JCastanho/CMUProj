package pt.ulisboa.tecnico.cmov.hoponcmu.server;

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
    public Response handle(SendQuizzesAnswersCommand cmd) {
        s.quizzAnswers(cmd.getId() ,cmd.getQuizzTitle(), cmd.getQuizzQuestions(), cmd.getQuizzAnswers());
        s.correctAnswers(cmd.getId() ,cmd.getQuizzTitle());
        System.out.println("SAVE: " + cmd.getQuizzTitle() + " " + cmd.getTime());
        s.saveTime(cmd.getId(), cmd.getQuizzTitle(), cmd.getTime());
        SendQuizzesAnswersResponse rsp = new SendQuizzesAnswersResponse(cmd.getId());
        return rsp;
    }

    @Override
    public Response handle(GetCorrectAnswersCommand cmd) {
        int correctAnswers = s.correctAnswers(cmd.getId() ,cmd.getQuizzTitle());
        int timeQuizz = s.getTime(cmd.getId(), cmd.getQuizzTitle());
        GetCorrectAnswersResponse rsp = new GetCorrectAnswersResponse(correctAnswers, timeQuizz);
        return rsp;
    }

    @Override
    public Response handle(RequestPrizesCommand cmd){
        String res = s.getQuizzesPrizes(cmd.getId());
        PrizesResponse rsp = new PrizesResponse(res);
        return rsp;
    }

    @Override
    public Response handle(GetAnsweredQuizzesCommand cmd){
        List<String> answeredQuizzes = s.getAnsweredQuizzes(cmd.getId());
        GetAnsweredQuizzesResponse rsp = new GetAnsweredQuizzesResponse(answeredQuizzes);
        return rsp;
    }
    //Adicionar aqui handle para outros comandos
}
