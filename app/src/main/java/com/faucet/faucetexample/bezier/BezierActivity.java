package com.faucet.faucetexample.bezier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.faucet.faucetexample.R;
import com.faucet.quickutils.utils.LogUtil;

public class BezierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        SeekBar seekBar2 = findViewById(R.id.seek_bar2);
        CheckBox checkbox = findViewById(R.id.checkbox);
        MaskView maskView = findViewById(R.id.mask);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maskView.setRadio(progress);
                LogUtil.d(progress+"====+++");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maskView.setRotate((int) (360 * ((float)progress / (float) 100)));
                LogUtil.d(progress+"====+++");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                maskView.reversalCutOut(isChecked);
            }
        });
    }
}