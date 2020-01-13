package com.faucet.quickutils.core.http.entity;

import java.io.Serializable;

/**
 * Created by Faucet on 16/8/30.
 */
public class BasicResponse<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return getCode().equals("0");
    }

    public boolean isNeedLogin() {
        return getCode().equals("3");
    }
}
