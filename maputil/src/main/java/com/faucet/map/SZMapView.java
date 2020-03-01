package com.faucet.map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
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
import com.faucet.map.bean.MapCameraPosition;
import com.faucet.map.bean.PoiLocationBean;
import com.faucet.map.bean.ReGeoLocationBean;
import com.faucet.map.builder.MapBuilder;
import com.faucet.map.impl.OnMapCameraChange;
import com.faucet.map.impl.OnMapMarkerClickListener;
import com.faucet.map.impl.OnReceiveGeoData;
import com.faucet.map.impl.OnReceiveGpsData;
import com.faucet.map.impl.OnReceivePoiData;
import com.faucet.map.impl.OnReceiveReGeoData;

import java.util.List;

/**
 * Created by faucet on 2017/11/1.
 */

public class SZMapView extends MapView {

    public static final int LOCATION_TYPE_SHOW = MyLocationStyle.LOCATION_TYPE_SHOW;//只定位一次。
    public static final int LOCATION_TYPE_LOCATE = MyLocationStyle.LOCATION_TYPE_LOCATE;//定位一次，且将视角移动到地图中心点。
    public static final int LOCATION_TYPE_FOLLOW = MyLocationStyle.LOCATION_TYPE_FOLLOW;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
    public static final int LOCATION_TYPE_MAP_ROTATE = MyLocationStyle.LOCATION_TYPE_MAP_ROTATE;//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
    public static final int LOCATION_TYPE_LOCATION_ROTATE = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE;//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
    public static final int LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
    public static final int LOCATION_TYPE_FOLLOW_NO_CENTER = MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER;//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
    public static final int LOCATION_TYPE_MAP_ROTATE_NO_CENTER = MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER;//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。

    private OnReceiveGpsData onReceiveGpsData;
    private OnMarkerClickListener onMarkerClickListener;
    private GeocodeSearch geocoderSearch;
    private int scope = 200;
    private boolean poiEnable;
    private boolean geoCoderEnable;
    public MapBuilder builder;
    private OnReceiveReGeoData onReceiveReGeoData;
    private OnReceiveGeoData onReceiveGeoData;
    private OnReceivePoiData onReceivePoiData;

    public SZMapView(Context context) {
        super(context);
        init();
    }

    public SZMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SZMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public SZMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        init();
    }

    private void init(){
        getSZMap().setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(onReceiveGpsData!=null){
                    onReceiveGpsData.onReceiveGpsData(new LocationBean().transformData(location));
                }
                //location信息可能存在不全的情况，通过逆地理编码查询更详细信息
                setReGeoParams(location.getLatitude(), location.getLongitude(), scope);
            }
        });
    }

    public MapBuilder getBuilder(){
        if(builder==null){
            builder = new SZMapBuilder();
        }
        return builder;
    }

    public AMap getSZMap(){
        return getMap();
    }

    private void setReGeoParams(double lat, double lon, int scope){
        if (geoCoderEnable) {
            if (geocoderSearch == null) {
                geocoderSearch = new GeocodeSearch(getContext());
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        if(onReceiveReGeoData !=null&&regeocodeResult!=null){
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
                geocoderSearch = new GeocodeSearch(getContext());
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

    private void setPoiParams(String keyWord, String cityCode, double latitude, double longitude, int distance, int currentPage){
        if(poiEnable){
            //keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", cityCode);
            query.setPageSize(20);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);//设置查询页码
            PoiSearch poiSearch = new PoiSearch(getContext(), query);
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
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,longitude), distance, true));
            poiSearch.searchPOIAsyn();
        }
    }

    public void showAllMarkers(List<Marker> markers){
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度
        for(int i=0;i<markers.size();i++){
            boundsBuilder.include(markers.get(i).getPosition());//把所有点都include进去（LatLng类型）
        }
        getSZMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));//第二个参数为四周留空宽度
    }

    /**
     * 添加marker
     * @param lat
     * @param lon
     * @param resId
     */
    public Marker addMarker(double lat, double lon, int resId){
        MarkerOptions markerOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),resId)))
                .position(new LatLng(lat, lon))
                .draggable(false);
        Marker marker = getSZMap().addMarker(markerOption);
        return marker;
    }

//    public Marker addMarker(double lat, double lon, int resId, String title, String content){
//        MarkerOptions markerOption = new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),resId)))
//                .position(new LatLng(lat, lon))
//                .title(title)
//                .snippet(content)
//                .draggable(false);
//        Marker marker = getSZMap().addMarker(markerOption);
//        marker.showInfoWindow();
//        return marker;
//    }

    public Marker addMarker(double lat, double lon, View view){
        MarkerOptions markerOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromView(view))
                .position(new LatLng(lat, lon))
                .draggable(false);
        Marker marker = getSZMap().addMarker(markerOption);
        return marker;
    }

    /**
     * 绘制圆圈
     *
     * @param latLng
     */
    public Circle drawCircle(LatLng latLng, int fillColor, int strokeColor, int strokeWidth, int radius) {
        Circle circle = getSZMap().addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius)
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(strokeWidth));
        return circle;
    }

    /**
     * 通过坐标点画线
     * @param latLngs
     * @param lineWidth 线的粗细
     * @param color
     */
    public void polyLine(List<LatLng> latLngs, int lineWidth, int color, boolean isDottedLine){
        getSZMap().addPolyline(new PolylineOptions().
                addAll(latLngs).width(lineWidth).color(color).setDottedLine(isDottedLine));
    }

    /**
     * 地图拖动监听
     * @param onMapCameraChange
     */
    public void setOnMapCameraChangeListener(final OnMapCameraChange onMapCameraChange){
        getSZMap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                MapCameraPosition mapCameraPosition = new MapCameraPosition();
                mapCameraPosition.setTarget(cameraPosition.target);
                onMapCameraChange.onCameraChange(mapCameraPosition);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                MapCameraPosition mapCameraPosition = new MapCameraPosition();
                mapCameraPosition.setTarget(cameraPosition.target);
                onMapCameraChange.onCameraChangeFinish(mapCameraPosition);
            }
        });
    }

    /**
     * 设置缩放
     * @param zoom
     */
    public void setZoom(float zoom){
        getSZMap().animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    /**
     * 移动到某个位置
     * @param latLng
     * @param zoom
     */
    public void moveCamera(LatLng latLng, float zoom){
        if(zoom==0){
            getSZMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }else{
            getSZMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
    }

    /**
     * 显示中国地图
     */
    public void selectOverlayWithoutZoom(){
        LatLng ll = new LatLng(36.0, 108.0);
        moveCamera(ll, 3.0f);
    }

    /**
     * marker点击事件
     * @param onMapMarkerClickListener
     */
    public void setOnMarkerClickListener(final OnMapMarkerClickListener onMapMarkerClickListener){
        getSZMap().setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onMapMarkerClickListener.onMarkerClick(marker);
                return true;
            }
        });
    }

    public class SZMapBuilder extends MapBuilder {

        @Override
        public MapBuilder setLocationEnable(Boolean locationEnable) {
            getSZMap().setMyLocationEnabled(locationEnable);
            return this;
        }

        @Override
        public MapBuilder setLocationButtonVisible(Boolean locationButtonVisible) {
            getSZMap().getUiSettings().setMyLocationButtonEnabled(locationButtonVisible);
            return this;
        }

        @Override
        public MapBuilder setInterval(Long intervalTime) {
            getMyLocationStyle().interval(intervalTime);
            return this;
        }

        @Override
        public MapBuilder setLocationType(Integer locationType) {
            getMyLocationStyle().myLocationType(locationType);
            return this;
        }

        @Override
        public MapBuilder setShowMyLocation(Boolean showMyLocation) {
            getMyLocationStyle().showMyLocation(showMyLocation);
            return this;
        }

        @Override
        public MapBuilder setMyLocationIcon(Integer myLocationIconId) {
            getMyLocationStyle().myLocationIcon(BitmapDescriptorFactory.fromResource(myLocationIconId));
            return this;
        }

        @Override
        public MapBuilder setStrokeColor(Integer strokeColor) {
            getMyLocationStyle().strokeColor(strokeColor);
            return this;
        }

        @Override
        public MapBuilder setRadiusFillColor(Integer radiusFillColor) {
            getMyLocationStyle().radiusFillColor(radiusFillColor);
            return this;
        }

        @Override
        public MapBuilder setStrokeWidth(Float strokeWidth) {
            getMyLocationStyle().strokeWidth(strokeWidth);
            return this;
        }

        @Override
        public MapBuilder setGeoCoderEnable(Boolean _geoCoderEnable) {
            geoCoderEnable = _geoCoderEnable;
            return this;
        }

        @Override
        public MapBuilder setGeoCoderScope(Integer _scope) {
            scope = _scope;
            return this;
        }

        @Override
        public MapBuilder setMarkerClickEnable(Boolean markerClickEnable) {
            if (markerClickEnable) {
                getSZMap().setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        onMarkerClickListener.onMarkerClick(marker);
                        return false;
                    }
                });
            }
            return this;
        }

        @Override
        public MapBuilder setPoiEnable(Boolean _enable) {
            poiEnable = _enable;
            return this;
        }

        @Override
        public MapBuilder setScaleControlsEnabled(Boolean scaleControlsEnabled) {
            getSZMap().getUiSettings().setScaleControlsEnabled(scaleControlsEnabled);
            return this;
        }

        @Override
        public MapBuilder setZoomControlsEnabled(Boolean zoomControlsEnabled) {
            getSZMap().getUiSettings().setZoomControlsEnabled(zoomControlsEnabled);
            return this;
        }

        @Override
        public MapBuilder setCompassEnabled(Boolean compassEnabled) {
            getSZMap().getUiSettings().setCompassEnabled(compassEnabled);
            return this;
        }

        @Override
        public SZMapView build() {
            getSZMap().setMyLocationStyle(getMyLocationStyle());
            return SZMapView.this;
        }
    }

    private MyLocationStyle getMyLocationStyle() {
        MyLocationStyle myLocationStyle = getSZMap().getMyLocationStyle();
        if (myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();
        }
        return myLocationStyle;
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

    public interface OnMarkerClickListener{
        void onMarkerClick(Marker marker);
    }

    public void setOnMarkerClickListener(OnMarkerClickListener _onMarkerClickListener){
        this.onMarkerClickListener = _onMarkerClickListener;
    }
}
