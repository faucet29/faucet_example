package com.faucet.map.impl;


import com.faucet.map.bean.LocationBean;

/**
 * Created by faucet on 2017/11/15.
 */

public interface OnReceiveGpsData {
    void onReceiveGpsData(LocationBean result);
}
