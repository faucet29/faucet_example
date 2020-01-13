package com.faucet.quickutils;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.faucet.quickutils.core.constant.AppConstant;
import com.faucet.quickutils.utils.LoggerInterceptor;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.OkHttpClient;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("OkHttpUtils", AppConstant.isDebug))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        // 必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回
        BGASwipeBackHelper.init(this, null);

        ViewTarget.setTagId(R.id.tag_glide);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // Enable MultiDex.
    }
}
