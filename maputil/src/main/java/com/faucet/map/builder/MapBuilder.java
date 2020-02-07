package com.faucet.map.builder;


import com.faucet.map.SZMapView;

/**
 * Created by faucet on 2017/11/2.
 */

public abstract class MapBuilder {

    /**
     * 设置是否开启定位
     * @param _locationEnable
     * @return
     */
    public abstract MapBuilder setLocationEnable(Boolean _locationEnable);

    /**
     * 设置默认定位图标是否显示
     * @param _locationButtonVisible
     * @return
     */
    public abstract MapBuilder setLocationButtonVisible(Boolean _locationButtonVisible);

    /**
     * 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。locationEnable生效才有效
     * @param _intervalTime
     * @return
     */
    public abstract MapBuilder setInterval(Long _intervalTime);

    /**
     * 设置定位类型，locationEnable生效才有效
     * @param _locationType
     * @return
     */
    public abstract MapBuilder setLocationType(Integer _locationType);

    /**
     * 设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。locationEnable生效才有效
     * @param _showMyLocation
     * @return
     */
    public abstract MapBuilder setShowMyLocation(Boolean _showMyLocation);

    /**
     * 设置定位蓝点自定义图标
     * @param _myLocationIconId
     * @return
     */
    public abstract MapBuilder setMyLocationIcon(Integer _myLocationIconId);

    /**
     * 设置定位蓝点精度圆圈的边框颜色的方法。
     * @param _strokeColor
     * @return
     */
    public abstract MapBuilder setStrokeColor(Integer _strokeColor);

    /**
     * 设置定位蓝点精度圆圈的填充颜色的方法。
     * @param _radiusFillColor
     * @return
     */
    public abstract MapBuilder setRadiusFillColor(Integer _radiusFillColor);

    /**
     * 设置定位蓝点精度圈的边框宽度的方法。
     * @param _strokeWidth
     * @return
     */
    public abstract MapBuilder setStrokeWidth(Float _strokeWidth);

    /**
     * 设置地理编码（正向和逆向）查询监听。正向逆向取决于设置的参数setReGeoParams和setGeoParams
     * @param _geoCoderEnable
     * @return
     */
    public abstract MapBuilder setGeoCoderEnable(Boolean _geoCoderEnable);

    /**
     * geo范围
     * @param _scope
     * @return
     */
    public abstract MapBuilder setGeoCoderScope(Integer _scope);

    /**
     * marker是否可点击
     * @param _markerClickEnable
     * @return
     */
    public abstract MapBuilder setMarkerClickEnable(Boolean _markerClickEnable);


    /**
     * 是否开启Poi
     * @param _enable
     * @return
     */
    public abstract MapBuilder setPoiEnable(Boolean _enable);

    /**
     * 是否显示标尺
     * @param _enable
     * @return
     */
    public abstract MapBuilder setScaleControlsEnabled(Boolean _enable);

    /**
     * 设置地图默认的缩放按钮是否显示
     * @param _enable
     * @return
     */
    public abstract MapBuilder setZoomControlsEnabled(Boolean _enable);

    /**
     * 设置地图默认的指南针是否显示
     * @param _enable
     * @return
     */
    public abstract MapBuilder setCompassEnabled(Boolean _enable);

    public abstract SZMapView build();

}
