package com.example.x541u.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListNotesActivity extends AppCompatActivity {

    private ArrayList<Note> array;
    private NoteAdapter adapter;
    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        if(adapter == null){ setAdapter(); }
    }

    public void setAdapter() {
        array = new ArrayList<>();
        adapter = new NoteAdapter(this,array);

        ListView listView = (ListView) findViewById(R.id.list_notes);
        listView.setAdapter(adapter);
    }

    public void createNote(View view) {
       Intent intent = new Intent(ListNotesActivity.this, CreateNoteActivity.class);
       startActivityForResult(intent,PICK_CONTACT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String tittle = data.getStringExtra("tittle");
                String notes = data.getStringExtra("notes");
                Note newNote = new Note(tittle,notes);
                array.add(newNote);
                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"Item added to List",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
