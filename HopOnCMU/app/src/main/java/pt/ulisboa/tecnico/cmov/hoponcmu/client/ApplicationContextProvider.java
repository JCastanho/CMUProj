package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

public class ApplicationContextProvider extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public Boolean getWifiState() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        return wifi.isWifiEnabled();
    }
}
