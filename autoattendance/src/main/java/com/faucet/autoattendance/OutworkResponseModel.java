package com.faucet.autoattendance;

import java.io.Serializable;

/**
 * Created by Faucet on 2016/3/29.
 */
public class OutworkResponseModel implements Serializable{

    private long id = 0;
    private long handle_time = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHandle_time() {
        return handle_time;
    }

    public void setHandle_time(long handle_time) {
        this.handle_time = handle_time;
    }
}
