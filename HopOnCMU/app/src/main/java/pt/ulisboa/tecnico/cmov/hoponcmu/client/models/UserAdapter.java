package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

/**
 * Created by Daniela on 05/05/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.AskNativesActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListResultsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.PeerListenerActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ShareResultsActivity;

public class UserAdapter extends ArrayAdapter<NearbyUser> {
    private final ArrayList<NearbyUser> array;
    private final Context context;

    public UserAdapter(Context c_context, ArrayList<NearbyUser> a_array) {
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

        NearbyUser currentUser = array.get(position);

        if(currentUser != null) {
            final TextView user = (TextView) listItem.findViewById(R.id.neighborUser);
            final String neighborName = currentUser.getName();
            user.setText(neighborName);

            final PeerListenerActivity activity = (PeerListenerActivity) context;

            ImageButton btn = (ImageButton) listItem.findViewById(R.id.shareResult_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String neighborAddress = activity.getUserAddress(neighborName);

                    if(context instanceof ShareResultsActivity) {
                        Log.d("UserAdapter","ShareResultsActivity");

                        Intent intent = new Intent(activity, ListResultsActivity.class);

                        intent.putExtra("Username", neighborName);
                        intent.putExtra("UserAddr", neighborAddress);

                        activity.startActivity(intent);
                    } else {
                        Log.d("UserAdapter","AskNativeActivity");

                        AskNativesActivity askNativesActvt = (AskNativesActivity) context;
                        askNativesActvt.sendResults(neighborAddress);
                    }
                }
            });
        }

        return listItem;
    }
}