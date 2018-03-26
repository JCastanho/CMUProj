package com.example.x541u.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class CreateNoteActivity extends AppCompatActivity{

    private ArrayList<String> array;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
    }

    public void addToList(View view) {

        Intent intent = new Intent(CreateNoteActivity.this, ListNotesActivity.class);
        EditText inserted_tittle = (EditText) findViewById(R.id.tittle);
        EditText inserted_notes = (EditText) findViewById(R.id.note);

        String tittle = inserted_tittle.getText().toString();
        String notes = inserted_notes.getText().toString();

        intent.putExtra("notes", notes);
        intent.putExtra("tittle", tittle);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void cancel(View view){

    }


}
