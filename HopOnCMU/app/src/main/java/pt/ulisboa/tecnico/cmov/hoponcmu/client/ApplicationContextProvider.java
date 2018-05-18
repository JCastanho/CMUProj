package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.NearbyUser;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class ApplicationContextProvider extends Application implements
        SimWifiP2pManager.GroupInfoListener{

    private static Context context;
    private Integer nearBeacon = -1;
    private ArrayList<NearbyUser> groupPeers = new ArrayList<>();
    private HashMap<String,List<String>> sharedResults = new HashMap<>();
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private HashMap<String, List<Integer>> quizzResults = new HashMap<>();
    private HashMap<String, List<Question>> quizz = new HashMap<>();
    private HashMap<Integer, List<String>> answeredQuizzes = new HashMap<>();

    // Security
    private EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");
    private ArrayList<String> nonces = new ArrayList<String>();
    private Date lastClear = Calendar.getInstance().getTime();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
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

    public HashMap<String, List<Integer>> getQuizzResults() {
        return quizzResults;
    }

    public void setQuizzResults(String monumento, List<Integer> quizzResults) {
        this.quizzResults.put(monumento, quizzResults);
    }

    public Boolean checkQuizzResults(String monumento){
        return quizzResults.containsKey(monumento);
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

    public Boolean checkDownloadedQuizz(String monumento){
        return quizz.containsKey(monumento);
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



    public String checkNonce(byte[] encryptedNonce){
        try {
            String nonce = new String(encryption.decrypt(encryptedNonce),"UTF-8");

            if(nonces.contains(nonce)) {
                return "NOK";
            }

            String[] parts = nonce.split("#");

            // Test Freshness
            Calendar nonceCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            nonceCalendar.setTime(sdf.parse(parts[1]));

            Date nonceDate = nonceCalendar.getTime();
            Calendar now = Calendar.getInstance();
            Date dNow = now.getTime();

            Calendar limit = now;
            limit.add(Calendar.HOUR, -2);
            Date dLimit = limit.getTime();

            if(dLimit.before(nonceDate) && dNow.after(nonceDate)) {
                nonces.add(nonce);
                return nonce;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NOK";
    }

    private void cleanNonces() {
        try {
            Calendar now = Calendar.getInstance();
            Calendar scheduled = now;
            scheduled.add(Calendar.DATE, -1);

            if(scheduled.before(lastClear)) {
                for(String nonce : nonces) {
                    String[] parts = nonce.split("#");


                    Calendar nonceCalendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    nonceCalendar.setTime(sdf.parse(parts[1]));
                    Date nonceDate = nonceCalendar.getTime();

                    Calendar limit = now;
                    limit.add(Calendar.HOUR, -1);

                    if(limit.after(nonceDate)) {
                        nonces.remove(nonce);
                    };
                }

                lastClear = Calendar.getInstance().getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}