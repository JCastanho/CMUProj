package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;

public class QuizActivity extends AppCompatActivity {

    private int q = 0;
    private String question;
    private ArrayList<String> answers;

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //TODO Remove after the mocks are not needed

        //ADD TITLE
        Bundle bundle = getIntent().getExtras();
        TextView view = (TextView) findViewById(R.id.txtTitle);

        view.setText(bundle.getString("Title"));
        setTitle(bundle.getString("Title"));


        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        //Get Quizzes, see next line
        String quizzes =  bundle.getString("Question");
        viewQst.setText(quizzes);

//        int quizzes =  bundle.getInt("Page");
//        viewQst.setText(Integer.toString(quizzes));

        //ADD RESPONSES
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);

        ArrayList<String> answers = bundle.getStringArrayList("Answers");

        RadioButton btn;
        for(int i = 0; i < 4; i++){
            btn = new RadioButton(this);
            btn.setText(answers.get(i));
            group.addView(btn);
        }

        //ENDTODO

    }

    public void onNext(View view){

        if(q < 3) {

//            Button btnPrev = (Button) findViewById(R.id.btnPrev);
//            btnPrev.setEnabled(true);

            q += 1;

            GetQuizzTask task = new GetQuizzTask(QuizActivity.this);
            task.execute(title, Integer.toString(q));



            Toast.makeText(this, "Next Question", Toast.LENGTH_SHORT).show();

        }
        if (q == 3){
//            Button btnSend = (Button) findViewById(R.id.btnSend);
//            btnSend.setEnabled(true);
        }
        else {

//            Button btn = (Button) findViewById(R.id.btnNext);
//            btn.setEnabled(false);
        }

    }

    public void onPrev(View view){
        if(q > 0) {

//            Button btnNext = (Button) findViewById(R.id.btnNext);
//            btnNext.setEnabled(true);

            q -= 1;

            GetQuizzTask task = new GetQuizzTask(QuizActivity.this);
            task.execute(title, Integer.toString(q));

            Toast.makeText(this, "Previous Question", Toast.LENGTH_SHORT).show();

        }
        else {

//            Button btnPrev = (Button) findViewById(R.id.btnPrev);
//            btnPrev.setEnabled(false);
        }
    }

    public void onSend(View view){
        QuizActivity.this.finish();
        Toast.makeText(this, "Answer Sent!", Toast.LENGTH_SHORT).show();
    }

    public void updateQuestion(String question, ArrayList<String> answers){
        this.question = question;
        this.answers = answers;

        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        viewQst.setText(question);

        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
        for(int i = 0; i < 4; i++){
            ((RadioButton) group.getChildAt(i)).setText(answers.get(i));
        }
    }
}
