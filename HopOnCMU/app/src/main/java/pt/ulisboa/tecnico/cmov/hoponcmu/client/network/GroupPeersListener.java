package pt.ulisboa.tecnico.cmov.hoponcmu.client.network;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.NearbyUser;

/**
 * Created by Daniela on 15/05/2018.
 */

public class GroupPeersListener implements Runnable{

    private final String TAG = "NEARBY_USERS";
    private final List<NearbyUser> array;
    private Context context;

    public GroupPeersListener(Context context, List<NearbyUser> array){
        this.array = array;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(TAG, "Finding nearby users...");

        ArrayList<NearbyUser> updatedUsers = ((ApplicationContextProvider) context).getGroupPeersList();

        if(!(array.containsAll(updatedUsers) && updatedUsers.containsAll(array))){
            array.addAll(updatedUsers);
        }
    }
}
