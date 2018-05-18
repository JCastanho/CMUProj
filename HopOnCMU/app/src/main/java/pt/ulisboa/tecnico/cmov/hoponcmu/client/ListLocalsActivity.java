package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetLocalsTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;

public class ListLocalsActivity extends AppCompatActivity {

    private GetQuizzTask task = null;
    private int userId;
    private String currentQuizz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);

        final ListView listView = (ListView) findViewById(R.id.list_tours);
        new GetLocalsTask(ListLocalsActivity.this, (ApplicationContextProvider) getApplicationContext()).execute("location");

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String text = (String) listView.getItemAtPosition(position);

                //if(Singleton.getInstance.nearBeacon(position+1)) {
                currentQuizz = text;

                if (Singleton.getInstance().checkAnsweredQuizz(currentQuizz)) {
                    Toast.makeText(ListLocalsActivity.this, "You already answered this quizz!", Toast.LENGTH_SHORT).show();
                } else {
                    if (Singleton.getInstance().checkDownloadedQuizz(currentQuizz)) {
                        startQuiz();
                    } else {

                        Toast.makeText(ListLocalsActivity.this, "Downloading quiz for: " + text, Toast.LENGTH_SHORT).show();

                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    task = new GetQuizzTask(ListLocalsActivity.this, userId, (ApplicationContextProvider) getApplicationContext());
                    task.execute(text);
                }
                /*} else {
                  Toast.makeText(ListLocalsActivity.this, "You are not near " + text, Toast.LENGTH_SHORT).show();
                }*/
                }
            }
        });
    }

    public void getQuizzes(List<Question> quizz){
        Log.d("List Locals", "quizz received");
        Toast.makeText(this, "Quizzes received!", Toast.LENGTH_SHORT).show();
        Singleton.getInstance().addQuizz(currentQuizz, quizz);
        startQuiz();
    }

    public void startQuiz(){
        Intent intent = new Intent(ListLocalsActivity.this, QuizActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Title", this.currentQuizz);
        bundle.putInt("id", userId);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void updateInterface(List<String> sucess){
        ListView listView = (ListView) findViewById(R.id.list_tours);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
        listView.setAdapter(adapter);
    }
}
