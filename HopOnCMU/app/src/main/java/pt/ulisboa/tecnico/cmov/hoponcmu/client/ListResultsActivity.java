package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetCorrectAnswersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.ResultAdapter;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.network.SendMessageTask;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;
	private String userAddress;
	private String username;
	private ListView listView;
	private TextView message;
	private int userId;
	private boolean finished;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		SimWifiP2pSocketManager.Init(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		userAddress = extras.getString("UserAddr");
		username = extras.getString("Username");
		userId = extras.getInt("id");

		listView= (ListView) findViewById(R.id.list_results);
		message = (TextView) findViewById(R.id.no_results);

		finished = true;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				setAdapter();
			}
		};

		runnable.run();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finished = true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.sendResults){
			String results = "";
			ArrayList<String> checkedResults = adapter.getCheckedResults();
			int lastResultPos = checkedResults.size();

			for(int position = 0; position < lastResultPos; position++){

				if(position != position-1){
					results += checkedResults.get(position) + ",";
				} else {
					results += checkedResults.get(position);
				}
			}

			if(results.length() != 0) {
				new SendMessageTask(ListResultsActivity.this).executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR,
						userAddress, "1-" + username + ":" + results);
			} else {
				Toast.makeText(ListResultsActivity.this, "No results were shared", Toast.LENGTH_SHORT).show();
			}

			this.finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.results_menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void setAdapter() {
		array = new ArrayList<>();

		List<String> answeredQuizzes = Singleton.getInstance().getAnsweredQuizzes();
		HashMap<String, List<Integer>> quizzResults = Singleton.getInstance().getQuizzResults();

		for(String answeredQuizz: answeredQuizzes){
			Log.d("ListResultsActivity","answered quizzzz");

			if(quizzResults.containsKey(answeredQuizz)){
				array.add(quizzResults.get(answeredQuizz).get(0)+ " correct answers for quiz " + answeredQuizz);
			} else {
				finished = false;
				new GetCorrectAnswersTask(ListResultsActivity.this).execute(""+userId,answeredQuizz);
			}
		}

		if(finished)
			finishedList();
	}

	public void finishedList(){
		adapter = new ResultAdapter(this, array);
		listView.setAdapter(adapter);
		checkEmptyResultList();
	}

	public void checkEmptyResultList(){
		if(array.size() == 0){
			listView.setEmptyView(message);
		}
	}

	public synchronized void updateInterface(String monument, List<Integer> o) {
		Log.d("ListResultsActivity","updateInterface");
		array.add(o.get(0) + " correct answers for quiz " + monument);
		finishedList();
	}
}