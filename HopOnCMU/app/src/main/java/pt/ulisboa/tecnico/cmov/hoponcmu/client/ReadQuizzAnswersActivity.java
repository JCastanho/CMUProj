package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetCorrectAnswersTask;

public class ReadQuizzAnswersActivity extends AppCompatActivity {

    private List<Integer> correctAnswers;
    private int userId = -1;
    private String text;
    private ApplicationContextProvider applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_quizz_answers);
        applicationContext = (ApplicationContextProvider) getApplicationContext();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        final ListView listView = (ListView) findViewById(R.id.list_tours_answers);
        new GetAnsweredQuizzesTask(ReadQuizzAnswersActivity.this, userId).execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int itemPosition=position;

                text = (String) listView.getItemAtPosition(position);

                //Toast.makeText(ReadQuizzAnswersActivity.this, "Getting Quizz answers for: " + text, Toast.LENGTH_SHORT).show();

                if(applicationContext.checkQuizzResults(text)){
                    Toast.makeText(ReadQuizzAnswersActivity.this, applicationContext.getQuizzResults().get(text).get(0) + " 22222222 correct answers in " + applicationContext.getQuizzResults().get(text).get(1) + " seconds", Toast.LENGTH_SHORT).show();
                }
                else{
                    GetCorrectAnswersTask task = new GetCorrectAnswersTask(ReadQuizzAnswersActivity.this, userId);
                    task.execute(text);
                }
            }
        });
    }

    public void correctAnswers(List<Integer> answers){
        if (answers != null){
            this.correctAnswers = answers;
            applicationContext.setQuizzResults(text,answers);

            Toast.makeText(ReadQuizzAnswersActivity.this, answers.get(0) + " 111111111 correct answers in " + answers.get(1) + " seconds", Toast.LENGTH_SHORT).show();
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
