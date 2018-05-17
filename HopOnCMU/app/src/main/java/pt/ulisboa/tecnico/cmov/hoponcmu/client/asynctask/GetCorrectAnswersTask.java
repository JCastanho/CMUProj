package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetCorrectAnswersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetCorrectAnswersResponse;

public class GetCorrectAnswersTask extends AsyncTask<String, Void, List<Integer>> {

    private ReadQuizzAnswersActivity activity;
    private int id;

    public GetCorrectAnswersTask(ReadQuizzAnswersActivity activity, int id) {
        this.activity = activity;
        this.id = id;
    }

    @Override
    protected List<Integer> doInBackground(String[] params){
        Socket server = null;
        List<Integer> reply = new ArrayList<>();

        GetCorrectAnswersCommand cmd = null;
        try {
            cmd = new GetCorrectAnswersCommand(id ,params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetCorrectAnswersResponse response = (GetCorrectAnswersResponse) ois.readObject();
            if(response.securityCheck())
            {
                reply.add(response.getCorrectAnswers());
                Log.d("GET TIME: ",""+response.getTime());
                reply.add(response.getTime());
            }

            oos.close();
            ois.close();
        }catch (Exception e){
            Log.d("Client", "Get Correct Answers failed " + e.getMessage());
            e.printStackTrace();
        }finally {
            if (server != null) {
                try { server.close(); }
                catch (Exception e) { }
            }
        }

        return reply;
    }

    @Override
    protected void onPostExecute(List<Integer> o){
        activity.correctAnswers(o);
    }
}
