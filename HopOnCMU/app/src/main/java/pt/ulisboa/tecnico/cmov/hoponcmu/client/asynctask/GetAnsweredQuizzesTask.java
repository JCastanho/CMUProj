package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetAnsweredQuizzesCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetAnsweredQuizzesResponse;
import static pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider.getContext;

public class GetAnsweredQuizzesTask extends AsyncTask<Integer, Void, List<String>> {

    private MainActivity activity;
    private ApplicationContextProvider context;

    public GetAnsweredQuizzesTask(MainActivity activity, ApplicationContextProvider ctx) {
        this.activity = activity;
        this.context = ctx;
    }


    @Override
    protected List<String> doInBackground(Integer[] params){
        Socket server = null;
        List<String> reply = null;
        GetAnsweredQuizzesCommand cmd = null;
        try {
            cmd = new GetAnsweredQuizzesCommand(params[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }


        try{
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.3.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetAnsweredQuizzesResponse response = (GetAnsweredQuizzesResponse) ois.readObject();

            response.securityCheck(context.checkNonce(response.getNonce()));
            reply = response.getLocations();

            oos.close();
            ois.close();
            Log.d("Client", "Muchas Gracias!");

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
    protected void onPostExecute(List<String> o){
        if(o != null){

            activity.updateAnsweredQuizz(o);
        }
    }
}
