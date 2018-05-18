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
    private String title = "";
    private int userId;
    private String currentQuizz;
    private List<String> answeredQuizzes;
    private List<String> donwloadedQuizzes;
    private ApplicationContextProvider applicationContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);
        applicationContext = (ApplicationContextProvider) getApplicationContext();

        final ListView listView = (ListView) findViewById(R.id.list_tours);
        new GetLocalsTask(ListLocalsActivity.this).execute("location");

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                String text = (String) listView.getItemAtPosition(position);

                //if(ApplicationContextProvider.nearBeacon(position+1)) {
                currentQuizz = text;

                if(applicationContext.checkDownloadedQuizz(currentQuizz)){
                    getQuizzes(applicationContext.getQuizz());
                }
                if(checkAnsweredQuizz()){
                    Toast.makeText(ListLocalsActivity.this, "You already answered this quizz!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ListLocalsActivity.this, "Downloading quiz for: " + text, Toast.LENGTH_SHORT).show();

                    title = text;
                    Log.d("List Tour info", text);

                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    task = new GetQuizzTask(ListLocalsActivity.this, userId);
                    task.execute(text);
                }
                /*} else {
                  Toast.makeText(ListLocalsActivity.this, "You are not near " + text, Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    public void getQuizzes(HashMap<String, List<Question>> quizz) {
        Toast.makeText(this, "Quizzes received!", Toast.LENGTH_SHORT).show();

        applicationContext.setQuizz(quizz);

        Intent intent = new Intent(ListLocalsActivity.this, QuizActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("Title", this.title);
        bundle.putInt("id", userId);


        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void updateInterface(List<String> sucess){
        ListView listView = (ListView) findViewById(R.id.list_tours);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
        listView.setAdapter(adapter);
    }


    public Boolean checkAnsweredQuizz(){

        answeredQuizzes = applicationContext.getAnsweredQuizzes().get(userId);

        try{
            for (int i = 0; i < answeredQuizzes.size(); i++){
                if(answeredQuizzes.get(i).equals(currentQuizz)){
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

}
