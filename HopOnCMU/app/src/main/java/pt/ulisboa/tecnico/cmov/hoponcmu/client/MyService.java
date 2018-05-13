package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.models.User;

/**
 * My simple service
 */
public class MyService extends Service implements
        SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener{

    private static Service instance;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        this.instance = this;
        Toast.makeText(this, "Service was Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY; // read more on: http://developer.android.com/reference/android/app/Service.html
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    public static Service getInstance(){ return instance; }

    public void updatePeers() {
        ApplicationContextProvider.getManager().requestPeers(ApplicationContextProvider.getChannel(), this);
    }

    public static void updateGroupPeers() {
       // ApplicationContextProvider.getManager().requestGroupInfo(ApplicationContextProvider.getChannel(), this);
    }


    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
        ArrayList<User> array = new ArrayList<>();

        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);

            //Verify that we're not adding beacons to the list
            if(!device.deviceName.matches("M[0-9]+"))
                array.add(new User(deviceName,device.getVirtIp()));
        }

        ApplicationContextProvider.setGroupPeers(array);
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {

    }
}
