package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.SharedResultAdapter;

public class SeeSharedResultsActivity extends AppCompatActivity {

    public static final String TAG = "SEE_RESULT";
    private ExpandableListView listView;
    private SharedResultAdapter adapter;
    private List<String> array;
    private HashMap<String, List<String>> expandableItems;
    private Boolean alive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_shared_results);

        SimWifiP2pSocketManager.Init(getApplicationContext());

        listView = (ExpandableListView) findViewById(R.id.sharedResults_list);
        alive = true;

        setAdapter(listView);

        new SharedResultsTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alive = false;
    }

    private void setAdapter(ExpandableListView listView) {
        HashMap<String, List<String>> results = Singleton.getInstance().getSharedResults();
        List<String> users = new ArrayList<>(results.keySet());

        array = new ArrayList<>(users);
        expandableItems = results;

        adapter = new SharedResultAdapter(this, array, expandableItems);

        listView.setAdapter(adapter);
        checkEmptyList();
    }

    private void checkEmptyList() {
        if(array.size() == 0)
            listView.setEmptyView(findViewById(R.id.no_results_shared));
    }

    private List<String> mergeNestedLists(Collection<List<String>> nestedLists) {
        List<String> mergedList = new ArrayList<>();

        for(List<String> nestedList: nestedLists){
            mergedList.addAll(nestedList);
        }

        return mergedList;
    }

    public class SharedResultsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"Working... ");

            while (alive) {

                HashMap<String,List<String>> results = Singleton.getInstance().getSharedResults();

                List<String> resultsValues = mergeNestedLists(results.values());
                List<String> expandableItemsValues = mergeNestedLists(expandableItems.values());

                if(!(resultsValues.containsAll(expandableItemsValues) && expandableItemsValues.containsAll(resultsValues))) {
                    Log.d(TAG,"New results: " + results.size());
                    Log.d(TAG,"My array size: " + array.size());

                    List<String> users = new ArrayList<>(results.keySet());

                    array.clear();
                    expandableItems.clear();
                    array.addAll(users);
                    expandableItems.putAll(results);

                    Log.d(TAG,"My updated array size: " + array.size());

                    publishProgress();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            adapter.notifyDataSetChanged();
            checkEmptyList();
        }
    }
}