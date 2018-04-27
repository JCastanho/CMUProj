package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class ListTourLocal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);
        ListView listView = (ListView) findViewById(R.id.list_tours);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                ListView listview = findViewById(R.id.list_tours);

                String text = (String) listview.getItemAtPosition(position);

                Toast.makeText(ListTourLocal.this, "Downloading quiz for: " + text, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListTourLocal.this, QuizActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("Title", text);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


}