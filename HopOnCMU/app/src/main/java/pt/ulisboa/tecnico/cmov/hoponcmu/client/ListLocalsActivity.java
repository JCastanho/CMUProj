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
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetLocalsTask;

public class ListLocalsActivity extends AppCompatActivity {

    private GetQuizzTask task = null;
    private String title = "";
    private int userId;
    private String currentQuizz;
    private List<String> answeredQuizzes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_local);

        final ListView listView = (ListView) findViewById(R.id.list_tours);
        new GetLocalsTask(ListLocalsActivity.this).execute("location");

        Bundle bundle = getIntent().getExtras();
        this.userId = bundle.getInt("id");

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                int itemPosition=position;

                String text = (String) listView.getItemAtPosition(position);

                currentQuizz = text;

                Log.d("text", text);

                new GetAnsweredQuizzesTask(ListLocalsActivity.this, userId).execute();

                if(check()){
                    Toast.makeText(ListLocalsActivity.this, "You already answered this quizz!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ListLocalsActivity.this, "Downloading quiz for: " + text, Toast.LENGTH_SHORT).show();

                    title = text;
                    Log.d("List Tour info", text);

                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    task = new GetQuizzTask(ListLocalsActivity.this);
                    task.execute(text,"0");
                }
            }
        });

    }

    public void jumpToQuestion(String question, ArrayList<String> answers, int page, int size) {
        Toast.makeText(this, "Quizzes received!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ListLocalsActivity.this, QuizActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("Title", this.title);
        bundle.putString("Question", question);
        bundle.putStringArrayList("Answers", answers);
        bundle.putInt("Page", page);
        bundle.putInt("Size", size);
        bundle.putInt("id", userId);


        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void updateInterface(List<String> sucess){

        ListView listView = (ListView) findViewById(R.id.list_tours);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sucess);
        listView.setAdapter(adapter);
    }

    public void checkQuizz(List<String> quizzes){
        Log.d("currentQuizz", currentQuizz);
        answeredQuizzes = quizzes;
    }

    public Boolean check(){
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
