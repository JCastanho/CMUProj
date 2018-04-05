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
    private String password;
    private String name;
    private int score;
    private String country;
    private int age;
    
    public User(String username, String password, String name, String country, int age){
        this.username=username;
        this.password=password;
        this.name = name;
        this.country=country;
        this.age=age;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
