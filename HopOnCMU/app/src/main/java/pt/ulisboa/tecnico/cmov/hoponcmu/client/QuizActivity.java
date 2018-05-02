package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //TODO Remove after the mocks are not needed

        //ADD TITLE
        Bundle bundle = getIntent().getExtras();
        TextView view = (TextView) findViewById(R.id.txtTitle);

        view.setText(bundle.getString("Title"));

        //Get Quizzes, see next line
        //bundle.getString("Quizzes");


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
    public void onSend(View view){
        QuizActivity.this.finish();
        Toast.makeText(this, "Answer Sent!", Toast.LENGTH_SHORT).show();
    }
}
