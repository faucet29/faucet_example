package com.faucet.map.builder;

import com.amap.api.location.AMapLocationClientOption;
import com.faucet.map.LocationService;

/**
 * Created by faucet on 2017/11/2.
 */

public abstract class LocationBuilder {

    /**
     * 设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
     * @param _mode
     * @return
     */
    public abstract LocationBuilder setLocationMode(AMapLocationClientOption.AMapLocationMode _mode);

    /**
     * 设置是否gps优先，只在高精度模式下有效。默认关闭
     * @param _gpsFirst
     * @return
     */
    public abstract LocationBuilder setGpsFirst(Boolean _gpsFirst);

    /**
     * 设置网络请求超时时间。默认为30秒。在仅设备模式下无效
     * @param _timeOutTime
     * @return
     */
    public abstract LocationBuilder setHttpTimeOut(Long _timeOutTime);

    /**
     * 设置定位间隔。默认为2秒
     * @param _intervalTime
     * @return
     */
    public abstract LocationBuilder setInterval(Long _intervalTime);

    /**
     * 设置是否单次定位。默认是false
     * @param _isOnceLocation
     * @return
     */
    public abstract LocationBuilder setOnceLocation(Boolean _isOnceLocation);

    /**
     * 是否开启geo
     * @param _geoCoderEnable
     * @return
     */
    public abstract LocationBuilder setGeoCoderEnable(Boolean _geoCoderEnable);

    /**
     * geo范围
     * @param _scope
     * @return
     */
    public abstract LocationBuilder setGeoCoderScope(Integer _scope);

    /**
     * 是否开启Poi
     * @param _enable
     * @return
     */
    public abstract LocationBuilder setPoiEnable(Boolean _enable);

    public abstract LocationService builder();
}
