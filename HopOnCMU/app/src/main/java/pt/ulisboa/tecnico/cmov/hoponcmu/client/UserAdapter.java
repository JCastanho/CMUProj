package pt.ulisboa.tecnico.cmov.hoponcmu.client;

/**
 * Created by Daniela on 05/05/2018.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

/**
 * Created by Daniela on 22/03/2018.
 */

public class UserAdapter extends ArrayAdapter<String> {

    private ArrayList<String> array;
    private Context context;

    public UserAdapter(Context c_context, ArrayList<String> a_array) {
        super(c_context, R.layout.content_userlist, a_array);
        array = a_array;
        context = c_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.content_userlist, parent, false);
        }

        String currentUser = array.get(position);

        if(currentUser != null) {
            TextView user = (TextView) listItem.findViewById(R.id.neighborUser);

            user.setText(currentUser);
        }

        return listItem;
    }
}