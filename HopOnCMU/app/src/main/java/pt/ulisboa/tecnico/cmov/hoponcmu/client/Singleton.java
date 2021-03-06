package pt.ulisboa.tecnico.cmov.hoponcmu.client;

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

public final class Singleton implements SimWifiP2pManager.GroupInfoListener{

    private static Singleton singleton = new Singleton( );
    private Integer nearBeacon = -1;
    private ArrayList<NearbyUser> groupPeers = new ArrayList<>();
    private HashMap<String,List<String>> sharedResults = new HashMap<>();
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private HashMap<String, List<Integer>> quizzResults = new HashMap<>();
    private HashMap<String, List<Question>> quizz = new HashMap<>();
    private List<String> answeredQuizzes = new ArrayList<>();

    private Singleton() {         Log.d("Singleton","fui criado");
    }

    public static Singleton getInstance( ) {
        return singleton;
    }

    protected void setManager(SimWifiP2pManager m) {
        mManager = m;
    }

    public void setChannel(SimWifiP2pManager.Channel c) {
        mChannel = c;
    }

    public ArrayList<NearbyUser> getGroupPeersList(){
        //Log.d("App Context Info Peers",""+System.identityHashCode(groupPeers));
        return groupPeers;
    }

    public HashMap<String,List<String>> getSharedResults() { return sharedResults; }

    public List<Question> getQuizz(String monumento) { return quizz.get(monumento); }

    public HashMap<String, List<Integer>> getQuizzResults() {
        return quizzResults;
    }

    public void addQuizz(String monument, List<Question> quizzz) {
        Log.d("App Context Info Peers","Size before" +this.quizz.size());
        this.quizz.put(monument,quizzz);
        Log.d("App Context Info Peers","Size after: "+this.quizz.size()+quizz.containsKey(monument));

    }

    public List<String> getAnsweredQuizzes() { return answeredQuizzes; }

    public void setAnsweredQuizzes(List<String > answeredQuizzzes) {
        this.answeredQuizzes = answeredQuizzzes;
        Log.d("App Context Info", "Answered Quizzes set");
    }

    public void addAnsweredQuizzes(String answeredQuizz) { this.answeredQuizzes.add(answeredQuizz); }

    public void addQuizzResults(String monumento, List<Integer> quizzResults) { this.quizzResults.put(monumento, quizzResults); }

    public Boolean checkQuizzResults(String monumento){ return quizzResults.containsKey(monumento); }

    public Boolean checkDownloadedQuizz(String monumento){
        return quizz.containsKey(monumento);
    }

    public Boolean checkAnsweredQuizz(String monumento){ return answeredQuizzes.contains(monumento); }

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

            List<String> oldResults = sharedResults.get(user);

            for(String result: results) {
                if (!oldResults.contains(result))
                    sharedResults.get(user).add(result);
            }
        } else {
            Log.d("App Context Info",user + "didn't yet share results with me");
            sharedResults.put(user,results);
        }
    }

    public void updateGroupPeers() {
        mManager.requestGroupInfo(mChannel, (SimWifiP2pManager.GroupInfoListener) Singleton.getInstance());
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
        Log.d("App Context Info", "Old list size: " + groupPeers.size());
        ArrayList<NearbyUser> auxList = new ArrayList<>();
        nearBeacon = -1;

        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);

            //Verify that we're not adding beacons to the list
            if (!deviceName.matches("M[0-9]+")) {
                auxList.add(new NearbyUser(deviceName, device.getVirtIp()));
                //Log.d("App Context Info", "Device name added: " + deviceName);
            } else {
                nearBeacon = Integer.parseInt(deviceName.split("M")[1]);
                //Log.d("App Context Info","Beacon near me: " + deviceName);
            }
        }

        groupPeers = auxList;
        Log.d("App Context Info", "Updated list size: " + groupPeers.size());
    }
}