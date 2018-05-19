package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.SendQuizzAnswersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;

public class QuizActivity extends AppCompatActivity {

    private int q = 0;
    private int userdId;

    private SendQuizzAnswersTask task = null;
    private String monumento;
    private String question;
    private ArrayList<String> answers;

    private ArrayList<String> questionSend = new ArrayList<String>();
    private ArrayList<String> answersSend = new ArrayList<String>();
    private List<Question> quizz = new ArrayList<>();

    private Timer quizzTimer;
    private TimerTask quizzTimerTask;

    private Button btnSend;
    private TextView time;
    private int min;
    private int seg;
    private int timeForQuizz;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

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

    public int getTimeForQuizz(){
        return timeForQuizz;
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

        setMonumento(bundle.getString("Title"));
        this.userdId = bundle.getInt("id");
        view.setText(monumento);

        quizz = Singleton.getInstance().getQuizz(monumento);

        timeForQuizz=0;
        min=0;
        seg=0;

        time = (TextView) findViewById(R.id.time);
        time.setText("0"+min+":"+seg);

        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPrev.setEnabled(false);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setEnabled(false);

        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        //Get Quizzes, see next line
        setQuestion(quizz.get(q).getQuestion());
        viewQst.setText(question);

        //ADD RESPONSES
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);

        setAnswers(quizz.get(q).getAnswers());

        RadioButton btn;
        for(int i = 0; i < 4; i++){
            btn = new RadioButton(this);
            btn.setText(answers.get(i));
            group.addView(btn);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startQuizzTimer();
    }

    public void startQuizzTimer() {
        quizzTimer = new Timer();
        initializeQuizzTimer();
        quizzTimer.schedule(quizzTimerTask, 500, 1000); //
    }

    public void initializeQuizzTimer() {

        quizzTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if(seg==59){
                            seg=0;
                            min+=1;
                        }
                        seg+=1;
                    }
                });
            }
        };
    }

    public void onNext(View view){

        if(q < 3) {

            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(true);

            RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
            int selectedId = group.getCheckedRadioButtonId();
            if(selectedId != -1){

                q += 1;

                RadioButton button = (RadioButton) findViewById(selectedId);
                getAnswersSend().add(button.getText().toString());

                updateQuestion();

                group.clearCheck();
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

            getAnswersSend().remove(answersSend.size() -1);

            updateQuestion();

        }
        if (q == 0){

            Button btnPrev = (Button) findViewById(R.id.btnPrev);
            btnPrev.setEnabled(false);
        }
    }

    public void onSend(View view){

        if (quizzTimer != null) {
            quizzTimer.cancel();
            quizzTimer = null;
        }

        timeForQuizz=(min*60)+seg;
        Log.d("TIME: ",""+timeForQuizz);

        Log.d("QUESTION: ",question);
        getQuestionSend().add(question);
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId != -1) {

            RadioButton button = (RadioButton) findViewById(selectedId);
            getAnswersSend().add(button.getText().toString());
            task = new SendQuizzAnswersTask(QuizActivity.this, userdId, (ApplicationContextProvider) getApplicationContext());
            task.execute(getMonumento());
        }
        else{
            Toast.makeText(this, "Please select answer", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateQuestion() {

        setQuestion(quizz.get(q).getQuestion());
        setAnswers(quizz.get(q).getAnswers());

        TextView viewQst = (TextView) findViewById(R.id.txtQst);
        viewQst.setText(question);

        RadioGroup group = (RadioGroup) findViewById(R.id.rdgResponses);
        for (int i = 0; i < 4; i++) {
            ((RadioButton) group.getChildAt(i)).setText(answers.get(i));
        }
    }

    public void updateInterface(Integer id) {
        Log.d("Quiz Activity",""+id);
        if(id == Integer.parseInt(getString(R.string.non_native_user_error))){
            Log.d("Quiz Activity","-2");
            Intent intent = new Intent(QuizActivity.this, AskNativesActivity.class);

            try {
                intent.putExtra("Command", new SendQuizzesAnswersCommand(id, monumento, getAnswersSend(), getTimeForQuizz()));
                intent.putExtra("id",userdId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            startActivity(intent);
        } else if( id != -1) {
            Toast.makeText(this, "Answer Sent with success!", Toast.LENGTH_SHORT).show();
            //new GetAnsweredQuizzesTask(QuizActivity.this, id).execute();
            Singleton.getInstance().addAnsweredQuizzes(monumento);
        } else {
            Toast.makeText(this, "Failed sending the quizz answers", Toast.LENGTH_SHORT).show();
        }

        QuizActivity.this.finish();
    }
}
