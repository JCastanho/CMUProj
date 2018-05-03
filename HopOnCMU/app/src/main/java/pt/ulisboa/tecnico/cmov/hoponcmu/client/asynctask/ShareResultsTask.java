package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ShareResultsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.ShareRsltCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.ShareRsltResponse;

public class ShareResultsTask extends AsyncTask<Integer, Void, Boolean> {

    private ShareResultsActivity activity;

    public ShareResultsTask(ShareResultsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Integer[] params) {
        Socket server = null;
        Boolean reply = false;
        ShareRsltCommand cmd = new ShareRsltCommand(params[0], params[1]);

        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            ShareRsltResponse response = (ShareRsltResponse) ois.readObject();
            reply = response.getSuccess();

            oos.close();
            ois.close();
            Log.d("Client", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("Client", "Share Results Task failed..." + e.getMessage());
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
    protected void onPostExecute(Boolean o) {
        activity.updateInterface(o);
    }
}