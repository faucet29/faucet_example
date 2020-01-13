package com.faucet.quickutils.core.manager;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by faucet on 2018/1/2.
 */

public class JavaScriptInterface {

    private WebView webView;
    private Context context;

    public JavaScriptInterface(Context context, WebView webView) {
        this.webView = webView;
        this.context = context;
    }

    @JavascriptInterface
    public void pickUser(String params) {
        //处理相应的事
    }

}
