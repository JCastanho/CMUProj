package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.SendQuizzAnswersTask;

public class QuizActivity extends AppCompatActivity {

    private int q = 0;
    private int id;

    private SendQuizzAnswersTask task = null;
    private String monumento;
    private String question;
    private ArrayList<String> answers;

    private ArrayList<String> questionSend = new ArrayList<String>();
    private ArrayList<String> answersSend = new ArrayList<String>();

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public String getMonumento() {
        return monumento;
    }

    public void setMonumento(String monumento) {
        this.monumento = monumento;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getQuestionSend() {
        return questionSend;
    }

    public void setQuestionSend(ArrayList<String> questionSend) {
        this.questionSend = questionSend;
    }

    public ArrayList<String> getAnswersSend() {
        return answersSend;
    }

    public void setAnswersSend(ArrayList<String> answersSend) {
        this.answersSend = answersSend;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //ADD TITLE
        Bundle bundle = getIntent().getExtras();
        TextView view = (TextView) findViewById(R.id.txtTitle);

        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPrev.setEnabled(false);

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        this.id = bundle.getInt("id");


        setMonumento(bundle.getString("Title"));
        view.setText(monumento);


        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        //Get Quizzes, see next line
        setQuestion(bundle.getString("Question"));
        viewQst.setText(question);

        //ADD RESPONSES
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);

        setAnswers(bundle.getStringArrayList("Answers"));

        RadioButton btn;
        for(int i = 0; i < 4; i++){
            btn = new RadioButton(this);
            btn.setText(answers.get(i));
            group.addView(btn);
        }

    }

    public void onNext(View view){

        if(q < 3) {

            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(true);

            getQuestionSend().add(question);
            RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
            int selectedId = group.getCheckedRadioButtonId();
            if(selectedId != -1){

                q += 1;

                RadioButton button = (RadioButton) findViewById(selectedId);
                getAnswersSend().add(button.getText().toString());


                GetQuizzTask task = new GetQuizzTask(QuizActivity.this);
                task.execute(monumento, Integer.toString(q));

                Toast.makeText(this, "Next Question", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Please select answer", Toast.LENGTH_SHORT).show();
            }

        }
        if(q == 3){
            Button btnSend = (Button) findViewById(R.id.btnSend);
            btnSend.setEnabled(true);

            Button btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setEnabled(false);
        }

    }

    public void onPrev(View view){
        if(q > 0) {

            Button btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setEnabled(true);

            Button btnSend = (Button) findViewById(R.id.btnSend);
            btnSend.setEnabled(false);

            q -= 1;

            getQuestionSend().remove(questionSend.size() -1);
            getAnswersSend().remove(answersSend.size() -1);

            GetQuizzTask task = new GetQuizzTask(QuizActivity.this);
            task.execute(monumento, Integer.toString(q));

            Toast.makeText(this, "Previous Question", Toast.LENGTH_SHORT).show();

        }
        if (q == 0){

            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(false);
        }
    }

    public void onSend(View view){
        getQuestionSend().add(question);
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId != -1) {

            RadioButton button = (RadioButton) findViewById(selectedId);
            getAnswersSend().add(button.getText().toString());
        }

        task = new SendQuizzAnswersTask(QuizActivity.this, id);
        task.execute(getMonumento());
        QuizActivity.this.finish();
    }

    public void updateQuestion(String question, ArrayList<String> answers) {

        setQuestion(question);
        setAnswers(answers);

        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        viewQst.setText(question);

        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
        for (int i = 0; i < 4; i++) {
            ((RadioButton) group.getChildAt(i)).setText(answers.get(i));
        }
    }

    public void updateInterface(Integer id) {
        if( id != -1) {
            Toast.makeText(this, "Answer Sent with success!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Answer failed, try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
