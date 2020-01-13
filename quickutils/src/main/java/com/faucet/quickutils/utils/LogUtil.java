package com.faucet.quickutils.utils;

import android.util.Log;

import com.faucet.quickutils.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log统一管理类
 * Created by Faucet on 2016/8/2.
 */
public class LogUtil {
    private static boolean LOGABLE = BuildConfig.IS_DEBUG;

    public static void setDebug(boolean debug) {
        LOGABLE = debug;
    }

    public static String getFileLineMethod() {
        StackTraceElement traceElement = new Exception()
                .getStackTrace()[1];
        StringBuffer toStringBuffer = new StringBuffer("[")
                .append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ")
                .append(traceElement.getMethodName()).append("()").append("]");
        return toStringBuffer.toString();
    }

    public static String _FILE_() {
        StackTraceElement traceElement = new Exception()
                .getStackTrace()[1];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = new Exception()
                .getStackTrace()[1];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = new Exception()
                .getStackTrace()[1];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date(0L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                Locale.getDefault());
        return sdf.format(now);
    }

    public static void v(String msg) {
        if (LOGABLE) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (LOGABLE) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (LOGABLE) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (LOGABLE) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if(StringUtils.isEmpty(msg)){
            return ;
        }
        if (LOGABLE) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg, boolean debug) {
        if (debug) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg, boolean debug) {
        if (debug) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg, boolean debug) {
        if (debug) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg, boolean debug) {
        if (debug) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg, boolean debug) {
        if (debug) {
            StackTraceElement traceElement = new Exception()
                    .getStackTrace()[1];
            StringBuffer toStringBuffer = new StringBuffer("[")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement.getMethodName()).append("()")
                    .append("]");
            String TAG = toStringBuffer.toString();
            Log.e(TAG, msg);
        }
    }
}
