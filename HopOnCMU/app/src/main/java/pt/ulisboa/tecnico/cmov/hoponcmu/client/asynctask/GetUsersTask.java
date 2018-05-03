package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.ShareResultsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.GetUsersCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendLocationCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetUsersResponse;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SendLocationResponse;

public class GetUsersTask extends AsyncTask<Integer, Void, List<String>>{

    private ShareResultsActivity activity;

    public GetUsersTask(ShareResultsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<String> doInBackground(Integer... params) {
        Socket server = null;
        List<String> reply = new ArrayList<>();
        GetUsersCommand cmd = new GetUsersCommand(params[0]);

        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetUsersResponse response = (GetUsersResponse) ois.readObject();
            reply = response.getUsers();

            oos.close();
            ois.close();
            Log.d("Client", "Hello m8!");
        }
        catch (Exception e) {
            Log.d("Client", "Get Users Task failed..." + e.getMessage());
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
    protected void onPostExecute(List<String> o) {
        activity.updateUserList(o);
    }
}
