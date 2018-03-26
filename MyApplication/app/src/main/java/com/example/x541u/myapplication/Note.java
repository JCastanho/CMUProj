package com.example.x541u.myapplication;

/**
 * Created by X541U on 22/03/2018.
 */

public class Note {

    private String tittle;
    private String notes;

    public Note(String s_tittle, String s_notes){
        tittle = s_tittle;
        notes = s_notes;
    }

    public String getTittle(){ return tittle;}

    public String getNotes(){ return notes; }
}
