package com.faucet.autoattendance;


import com.faucet.quickutils.core.http.entity.BasicRequest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Faucet on 2016/11/25.
 */

public class OutWorkPunchInRequestModel extends BasicRequest implements Serializable{

    private String address;
    private List<Double> address_xy;
    private List<String> pics;
    private String comment;
    private int has_relation;
    private String address_json;
    private List<RelationBean> relation;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Double> getAddress_xy() {
        return address_xy;
    }

    public void setAddress_xy(List<Double> address_xy) {
        this.address_xy = address_xy;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHas_relation() {
        return has_relation;
    }

    public void setHas_relation(int has_relation) {
        this.has_relation = has_relation;
    }

    public String getAddress_json() {
        return address_json;
    }

    public void setAddress_json(String address_json) {
        this.address_json = address_json;
    }

    public List<RelationBean> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationBean> relation) {
        this.relation = relation;
    }

    @Override
    public String getHttpRequestPath() {
        return "https://api.shaozi.com/Attendance/Outwork";
//        return "https://pre-api.shaozi.com/Attendance/Outwork";
    }

    public static class RelationBean implements Serializable{
        private long target_type;
        private long target_id;

        public long getTarget_type() {
            return target_type;
        }

        public void setTarget_type(long target_type) {
            this.target_type = target_type;
        }

        public long getTarget_id() {
            return target_id;
        }

        public void setTarget_id(long target_id) {
            this.target_id = target_id;
        }
    }
}
