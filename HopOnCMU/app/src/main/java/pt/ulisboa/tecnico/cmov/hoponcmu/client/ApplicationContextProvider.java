package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;

import java.util.List;

public class ApplicationContextProvider extends Application {

    private List<String> sContext;

    public List<String> getsContext() {
        return sContext;
    }

    public static void setContext(List<String> sContext) {
        sContext = sContext;
    }
}
