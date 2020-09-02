package com.faucet.quickutils.views.groupheadview.listener;

import android.graphics.Bitmap;

public interface OnProgressListener {
    void onStart();

    void onComplete(Bitmap bitmap);
}
