package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.os.Bundle;

public class ShareResultsActivity extends PeerListenerActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.setAdapter(ShareResultsActivity.this);
		super.findGroupPeers();
	}
}
