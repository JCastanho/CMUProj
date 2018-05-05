package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

    private ShareResultsActivity mActivity;

    public SimWifiP2pBroadcastReceiver(ShareResultsActivity activity) {
        super();
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        	// This action is triggered when the Termite service changes state:
        	// - creating the service generates the WIFI_P2P_STATE_ENABLED event
        	// - destroying the service generates the WIFI_P2P_STATE_DISABLED event

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
        		Toast.makeText(mActivity, "Wi-Fi Direct enabled",
        				Toast.LENGTH_SHORT).show();
            } /*else {
        		Toast.makeText(mActivity, "Wi-Fi Direct disabled",
        				Toast.LENGTH_SHORT).show();
            }*/

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

        	Toast.makeText(mActivity, "New users detected",
    				Toast.LENGTH_SHORT).show();
        	mActivity.showPeers();
        }
    }
}
