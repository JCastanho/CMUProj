package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

/**
 * Created by Daniela on 22/03/2018.
 */

public class ResultAdapter extends ArrayAdapter<String> {

    private ArrayList<String> array;
    private Context context;

    public ResultAdapter(Context c_context, ArrayList<String> a_array) {
        super(c_context, R.layout.checkbox, a_array);
        array = a_array;
        context = c_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.checkbox, parent, false);
        }

        String currentResult = array.get(position);

        if(currentResult != null) {
            CheckBox result = listItem.findViewById(R.id.checkBox);

            result.setText(currentResult);
        }

        return listItem;
    }

}