package com.faucet.quickutils.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用来计算显示的时间是多久之前的！
 */
public class TransitionTime {

    private static SimpleDateFormat formatBuilder;
    public static final int WEEKDAYS = 7;
    public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param milliseconds    data
     * @return  日期变量转成对应的星期字符串
     */
    public static String DateToWeek(long milliseconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    /**
     * 时间描述
     * @param milliseconds
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getSimpleTimeDesc(long milliseconds) {
        formatBuilder = new SimpleDateFormat("HH:mm");
        String time = formatBuilder.format(milliseconds);
        StringBuilder sb = new StringBuilder();
        String dateStr = DateUtil.date2Str(new Date(milliseconds),DateUtil.FORMAT_YMD)+" 23:59:59";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        long datetime = System.currentTimeMillis() - DateUtil.parse(dateStr,DateUtil.FORMAT_YMDHMS).getTime();
        long day = (long) Math.ceil(datetime / 24 / 60 / 60 / 1000.0f);// 天前
        System.out.println("day:"+day);
        if (day>=0 && day <= 7) {// 一周内
            if (day == 0) {// 今天
                sb.append(time);
            } else if (day == 1) {// 昨天
                sb.append("昨天 ").append(time);
            } else {
                sb.append(DateToWeek(milliseconds)).append(" ").append(time);
            }
        } else {// 一周之前
            //是否当前年份
            String curYear = DateUtil.date2Str(new Date(),DateUtil.FORMAT_Y);
            String mYear = DateUtil.date2Str(new Date(milliseconds),DateUtil.FORMAT_Y);
            if(curYear.equals(mYear)){
                sb.append(DateUtil.date2Str(calendar,"MM.dd HH:mm"));
            }else{
                sb.append(DateUtil.date2Str(calendar,"yy.MM.dd HH:mm"));
            }
        }
        return sb.toString();
    }
}