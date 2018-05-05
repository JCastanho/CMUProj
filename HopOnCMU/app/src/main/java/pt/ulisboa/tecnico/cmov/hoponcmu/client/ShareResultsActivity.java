package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ShareResultsActivity extends AppCompatActivity implements
        PeerListListener {

    public static final String TAG = "beaconsfinder";

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

		//final Integer identifier = getIntent().getExtras().getInt("id",-1);

		// Register broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		mReceiver = new SimWifiP2pBroadcastReceiver(this);
		registerReceiver(mReceiver, filter);

		// Set adapter for user list
		if (adapter == null) { setAdapter(); }
	}

	private void setAdapter() {
		array = new ArrayList<>();
		adapter = new UserAdapter(this,array);

		ListView listView = findViewById(R.id.list_users);
		listView.setAdapter(adapter);
	}

	public void shareResult(View view) {
		Intent intent = new Intent(ShareResultsActivity.this, ListResultsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		wifiOn();
	}

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);

		super.onDestroy();
	}

	public void wifiOn(){
		Intent intent = new Intent(ShareResultsActivity.this, SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;
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
			//Verify that we're not adding beacon devices
			array.add(device.deviceName);
		}

		adapter.notifyDataSetChanged();
	}
}