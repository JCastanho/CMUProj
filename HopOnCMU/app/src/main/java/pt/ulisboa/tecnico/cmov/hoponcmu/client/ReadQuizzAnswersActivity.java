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
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetCorrectAnswersTask;

public class ReadQuizzAnswersActivity extends AppCompatActivity {

    //private List<Integer> correctAnswers;
    private int userId = -1;
    private String text;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_quizz_answers);

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        listView = (ListView) findViewById(R.id.list_tours_answers);

        updateInterface(Singleton.getInstance().getAnsweredQuizzes());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int itemPosition=position;

                text = (String) listView.getItemAtPosition(position);

                if(Singleton.getInstance().checkQuizzResults(text)){
                    Toast.makeText(ReadQuizzAnswersActivity.this, Singleton.getInstance().getQuizzResults().get(text).get(0) + " 22222222 correct answers in " + Singleton.getInstance().getQuizzResults().get(text).get(1) + " seconds", Toast.LENGTH_SHORT).show();
                }
                else{
                    GetCorrectAnswersTask task = new GetCorrectAnswersTask(ReadQuizzAnswersActivity.this);
                    task.execute(""+userId,text);
                }
            }
        });
    }

    public void correctAnswers(List<Integer> answers){
        if (answers != null){
            Singleton.getInstance().addQuizzResults(text,answers);
            Toast.makeText(ReadQuizzAnswersActivity.this, answers.get(0) + " 111111111 correct answers in " + answers.get(1) + " seconds", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ReadQuizzAnswersActivity.this, "You didn't answer this Quizz", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateInterface(List<String> sucess){
        if(sucess.size() != 0){
            ListView listView = (ListView) findViewById(R.id.list_tours_answers);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
            listView.setAdapter(adapter);
        }
        else{
            listView.setEmptyView(findViewById(R.id.no_results_read));
        }
    }

}
