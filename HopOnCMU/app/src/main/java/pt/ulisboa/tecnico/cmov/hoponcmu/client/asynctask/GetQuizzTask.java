package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetQuizzesCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetQuizzesResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class GetQuizzTask extends AsyncTask<String, Void, Question> {

    private ListLocalsActivity activity;
    private QuizActivity quizActivity;

    public GetQuizzTask(QuizActivity activity){
        this.quizActivity = activity;
    }

    public GetQuizzTask(ListLocalsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Question doInBackground(String[] params) {
        Socket server = null;
        Question reply = null;
        GetQuizzesCommand hc = new GetQuizzesCommand(2,params[0], Integer.parseInt(params[1]));
        try {
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(hc);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetQuizzesResponse hr = (GetQuizzesResponse) ois.readObject();
            reply = new Question(hr.getQuestion(), hr.getAnswers(), hr.getPage(), hr.getSize());

            oos.close();
            ois.close();
            Log.d("DummyClient", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("DummyClient", "GetQuizzTask failed..." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try { server.close(); }
                catch (Exception e) { }
            }
        }
        return reply;
    }

    @Override
    protected void onPostExecute(Question o) {
        if (o != null) {
            try {
                activity.jumpToQuestion(o.getQuestion(), o.getAnswers(), o.getPage(), o.getSize());
            }
            catch (Exception e){
                Log.d("Get Quizz List Tour", "Invalid Activity");
            }

            try{
                quizActivity.updateQuestion(o.getQuestion(), o.getAnswers());
            }
            catch (Exception e){
                Log.d("Update Question", "Invalid Activity");
            }
        }
    }
}
