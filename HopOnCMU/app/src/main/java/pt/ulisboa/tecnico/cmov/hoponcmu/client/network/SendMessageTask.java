package pt.ulisboa.tecnico.cmov.hoponcmu.client.network;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class SendMessageTask extends AsyncTask<String, Void, String> {

    private Context context;
    private SimWifiP2pSocket mCliSocket = null;

    public SendMessageTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            mCliSocket = new SimWifiP2pSocket(params[0], Integer.parseInt(context.getString(R.string.port)));

            mCliSocket.getOutputStream().write((params[1] + "\n").getBytes());
            BufferedReader sockIn = new BufferedReader(
                    new InputStreamReader(mCliSocket.getInputStream()));
            sockIn.readLine();
            mCliSocket.close();
        } catch (UnknownHostException e) {
            return "Unknown Host:" + e.getMessage();
        } catch (IOException e) {
            return "IO error:" + e.getMessage();
        }
        mCliSocket = null;

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Results Sent!", Toast.LENGTH_SHORT).show();
        }
    }
}
