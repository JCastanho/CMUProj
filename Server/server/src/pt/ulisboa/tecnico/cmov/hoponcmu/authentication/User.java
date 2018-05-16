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

    private Map<String, Integer> quizzAnswer;
    private String username;
    private String code;
    private int timeForQuizz;

    public User(String username, String code){
        this.username=username;
        this.code=code;
        quizzAnswer = new HashMap<>();
        timeForQuizz=0;
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

    public int getTimeForQuizz() {
        return timeForQuizz;
    }

    public void setTimeForQuizz(int timeForQuizz) {
        this.timeForQuizz = timeForQuizz;
    }
        
    //TODO add getPoints, n shit
    public Map<String, Integer> getQuizzAnswser() {
        return quizzAnswer;
    }

    public void setQuizzAnswser(String quizzTitle, int correctAnswer) {
        if(!quizzAnswer.containsKey(quizzTitle)){
            quizzAnswer.put(quizzTitle, correctAnswer);
        }
    }
}
