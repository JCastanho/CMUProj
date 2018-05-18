package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.Question;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetQuizzesCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetQuizzesResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class GetQuizzTask extends AsyncTask<String, Void, List<Question>> {

    private ListLocalsActivity activity;
    private QuizActivity quizActivity;
    private int userId;

    public GetQuizzTask(QuizActivity activity, int userId){
        this.quizActivity = activity;
        this.userId = userId;
    }

    public GetQuizzTask(ListLocalsActivity activity, int userId) {
        this.activity = activity;
        this.userId = userId;
    }

    @Override
    protected List<Question> doInBackground(String[] params) {
        Socket server = null;
        List<Question> reply = new ArrayList<>();
        GetQuizzesCommand hc = null;
        try {
            hc = new GetQuizzesCommand(userId,params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(hc);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetQuizzesResponse hr = (GetQuizzesResponse) ois.readObject();

            if(hr.securityCheck()) {
                for (int i = 0; i < hr.getQuestion().size(); i++) {
                    reply.add(new Question(hr.getQuestion().get(i), hr.getAnswers().get(i)));
                }
            }

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
    protected void onPostExecute(List<Question> o) {
        if (o != null) {
            try{
                activity.getQuizzes(o);
            }catch (Exception e){
                Log.d("List Tours Activity","Invalid activity " + e.getMessage());
            }
        }
    }
}
