package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class QuizActivity extends AppCompatActivity {

    private int q = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //TODO Remove after the mocks are not needed

        //ADD TITLE
        Bundle bundle = getIntent().getExtras();
        TextView view = (TextView) findViewById(R.id.txtTitle);

        view.setText(bundle.getString("Question"));


        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        //Get Quizzes, see next line
        String quizzes =  bundle.getString("Quizzes");



        viewQst.setText(quizzes);

        //ADD RESPONSES
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);

        RadioButton btn;
        for(int i = 1; i <= 4; i++){
            btn = new RadioButton(this);
            btn.setText("Response " + i);
            group.addView(btn);
        }

        //ENDTODO

    }

    public void onNext(View view){

        if(q < 4) {

            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(true);

//            TextView textView = (TextView) findViewById(R.id.txtQst);
//
//            textView.setText("Question " + q);
            q += 1;

            Toast.makeText(this, "Next Question", Toast.LENGTH_SHORT).show();

        }
        else {

//            TextView textView = (TextView) findViewById(R.id.txtQst);
//
//            textView.setText("Question " + q);

//            q += 1;
            Button btn = (Button) findViewById(R.id.btnNext);
            btn.setEnabled(false);
        }

    }

    public void onPrev(View view){
        if(q > 1) {

            Button btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setEnabled(true);

//            TextView textView = (TextView) findViewById(R.id.txtQst);
//
//            textView.setText("Question " + q);
            q -= 1;

            Toast.makeText(this, "Previous Question", Toast.LENGTH_SHORT).show();

        }
        else {

//            TextView textView = (TextView) findViewById(R.id.txtQst);
//
//            textView.setText("Question " + q);

//            q -= 1;
            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(false);
        }
    }

    public void onSend(View view){
        QuizActivity.this.finish();
        Toast.makeText(this, "Answer Sent!", Toast.LENGTH_SHORT).show();
    }
}
