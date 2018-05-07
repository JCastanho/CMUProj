package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ShareResultsActivity extends AppCompatActivity implements
        PeerListListener {

    public static final String TAG = "beaconsfinder";
	static final int PICK_CONTACT_REQUEST = 1;

    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;
	private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private UserAdapter adapter;
	private ArrayList<String> array;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharerslt);

		registerBroadcastReceiver();

		// Set adapter for user list
		if (adapter == null) { setAdapter(); }

		wifiOn();
	}

    @Override
    public void onPause() {
		unregisterReceiver(mReceiver);
		super.onPause();
    }

	@Override
	protected void onRestart() {
		wifiOn();
		registerBroadcastReceiver();
		super.onRestart();
	}

	public void registerBroadcastReceiver(){
		IntentFilter filter = new IntentFilter();

		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);

		mReceiver = new SimWifiP2pBroadcastReceiver(this);

		registerReceiver(mReceiver, filter);
	}

	private void setAdapter() {
		array = new ArrayList<>();
		adapter = new UserAdapter(this,array);

		ListView listView = findViewById(R.id.list_users);

		listView.setAdapter(adapter);
		listView.setEmptyView(findViewById(R.id.empty_list));
	}

	public void wifiOn(){
		Intent intent = new Intent(ShareResultsActivity.this, SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;
	}

	public void showResultsToShare(View view) {
		Intent intent = new Intent(ShareResultsActivity.this, ListResultsActivity.class);
		startActivityForResult(intent,PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CONTACT_REQUEST) {
			if (resultCode == RESULT_OK) {
				String results = data.getStringExtra("Results");
				Toast.makeText(getBaseContext(),results,Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void showPeers(){
		if(mBound) {
			mManager.requestPeers(mChannel, ShareResultsActivity.this);
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		// callbacks for service binding, passed to bindService()

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mManager = null;
            mChannel = null;
            mBound = false;
		}
	};


	/*
	 * Termite listeners
	 */
	
	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		array.clear();

		for (SimWifiP2pDevice device : peers.getDeviceList()) {

			//Verify that we're not adding beacons to the list
			if(!device.deviceName.matches("M[0-9]+"))
				array.add(device.deviceName);
		}

		adapter.notifyDataSetChanged();
	}

}