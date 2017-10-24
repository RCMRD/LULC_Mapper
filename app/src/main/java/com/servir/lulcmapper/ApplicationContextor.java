package com.servir.lulcmapper;

import android.app.Application;
import android.content.Context;

public class ApplicationContextor extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ApplicationContextor.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ApplicationContextor.context;
    }
}