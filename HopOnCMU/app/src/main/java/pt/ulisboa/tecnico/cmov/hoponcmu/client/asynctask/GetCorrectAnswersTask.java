package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetCorrectAnswersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetCorrectAnswersResponse;

public class GetCorrectAnswersTask extends AsyncTask<String, Void, Integer> {

    private ReadQuizzAnswersActivity activity;
    private int id;

    public GetCorrectAnswersTask(ReadQuizzAnswersActivity activity, int id) {
        this.activity = activity;
        this.id = id;
    }

    @Override
    protected Integer doInBackground(String[] params){
        Socket server = null;
        int reply = -1;
        GetCorrectAnswersCommand cmd = new GetCorrectAnswersCommand(id ,params[0]);

        try{
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            Log.d("Teste" ,"Reply: " + Integer.toString(reply));

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetCorrectAnswersResponse response = (GetCorrectAnswersResponse) ois.readObject();
            reply = response.getCorrect();

            Log.d("Teste" ,"Respostas: " + Integer.toString(reply));

            oos.close();
            ois.close();
            Log.d("Client", "Hello friend!");
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
    protected void onPostExecute(Integer o){
        activity.correctAnswers(o);
    }
}
