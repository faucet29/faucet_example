package com.faucet.faucetexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rv_chat = findViewById(R.id.rv_chat);
        EditText et_chat = findViewById(R.id.et_chat);
        RelativeLayout root_layout = findViewById(R.id.root_layout);
        List<String> list = new ArrayList<>();
        for (int i=0;i<100;i++) {
            list.add("测试数据" + i);
        }
        final ChatAdapter chatAdapter = new ChatAdapter(list);
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        rv_chat.setAdapter(chatAdapter);
        root_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != -1 && oldBottom > bottom) {
                    rv_chat.requestLayout();
                    rv_chat.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_chat.scrollToPosition(chatAdapter.getData().size() -1);
                        }
                    });
                }
            }
        });
    }
}
