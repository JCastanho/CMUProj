/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author josea
 */
public class Session {

    private List<User> users;
    private Map<Integer, User> login;
    private Map<String, String> quizzes;
    private Integer idSequence = 0;

    public Session(){
        users=new ArrayList<>();
        login=new HashMap<>();
        quizzes = new HashMap<>();
        populateQuizzes();
        users.add(new User("a","a"));
    }

    public Boolean createUser(String username, String code){
        for(User u: users){
            if(u.getUsername().equals(username) || u.getCode().equals(code)){
                return false;
            }
        }

        User nu = new User(username, code);

        return users.add(nu);
    }

    public Boolean verifyUser(String username, String password){
        for(User u: users){
            if(u.getUsername().equals(username) && u.getCode().equals(password))
                return true;
        }

        return false;
    }

    public Integer generateID(){
        return idSequence++;
    }

    public String getQuizz(String monument){
        return quizzes.get(monument);
    }

    public void populateQuizzes(){
    	String[] Numbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};
    	
        String a = "[{\"quizz\":{\"1\":{\"pergunta\":\"pergunta1\",\"respostas\":"
                + "{\"primeira\":\"resposta11\",\"segunda\":\"resposta12\",\"terceira\":\"resposta13\",\"quatro\":\"resposta14\"}},"
                + "\"2\":{\"pergunta\":\"pergunta2\",\"respostas\":{\"primeira\":\"resposta21\",\"segunda\":\"resposta22\","
                + "\"terceira\":\"resposta23\",\"quatro\":\"resposta24\"}},\"3\":{\"pergunta\":\"pergunta3\",\"respostas\":{"
                + "\"primeira\":\"resposta31\",\"segunda\":\"resposta32\",\"terceira\":\"resposta33\",\"quatro\":\"resposta34\"}},"
                + "\"4\":{\"pergunta\":\"pergunta4\",\"respostas\":{\"primeira\":\"resposta41\",\"segunda\":\"resposta42\",\"terceira"
                + "\":\"resposta43\",\"quatro\":\"resposta44\"}}}}]";

        
        for(int i = 0; i < Numbers.length; i++) {
        	quizzes.put("Monument " + Numbers[i], a);
        }
    }
}
