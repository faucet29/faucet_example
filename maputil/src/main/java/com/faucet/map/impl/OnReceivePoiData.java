package com.faucet.map.impl;


import com.faucet.map.bean.PoiLocationBean;

/**
 * Created by faucet on 2017/11/15.
 */

public interface OnReceivePoiData {
    void onReceiveGeoData(PoiLocationBean geoLocationBean);
}
