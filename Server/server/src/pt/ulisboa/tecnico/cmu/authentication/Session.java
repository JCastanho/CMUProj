/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmu.authentication;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author josea
 */
public class Session {
    
    private Map<String,String> users;
    User u;
    
    public Session(){
        users=new HashMap<>();
        u = new User();
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
    
    public Boolean createUser(){
    }
    
    public Boolean verifyUser(String user, String pass){
        if(users.containsKey(user)){
            if(users.get(user).equals(pass)){
                return true;
            }
        }
        return false;
    }
}
