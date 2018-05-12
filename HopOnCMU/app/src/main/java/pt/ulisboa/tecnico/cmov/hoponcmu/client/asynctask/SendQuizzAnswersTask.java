package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SendQuizzesAnswersResponse;

public class SendQuizzAnswersTask extends AsyncTask<String, Void, Integer> {

    private QuizActivity activity;

    public SendQuizzAnswersTask(QuizActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(String[] params) {
        Socket server = null;
        int reply = -1;
        SendQuizzesAnswersCommand cmd = new SendQuizzesAnswersCommand(2, params[0], activity.getQuestionSend(), activity.getAnswersSend());

        try{
            server = new Socket("10.0.2.2", 9090);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            SendQuizzesAnswersResponse response = (SendQuizzesAnswersResponse) ois.readObject();
            reply = response.getId();

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
    protected void onPostExecute(Integer o) {
        activity.updateInterface(o);
    }
}
