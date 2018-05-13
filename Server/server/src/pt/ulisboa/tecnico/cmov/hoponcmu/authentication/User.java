/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author josea
 */
public class User {

    private Map<String, Integer> quizzAnwser;
    private String username;
    private String code;

    public User(String username, String code){
        this.username=username;
        this.code=code;
        quizzAnwser = new HashMap<>();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code){
        this.code=code;
    }
    
    //TODO add getPoints, n shit
    public Map<String, Integer> getQuizzAnwser() {
        return quizzAnwser;
    }

    public void setQuizzAnwser(Map<String, Integer> quizzAnwser) {
        this.quizzAnwser = quizzAnwser;
    }
}
