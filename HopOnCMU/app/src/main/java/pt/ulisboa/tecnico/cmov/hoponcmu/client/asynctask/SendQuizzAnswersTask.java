package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;

import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.QuizActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;

public class SendQuizzAnswersTask extends AsyncTask<String, Void, String> {

    private QuizActivity activity;

    public SendQuizzAnswersTask(QuizActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        SendQuizzesAnswersCommand cmd = new SendQuizzesAnswersCommand(3, params[0]);

        return reply;
    }
}
