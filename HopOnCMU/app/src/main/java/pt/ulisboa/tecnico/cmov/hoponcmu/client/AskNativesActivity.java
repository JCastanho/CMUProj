package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.User;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.UserAdapter;

public class AskNativesActivity extends AppCompatActivity {

	private static ArrayList<User> array;
	private static UserAdapter adapter;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asknatives);

		listView = findViewById(R.id.list_nativeusers);

		if (adapter == null) { setAdapter(); }
	}

	private void setAdapter() {
		array = ApplicationContextProvider.getGroupPeersList();
		adapter = new UserAdapter(AskNativesActivity.this, array);

		listView.setAdapter(adapter);
		listView.setEmptyView(findViewById(R.id.no_near_users));
	}

	public String getUserAddress(String name){
		for(User u: array){
			if(u.getName().equals(name))
				return u.getAddress();
		}

		return "";
	}

	private void checkEmptyList(){
		if(array.size() == 0)
			listView.setEmptyView(findViewById(R.id.no_near_users));
	}
}

