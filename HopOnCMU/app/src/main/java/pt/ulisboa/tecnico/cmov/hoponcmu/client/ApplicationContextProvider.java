package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class ApplicationContextProvider extends Application{

    private static Context context;

    // Security
    private EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");
    private ArrayList<String> nonces = new ArrayList<String>();
    private Date lastClear = Calendar.getInstance().getTime();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public String checkNonce(byte[] encryptedNonce){
        try {
            String nonce = new String(encryption.decrypt(encryptedNonce),"UTF-8");

            if(nonces.contains(nonce)) {
                return "NOK";
            }

            String[] parts = nonce.split("#");

            // Test Freshness
            Calendar nonceCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            nonceCalendar.setTime(sdf.parse(parts[1]));

            Date nonceDate = nonceCalendar.getTime();
            Calendar now = Calendar.getInstance();

            Calendar limit = now;
            limit.add(Calendar.HOUR, -2);
            Date dLimit = limit.getTime();


            now.add(Calendar.HOUR, 4);
            now.add(Calendar.HOUR, 2);
            Date dNow = now.getTime();

            if(dLimit.before(nonceDate) && dNow.after(nonceDate)) {
                nonces.add(nonce);
                return nonce;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NOK";
    }
}