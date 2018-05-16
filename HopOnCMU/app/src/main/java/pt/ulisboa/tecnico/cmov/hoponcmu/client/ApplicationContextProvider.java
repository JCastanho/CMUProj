package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.User;

public class ApplicationContextProvider extends Application implements
        SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    private static Context context;
    private static ArrayList<User> peers;
    private static ArrayList<User> groupPeers;
    private static HashMap<String,List<String>> sharedResults;
    private static SimWifiP2pManager mManager = null;
    private static SimWifiP2pManager.Channel mChannel = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        groupPeers = new ArrayList<>();
        peers = new ArrayList<>();
        sharedResults = new HashMap<>();
    }

    public static Context getContext() {
        return context;
    }

    public static void setManager(SimWifiP2pManager m) {
        mManager = m;
    }

    public static void setChannel(SimWifiP2pManager.Channel c) {
        mChannel = c;
    }

    public static ArrayList<User> getPeersList(){ return peers; }

    public static ArrayList<User> getGroupPeersList(){ return groupPeers; }

    public static HashMap<String,List<String>> getSharedResults() { return sharedResults; }

    public static void parseResult(String sharedResult) {
        String[] parsed = sharedResult.split(":");
        String[] parsedResults = parsed[1].split(",");
        String user = parsed[0];
        List<String> results = new ArrayList<>(Arrays.asList(parsedResults));

        if(sharedResults.containsKey(user)) {
            Log.d("App Context Info","User already shared results with me");
            sharedResults.get(user).addAll(results);
        } else {
            Log.d("App Context Info",user + "didn't yet share results with me");
            sharedResults.put(user,results);
        }

        Log.d("App Context Info","Parsing done: " + sharedResults.size());
        //TODO Insert on sqllite table eventually
    }

    public static void updateGroupPeers() {
        mManager.requestGroupInfo(mChannel,(SimWifiP2pManager.GroupInfoListener) context);
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
        Log.d("App Context Info", "Old list size: " + groupPeers.size());
        ArrayList<User> auxList = new ArrayList<>();

        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);

            //Verify that we're not adding beacons to the list
            if(!deviceName.matches("M[0-9]+")) {
                auxList.add(new User(deviceName, device.getVirtIp()));
                Log.d("App Context Info", "Device name added: " + deviceName);
            }
        }
        groupPeers = auxList;
        Log.d("App Context Info", "New list size: " + groupPeers.size());
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {
        //TODO onPeersAvailable
    }
}