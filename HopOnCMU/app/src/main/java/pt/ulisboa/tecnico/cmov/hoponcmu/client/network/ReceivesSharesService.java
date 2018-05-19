package pt.ulisboa.tecnico.cmov.hoponcmu.client.network;

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
import pt.ulisboa.tecnico.cmov.hoponcmu.client.Singleton;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.SendQuizzAnswersTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;

/**
 * Socket that keeps listening to receive shared results
 */
public class ReceivesSharesService extends Service {

    private SimWifiP2pSocketServer mSrvSocket = null;
    private final String TAG = "RESULT_RECEIVER";
    private Boolean alive;
    private ApplicationContextProvider applicationContext;

    public ReceivesSharesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        applicationContext = (ApplicationContextProvider) getApplicationContext();

        SimWifiP2pSocketManager.Init(applicationContext);

        alive = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Receives shares service started (" + this.hashCode() + ").");

        new IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        alive = false;
        super.onDestroy();
    }

	/*
	 * Asynctask implementing the result share
	 */

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");
            SimWifiP2pSocket sock = null;

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.termitePort)));
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

                        String[] code = st.split("-");

                        if(code[0].equals("2")){
                            //new SendQuizzAnswersTask(cmd).execute("");
                        } else {
                            publishProgress(code[1]);
                        }

                        sock.getOutputStream().write(("\n").getBytes());

                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                        //break;
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }

            try {
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Singleton.getInstance().parseResult(values[0]);
        }
    }
}
