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
public class User {
    
    private String username;
    private String code;
    
    public User(String username, String code){
        this.username=username;
        this.code=code;
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
}
