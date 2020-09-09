package com.faucet.faucetexample.popwindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.faucet.faucetexample.R;

import java.util.List;

public class PopItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public PopItemAdapter(List<String> data) {
        super(R.layout.item_pop, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_pop, s);
    }
}
