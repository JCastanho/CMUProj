package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetUsersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.ShareResultsTask;


public class ShareResultsActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_rslts);

        final Integer identifier = getIntent().getExtras().getInt("id",-1);
        listView = (ListView) findViewById(R.id.list_users);

        new GetUsersTask(ShareResultsActivity.this).execute(identifier);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                new ShareResultsTask(ShareResultsActivity.this).execute(identifier,position);
            }
        });

    }

    public void updateUserList(List<String> users) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, users);
        listView.setAdapter(adapter);
    }

    public void updateInterface(Boolean success) {
        if (success) {
            Toast.makeText(this, "The results were successfully shared", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "It was results couldn't be shared", Toast.LENGTH_SHORT).show();
        }
    }
}