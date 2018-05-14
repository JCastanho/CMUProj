package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

/**
 * Created by Daniela on 05/05/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;

public class LocationAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> array;
    private final Context context;

    public LocationAdapter(Context c_context, ArrayList<String> a_array) {
        super(c_context, R.layout.content_location, a_array);
        array = a_array;
        context = c_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.content_location, parent, false);
        }

        String currentLocation = array.get(position);

        if(currentLocation != null) {
            final TextView location = (TextView) listItem.findViewById(R.id.location);
            location.setText(currentLocation);

            final ListLocalsActivity activity = (ListLocalsActivity) context;

            ImageButton btn = (ImageButton) listItem.findViewById(R.id.dwnldQuizz_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, QuizActivity.class);
                    //intent.putExtra("UserAddr", context.getUserAddress(neighborName));
                    activity.startActivity(intent);
                }
            });
        }

        return listItem;
    }
}