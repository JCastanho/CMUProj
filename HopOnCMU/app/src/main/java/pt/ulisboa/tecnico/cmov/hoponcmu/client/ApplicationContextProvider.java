package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.NearbyUser;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;

public class ApplicationContextProvider extends Application implements
        SimWifiP2pManager.GroupInfoListener{

    private Context context;
    private Integer nearBeacon = -1;
    private ArrayList<NearbyUser> groupPeers = new ArrayList<>();
    private HashMap<String,List<String>> sharedResults = new HashMap<>();
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private HashMap<String, List<Question>> quizz = new HashMap<>();
    private HashMap<Integer, List<String>> answeredQuizzes = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public void setManager(SimWifiP2pManager m) {
        mManager = m;
    }

    public void setChannel(SimWifiP2pManager.Channel c) {
        mChannel = c;
    }

    public ArrayList<NearbyUser> getGroupPeersList(){
        Log.d("App Context Info Peers",""+System.identityHashCode(groupPeers));
        return groupPeers;
    }

    public HashMap<String,List<String>> getSharedResults() { return sharedResults; }

    public HashMap<String, List<Question>> getQuizz() {
        return quizz;
    }

    public void setQuizz(HashMap<String, List<Question>> quizz) {
        this.quizz = quizz;
    }

    public HashMap<Integer, List<String>> getAnsweredQuizzes() {
        return answeredQuizzes;
    }

    public void setAnsweredQuizzes(HashMap<Integer, List<String>> answeredQuizzes) {
        this.answeredQuizzes = answeredQuizzes;
    }

    public Boolean nearBeacon(int monumentPos) {
        Log.d("App Context Info","Monument position: " + monumentPos + " Beacon : " + nearBeacon);
        return nearBeacon == monumentPos;
    }

    public Boolean betweenStops(){
        if(groupPeers.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void parseResult(String sharedResult) {
        String[] parsed = sharedResult.split(":");
        String[] parsedResults = parsed[1].split(",");
        String user = parsed[0];
        List<String> results = new ArrayList<>(Arrays.asList(parsedResults));

        if(sharedResults.containsKey(user)) {
            Log.d("App Context Info","NearbyUser already shared results with me");
            sharedResults.get(user).addAll(results);
        } else {
            Log.d("App Context Info",user + "didn't yet share results with me");
            sharedResults.put(user,results);
        }

        Log.d("App Context Info","Parsing done: " + sharedResults.size());
    }

    public void updateGroupPeers() {
        mManager.requestGroupInfo(mChannel,(SimWifiP2pManager.GroupInfoListener) context);
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
        Log.d("App Context Info", "Old list size: " + groupPeers.size());
        ArrayList<NearbyUser> auxList = new ArrayList<>();
        nearBeacon = -1;

        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);

            //Verify that we're not adding beacons to the list
            if(!deviceName.matches("M[0-9]+")) {
                auxList.add(new NearbyUser(deviceName, device.getVirtIp()));
                Log.d("App Context Info", "Device name added: " + deviceName);
            } else {
                nearBeacon = Integer.parseInt(deviceName.split("M")[1]);
                Log.d("App Context Info","Beacon near me: " + deviceName);
            }
        }

        groupPeers = auxList;
        Log.d("App Context Info", "New list size: " + groupPeers.size());
    }
}