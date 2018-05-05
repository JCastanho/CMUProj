package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.app.Application;
import android.content.Context;

import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.GetQuizzTask;

public class ApplicationContextProvider extends Application {

    private static Application sApplication;

    public static ListLocalsActivity getActivity() {
        return activity;
    }

    public static void setActivity(ListLocalsActivity activity) {
        ApplicationContextProvider.activity = activity;
    }

    private static ListLocalsActivity activity;

    public static GetQuizzTask getTask() {
        return task;
    }

    public static void setTask(GetQuizzTask task) {
        ApplicationContextProvider.task = task;
    }

    private static GetQuizzTask task;

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
}
