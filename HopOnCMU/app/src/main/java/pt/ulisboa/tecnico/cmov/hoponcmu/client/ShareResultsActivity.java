package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ShareResultsActivity extends AppCompatActivity implements
		SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

	private ArrayList<User> array;
	private UserAdapter adapter;
	private ListView listView;
	private SimWifiP2pBroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharerslt);

		setAdapter();
		//Log.d("RESULTS SHARE INFO","Adapter is not null");
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerBroadcastReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		ApplicationContextProvider.setGroupPeers(array);
	}

	private void registerBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();

		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);

		mReceiver = new SimWifiP2pBroadcastReceiver(this);

		registerReceiver(mReceiver, filter);
	}

	private void setAdapter() {
		listView = findViewById(R.id.list_users);

		array = ApplicationContextProvider.groupPeersList();
		adapter = new UserAdapter(ShareResultsActivity.this,array);

		listView.setAdapter(adapter);

		checkEmptyList();
	}

	public String getUserAddress(String name){
		for(User u: array){
			if(u.getName().equals(name))
				return u.getAddress();
		}

		return "";
	}

	private void checkEmptyList(){
		if(array.size() == 0)
			listView.setEmptyView(findViewById(R.id.empty_list));
	}

	public void updatePeers() {
		ApplicationContextProvider.getManager().requestPeers(ApplicationContextProvider.getChannel(), ShareResultsActivity.this);
	}

	public void updateGroupPeers() {
		ApplicationContextProvider.getManager().requestGroupInfo(ApplicationContextProvider.getChannel(), ShareResultsActivity.this);
	}

	@Override
	public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
		array.clear();

		for (String deviceName : groupInfo.getDevicesInNetwork()) {
			SimWifiP2pDevice device = devices.getByName(deviceName);

			//Verify that we're not adding beacons to the list
			if(!device.deviceName.matches("M[0-9]+")) {
				array.add(new User("" + deviceName + " (" +
						((device == null) ? "??" : device.getVirtIp()) + ")", device.getVirtIp()));
			} else {
				//verify if i'm close
			}
		}

		adapter.notifyDataSetChanged();
		checkEmptyList();
	}

	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		//verify if i'm close
	}
}