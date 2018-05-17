package pt.ulisboa.tecnico.cmov.hoponcmu.client.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {


    public SimWifiP2pBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
        		Toast.makeText(ApplicationContextProvider.getContext(), "WiFi Direct enabled",
        				Toast.LENGTH_SHORT).show();
            } else {
        		Toast.makeText(ApplicationContextProvider.getContext(), "WiFi Direct disabled",
        				Toast.LENGTH_SHORT).show();
            }

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

        	/*Toast.makeText(mActivity, "Peer list changed",
    				Toast.LENGTH_SHORT).show();*/

        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {

        	SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
        			SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
        	ginfo.print();
    		Toast.makeText(ApplicationContextProvider.getContext(), "Network membership changed",
    				Toast.LENGTH_SHORT).show();
            ApplicationContextProvider.updateGroupPeers();


        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

        	SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
        			SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
        	ginfo.print();
    		Toast.makeText(ApplicationContextProvider.getContext(), "Group ownership changed",
    				Toast.LENGTH_SHORT).show();
        }
    }
}