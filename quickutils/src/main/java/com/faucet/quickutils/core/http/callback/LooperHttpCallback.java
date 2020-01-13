package com.faucet.quickutils.core.http.callback;

import android.os.Handler;
import android.os.Message;

import com.faucet.quickutils.core.http.impl.HttpInterface;

/**
 * Created by mac on 15/9/17.
 */
public class LooperHttpCallback<T> implements Handler.Callback {
    private T t;
    private String error;
    private HttpInterface<T> listener;
    private boolean isSuccess;

    public LooperHttpCallback(HttpInterface<T> listener, T t, String error, boolean isSuccess) {
        this.listener = listener;
        this.t = t;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    @Override
    public boolean handleMessage(Message message) {
        if(!isSuccess){
            listener.onFail(error);
        }else{
            listener.onSuccess(t);
        }

        return true;
    }
}