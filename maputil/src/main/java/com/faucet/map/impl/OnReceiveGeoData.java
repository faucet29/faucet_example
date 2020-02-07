package com.faucet.map.impl;


import com.amap.api.services.geocoder.GeocodeResult;

/**
 * Created by faucet on 2017/11/15.
 */

public interface OnReceiveGeoData {
    void onReceiveGeoData(GeocodeResult geocodeResult);
}
