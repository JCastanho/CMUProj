package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.NearbyUser;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.UserAdapter;

public class PeerListenerActivity extends AppCompatActivity{

	private ArrayList<NearbyUser> array;
	private UserAdapter adapter;
	private ListView listView;
	private final String TAG = "NEARBY_USERS";
	private Boolean alive;
	private ApplicationContextProvider applicationContext;
	private Boolean mutex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharerslt);

		applicationContext = (ApplicationContextProvider) getApplicationContext();
		listView = findViewById(R.id.list_users);

		mutex(true);
		alive = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		alive = false;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		array.clear();
	}

	public void setAdapter(Context context) {
		Log.d("Set Adapter: ", ""+applicationContext.getGroupPeersList().size());

		array = applicationContext.getGroupPeersList();

		adapter = new UserAdapter(context,array);
		listView.setAdapter(adapter);

		checkEmptyList();
	}

	public void findGroupPeers(){
		new NearbyUsersTask().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void mutex(Boolean isOpen){
		mutex = isOpen;
	}

	public String getUserAddress(String name) {
		for(NearbyUser u: array) {
			if(u.getName().equals(name))
				return u.getAddress();
		}

		return "";
	}

	private void checkEmptyList(){
		if(array.size() == 0)
			listView.setEmptyView(findViewById(R.id.no_near_users));
	}

	public class NearbyUsersTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			while (alive) {

				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Log.d(TAG, "Working..");

				ArrayList<NearbyUser> updatedUsers = applicationContext.getGroupPeersList();

				if(mutex) {
					if (!(array.containsAll(updatedUsers) && updatedUsers.containsAll(array))) {
						mutex(false);
						Log.d(TAG, "Old list size: " + array.size());
						Log.d(TAG, "New list size: " + updatedUsers.size());
						array.clear();
						array.addAll(updatedUsers);
						Log.d(TAG, "Updated list size: " + array.size());
						publishProgress();
					}
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... params) {
			adapter.notifyDataSetChanged();
			checkEmptyList();
			mutex(true);
		}
	}
}