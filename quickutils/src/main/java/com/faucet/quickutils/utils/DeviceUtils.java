package com.faucet.quickutils.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/1
 *     desc  : 设备相关工具类
 * </pre>
 */
public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @param context 上下文
     * @return MAC地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress().replace(":", "");
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备MAC地址
     *
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return MAC地址
     */
    public static String getMacAddress() {
        String macAddress = null;
        LineNumberReader reader = null;
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            reader = new LineNumberReader(ir);
            macAddress = reader.readLine().replace(":", "");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备厂商，如Xiaomi
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取当前IMEI 设备号
     * 注意:TelephonyManager可能获取不到
     * @return boolean
     */
    public static String getDeviceId(Context context) {
        String fileName = "SP_SYSTEM_INFO";
        String fieldName = "SP_DEVICE_ID";
        SPUtils spUtils = new SPUtils(context,fileName);
        String deviceId = spUtils.getString(fieldName);

        if(StringUtils.isEmpty(deviceId)){
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
                if(StringUtils.isEmpty(deviceId)){
                    deviceId = getSpareDeviceId(context);
                }
            } catch (Exception e) {
                deviceId = getSpareDeviceId(context);
            }
            if (!StringUtils.isEmpty(deviceId)) {
                spUtils.putString(fieldName,deviceId);
            }
        }
        return deviceId;
    }

    private static String getSpareDeviceId(Context context) {
        return EncryptUtils.encryptMD5ToString(RandomUtils.getRandomCapitalLetters(16)+"-"+DateUtil.getTimeString());
    }
}
