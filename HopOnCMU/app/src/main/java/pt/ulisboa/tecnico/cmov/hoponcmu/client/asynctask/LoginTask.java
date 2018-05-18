package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.SignatureException;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.LoginActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class LoginTask extends AsyncTask<String, Void, Integer> {

    private LoginActivity activity;

    public LoginTask(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(String[] params) {
        Socket server = null;
        int reply = -1;
        LoginCommand cmd = null;
        try {
            cmd = new LoginCommand(params[0], params[1]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        try {
            //If you're not using geny emulator use 10.0.2.2
            server = new Socket("10.0.2.2", 9090);

            Log.d("COMMAND",cmd.toString());

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            LoginResponse response = (LoginResponse) ois.readObject();


            if(response.securityCheck())
                reply = response.getID();

            oos.close();
            ois.close();
            Log.d("Client", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("Client", "Login Task failed..." + e.getMessage());
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
    protected void onPostExecute(Integer o) {
        activity.updateInterface(o);
    }
}