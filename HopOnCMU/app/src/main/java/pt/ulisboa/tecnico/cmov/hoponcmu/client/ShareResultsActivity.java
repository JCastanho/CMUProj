package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.User;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.UserAdapter;

public class ShareResultsActivity extends AppCompatActivity{

	private ArrayList<User> array;
	private UserAdapter adapter;
	private ListView listView;
	private final String TAG = "NEARBY_USERS";
	private Boolean alive;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharerslt);

		listView = findViewById(R.id.list_users);
		alive = true;

		setAdapter();

		new NearbyUsersTask().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR);
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

	private void setAdapter() {
		array = ApplicationContextProvider.getGroupPeersList();
		Log.d("Set Adapter: ", ""+array.size());
		adapter = new UserAdapter(ShareResultsActivity.this, array);
		listView.setAdapter(adapter);
		checkEmptyList();
	}

	public String getUserAddress(String name) {
		for(User u: array) {
			if(u.getName().equals(name))
				return u.getAddress();
		}

		return "";
	}

	private void checkEmptyList(){
		if(array.size() == 0)
			listView.setEmptyView(findViewById(R.id.no_near_users));
	}
	private ArrayList<String> getListOfUsernames(ArrayList<User> users) {
		ArrayList<String> usernames = new ArrayList<>();

		for(User user: users) {
			usernames.add(user.getName());
		}

		return usernames;
	}

	public class NearbyUsersTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			Log.d(TAG, "Working...");

			while (alive) {
				ArrayList<User> updatedUsers = ApplicationContextProvider.getGroupPeersList();

				/*List<String> updatedUsersSorted = getListOfUsernames(updatedUsers);
				List<String> arraySorted = getListOfUsernames(array);
				Collections.sort(updatedUsersSorted);
				Collections.sort(arraySorted);

				if(!updatedUsers.equals(arraySorted)) {
					Log.d(TAG, "Old list size: " + array.size());
					array.clear();
					array.addAll(updatedUsers);
					Log.d(TAG, "New list size: " + array.size());
					publishProgress();
				}*/

				//TODO Fix concurrent modification
				if(!(array.containsAll(updatedUsers) && updatedUsers.containsAll(array))){
					Log.d(TAG, "Old list size: " + array.size());
					Log.d(TAG, "New list size: " + updatedUsers.size());
					Log.d(TAG, "New list size app: " + ApplicationContextProvider.getGroupPeersList().size());
					array.clear();
					array.addAll(updatedUsers);
					Log.d(TAG, "Updated list size: " + array.size());
					publishProgress();
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... params) {
			adapter.notifyDataSetChanged();
			checkEmptyList();
		}
	}
}