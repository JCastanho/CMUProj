package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetLocalsTask;

public class ListLocalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);
        final ListView listView = (ListView) findViewById(R.id.list_tours);
        new GetLocalsTask(ListLocalsActivity.this).execute("location");

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int itemPosition=position;

                String itemValue = (String) listView.getItemAtPosition(position);

                Toast.makeText(ListLocalsActivity.this, "Downloading quiz for: " + itemValue, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListLocalsActivity.this, QuizActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("Title", itemValue);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void updateInterface(List<String> sucess){
        ListView listView = (ListView) findViewById(R.id.list_tours);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
        listView.setAdapter(adapter);
    }
}
