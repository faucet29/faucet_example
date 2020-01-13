package com.faucet.quickutils.views.overscroll;

import android.content.Context;
import android.os.Build;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;

import com.faucet.quickutils.views.overscroll.footer.ClassicHoldLoadView;
import com.faucet.quickutils.views.overscroll.footer.ClassicsFooter;
import com.faucet.quickutils.views.overscroll.header.ClassicsHeader;

import java.lang.reflect.Constructor;

/**
 * Created by sunsh on 2017/11/16.
 */
public class OverScrollUtils {

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public static boolean canChildScrollUp(View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) targetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(targetView, -1) || targetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(targetView, -1);
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll down. Override this if the child view is a custom view.
     */
    public static boolean canChildScrollDown(View targetView) {
        if (Build.VERSION.SDK_INT < 14) {
            if (targetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) targetView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(targetView, 1) || targetView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(targetView, 1);
        }
    }

    /**
     * common utils
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int dipToPx(Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    /**
     * parseClassName
     *
     * @param context   context
     * @param className className
     * @return
     */
    static View parseClassName(Context context, String className) {
        if (!TextUtils.isEmpty(className)) {
            try {
                final Class<?>[] CONSTRUCTOR_PARAMS = new Class<?>[]{Context.class};
                final Class<View> clazz = (Class<View>) Class.forName(className, true, context.getClassLoader());
                Constructor<View> constructor = clazz.getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                return constructor.newInstance(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void defaultConfig(OverScrollLayout overScrollLayout){
        overScrollLayout.setHeaderView(new ClassicsHeader(overScrollLayout));
        overScrollLayout.setFooterView(new ClassicsFooter(overScrollLayout.getContext(),overScrollLayout));
        overScrollLayout.setLoadMoreEnable(true);
        overScrollLayout.setLoadTriggerDistance((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, overScrollLayout.getContext().getResources().getDisplayMetrics()));
    }

    public static void defaultConfig2(OverScrollLayout overScrollLayout){
        overScrollLayout.setHeaderView(new ClassicsHeader(overScrollLayout));
        overScrollLayout.setFooterView(new ClassicHoldLoadView(overScrollLayout.getContext(),overScrollLayout));
        overScrollLayout.setAutoLoadingEnable(true);
        overScrollLayout.setLoadMoreEnable(true);
        overScrollLayout.setLoadTriggerDistance((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, overScrollLayout.getContext().getResources().getDisplayMetrics()));
    }

    public static void defaultMoreConfig(OverScrollLayout overScrollLayout){
        overScrollLayout.setFooterView(new ClassicHoldLoadView(overScrollLayout.getContext(),overScrollLayout));
        overScrollLayout.setLoadMoreEnable(true);
        overScrollLayout.setLoadTriggerDistance((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, overScrollLayout.getContext().getResources().getDisplayMetrics()));
    }
}