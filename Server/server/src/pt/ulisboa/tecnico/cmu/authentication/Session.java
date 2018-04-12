/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmu.authentication;

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
    private Map<String, String> login;
    private Map<String, String> quizzes;
    
    public Session(){
        users=new ArrayList<>();
        login=new HashMap<>();
        quizzes = new HashMap<>();
        populateQuizzes();
    }

    public Boolean createUser(String username, String code){
        for(User u: users){
            if(u.getUsername().equals(username)){
                return false;
            }
        }
        User nu = new User(username, code);
        login.put(username,code);
        return users.add(nu);
    }
    
    public Boolean verifyUser(String username, String password){
        if(login.containsKey(username)){
            if(login.get(username).equals(password)){
                return true;
            }
        }
        return false;
    }
    
    public String getQuizz(String monument){
        return quizzes.get(monument);
    }
    
    public void populateQuizzes(){
        String a = "[{\"quizz\":{\"1\":{\"pergunta\":\"pergunta1\",\"respostas\":"
                + "{\"primeira\":\"resposta11\",\"segunda\":\"resposta12\",\"terceira\":\"resposta13\",\"quatro\":\"resposta14\"}},"
                + "\"2\":{\"pergunta\":\"pergunta2\",\"respostas\":{\"primeira\":\"resposta21\",\"segunda\":\"resposta22\","
                + "\"terceira\":\"resposta23\",\"quatro\":\"resposta24\"}},\"3\":{\"pergunta\":\"pergunta3\",\"respostas\":{"
                + "\"primeira\":\"resposta31\",\"segunda\":\"resposta32\",\"terceira\":\"resposta33\",\"quatro\":\"resposta34\"}},"
                + "\"4\":{\"pergunta\":\"pergunta4\",\"respostas\":{\"primeira\":\"resposta41\",\"segunda\":\"resposta42\",\"terceira"
                + "\":\"resposta43\",\"quatro\":\"resposta44\"}}}}]";
        
        quizzes.put("Monument One", a);
    }
}
