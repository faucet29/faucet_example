package com.faucet.greendao;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication INSTANCE;

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
