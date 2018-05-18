package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListResultsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetCorrectAnswersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetCorrectAnswersResponse;

public class GetCorrectAnswersTask extends AsyncTask<String, Void, List<Integer>> {

    private Context activity;
    private String monument;

    public GetCorrectAnswersTask(Context context) {
        this.activity = context;
    }

    @Override
    protected List<Integer> doInBackground(String[] params){
        Socket server = null;
        List<Integer> reply = new ArrayList<>();

        GetCorrectAnswersCommand cmd = null;
        try {
            cmd = new GetCorrectAnswersCommand(Integer.parseInt(params[0]),params[1]);
            monument = params[1];
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.3.2", 9090);
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
        if(activity instanceof ReadQuizzAnswersActivity) {
            ReadQuizzAnswersActivity lrActivity = (ReadQuizzAnswersActivity) activity;
            lrActivity.correctAnswers(o);
        } else {
            ListResultsActivity lrActivity = (ListResultsActivity) activity;
            lrActivity.updateInterface(monument, o);
        }
    }
}
