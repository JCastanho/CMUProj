package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetAnsweredQuizzesTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.network.ReceivesSharesService;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.LogoutTask;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.network.SimWifiP2pBroadcastReceiver;

public class MainActivity extends AppCompatActivity {

    private Integer userId;
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private static Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getExtras().getInt("id",-1);
        singleton = Singleton.getInstance();

        registerBroadcastReceiver();
        setServices();

        new GetAnsweredQuizzesTask(MainActivity.this, (ApplicationContextProvider) getApplicationContext()).execute(userId);

        Button list_btn = (Button) findViewById(R.id.list_btn);
        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListLocalsActivity.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        Button readQuizz_btn = (Button) findViewById(R.id.readRslt_btn);
        readQuizz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReadQuizzAnswersActivity.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        Button prizes_btn = (Button) findViewById(R.id.prizes_btn);
        prizes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrizesActivity.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        Button share_btn = (Button) findViewById(R.id.share_btn);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if user is between bus stops
                Intent intent = new Intent(MainActivity.this, ShareResultsActivity.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        Button see_shared_rslt_btn = (Button) findViewById(R.id.see_share_rslt_btn);
        see_shared_rslt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SeeSharedResultsActivity.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new LogoutTask(MainActivity.this).execute(userId);
        Intent termite = new Intent(MainActivity.this, SimWifiP2pService.class);
        Intent sharingService = new Intent(MainActivity.this, SimWifiP2pService.class);

        stopService(sharingService);
        unbindService(mConnection);
        stopService(termite);
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.log_out){
            new LogoutTask(MainActivity.this).execute(userId);

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);

        mReceiver = new SimWifiP2pBroadcastReceiver((ApplicationContextProvider) getApplicationContext());

        registerReceiver(mReceiver, filter);
    }

    private void setServices() {
        Intent termite = new Intent(this, SimWifiP2pService.class);
        startService(termite);
		bindService(termite, mConnection, Context.BIND_AUTO_CREATE);

		startService(new Intent(this, ReceivesSharesService.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            singleton.setChannel(mChannel);
            singleton.setManager(mManager);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
        }
    };

    public void updateAnsweredQuizz(List<String> o) {
        singleton.setAnsweredQuizzes(o);
    }
}