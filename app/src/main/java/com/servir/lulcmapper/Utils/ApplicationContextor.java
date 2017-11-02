package com.servir.lulcmapper.Utils;

import android.app.Application;
import android.content.Context;

public class ApplicationContextor extends Application {

    private static Context context;
    private Locatori Locatori;
    private static ApplicationContextor mInstance;

    public void onCreate() {
        super.onCreate();
        ApplicationContextor.context = getApplicationContext();

        mInstance = this;
        Locatori = new Locatori(mInstance);
    }

    public static Context getAppContext() {
        return ApplicationContextor.context;
    }

    public static synchronized ApplicationContextor getInstance() {
        return mInstance;
    }

    public Locatori getGoogleApiHelperInstance() {
        return this.Locatori;
    }
    public static Locatori getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }
}