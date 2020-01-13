package com.faucet.quickutils.views;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.faucet.quickutils.R;

public abstract class BaseFullScreenDialog extends Dialog {

    public BaseFullScreenDialog(@NonNull Context context) {
        super(context);
        setContentView(getLayoutId());
        setCanceledOnTouchOutside(false);
    }

    public BaseFullScreenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(getLayoutId());
        setCanceledOnTouchOutside(false);
    }

    protected BaseFullScreenDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(getLayoutId());
        setCanceledOnTouchOutside(false);
    }

    public void showDialog() {
        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置弹窗位置
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        show();
        initView();
    }

    protected abstract int getLayoutId ();

    protected abstract void initView ();
}
