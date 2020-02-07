package com.faucet.map.bean;

import com.amap.api.maps.model.LatLng;

/**
 * Created by faucet on 2017/11/15.
 */

public class MapCameraPosition {

    private LatLng target;

    public LatLng getTarget() {
        return target;
    }

    public void setTarget(LatLng target) {
        this.target = target;
    }
}
