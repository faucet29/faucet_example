package com.faucet.faucetexample.drag;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.faucet.faucetexample.R;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class DragAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements ItemTouchHelperAdapter{

    public DragAdapter(List<String> data) {
        super(R.layout.item_drag, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_chat, s);
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source,
                           RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < getData().size() && toPosition < getData().size()) {
            //交换数据位置
            Collections.swap(getData(), fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {

        int position = source.getAdapterPosition();
        getData().remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        //当拖拽选中时放大选中的view
        viewHolder.itemView.setScaleX(1.2f);
        viewHolder.itemView.setScaleY(1.2f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {

        //拖拽结束后恢复view的状态
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }
}
