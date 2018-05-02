package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.ListLocalsActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.ListLocalsCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.GetLocalsResponse;

public class GetLocalsTask extends AsyncTask<String, Void, String> {

    private ListLocalsActivity activity;

    public GetLocalsTask(ListLocalsActivity listLocalsActivity) {
        this.activity = listLocalsActivity;
    }

    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;

        ListLocalsCommand cmd = new ListLocalsCommand(params[0]);

        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            GetLocalsResponse response = (GetLocalsResponse) ois.readObject();
            reply = response.getLocation();

            oos.close();
            ois.close();
            Log.d("Client", "Hello m8!");
        }
        catch (Exception e) {
            Log.d("Client", "Get Locals Task failed..." + e.getMessage());
            e.printStackTrace();
        }

        return reply;
    }


//    protected void onPostExecute(Boolean o) {
//        activity.updateInterface(o);
//    }
}
