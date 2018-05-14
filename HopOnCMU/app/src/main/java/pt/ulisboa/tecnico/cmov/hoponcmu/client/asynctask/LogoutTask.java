package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.LoginActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LogoutCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class LogoutTask extends AsyncTask<Integer, Void, Void> {

    private MainActivity activity;

    public LogoutTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Integer... param) {
        Socket server = null;
        LogoutCommand cmd = new LogoutCommand(param[0]);

        try {
            //If you're using geny emulator use 10.0.3.2
            server = new Socket("10.0.2.2", 9090);

            Log.d("COMMAND", cmd.toString());

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            //LogoutResponse response = (LogoutResponse) ois.readObject();
            //reply = response.getID();

            oos.close();
            ois.close();
            Log.d("Client", "Hi there!!");
        } catch (Exception e) {
            Log.d("Client", "Logout Task failed..." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }


    /*@Override
    protected Void doInBackground(Integer... integers) {
        return null;
    }*/
}