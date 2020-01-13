package com.faucet.quickutils.views.overscroll.footer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faucet.quickutils.R;
import com.faucet.quickutils.views.overscroll.NestedFrameLayout;
import com.faucet.quickutils.views.overscroll.OverScrollLayout;
import com.faucet.quickutils.views.overscroll.OverScrollUtils;
import com.faucet.quickutils.views.overscroll.ShowGravity;
import com.faucet.quickutils.views.overscroll.ViscousInterpolator;
import com.faucet.quickutils.views.overscroll.pathview.ProgressDrawable;

/**
 * Created by sunsh on 2017/11/22.
 * 自动加载（加载完成footer不隐藏，可跟随滑动）
 */

public class ClassicHoldLoadView extends NestedFrameLayout implements OverScrollLayout.OnPullListener {


    public static String LOADING = "加载中...";
    public static String LOADING_COMPLETE = "加载完成";

    private OverScrollLayout refreshLayout;
    private ObjectAnimator objectAnimator;
    private ProgressDrawable progressDrawable;
    private ImageView progressView;
    private LinearLayout nomoreView;
    private LinearLayout loadView;

    public ClassicHoldLoadView(@NonNull Context context, final OverScrollLayout refreshLayout) {
        super(context);
        this.refreshLayout = refreshLayout;
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.refreshLayout.setFooterFront(true);
        this.refreshLayout.setFooterShowGravity(ShowGravity.PLACEHOLDER);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
        initView();
        scrollInit();
    }

    private void scrollInit() {
        post(new Runnable() {
            @Override
            public void run() {
                final View target = refreshLayout.getTargetView();
                refreshLayout.setLoadTriggerDistance(OverScrollUtils.dipToPx(getContext(), 60));
                target.setOverScrollMode(OVER_SCROLL_NEVER);
                ((ViewGroup) target).setClipToPadding(false);

                if (target instanceof NestedScrollView) {
                    ((NestedScrollView) target).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (refreshLayout.isRefreshing()) {
                                return;
                            }
                            setTargetTranslationY();
                        }
                    });
                } else if (target instanceof RecyclerView) {
                    ((RecyclerView) target).addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            if (refreshLayout.isRefreshing()) {
                                return;
                            }
                            setTargetTranslationY();
                        }
                    });
                }

                setTargetTranslationY();
            }
        });
    }

    private TextView mFooterView;

    private void initView() {

        loadView = new LinearLayout(getContext());
        loadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, OverScrollUtils.dipToPx(getContext(), 60)));
        loadView.setGravity(Gravity.CENTER);
        LinearLayout center = new LinearLayout(getContext());
        center.setGravity(Gravity.CENTER_VERTICAL);
        center.setOrientation(LinearLayout.HORIZONTAL);
        progressView = new ImageView(getContext());
        progressView.setLayoutParams(new LinearLayout.LayoutParams(OverScrollUtils.dipToPx(getContext(), 20), OverScrollUtils.dipToPx(getContext(), 20)));
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) progressView.getLayoutParams();
        layoutParams.rightMargin = OverScrollUtils.dipToPx(getContext(), 20);
        progressView.setLayoutParams(layoutParams);
        progressView.animate().setInterpolator(new LinearInterpolator());
        mFooterView = new TextView(getContext());
        mFooterView.setText(LOADING);
        mFooterView.setTextColor(Color.parseColor("#666666"));
        mFooterView.setTextSize(12);
        progressDrawable = new ProgressDrawable();
        progressDrawable.setColor(Color.parseColor("#666666"));
        progressView.setImageDrawable(progressDrawable);
        center.addView(progressView);
        center.addView(mFooterView);
        progressView.setVisibility(GONE);
        loadView.addView(center);
        refreshLayout.setAnimationMainInterpolator(new ViscousInterpolator());
        refreshLayout.setAnimationOverScrollInterpolator(new LinearInterpolator());

        nomoreView = new LinearLayout(getContext());
        nomoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, OverScrollUtils.dipToPx(getContext(), 60)));
        nomoreView.setGravity(Gravity.CENTER);
        View line1 = new View(getContext());
        line1.setLayoutParams(new ViewGroup.LayoutParams(200, 1));
        line1.setBackgroundColor(Color.parseColor("#cdcdcd"));
        TextView textView = new TextView(getContext());
        textView.setText("我是有底线的");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(12);
        textView.setPadding(30, 0, 30, 0);
        View line2 = new View(getContext());
        line2.setLayoutParams(new ViewGroup.LayoutParams(200, 1));
        line2.setBackgroundColor(Color.parseColor("#cdcdcd"));
        nomoreView.addView(line1);
        nomoreView.addView(textView);
        nomoreView.addView(line2);
        addView(loadView);
        addView(nomoreView);
        nomoreView.setVisibility(GONE);
    }

    // 动画初始化
    private void animationInit() {
        if (objectAnimator != null) return;

        objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, 0);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new ViscousInterpolator(8));

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                refreshLayout.loadMoreComplete();
                refreshLayout.setDispatchTouchAble(true);
                refreshLayout.cancelTouchEvent();
                mFooterView.setText(LOADING_COMPLETE);
                progressView.setVisibility(GONE);
            }
        });
    }

    // 自定义回复动画
    public void startBackAnimation() {
        // 记录refreshLayout移动距离
        final int moveDistance = refreshLayout.getMoveDistance();
        if (moveDistance >= 0) {// moveDistance大于等于0时不主动处理
            refreshLayout.loadMoreComplete();
            refreshLayout.setDispatchTouchAble(true);
            mFooterView.setText(LOADING_COMPLETE);
            progressView.setVisibility(GONE);
            return;
        }

        // 阻止refreshLayout的事件分发
        refreshLayout.setDispatchTouchAble(false);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置事件为ACTION_CANCEL
                refreshLayout.cancelTouchEvent();
                // 再设置内容移动到0的位置
                refreshLayout.moveChildren(0);
                refreshLayout.getTargetView().scrollBy(0, -moveDistance);

                // 调用自定义footer动画
                animationInit();

                objectAnimator.setFloatValues(getHeight() + moveDistance, getHeight());
                objectAnimator.start();
            }
        }, 150);

    }

    public void loadFinish(boolean isHold) {
        if (refreshLayout.isLoadMoreEnable()) {
            refreshLayout.setLoadMoreEnable(false);
            refreshLayout.setAutoLoadingEnable(false);
            progressDrawable.stop();
            mFooterView.setText(LOADING_COMPLETE);
            loadView.setVisibility(GONE);
            nomoreView.setVisibility(VISIBLE);
            if (isHold) {
                ViewGroup target = refreshLayout.getTargetView();
                target.setPadding(0, 0, 0, OverScrollUtils.dipToPx(getContext(), 60));//方便起见，这里就不考虑设置target已设置的情况

                if (refreshLayout.getMoveDistance() < 0) {
                    int offsetY = Math.min(OverScrollUtils.dipToPx(getContext(), 60), -refreshLayout.getMoveDistance());
                    target.scrollBy(0, offsetY);
                    refreshLayout.moveChildren(refreshLayout.getMoveDistance() + offsetY);
                    setTargetTranslationY();
                }
            }
            refreshLayout.loadMoreComplete();
        }
    }

    private void setTargetTranslationY() {
        View target = refreshLayout.getTargetView();
        if (target instanceof NestedScrollView) {
            setTranslationY(Math.max(getHeight(), ((NestedScrollView) target).getChildAt(0).getHeight()) - target.getScrollY() + refreshLayout.getMoveDistance());
        } else if (refreshLayout.getTargetView() instanceof RecyclerView) {
            RecyclerView rv = (RecyclerView) target;
            RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            RecyclerView.ViewHolder viewHolder = rv.findViewHolderForAdapterPosition(layoutManager.getItemCount() - 1);
            float offset = 0;
            if (viewHolder != null) {
                offset = refreshLayout.isTargetAbleScrollDown() || refreshLayout.isTargetAbleScrollUp() ?
                        viewHolder.itemView.getBottom() - refreshLayout.getTargetView().getHeight() : 0;
            }
            setTranslationY(getHeight() + refreshLayout.getMoveDistance() + offset);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
    }

    @Override
    public void onPullChange(float percent) {
        Log.e("onFooterPullChange", "setTargetTranslationY: " + percent);
        onPullHolding();

        setTargetTranslationY();

        // 判断是否处在 拖拽的状态
        if (refreshLayout.isDragDown() || refreshLayout.isDragUp() || !refreshLayout.isLoadMoreEnable()) {
            return;
        }

        if (!refreshLayout.isHoldingTrigger() && (percent < 0)) {
            refreshLayout.autoLoading();
        }
    }

    @Override
    public void onPullHoldTrigger() {
    }

    @Override
    public void onPullHoldUnTrigger() {
    }

    @Override
    public void onPullHolding() {
        if (progressView.getVisibility() != VISIBLE && refreshLayout.isLoadMoreEnable()) {
            progressView.setVisibility(VISIBLE);
            progressDrawable.start();
            mFooterView.setText(LOADING);
        }
    }

    @Override
    public void onPullFinish() {
        if (refreshLayout.isLoadMoreEnable()) {
            mFooterView.setText(LOADING_COMPLETE);
        }
        progressDrawable.stop();
        progressView.setVisibility(GONE);
    }

    @Override
    public void onPullReset() {
        /*
         * 内容没有铺满时继续执行自动加载
         */
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!refreshLayout.isTargetAbleScrollDown() && !refreshLayout.isTargetAbleScrollUp()) {
                    if (!refreshLayout.isRefreshing()) {
                        refreshLayout.autoLoading();
                    }
                }
            }
        }, 250);
    }

    @Override
    public void setNoMore(boolean b) {
        if (b) {
            refreshLayout.setLoadMoreEnable(false);
            refreshLayout.setAutoLoadingEnable(false);
            progressDrawable.stop();
            mFooterView.setText(LOADING_COMPLETE);
            loadView.setVisibility(GONE);
            nomoreView.setVisibility(VISIBLE);
        } else {
            ViewGroup target = refreshLayout.getTargetView();
            target.setPadding(0, 0, 0, 0);
            loadView.setVisibility(VISIBLE);
            nomoreView.setVisibility(GONE);
            refreshLayout.setLoadMoreEnable(true);
            refreshLayout.setAutoLoadingEnable(true);
        }
    }

    @Override
    public void loadMoreComplete(boolean b) {
        setNoMore(b);
        if (b) {
            ViewGroup target = refreshLayout.getTargetView();
            target.setPadding(0, 0, 0, OverScrollUtils.dipToPx(getContext(), 60));//方便起见，这里就不考虑设置target已设置的情况

            if (refreshLayout.getMoveDistance() < 0) {
                int offsetY = Math.min(OverScrollUtils.dipToPx(getContext(), 60), -refreshLayout.getMoveDistance());
                target.scrollBy(0, offsetY);
                refreshLayout.moveChildren(refreshLayout.getMoveDistance() + offsetY);
                setTargetTranslationY();
            }
            refreshLayout.loadMoreComplete();
        } else {
            startBackAnimation();
        }
    }

}
