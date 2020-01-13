package com.faucet.quickutils.utils;

import android.view.View;

/**
 * Created by apple on 2017/5/24.
 */

public abstract class NoRepetitionClickListener implements View.OnClickListener {

    private final static long MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoRepeatClick(v);
        }
    }

    public abstract void onNoRepeatClick(View view);
}
