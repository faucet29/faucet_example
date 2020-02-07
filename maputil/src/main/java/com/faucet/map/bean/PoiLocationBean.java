package com.faucet.map.bean;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faucet on 2017/11/3.
 */

public class PoiLocationBean {

    private List<PoiBean> poiBeanList;

    public List<PoiBean> getPoiBeanList() {
        return poiBeanList;
    }

    public void setPoiBeanList(List<PoiBean> poiBeanList) {
        this.poiBeanList = poiBeanList;
    }

    public class PoiBean {
        private String building;
        private String adCode;
        private String city;
        private String district;
        private String address;
        private double lat;
        private double lont;
        private String province;
        private String townShip;
        private String neighborhood;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getAdCode() {
            return adCode;
        }

        public void setAdCode(String adCode) {
            this.adCode = adCode;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLont() {
            return lont;
        }

        public void setLont(double lont) {
            this.lont = lont;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getTownShip() {
            return townShip;
        }

        public void setTownShip(String townShip) {
            this.townShip = townShip;
        }

        public String getNeighborhood() {
            return neighborhood;
        }

        public void setNeighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
        }
    }

    public PoiLocationBean transformData(PoiResult location){
        List<PoiBean> temp = new ArrayList<>();
        for(int i=0;i<location.getPois().size();i++){
            PoiItem geocodeAddress = location.getPois().get(i);
            PoiBean geoAddressBean = new PoiBean();
            geoAddressBean.setLat(geocodeAddress.getLatLonPoint().getLatitude());
            geoAddressBean.setLont(geocodeAddress.getLatLonPoint().getLongitude());
            geoAddressBean.setAdCode(geocodeAddress.getAdCode());
            geoAddressBean.setCity(geocodeAddress.getCityName());
            geoAddressBean.setDistrict(geocodeAddress.getAdName());
            geoAddressBean.setAddress(geocodeAddress.getSnippet());
            geoAddressBean.setProvince(geocodeAddress.getProvinceName());
            geoAddressBean.setBuilding(geocodeAddress.getTitle());
            geoAddressBean.setName(geocodeAddress.toString());
            temp.add(geoAddressBean);
        }
        setPoiBeanList(temp);
        return this;
    }
}
