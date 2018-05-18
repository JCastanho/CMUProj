package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.SignatureException;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SendQuizzesAnswersResponse;

public class SendQuizzAnswersTask extends AsyncTask<String, Void, Integer> {

    private QuizActivity activity;
    private int id;

    public SendQuizzAnswersTask(QuizActivity activity, int id) {
        this.activity = activity;
        this.id = id;
    }

    @Override
    protected Integer doInBackground(String[] params) {
        Socket server = null;
        int reply = -1;
        SendQuizzesAnswersCommand cmd = null;
        try {
            cmd = new SendQuizzesAnswersCommand(id, params[0], activity.getAnswersSend(), activity.getTimeForQuizz());
//            Log.d("TIME TASK: ",""+cmd.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket();
            server.connect( new InetSocketAddress("10.0.3.2", 9090),4000);
            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            SendQuizzesAnswersResponse response = (SendQuizzesAnswersResponse) ois.readObject();

            if(response.securityCheck()){
                reply = response.getId();
            }

            oos.close();
            ois.close();
            Log.d("Client", "Hello friend!");

        } catch (java.net.SocketTimeoutException e){
            //Foreign user
            //-2 bc when server couldn't save the answers is sends -1
            reply = R.string.non_native_user_error;
            Log.d("Client", "-2 " + e.getMessage());
        } catch (Exception e) {
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
