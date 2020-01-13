package com.faucet.quickutils.core.http.impl;


/**
 * Created by mac on 16/3/17.
 */
public interface HttpInterface<T> {
    void onSuccess(T data);
    void onFail(String error);
}
