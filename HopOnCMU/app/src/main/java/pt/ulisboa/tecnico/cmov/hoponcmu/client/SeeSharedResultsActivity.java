package pt.ulisboa.tecnico.cmov.hoponcmu.client;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.SharedResultAdapter;

public class SeeSharedResultsActivity extends AppCompatActivity {

    public static final String TAG = "RESULT_RECEIVER";
    //private TextView mTextOutput;
    private ExpandableListView listView;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private static SharedResultAdapter adapter;
    private static List<String> array;
    private static HashMap<String,List<String>> expandableItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_shared_results);

        SimWifiP2pSocketManager.Init(getApplicationContext());

        //mTextOutput = (TextView) findViewById(R.id.outputText);
        listView = (ExpandableListView) findViewById(R.id.sharedResults_list);

        if(adapter == null) { setAdapter(listView); }

        //TODO Get results from server
        //new GetCorrectAnswersCommand().execute(

        new IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setAdapter(ExpandableListView listView) {
        array = new ArrayList<>();
        expandableItems = new HashMap<>();
        // TODO: Get shared results in server??

        adapter = new SharedResultAdapter(this, this.array, this.expandableItems);

        listView.setAdapter(adapter);
    }

    private void parseResult(String results) {
        List<String> items = new ArrayList<>();

        String[] parsed = results.split(":");
        String[] parsedResults = parsed[1].split(",");

        array.add(parsed[0]);
        expandableItems.put(parsed[0], Arrays.asList(parsedResults));

        adapter.notifyDataSetChanged();
        listView.setEmptyView(findViewById(R.id.no_results_shared));
    }

    /*
	 * Asynctasks implementing message exchange
	 */

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.port)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    try {
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));
                        String st = sockIn.readLine();
                        publishProgress(st);
                        sock.getOutputStream().write(("\n").getBytes());
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            parseResult(values[0]);
        }
    }
}
