package com.faucet.quickutils.utils;

import android.Manifest;

/**
 * Created by Faucet on 2017/7/11.
 */

public enum PermissionEnum {

    CAMERA("相机",Manifest.permission.CAMERA, "您已关闭相机权限，可能导致相机功能无法正常使用，建议开启"),
    EXTERNAL_STORAGE("存储", Manifest.permission.WRITE_EXTERNAL_STORAGE, "您已关闭存储权限，可能导致无法正常访问本地文件，建议开启"),
    LOCATION("位置",Manifest.permission.ACCESS_FINE_LOCATION, "您已关闭位置权限，可能导致无法正确获取周边商户，建议开启"),
    CONTACTS("通讯录",Manifest.permission.READ_CONTACTS, "您已关闭通讯录权限"),
    PHONE("电话",Manifest.permission.CALL_PHONE, "您已关闭此权限，可能会导致您无法正常使用，建议开启"),
    CALENDAR("日历",Manifest.permission.READ_CALENDAR, "您已关闭日历权限"),
    SENSORS("身体传感器",Manifest.permission.BODY_SENSORS, "您已关闭身体传感器权限"),
    RECORD_AUDIO("麦克风",Manifest.permission.RECORD_AUDIO, "您已关闭麦克风权限，可能导致无法发送语音，建议开启"),
    SMS("短信",Manifest.permission.READ_SMS, "您已关闭短信权限");

    private final String name;
    private final String permission;
    private final String denidStr;

    PermissionEnum(String name, String permission, String denidStr) {
        this.name = name;
        this.permission = permission;
        this.denidStr = denidStr;
    }

    public static PermissionEnum statusOf(String v) {
        for (PermissionEnum s : values()) {
            if (s.permission.equals(v)) {
                return s;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String permission() {
        return permission;
    }

    public String getDenidStr(){
        return denidStr;
    }
}
