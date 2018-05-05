package pt.ulisboa.tecnico.cmov.hoponcmu.client;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ListResultsActivity extends AppCompatActivity {

	private ResultAdapter adapter;
	private ArrayList<String> array;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listrslt);

		ListView lResults = findViewById(R.id.list_results);

		if (adapter == null) { setAdapter(lResults); }

		lResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Log.d("Clicked on check box","oh wee");
			}
		});

	}

	private void setAdapter(ListView listView) {
		array = new ArrayList<>();
		array.add("100%");
		array.add("50%");
		array.add("70%");
		array.add("50%");
		adapter = new ResultAdapter(this,array);

		listView.setAdapter(adapter);
	}

	//get results
}
