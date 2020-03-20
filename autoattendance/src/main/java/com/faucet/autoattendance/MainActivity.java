package com.faucet.autoattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.faucet.quickutils.core.controler.BaseActivity;
import com.faucet.quickutils.core.controler.BaseBarActivity;
import com.faucet.quickutils.utils.PermissionEnum;
import com.faucet.quickutils.utils.SPUtils;
import com.faucet.quickutils.utils.StringUtils;
import com.faucet.quickutils.utils.ToastUtil;

public class MainActivity extends BaseBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkHasSelfPermissions(new OnPermissionResult() {
            @Override
            public void permissionAllow() {

            }

            @Override
            public void permissionForbid() {

            }
        }, PermissionEnum.LOCATION.permission());
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        final EditText et_token = (EditText) findViewById(R.id.et_token);
        final EditText et_id = (EditText) findViewById(R.id.et_id);
        et_token.setText(MyApplication.getInstance().spUtils.getString("token", ""));
        et_id.setText(MyApplication.getInstance().spUtils.getLong("id", 0)+"");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(et_token.getText().toString())) {
                    ToastUtil.showShort(MainActivity.this, "token 不能为空");
                    return;
                }
                MyApplication.getInstance().spUtils.putString("token", et_token.getText().toString());
                if (!StringUtils.isEmpty(et_id.getText().toString())) {
                    MyApplication.getInstance().spUtils.putLong("id", Long.parseLong(et_id.getText().toString()));
                } else {
                    MyApplication.getInstance().spUtils.remove("id");
                }
                ToastUtil.showShort(MainActivity.this, "保存成功");
            }
        });
    }
}
