package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.command.SendQuizzesAnswersCommand;

public class AskNativesActivity extends PeerListenerActivity{

	private SimWifiP2pSocket mCliSocket = null;
	private SendQuizzesAnswersCommand cmd;
	private int userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setAdapter(AskNativesActivity.this);
		super.findGroupPeers();

		Bundle bundle = getIntent().getExtras();
		userId = bundle.getInt("id");
		cmd = (SendQuizzesAnswersCommand) bundle.get("cmd");
	}

	public void sendResults(String neighborAddr){
		new sendMessageTask().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR,
				neighborAddr);
	}

	public class sendMessageTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				mCliSocket = new SimWifiP2pSocket(params[0],
						Integer.parseInt(getString(R.string.termitePort)));

				mCliSocket.getOutputStream().write(("2-").getBytes());
				//mCliSocket.getOutputStream().write((cmd).getBytes());

				BufferedReader sockIn = new BufferedReader(
						new InputStreamReader(mCliSocket.getInputStream()));
				sockIn.readLine();
				mCliSocket.close();
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			mCliSocket = null;

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(),"Results Sent!",Toast.LENGTH_SHORT).show();
			}
		}
	}
}
