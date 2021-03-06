package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LogoutCommand;

public class LogoutTask extends AsyncTask<Integer, Void, Void> {

    private MainActivity activity;

    public LogoutTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Integer... param) {
        Socket server = null;
        LogoutCommand cmd = null;
        try {
            cmd = new LogoutCommand(param[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        try {
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.3.2", 9090);

            Log.d("COMMAND", cmd.toString());

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            oos.close();
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

}