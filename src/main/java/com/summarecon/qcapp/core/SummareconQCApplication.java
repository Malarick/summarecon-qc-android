package com.summarecon.qcapp.core;

import android.app.Application;

public class SummareconQCApplication extends Application {

    private Configuration config;

    @Override
    public void onCreate() {
        super.onCreate();
        config = new Configuration(this);
    }

}
