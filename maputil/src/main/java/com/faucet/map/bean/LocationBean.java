package com.faucet.map.bean;

import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.Serializable;

/**
 * Created by faucet on 2017/11/2.
 */

public class LocationBean implements Serializable {

    private double lat;
    private double lon;
    private int locationType;
    private double accuracy;//精度
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNum;
    private String address;
    private String cityCode;
    private String adcode;//区域码
    private int errorCode;
    private String errorInfo;

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

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public LocationBean transformData(Location location){
        setLat(location.getLatitude());
        setLon(location.getLongitude());
        setAccuracy(location.getAccuracy());
        Bundle bundle = location.getExtras();
        if(bundle!=null){
            setLocationType(bundle.getInt(MyLocationStyle.LOCATION_TYPE));
            setCountry(bundle.getString("Country"));
            setProvince(bundle.getString("Province"));
            setCity(bundle.getString("City"));
            setDistrict(bundle.getString("District"));
            setAddress(bundle.getString("Address"));
            setAdcode(bundle.getString("AdCode"));
            setCityCode(bundle.getString("citycode"));
            setStreet(bundle.getString("Street"));
            setStreetNum(bundle.getString("StreetNum"));
            setErrorCode(bundle.getInt(MyLocationStyle.ERROR_CODE));
            setErrorInfo(bundle.getString(MyLocationStyle.ERROR_INFO));
        }
        return this;
    }

    public LocationBean transformData(AMapLocation location){
        setLat(location.getLatitude());
        setLon(location.getLongitude());
        setAccuracy(location.getAccuracy());
        setLocationType(location.getLocationType());
        setCountry(location.getCountry());
        setProvince(location.getProvince());
        setCity(location.getCity());
        setDistrict(location.getDistrict());
        setAddress(location.getAddress());
        setAdcode(location.getAdCode());
        setCityCode(location.getCityCode());
        setStreet(location.getStreet());
        setStreetNum(location.getStreetNum());
        setErrorCode(location.getErrorCode());
        setErrorInfo(location.getLocationDetail());
        return this;
    }
}
