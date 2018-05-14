package pt.ulisboa.tecnico.cmov.hoponcmu.client;

/**
 * Created by Daniela on 05/05/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class UserAdapter extends ArrayAdapter<User> {
    private final ArrayList<User> array;
    private final Context context;

    public UserAdapter(Context c_context, ArrayList<User> a_array) {
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

        User currentUser = array.get(position);

        if(currentUser != null) {
            final TextView user = (TextView) listItem.findViewById(R.id.neighborUser);
            final String neighborName = currentUser.getName();
            user.setText(neighborName);

            final ShareResultsActivity activity = (ShareResultsActivity) context;

            ImageButton btn = (ImageButton) listItem.findViewById(R.id.shareResult_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ListResultsActivity.class);

                   // Bundle bundle = new Bundle();
                    intent.putExtra("UserAddr", activity.getUserAddress(neighborName));
                    intent.putExtra("Username", neighborName);

                    //bundle.putString("Username", neighborName);
                    //intent.putExtras(bundle);

                    activity.startActivity(intent);
                }
            });
        }

        return listItem;
    }
}