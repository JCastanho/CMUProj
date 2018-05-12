package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.SignUpActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.CreateUserCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.SignupResponse;

public class SignupTask extends AsyncTask<String, Void, Boolean> {

    private SignUpActivity activity;

    public SignupTask(SignUpActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        Socket server = null;
        Boolean reply = false;
        CreateUserCommand cmd = new CreateUserCommand(params[0],params[1]);

        try {
            //If you're using geny emulator use 10.0.3.2
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(cmd);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            SignupResponse sr = (SignupResponse) ois.readObject();
            reply = sr.getAuthorization();

            oos.close();
            ois.close();
            Log.d("Client", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("Client", "Signup Task failed..." + e.getMessage());
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
    protected void onPostExecute(Boolean o) { activity.updateInterface(o); }
}
