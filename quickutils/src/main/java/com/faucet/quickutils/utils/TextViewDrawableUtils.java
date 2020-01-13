package com.faucet.quickutils.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * 作者：shiliang on 2016/12/12 17:30
 * 邮箱：243046923@qq.com
 */
public class TextViewDrawableUtils {

    /**
     * 设置提醒空图
     * @param targetView
     * @param resId
     */
    public static void setPromptTextDrawable(Context context, TextView targetView, int resId){
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        targetView.setCompoundDrawables(null, drawable, null, null);//画在右边
    }
}
