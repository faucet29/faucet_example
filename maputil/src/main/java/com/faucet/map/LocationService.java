package com.faucet.map;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.faucet.map.bean.LocationBean;
import com.faucet.map.bean.PoiLocationBean;
import com.faucet.map.bean.ReGeoLocationBean;
import com.faucet.map.builder.LocationBuilder;
import com.faucet.map.impl.OnReceiveGeoData;
import com.faucet.map.impl.OnReceiveGpsData;
import com.faucet.map.impl.OnReceivePoiData;
import com.faucet.map.impl.OnReceiveReGeoData;

/**
 * Created by faucet on 2017/11/2.
 */

public class LocationService {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private int scope = 200;
    private Context context;
    private GeocodeSearch geocoderSearch;
    private OnReceiveGpsData onReceiveGpsData;
    private OnReceiveReGeoData onReceiveReGeoData;
    private OnReceiveGeoData onReceiveGeoData;
    private OnReceivePoiData onReceivePoiData;
    private boolean geoCoderEnable;
    private boolean poiEnable;
    private LocationBuilder szLocationBuilder;

    public LocationService(Context context){
        this.context = context;
        initLocation(context);
    }

    private void initLocation(Context context){
        //初始化client
        locationClient = new AMapLocationClient(context);
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(onReceiveGpsData!=null){
                    onReceiveGpsData.onReceiveGpsData(new LocationBean().transformData(aMapLocation));
                }
                setReGeoParams(aMapLocation.getLatitude(), aMapLocation.getLongitude(), scope);
            }
        });
    }

    public void startLocation(){
        //启动定位
        locationClient.startLocation();
    }

    public void stopLocation(){
        locationClient.stopLocation();
    }

    private void setReGeoParams(double lat, double lon, int scope){
        if (geoCoderEnable) {
            if (geocoderSearch == null) {
                geocoderSearch = new GeocodeSearch(context);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        //坐标转地址
                        if(onReceiveReGeoData !=null){
                            onReceiveReGeoData.onReceiveReGeoData(new ReGeoLocationBean().transformData(regeocodeResult));
                        }
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    }
                });
            }
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lon), scope, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        }
    }

    private void setGeoParams(String name) {
        if (geoCoderEnable) {
            if (geocoderSearch == null) {
                geocoderSearch = new GeocodeSearch(context);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        if(onReceiveGeoData !=null&&geocodeResult!=null){
                            onReceiveGeoData.onReceiveGeoData(geocodeResult);
                        }
                    }
                });
            }
            // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
            GeocodeQuery query = new GeocodeQuery(name, "010");
            geocoderSearch.getFromLocationNameAsyn(query);
        }
    }

    /**
     *
     * @param keyWord  表示搜索字符串，第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
     * @param cityCode  表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
     */
    private void setPoiParams(String keyWord, String cityCode, int currentPage){
        setPoiParams(keyWord, cityCode, 0, 0, 0, currentPage);
    }

    private void setPoiParams(String keyWord, String cityCode, double latitude, double longitude, int distance, int currentPage){
        if(poiEnable){
            //keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", cityCode);
            query.setPageSize(100);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);//设置查询页码
            PoiSearch poiSearch = new PoiSearch(context, query);
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    if(onReceivePoiData !=null){
                        onReceivePoiData.onReceiveGeoData(new PoiLocationBean().transformData(poiResult));
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {
                    Log.e("sss", "");
                }
            });
            if (longitude > 0 && latitude > 0) {
                if (distance == 0) {
                    distance = 10000;
                }
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,longitude), distance, true));
            }
            poiSearch.searchPOIAsyn();
        }
    }

    public LocationBuilder getBuilder(){
        if(szLocationBuilder==null){
            szLocationBuilder = new SZLocationBuilder();
        }
        return szLocationBuilder;
    }

    public void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    private AMapLocationClientOption getLocationOption() {
        if(locationOption==null){
            locationOption = new AMapLocationClientOption();
        }
        return locationOption;
    }

    public class SZLocationBuilder extends LocationBuilder {

        @Override
        public LocationBuilder setLocationMode(AMapLocationClientOption.AMapLocationMode mode) {
            getLocationOption().setLocationMode(mode);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            return this;
        }

        @Override
        public LocationBuilder setGpsFirst(Boolean gpsFirst) {
            getLocationOption().setGpsFirst(gpsFirst);
            return this;
        }

        @Override
        public LocationBuilder setHttpTimeOut(Long timeOutTime) {
            getLocationOption().setHttpTimeOut(timeOutTime);
            return this;
        }

        @Override
        public LocationBuilder setInterval(Long intervalTime) {
            getLocationOption().setInterval(intervalTime);
            return this;
        }

        @Override
        public LocationBuilder setOnceLocation(Boolean isOnceLocation) {
            getLocationOption().setOnceLocation(isOnceLocation);
            return this;
        }

        @Override
        public LocationBuilder setGeoCoderEnable(Boolean _geoCoderEnable) {
            geoCoderEnable = _geoCoderEnable;
            return this;
        }

        @Override
        public LocationBuilder setGeoCoderScope(Integer _scope) {
            scope = _scope;
            return this;
        }

        @Override
        public LocationBuilder setPoiEnable(Boolean _enable) {
            poiEnable = _enable;
            return this;
        }

        @Override
        public LocationService builder() {
            //设置定位参数
            locationClient.setLocationOption(getLocationOption());
            return LocationService.this;
        }
    }

    public void setOnReceiveGpsListener(OnReceiveGpsData listener){
        this.onReceiveGpsData = listener;
    }

    /**
     * 逆地理编码（坐标转地址信息）
     * @param lat
     * @param lon
     * @param scope 表示范围多少米
     */
    public void setOnReceiveReGeoListener(double lat, double lon, int scope, OnReceiveReGeoData listener){
        setOnReceiveReGeoListener(listener);
        setReGeoParams(lat, lon, scope);
    }

    public void setOnReceiveReGeoListener(OnReceiveReGeoData listener){
        this.onReceiveReGeoData = listener;
    }

    /**
     * 地理编码（地址转坐标）
     * @param name
     */
    public void setOnReceiveGeoListener(String name, OnReceiveGeoData listener){
        setOnReceiveGeoListener(listener);
        setGeoParams(name);
    }

    public void setOnReceiveGeoListener(OnReceiveGeoData listener){
        this.onReceiveGeoData = listener;
    }

    /**
     *
     * @param keyWord  表示搜索字符串
     * @param cityCode  表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
     */
    public void setOnReceivePoiListener(String keyWord, String cityCode, double latitude, double longitude, int distance, int currentPage, OnReceivePoiData listener){
        this.onReceivePoiData = listener;
        setPoiParams(keyWord, cityCode, latitude, longitude, distance, currentPage);
    }
}
