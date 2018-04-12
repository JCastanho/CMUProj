package pt.ulisboa.tecnico.cmu.dummyclient.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmu.command.ResponseCommand;
import pt.ulisboa.tecnico.cmu.dummyclient.MainActivity;
import pt.ulisboa.tecnico.cmu.response.CommandResponse;

public class VerifyUserTask extends AsyncTask<String, Void, Boolean> {

    private MainActivity mainActivity;

    public VerifyUserTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        Socket server = null;
        boolean reply = false;
        ResponseCommand hc = new ResponseCommand(0, params[1]);
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(hc);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            CommandResponse hr = (CommandResponse) ois.readObject();
            reply = hr.getLoginOrCreate();

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
    protected void onPostExecute(Boolean o) {
        if (o != null) {
            mainActivity.updateInterface("" + o);
        }
    }
}