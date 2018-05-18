package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetCorrectAnswersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.ResultAdapter;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.network.SendMessageTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetCorrectAnswersCommand;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;
	private String userAddress;
	private String username;
	private ApplicationContextProvider applicationContext;
	private int userId;
	private boolean mutex;
	private boolean finished;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		applicationContext = (ApplicationContextProvider) getApplicationContext();

		SimWifiP2pSocketManager.Init(applicationContext);

		Bundle extras = getIntent().getExtras();
		userAddress = extras.getString("UserAddr");
		username = extras.getString("Username");
		userId = extras.getInt("id");

		ListView lResults = (ListView) findViewById(R.id.list_results);
		setAdapter(lResults);

		mutex = true;
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

			new SendMessageTask(ListResultsActivity.this).executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR,
					userAddress, "1-" + username + ":" + results);

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

	private void setAdapter(ListView listView) {
		array = new ArrayList<>();

		List<String> answeredQuizzes = applicationContext.getAnsweredQuizzes();
		HashMap<String, List<Integer>> quizzResults = applicationContext.getQuizzResults();

		for(String answeredQuizz: answeredQuizzes){
			Log.d("ListResultsActivity",answeredQuizz);
			if(quizzResults.containsKey(answeredQuizz)){
				//mutex(false);
				array.add(quizzResults.get(answeredQuizz)+ "correct answers for quiz " + answeredQuizz);
				//mutex(true);
			/*} else {
				finished = false;
				new GetCorrectAnswersTask(ListResultsActivity.this).execute(""+userId,answeredQuizz);*/
			}
		}

		//if(finished) {
		adapter = new ResultAdapter(this, array);
		listView.setAdapter(adapter);
		//}
	}

	public void mutex(Boolean isOpen){
		mutex = isOpen;
	}

	public void updateInterface(String monument, List<Integer> o) {
		//while()
		array.add(o.get(0) + " correct answers for quiz " + monument);
		finished = true;
			//mutex(true);
		//}
	}
}