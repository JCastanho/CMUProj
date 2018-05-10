package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;
import android.content.ServiceConnection;

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pManager;

public class ApplicationContextProvider extends Application {

    //private static Application application;
    private static Context context;
    private static ArrayList<User> peers;
    private static ArrayList<User> groupPeers;
    private static SimWifiP2pManager mManager = null;
    private static SimWifiP2pManager.Channel mChannel = null;
    private static ServiceConnection mConnection = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        groupPeers = new ArrayList<>();
        peers = new ArrayList<>();
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

    public static SimWifiP2pManager getManager() {
        return mManager;
    }

    public static SimWifiP2pManager.Channel getChannel() {
        return mChannel;
    }

    public static void setPeers(ArrayList<User> p) { peers = p; }

    public static void setGroupPeers(ArrayList<User> p) {
        groupPeers = p;
    }

    public static ArrayList<User> peersList(){ return peers; }

    public static ArrayList<User> groupPeersList(){ return groupPeers; }
}
