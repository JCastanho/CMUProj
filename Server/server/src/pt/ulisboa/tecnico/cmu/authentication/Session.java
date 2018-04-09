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
    
    public Session(){
        users=new ArrayList<>();
        login=new HashMap<>();
        User nu = new User("olaola", "", "", "", 23);
        login.put(nu.getUsername(),nu.getPassword());
    }

    public Boolean createUser(String username, String password, String name, String country, int age){
        for(User u: users){
            if(u.getUsername().equals(username)){
                return false;
            }
        }
        User nu = new User(username, password, name, country, age);
        login.put(username,password);
        return users.add(nu);
    }
    
    public Boolean verifyUser(String username, String password){
        if(login.containsKey(username)){
            System.out.println("entrei verifyUser");
            //if(login.get(username).equals(password)){
            return true;
        }
            //return false;
        return false;
    }
}
