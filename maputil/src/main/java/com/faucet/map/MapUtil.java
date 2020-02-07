package com.faucet.map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;

import java.net.URISyntaxException;

public class MapUtil {

    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /***
     *
     * @Description 调用高德去导航
     * @param mLatLng
     * @param type
     */
    public static void startNavByAmap(LatLng mLatLng, int type, Context mContext) {
        try {
            if (isInstallApp(mContext, "com.autonavi.minimap")) {
                // 构造导航参数
                NaviPara naviPara = new NaviPara();
                // 设置终点位置
                naviPara.setTargetPoint(mLatLng);
                // 设置导航策略，这里是避免拥堵
                naviPara.setNaviStyle(type);
                // 调起高德地图导航
                AMapUtils.openAMapNavi(naviPara, mContext);
            }

        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    public static void startNavByUri (String title, LatLng mLatLng, Context mContext) {
        if (isInstallApp(mContext, "com.autonavi.minimap")) {
            Intent gddtIntent = new Intent("android.intent.action.VIEW",
                    Uri.parse("androidamap://navi?sourceApplication=“name”&poiname="
                            + title + "&lat=" + mLatLng.latitude + "&lon=" + mLatLng.longitude + "&dev=" + 0
                            + "&style=" + 4));
            gddtIntent.addCategory("android.intent.category.DEFAULT");
            gddtIntent.setPackage("com.autonavi.minimap");
            gddtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(gddtIntent);
        }
    }

    public static void startBaiduByUri (String title, LatLng mLatLng, Context mContext) {
        double[] position = gaoDeToBaidu(mLatLng.latitude, mLatLng.longitude);
        Intent intent = null;
        try {
            if (isInstallApp(mContext, "com.baidu.BaiduMap")) {
                intent = Intent
                        .getIntent("intent://map/navi?location="
                                + position[0]
                                + ","
                                + position[1]
                                + "&title="+title
                                + "&content="+title
                                + "&coordType=bd09ll&src=兔邦邦#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                mContext.startActivity(intent);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param mLatLng 终点纬度
     * @param dname 终点名称 必填
     *
     */
    public static void startTencentMap(Context context, LatLng mLatLng, String dname) {
        if (isInstallApp(context, "com.tencent.map")) {
            String uriString = null;
            StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&policy=0&referer=zhongshuo");
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(mLatLng.latitude)
                    .append(",")
                    .append(mLatLng.longitude);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.tencent.map");
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        }
    }

    /**
     * 根据包名判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    private static boolean isInstallApp(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }
}
