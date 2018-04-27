package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.LoginActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class LoginTask extends AsyncTask<String, Void, Boolean> {

    private LoginActivity activity;

    public LoginTask(LoginActivity activity) {

        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        Socket server = null;
        boolean reply = false;
        LoginCommand command = new LoginCommand(0, params[0], params[1]);
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(command);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            LoginResponse response = (LoginResponse) ois.readObject();
            reply = response.getLoginOrCreate();

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
            //mainActivity.updateInterface("" + o);
        }
    }
}