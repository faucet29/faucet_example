package com.faucet.autoattendance;

import java.io.Serializable;

/**
 * Created by Faucet on 2016/11/16.
 */

public class AddressBean implements Serializable{
    //address_json={"district":"思明区","street_name":"环岛东路辅路","street_number":"","province":"福建省","city":"厦门市"}
    private String district;
    private String street_name;
    private String street_number;
    private String province;
    private String city;

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

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
