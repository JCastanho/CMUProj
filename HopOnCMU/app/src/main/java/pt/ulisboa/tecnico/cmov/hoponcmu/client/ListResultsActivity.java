package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;
	private ArrayList<String> checkedResults = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		ListView lResults = (ListView) findViewById(R.id.list_results);

		if (adapter == null) { setAdapter(lResults); }

		lResults.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int item,
									long id) {

				ListView checkedRslt = (ListView) parent;

				Toast.makeText(getBaseContext(),"h wee",Toast.LENGTH_SHORT).show();

				/*if(checkedRslt.isItemChecked(item))
					checkedResults.add(text);
				else
					checkedResults.remove(text);*/
			}
		});
	}

	private void setAdapter(ListView listView) {
		array = new ArrayList<>();
		array.add("100%");
		array.add("50%");
		array.add("70%");
		adapter = new ResultAdapter(this,array);

		listView.setAdapter(adapter);
	}

    public void shareResults(View view) {
		Intent intent = new Intent(ListResultsActivity.this, ShareResultsActivity.class);
		String results = "";

		for(String rslt: checkedResults){
			results += rslt;
		}

		intent.putExtra("Results",results);
		setResult(RESULT_OK,intent);
		finish();
	}

	// TODO: Get results in server
}
