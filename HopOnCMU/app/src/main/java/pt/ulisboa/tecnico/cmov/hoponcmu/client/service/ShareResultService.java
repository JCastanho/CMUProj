package pt.ulisboa.tecnico.cmov.hoponcmu.client.service;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider;

/**
 * Socket that keeps listening for shared results
 */
public class ShareResultService extends Service {

    private SimWifiP2pSocketServer mSrvSocket = null;
    private final String TAG = "RESULT_RECEIVER";
    private Boolean alive;

    public ShareResultService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        SimWifiP2pSocketManager.Init(getApplicationContext());

        alive = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "ShareResultService started (" + this.hashCode() + ").");

        new IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        alive = false;
    }

	/*
	 * Asynctasks implementing message exchange
	 */

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");
            SimWifiP2pSocket sock = null;

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.port)));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            while (alive) {
                try {
                     sock = mSrvSocket.accept();
                    try {
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));
                        String st = sockIn.readLine();
                        publishProgress(st);
                        sock.getOutputStream().write(("\n").getBytes());
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }

            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            ApplicationContextProvider.parseResult(values[0]);
        }
    }
}
