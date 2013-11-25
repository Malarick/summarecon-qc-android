package com.summarecon.qcapp.core;

import android.app.Application;

public class SummareconQCApplication extends Application {

    private QCConfig config;

    @Override
    public void onCreate() {
        super.onCreate();
        config = new QCConfig(this);
    }

}
