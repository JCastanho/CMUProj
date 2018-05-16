package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Map;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.RequestPrizesTask;

public class PrizesActivity extends AppCompatActivity {

    private int id;
    TextView logins = null;
    TextView rank = null;
    TextView prize = null;
    TextView time=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);

        logins = (TextView) findViewById(R.id.logins);
        rank = (TextView) findViewById(R.id.rank);
        prize = (TextView) findViewById(R.id.prize);
        time = (TextView) findViewById(R.id.time);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        new RequestPrizesTask(PrizesActivity.this).execute(""+id);
    }

    public void updateInterface(Map<String, Integer> map){

        boolean flag=false;
        boolean flagFinal=false;
        int position=0;
        int iterator=1;
        int points=0;
        int timeForQuizz=0;

        if(map.keySet().size()>1){
            logins.setText(map.keySet().size() + " people are connected!");
        }
        else{
            logins.setText(map.keySet().size() + " people is connected!");
        }

        for(String s: map.keySet()){
            if(s.contains("SELECTED")){
                String[] si = s.split("/");
                timeForQuizz = Integer.parseInt(si[1]);
                flag=true;
                position=iterator;
                points = map.get(s);
                if(s.contains("FINAL")){
                    flagFinal=true;
                }
            }
            iterator++;
        }

        if(flag){
            rank.setText("Points: " + points + "\nYou're ranked " + position + " on total of " + map.keySet().size() + " contestants");
            time.setText("Quizzes Time: " + timeForQuizz + " seconds");
            if(flagFinal){
                switch(position){
                    case 1:
                        rank.setText("Points: " + points);
                        prize.setText("You've completed all Quizzes\nYou're currently at first place!\n Congratulations!");
                        break;
                    case 2:
                        rank.setText("Points: " + points);
                        prize.setText("You've completed all Quizzes\nYou're currently at second place!\n Congratulations!");
                        break;
                    case 3:
                        rank.setText("Points: " + points);
                        prize.setText("You've completed all Quizzes\nYou're currently at third place!\n Congratulations!");
                        break;
                    default:
                        prize.setText("You've completed all Quizzes\nYou're currently at third place!\n Congratulations!");
                        break;
                    }
            }
            else{
                prize.setText("");
            }
        }
        else{
            rank.setText("You still have 0 points!");
            prize.setText("");
            time.setText("");
        }
        Toast.makeText(PrizesActivity.this, "" + map.keySet(), Toast.LENGTH_SHORT).show();
    }
}
