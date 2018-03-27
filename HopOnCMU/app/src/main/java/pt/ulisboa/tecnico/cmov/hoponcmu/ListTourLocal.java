package pt.ulisboa.tecnico.cmov.hoponcmu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListTourLocal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);
        ListView listView = (ListView) findViewById(R.id.list_tours);
    }
}
