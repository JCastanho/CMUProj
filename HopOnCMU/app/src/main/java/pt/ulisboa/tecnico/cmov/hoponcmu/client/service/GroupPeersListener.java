package pt.ulisboa.tecnico.cmov.hoponcmu.client.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.NearbyUser;

/**
 * Created by Daniela on 15/05/2018.
 */

public class GroupPeersListener implements Runnable{

    private final String TAG = "NEARBY_USERS";
    private final List<NearbyUser> array;

    public GroupPeersListener(List<NearbyUser> array){
        this.array = array;
    }

    @Override
    public void run() {
        /*Log.d(TAG, "Finding nearby users...");

        ArrayList<NearbyUser> updatedUsers = ApplicationContextProvider.getGroupPeersList();

        if(!(array.containsAll(updatedUsers) && updatedUsers.containsAll(array))){
            array.addAll(updatedUsers);
        }*/
    }
}
