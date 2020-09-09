package com.faucet.faucetexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rv_chat = findViewById(R.id.rv_chat);
        List<String> list = new ArrayList<>();
        list.add("仿微信、钉钉群组头像");
        list.add("popwindow测试");
        final ChatAdapter chatAdapter = new ChatAdapter(list);
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        rv_chat.setAdapter(chatAdapter);
        chatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, GroupHeadViewActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, PopWindowActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
