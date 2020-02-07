package com.faucet.map.impl;


import com.faucet.map.bean.MapCameraPosition;

/**
 * Created by faucet on 2017/11/15.
 */

public interface OnMapCameraChange {
    void onCameraChange(MapCameraPosition mapCameraPosition);
    void onCameraChangeFinish(MapCameraPosition mapCameraPosition);
}
