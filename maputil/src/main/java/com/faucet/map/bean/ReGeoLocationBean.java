package com.faucet.map.bean;

import com.amap.api.services.geocoder.RegeocodeResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by faucet on 2017/11/2.
 */

public class ReGeoLocationBean extends LocationBean {

    private List<PoiItem> poiItemList = new ArrayList<>();

    public List<PoiItem> getPoiItemList() {
        return poiItemList;
    }

    public void setPoiItemList(List<PoiItem> poiItemList) {
        this.poiItemList = poiItemList;
    }

    public class PoiItem implements Serializable {
        private double lat;
        private double lon;
        private String title;
        private String address;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public ReGeoLocationBean transformData(RegeocodeResult location){
        List<PoiItem> poiItemList = new ArrayList<>();
        for(int i=0;i<location.getRegeocodeAddress().getPois().size();i++){
            PoiItem poiItem = new PoiItem();
            poiItem.setLat(location.getRegeocodeAddress().getPois().get(i).getLatLonPoint().getLatitude());
            poiItem.setLon(location.getRegeocodeAddress().getPois().get(i).getLatLonPoint().getLongitude());
            poiItem.setAddress(location.getRegeocodeAddress().getPois().get(i).getSnippet());
            poiItem.setTitle(location.getRegeocodeAddress().getPois().get(i).getTitle());
            poiItemList.add(poiItem);
        }
        setLat(location.getRegeocodeQuery().getPoint().getLatitude());
        setLon(location.getRegeocodeQuery().getPoint().getLongitude());
        setProvince(location.getRegeocodeAddress().getProvince());
        setCity(location.getRegeocodeAddress().getCity());
        setDistrict(location.getRegeocodeAddress().getDistrict());
        setStreet(location.getRegeocodeAddress().getStreetNumber().getStreet());
        setStreetNum(location.getRegeocodeAddress().getStreetNumber().getNumber());
        setAddress(location.getRegeocodeAddress().getFormatAddress());
        setAdcode(location.getRegeocodeAddress().getAdCode());
        setCityCode(location.getRegeocodeAddress().getCityCode());
        setPoiItemList(poiItemList);
        return this;
    }
}
