package com.faucet.quickutils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    private OnScrollChangedListener onScrollChangedListener = null;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScrollChangedListener {

        void onScrollChanged(CustomWebView scrollView, int x, int y, int oldx, int oldy);

    }
}
