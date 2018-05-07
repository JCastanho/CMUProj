package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		ListView lResults = (ListView) findViewById(R.id.list_results);

		if (adapter == null) { setAdapter(lResults); }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.sendResults){

			Intent intent = new Intent(ListResultsActivity.this, ShareResultsActivity.class);
			String results = "";

			for(String rslt: adapter.getCheckedResults()){
				results += rslt;
			}

			intent.putExtra("Results",results);
			setResult(RESULT_OK,intent);
			finish();
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

		adapter = new ResultAdapter(this,array);

		listView.setAdapter(adapter);
	}

}
