package com.faucet.quickutils.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faucet.quickutils.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Toast统一管理类
 * Created by Faucet on 2016/8/2.
 */
public class ToastUtil {
    public static boolean isShow = true;

    /*cannot be instantiated*/
    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    public static void showCenterBigToast(Context context, String str, int raw){
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        ViewGroup toastView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout
                .toastview, null, false);
        ImageView iv_toast = (ImageView) toastView.findViewById(R.id.iv_toast);
        TextView tv_toast = (TextView) toastView.findViewById(R.id.tv_toast);
        if(raw==0){
            iv_toast.setVisibility(View.GONE);
        }else{
            iv_toast.setImageResource(raw);
        }
        tv_toast.setText(str);
        initTN(toast, R.style.anim_view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        //以下为另一种实现方式
//        LinearLayout toastView = (LinearLayout) toast.getView();
//        ImageView imageCodeProject = new ImageView(context);
//        imageCodeProject.setImageResource(raw);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0,0,0,20);
//        layoutParams.gravity = Gravity.CENTER;
//        imageCodeProject.setLayoutParams(layoutParams);
//        toastView.addView(imageCodeProject, 0);
        toast.setView(toastView);
        toast.show();
    }

    private static void initTN(Toast toast, int animations) {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            Object mTN = tnField.get(toast);
            Method show = mTN.getClass().getMethod("show");
            Method hide = mTN.getClass().getMethod("hide");

            /**设置动画*/
            if (animations != -1) {
                Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                tnParamsField.setAccessible(true);
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
                params.windowAnimations = animations;
            }

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
