package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetAnsweredQuizzesCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetAnsweredQuizzesResponse;

public class GetAnsweredQuizzesTask extends AsyncTask<String, Void, List<String>> {

    private ListLocalsActivity listLocalsActivity;
    private ReadQuizzAnswersActivity readQuizzAnswersActivity;
    private int id;

    public GetAnsweredQuizzesTask(ListLocalsActivity listLocalsActivity, int id){
        this.listLocalsActivity = listLocalsActivity;
        this.id = id;
    }

    public GetAnsweredQuizzesTask(ReadQuizzAnswersActivity readQuizzAnswersActivity, int id){
        this.readQuizzAnswersActivity = readQuizzAnswersActivity;
        this.id = id;
    }

    @Override
    protected List<String> doInBackground(String[] params){
        Socket server = null;
        List<String> reply = null;
        GetAnsweredQuizzesCommand cmd = null;
        try {
            cmd = new GetAnsweredQuizzesCommand(id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        try{
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetAnsweredQuizzesResponse response = (GetAnsweredQuizzesResponse) ois.readObject();
            if(response.securityCheck())
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
            try{
                listLocalsActivity.checkQuizz(o);
            }catch (Exception e){
                Log.d("List Tour", "Invalid Activity!");
            }

            try{
                readQuizzAnswersActivity.updateInterface(o);
            }catch (Exception e){
                Log.d("Read Quizz Answers Tour", "Invalid Activity!");
            }
        }
    }
}
