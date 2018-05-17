package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.ResultAdapter;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;
	private String userAddress;
	private String username;
	private SimWifiP2pSocket mCliSocket = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		SimWifiP2pSocketManager.Init(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		userAddress = extras.getString("UserAddr");
		username = extras.getString("Username");

		if (adapter == null) {
			ListView lResults = (ListView) findViewById(R.id.list_results);
			setAdapter(lResults);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.sendResults){
			String results = "";

			for(String rslt: adapter.getCheckedResults()){
				results += rslt + ",";
			}

			new sendMessageTask().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR,
					userAddress, username, results);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.results_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void setAdapter(ListView listView) {
		array = new ArrayList<>();
		array.add("100%");
		array.add("50%");
		array.add("70%");

		// TODO: Get results in server
		//new GetCorrectAnswersCommand().execute()

		adapter = new ResultAdapter(this,array);

		listView.setAdapter(adapter);
	}

	/*
     * Asynctask implementing message exchange
     */

	public class sendMessageTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				mCliSocket = new SimWifiP2pSocket(params[0],
						Integer.parseInt(getString(R.string.port)));

				mCliSocket.getOutputStream().write((params[1] + ":" + params[2] + "\n").getBytes());
				BufferedReader sockIn = new BufferedReader(
						new InputStreamReader(mCliSocket.getInputStream()));
				sockIn.readLine();
				mCliSocket.close();
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			mCliSocket = null;

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(),"Results Sent!",Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
}