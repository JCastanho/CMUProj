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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ShareResultsActivity extends AppCompatActivity implements
		SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

	private static ArrayList<User> array;
	private static UserAdapter adapter;
	private ListView listView;
	private SimWifiP2pBroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharerslt);

		registerBroadcastReceiver();

		listView = findViewById(R.id.list_users);

		if (adapter == null) { setAdapter(); }
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		registerBroadcastReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
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
		array = new ArrayList<>();
		adapter = new UserAdapter(ShareResultsActivity.this,array);

		listView.setAdapter(adapter);
		listView.setEmptyView(findViewById(R.id.empty_list));
	}

	public String getUserAddress(String name){
		//this is called in UserAdapter
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
			if(!device.deviceName.matches("M[0-9]+"))
				array.add(new User(deviceName,device.getVirtIp()));

				//When clicking on the share button, this address will be sent to next activity on User Adapter
			else {
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

