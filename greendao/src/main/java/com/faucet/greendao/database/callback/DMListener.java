package com.faucet.greendao.database.callback;

/**
 * Created by Faucet on 2016/8/31.
 */
public interface DMListener<T> {
    void onFinish(T data);
    void onError(String error);
}
