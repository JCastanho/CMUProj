package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.PrizesActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.RequestPrizesCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.PrizesResponse;

public class RequestPrizesTask extends AsyncTask<Integer, Void, String> {

    private PrizesActivity activity;

    public RequestPrizesTask(PrizesActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Integer[] params) {
        Socket server = null;
        String reply = null;
        RequestPrizesCommand cmd = new RequestPrizesCommand(params[0]);

        try{
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            PrizesResponse response = (PrizesResponse) ois.readObject();
            reply = response.getRes();
            oos.close();
            ois.close();
            Log.d("Client", "Hello friend!");
        }
        catch (Exception e) {
            Log.d("Client", "Send Quizz Answers failed " + e.getMessage());
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
    protected void onPostExecute(String o) {
        activity.updateInterface(o);
    }
}
