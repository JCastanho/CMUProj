package pt.ulisboa.tecnico.cmu.dummyclient.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmu.command.ResponseCommand;
import pt.ulisboa.tecnico.cmu.dummyclient.MainActivity;
import pt.ulisboa.tecnico.cmu.response.CommandResponse;

public class GetQuizzTask extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    public GetQuizzTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String[] params) {
        Socket server = null;
        String reply = null;
        ResponseCommand hc = new ResponseCommand(2,params[0]);
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(hc);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            CommandResponse hr = (CommandResponse) ois.readObject();
            reply = hr.getMessage();

            oos.close();
            ois.close();
            Log.d("DummyClient", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("DummyClient", "DummyTask failed..." + e.getMessage());
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
    protected void onPostExecute(String o) {
        if (o != null) {
            mainActivity.updateInterface(o);
        }
    }
}
