package com.example.x541u.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by X541U on 22/03/2018.
 */

public class NoteAdapter extends ArrayAdapter<Note>{

    private ArrayList<Note> array;
    private Context context;

    public NoteAdapter(Context c_context, ArrayList<Note> a_array){
        super(c_context,R.layout.content_list_notes,a_array);
        array = a_array;
        context = c_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.content_list_notes,parent,false);
        }

        Note currentNote = array.get(position);

        if(currentNote != null) {
            TextView item = (TextView) listItem.findViewById(R.id.textView);
            String tittle = currentNote.getTittle();
            item.setText(tittle);
        }

        return listItem;
    }
}
