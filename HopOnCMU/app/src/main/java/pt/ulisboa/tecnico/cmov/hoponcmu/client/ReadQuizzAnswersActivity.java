package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetCorrectAnswersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetLocalsTask;

public class ReadQuizzAnswersActivity extends AppCompatActivity {

    private int correctAnswers;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_quizz_answers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        final ListView listView = (ListView) findViewById(R.id.list_tours_answers);
        new GetLocalsTask(ReadQuizzAnswersActivity.this).execute("location");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int itemPosition=position;

                String text = (String) listView.getItemAtPosition(position);

                Toast.makeText(ReadQuizzAnswersActivity.this, "Getting Quizz answers for: " + text, Toast.LENGTH_SHORT).show();

                GetCorrectAnswersTask task = new GetCorrectAnswersTask(ReadQuizzAnswersActivity.this, userId);
                task.execute(text);

            }
        });
    }

    public void correctAnswers(int answers){
        if (answers != -1){
            this.correctAnswers = answers;
            Toast.makeText(ReadQuizzAnswersActivity.this, Integer.toString(answers) + " of 4", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ReadQuizzAnswersActivity.this, "You didn't answer this Quizz", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateInterface(List<String> sucess){

        ListView listView = (ListView) findViewById(R.id.list_tours_answers);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
        listView.setAdapter(adapter);
    }

}
