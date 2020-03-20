package com.faucet.autoattendance;

import android.app.Application;

import com.faucet.quickutils.utils.SPUtils;

public class MyApplication extends Application {

    public SPUtils spUtils;
    private static MyApplication INSTANCE;
    public static MyApplication getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        spUtils = new SPUtils(this, "appDta");
    }
}
