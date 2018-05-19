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
    TextView rank = null;
    TextView prize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);
        rank = (TextView) findViewById(R.id.rank);
        prize = (TextView) findViewById(R.id.prize);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        new RequestPrizesTask(PrizesActivity.this, (ApplicationContextProvider) getApplicationContext()).execute(id);
    }

    public void updateInterface(String res){

        String[] parse = res.split("/");
        boolean madeQuizz = Boolean.parseBoolean(parse[0]);
        int resRank = Integer.parseInt(parse[1]);
        int resPont = Integer.parseInt(parse[2]);
        boolean resFinal = Boolean.parseBoolean(parse[3]);

        if(madeQuizz){
            if(resFinal){
                rank.setText("Points: " + resPont);
                switch (resRank){
                    case 1:
                        prize.setText("Congratulations! You are in 1 place!");
                        break;
                    case 2:
                        prize.setText("Well Done! You are in 2 place!");
                        break;
                    case 3:
                        prize.setText("Good Job! You are in 3 place!");
                        break;
                    default:
                        prize.setText("You are in " + resRank + "!");
                }
            }
            else{
                rank.setText("Rank: " + resRank+ "\nPoints: " + resPont);
                prize.setText("");
            }
        }
        else{
            rank.setText("Complete a Quizz to receive a rank");
            prize.setText("");
        }

    }
}
