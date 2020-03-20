package com.faucet.autoattendance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import com.faucet.map.LocationService;
import com.faucet.map.bean.ReGeoLocationBean;
import com.faucet.map.builder.LocationBuilder;
import com.faucet.map.impl.OnReceiveReGeoData;
import com.faucet.quickutils.core.http.HttpManager;
import com.faucet.quickutils.core.http.callback.HttpCallBack;
import com.faucet.quickutils.utils.LogUtil;
import com.faucet.quickutils.utils.RandomUtils;
import com.faucet.quickutils.utils.TimeUtils;
import com.faucet.quickutils.utils.ToastUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import okhttp3.Call;

public class MyService extends Service {

    private String[] str = {"08:50", "12:00", "13:50", "18:20"};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int i = calendar.get(Calendar.DAY_OF_WEEK);
                System.out.println("99999988" + i);//这是定时所执行的任务
                if (i == 1) {
                    return;
                }

                final String s = TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("HH:mm", Locale.getDefault()));
                for (String temp : str) {
                    if (s.equals(temp)) {
                        int random = RandomUtils.getRandom(9);
                        Looper.prepare();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final LocationService locationService = new LocationService(MyService.this);
                                locationService.getBuilder()
                                        .setGeoCoderEnable(true)
                                        .setOnceLocation(true)
                                        .builder();
                                locationService.startLocation();
                                locationService.setOnReceiveReGeoListener(new OnReceiveReGeoData() {
                                    @Override
                                    public void onReceiveReGeoData(ReGeoLocationBean geoLocationBean) {
                                        locationService.destroyLocation();
                                        AddressBean addressBean = new AddressBean();
                                        addressBean.setProvince(geoLocationBean.getProvince());
                                        addressBean.setCity(geoLocationBean.getCity());
                                        addressBean.setDistrict(geoLocationBean.getDistrict());
                                        addressBean.setStreet_name(geoLocationBean.getStreet());
                                        addressBean.setStreet_number(geoLocationBean.getStreetNum());
                                        List<Double> addressXy = new ArrayList<>();
                                        addressXy.add(geoLocationBean.getLon());
                                        addressXy.add(geoLocationBean.getLat());
                                        List<OutWorkPunchInRequestModel.RelationBean> relationList = new ArrayList<>();
                                        OutWorkPunchInRequestModel.RelationBean bean = new OutWorkPunchInRequestModel.RelationBean();
                                        bean.setTarget_type(112);
//                bean.setTarget_id(1150023l);
                                        bean.setTarget_id(MyApplication.getInstance().spUtils.getLong("id", 0));
                                        relationList.add(bean);
                                        OutWorkPunchInRequestModel outWorkPunchInRequestModel = new OutWorkPunchInRequestModel();
                                        outWorkPunchInRequestModel.setAddress(geoLocationBean.getAddress());
                                        outWorkPunchInRequestModel.setAddress_json(new Gson().toJson(addressBean, AddressBean.class));
                                        outWorkPunchInRequestModel.setAddress_xy(addressXy);
                                        outWorkPunchInRequestModel.setRelation(relationList);


//                                        AddressBean addressBean = new AddressBean();
//                                        addressBean.setProvince("福建省");
//                                        addressBean.setCity("泉州市");
//                                        addressBean.setDistrict("泉港区");
//                                        addressBean.setStreet_name("");
//                                        addressBean.setStreet_number("");
//                                        List<Double> addressXy = new ArrayList<>();
//                                        addressXy.add(118.972404);
//                                        addressXy.add(25.203182);
//                                        List<OutWorkPunchInRequestModel.RelationBean> relationList = new ArrayList<>();
//                                        OutWorkPunchInRequestModel.RelationBean bean = new OutWorkPunchInRequestModel.RelationBean();
//                                        bean.setTarget_type(112);
//                                        bean.setTarget_id(1150023l);
//                                        relationList.add(bean);
//                                        OutWorkPunchInRequestModel outWorkPunchInRequestModel = new OutWorkPunchInRequestModel();
//                                        outWorkPunchInRequestModel.setAddress("建省泉州市泉港区");
//                                        outWorkPunchInRequestModel.setAddress_json(new Gson().toJson(addressBean, AddressBean.class));
//                                        outWorkPunchInRequestModel.setAddress_xy(addressXy);
//                                        outWorkPunchInRequestModel.setRelation(relationList);

                                        AutoAttendanceHttpManager.getInstance().postString(MyService.this, outWorkPunchInRequestModel, new HttpCallBack<BasicResponse<OutworkResponseModel>>() {

                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                ToastUtil.showShort(MyService.this, e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(BasicResponse<OutworkResponseModel> response, int id) {
                                                if (response.isSuccess()) {
                                                    ToastUtil.showShort(MyService.this, "打卡成功");
                                                } else if(response.isNeedLogin()) {

                                                } else {
                                                    ToastUtil.showShort(MyService.this, response.getMsg());
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }, random*60*1000);
                        Looper.loop();
                    }
                }
            }
        }).start();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int anhour=60*1000;
        long triggerAtMillis = SystemClock.elapsedRealtime()+anhour;

        Intent alarmIntent = new Intent(this,MyService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMillis, pendingIntent);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
