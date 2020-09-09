package com.faucet.faucetexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.faucet.faucetexample.popwindow.PopupWindowsNew;
import com.faucet.quickutils.utils.ScreenUtils;

public class PopWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window);
        Button button = findViewById(R.id.bt_test);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupWindowsNew popupWindowsNew = new PopupWindowsNew(PopWindowActivity.this, ScreenUtils.getScreenWidth(PopWindowActivity.this) * 2/3, 0);
                popupWindowsNew.show(view);
                return false;
            }
        });
    }

}