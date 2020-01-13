package com.faucet.quickutils.views;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.faucet.quickutils.R;

public class BottomDialog extends Dialog {

    private Window window;

    public BottomDialog(@NonNull Context context) {
        super(context, R.style.dialog_bottom_full);
        init();
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init () {
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        window = getWindow();
    }

    public void setView (View view) {
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }
}
