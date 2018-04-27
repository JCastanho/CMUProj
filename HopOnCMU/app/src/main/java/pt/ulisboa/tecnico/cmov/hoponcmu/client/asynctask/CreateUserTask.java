package pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.signupActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.CreateUserCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.LoginCommand;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.MainActivity;
import pt.ulisboa.tecnico.cmov.hoponcmu.response.LoginResponse;

public class CreateUserTask extends AsyncTask<String, Void, Boolean> {

    private signupActivity activity;

    public CreateUserTask(signupActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        Socket server = null;
        boolean reply = false;
       /* CreateUserCommand hc = new CreateUserCommand(params[0], params[1]);
        try {
            server = new Socket("10.0.2.2", 9090);

            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(hc);

            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
            LoginResponse hr = (LoginResponse) ois.readObject();
            reply = true; //só para dar

            oos.close();
            ois.close();
            Log.d("DummyClient", "Hi there!!");
        }
        catch (Exception e) {
            Log.d("DummyClient", "CreateUserTask failed..." + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try { server.close(); }
                catch (Exception e) { }
            }
        }*/
        return reply;
    }

    @Override
    protected void onPostExecute(Boolean o) {
        if (o != null) {
           // mainActivity.updateInterface("" + o);
        }
    }
}
