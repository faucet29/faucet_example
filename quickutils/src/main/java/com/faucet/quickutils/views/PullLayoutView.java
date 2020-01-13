package com.faucet.quickutils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.faucet.quickutils.utils.SizeUtils;
import com.faucet.quickutils.views.overscroll.OverScrollLayout;
import com.faucet.quickutils.views.overscroll.footer.ClassicHoldLoadView;
import com.faucet.quickutils.views.overscroll.footer.ClassicsFooter;
import com.faucet.quickutils.views.overscroll.header.ClassicsHeader;


/**
 * Created by faucet on 2017/8/2.
 * 一定要记得把adapter转为RecyclerAdapterWithHF类型
 */

public class PullLayoutView extends OverScrollLayout {

    private PullListener pullListener;
    public View headView;
    public View footView;

    public PullLayoutView(Context context) {
        super(context);
        init();
    }

    public PullLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化(adapter之前)
     */
    public void initPullLayout(PullListener pullListener){
        this.pullListener = pullListener;
        headView = new ClassicsHeader(this);
        footView = new ClassicsFooter(this.getContext(),this);
        setRefreshView(headView);
        setLoadMoreView(footView);
        init();
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (PullLayoutView.this.pullListener != null) {
                    PullLayoutView.this.pullListener.onRefresh(PullLayoutView.this);
                }
            }

            @Override
            public void onLoading() {
                if (PullLayoutView.this.pullListener != null) {
                    PullLayoutView.this.pullListener.onLoadMore(PullLayoutView.this);
                }
            }
        });
    }

    private void init () {
        setPullDownMaxDistance(SizeUtils.dp2px(this.getContext(), 100));
        setPullUpMaxDistance(SizeUtils.dp2px(this.getContext(), 100));
    }

    public void setRefreshView(View view){
        headView = view;
        setHeaderView(view);
    }

    public void setLoadMoreView(View view){
        footView = view;
        setFooterView(view);
    }

    /**
     * 设置支持加载更多 （adapter之后）
     * @param canLoadMore
     */
    public void setPullLayoutLoadMoreEnable(boolean canLoadMore){
        setLoadMoreEnable(canLoadMore);
        setLoadMoreView(canLoadMore ? footView : null);
    }

    /**
     * 设置支持下拉刷新（adapter之后）
     * @param canRefresh
     */
    public void setPullLayoutRefreshEnable(boolean canRefresh){
        setRefreshEnable(canRefresh);
        setRefreshView(canRefresh ? headView : null);
    }

    /**
     * 是否进入页面自动刷新
     * @param autoRefresh
     */
    public void setAutoRefresh(boolean autoRefresh){
        if(autoRefresh){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoRefresh();
                }
            }, 150);
        }
    }

    /**
     * 是否到底自动加载
     * @param autoLoadMore
     */
    public void setAutoLoadMore(boolean autoLoadMore){
        setAutoLoadingEnable(autoLoadMore);
        if (autoLoadMore) {
            setLoadMoreView(new ClassicHoldLoadView(this.getContext(),this));
        } else {
            setLoadMoreView(new ClassicsFooter(this.getContext(),this));
        }
    }

    /**
     * 加载更多结束
     */
    public void setLoadMoreComplete(){
        loadMoreComplete();
    }

    /**
     * 下拉刷新结束
     */
    public void setRefreshComplete(){
        refreshComplete();
    }

    /**
     * 加载到最后一页
     */
    public void setLoadMoreEnd(){
        loadMoreComplete(true);
    }

    /**
     * 下拉最大距离
     * @param pullDownMaxDistance
     */
    public void setPullDownMaxDistance(int pullDownMaxDistance) {
        super.setPullDownMaxDistance(pullDownMaxDistance);
    }

    /**
     * 上拉最大距离
     * @param pullUpMaxDistance
     */
    public void setPullUpMaxDistance(int pullUpMaxDistance) {
        super.setPullUpMaxDistance(pullUpMaxDistance);
    }

    public interface PullListener{
        void onRefresh(PullLayoutView pullLayoutView);
        void onLoadMore(PullLayoutView pullLayoutView);
    }

}
