package com.faucet.quickutils.core.http.entity;

import java.io.Serializable;

/**
 * Created by Faucet on 2016/9/3.
 */
public abstract class BasicRequest implements Serializable {
    public abstract String getHttpRequestPath();
}
