package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;

import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ReadQuizzAnswersActivity;

public class GetCorrectAnswersTask extends AsyncTask<String, Void, String> {

    private ReadQuizzAnswersActivity activity;

    public GetCorrectAnswersTask(ReadQuizzAnswersActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String[] params){
        Socket server = null;
        String reply = null;


        return reply;
    }
}
