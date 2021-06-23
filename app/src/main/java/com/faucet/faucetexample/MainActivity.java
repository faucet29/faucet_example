package com.faucet.faucetexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.faucet.faucetexample.bezier.BezierActivity;
import com.faucet.faucetexample.drag.RecycleViewDragActivity;

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
        list.add("recycleView拖拽");
        list.add("贝塞尔曲线");
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
                    case 2:
                        intent = new Intent(MainActivity.this, RecycleViewDragActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, BezierActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
