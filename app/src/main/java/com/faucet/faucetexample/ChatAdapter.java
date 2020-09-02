package com.faucet.faucetexample;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class ChatAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ChatAdapter(List<String> data) {
        super(R.layout.item_chat, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_chat, s);
    }
}
