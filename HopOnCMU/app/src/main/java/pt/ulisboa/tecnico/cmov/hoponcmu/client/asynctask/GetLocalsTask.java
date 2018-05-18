package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendLocationCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SendLocationResponse;

public class GetLocalsTask extends AsyncTask<String, Void, List<String>>{

    private ListLocalsActivity activity;
    private ReadQuizzAnswersActivity quizzAnswersActivity;

    public GetLocalsTask(ListLocalsActivity listLocalsActivity) {
        this.activity = listLocalsActivity;
    }

    public GetLocalsTask(ReadQuizzAnswersActivity quizzAnswersActivity){
        this.quizzAnswersActivity = quizzAnswersActivity;
    }

    @Override
    protected List<String> doInBackground(String[] params) {
        Socket server = null;
        List<String> reply = null;
        SendLocationCommand cmd = null;
        try {
            cmd = new SendLocationCommand(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.2.2", 9090);

            Log.d("COMMAND",cmd.getLocation());

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            SendLocationResponse response = (SendLocationResponse) ois.readObject();

            if(response.securityCheck()) {
                reply = response.getLocations();
                Log.d("REPLY", reply.get(0));
            }

            oos.close();
            ois.close();
            Log.d("Client", "Hello m8!");
        }

        catch (Exception e) {
            Log.d("Client", "Get Locals Task failed..." + e.getMessage());
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
    protected void onPostExecute(List<String> o) {
        if(o != null){
            try {
                activity.updateInterface(o);
            }catch (Exception e){
                Log.d("List Tour", "Invalid Activity");
            }

            try{
                quizzAnswersActivity.updateInterface(o);
            }catch (Exception e){
                Log.d("Read Quizz Answers", "Invalid Activity");
            }
        }

    }
}
