package com.faucet.quickutils.views.overscroll.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faucet.quickutils.R;
import com.faucet.quickutils.utils.SizeUtils;
import com.faucet.quickutils.views.overscroll.NestedRelativeLayout;
import com.faucet.quickutils.views.overscroll.OverScrollLayout;
import com.faucet.quickutils.views.overscroll.pathview.ProgressDrawable;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 经典下拉头部
 * Created by sunsh on 2017/11/19.
 */
public class ChatMessagePullHeader extends NestedRelativeLayout implements OverScrollLayout.OnPullListener {

    public static String REFRESH_HEADER_PULLDOWN = "下拉加载历史聊天记录";
    public static String REFRESH_HEADER_REFRESHING = "正在加载...";
    public static String REFRESH_HEADER_RELEASE = "释放立即加载";
    public static String REFRESH_HEADER_FINISH = "加载完成";
    public static String REFRESH_HEADER_FAILED = "加载失败";

    protected TextView mHeaderText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected ProgressDrawable mProgressDrawable;

    public ChatMessagePullHeader(Context context) {
        super(context);
        this.initView(context, null);
    }

    public ChatMessagePullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        setMinimumHeight(SizeUtils.dp2px(getContext(), 80));
        LinearLayout layout = new LinearLayout(context);
        layout.setId(android.R.id.widget_frame);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        mHeaderText = new TextView(context);
        mHeaderText.setText(REFRESH_HEADER_PULLDOWN);
        mHeaderText.setTextColor(Color.parseColor("#666666"));
        mHeaderText.setTextSize(16);

        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mHeaderText, lpHeaderText);
        RelativeLayout.LayoutParams lpHeaderLayout = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_IN_PARENT);
        addView(layout, lpHeaderLayout);

        mProgressView = new ImageView(context);
        mProgressView.animate().setInterpolator(new LinearInterpolator());
        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams(SizeUtils.dp2px(getContext(), 20), SizeUtils.dp2px(getContext(), 20));
        lpProgress.rightMargin = SizeUtils.dp2px(getContext(), 20);
        lpProgress.addRule(CENTER_VERTICAL);
        lpProgress.addRule(LEFT_OF, android.R.id.widget_frame);
        addView(mProgressView, lpProgress);

        mArrowView = new ImageView(context);
        addView(mArrowView, lpProgress);

        if (isInEditMode()) {
            mArrowView.setVisibility(GONE);
            mHeaderText.setText(REFRESH_HEADER_REFRESHING);
        } else {
            mProgressView.setVisibility(GONE);
        }


        mArrowView.setImageResource(R.mipmap.rl_arrow_down);


        mProgressDrawable = new ProgressDrawable();
        mProgressDrawable.setColor(Color.parseColor("#666666"));
        mProgressView.setImageDrawable(mProgressDrawable);


        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi") List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPullChange(float percent) {

    }

    @Override
    public void onPullHoldTrigger() {
        mHeaderText.setText(REFRESH_HEADER_RELEASE);
        mArrowView.animate().rotation(180);
    }

    @Override
    public void onPullHoldUnTrigger() {
        mHeaderText.setText(REFRESH_HEADER_PULLDOWN);
        mArrowView.setVisibility(VISIBLE);
        mProgressView.setVisibility(GONE);
        mArrowView.animate().rotation(0);
    }

    @Override
    public void onPullHolding() {
        Log.e("onPullHolding", "onPullHolding: ");
        mHeaderText.setText(REFRESH_HEADER_REFRESHING);
        mProgressView.setVisibility(VISIBLE);
        mArrowView.setVisibility(GONE);

        if (mProgressDrawable != null) {
            mProgressDrawable.start();
        } else {
            mProgressView.animate().rotation(36000).setDuration(100000);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressDrawable != null) {
            mProgressDrawable.stop();
        }
    }

    public void setRefreshError() {
        mHeaderText.setText(REFRESH_HEADER_FAILED);
    }

    @Override
    public void onPullFinish() {
        Log.e("onPullFinish", "onPullFinish: ");
        if (mProgressDrawable != null) {
            mProgressDrawable.stop();
        } else {
            mProgressView.animate().rotation(0).setDuration(300);
        }
        if (!mHeaderText.getText().toString().equals(REFRESH_HEADER_FAILED)) {
            mHeaderText.setText(REFRESH_HEADER_FINISH);
        }
        mProgressView.setVisibility(GONE);
    }

    @Override
    public void onPullReset() {
        Log.e("onPullReset", "onPullReset: ");
        onPullHoldUnTrigger();
    }

    @Override
    public void setNoMore(boolean b) {

    }

    @Override
    public void loadMoreComplete(boolean b) {

    }
}
