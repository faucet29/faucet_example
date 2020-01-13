package com.faucet.quickutils.utils;

import android.view.View;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * Created by faucet on 2017/8/23.
 */

public class BaseItemViewDelegate {

    protected MultiItemTypeAdapter mMultiItemTypeAdapter;
    protected OnItemViewClickListener onItemClickListener;
    protected OnItemViewLongClickListener onItemLongClickListener;
    protected OnItemChildViewClickListener onItemChildViewClickListener;

    public void setMultiItemTypeAdapter(MultiItemTypeAdapter mMultiItemTypeAdapter) {
        this.mMultiItemTypeAdapter = mMultiItemTypeAdapter;
    }

    /**
     * item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemViewClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item长按事件
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemViewLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * item中子view的点击事件
     * @param onItemChildViewClickListener
     */
    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener){
        this.onItemChildViewClickListener = onItemChildViewClickListener;
    }

    public interface OnItemViewClickListener<T> {
        void OnItemViewClick(T t, int position);
    }

    public interface OnItemViewLongClickListener<T> {
        void OnItemViewLongClick(T t, int position);
    }

    public interface OnItemChildViewClickListener<T> {
        void OnItemChildViewClick(View view, T t, int position);
    }
}
