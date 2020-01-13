package com.faucet.quickutils.views.overscroll.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
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
import com.faucet.quickutils.views.overscroll.NestedRelativeLayout;
import com.faucet.quickutils.views.overscroll.OverScrollLayout;
import com.faucet.quickutils.views.overscroll.OverScrollUtils;
import com.faucet.quickutils.views.overscroll.pathview.ProgressDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 经典下拉头部
 * Created by sunsh on 2017/5/28.
 */
public class ClassicsHeader extends NestedRelativeLayout implements OverScrollLayout.OnPullListener {

    public static String REFRESH_HEADER_PULLDOWN = "下拉可以刷新";
    public static String REFRESH_HEADER_REFRESHING = "正在刷新...";
    public static String REFRESH_HEADER_RELEASE = "释放立即刷新";
    public static String REFRESH_HEADER_FINISH = "刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";


    private Date mLastTime;
    protected TextView mHeaderText;
    protected TextView mLastUpdateText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected ProgressDrawable mProgressDrawable;
    private DateFormat mFormat = new SimpleDateFormat("上次更新 M-d HH:mm", Locale.CHINA);
    private OverScrollLayout overScrollLayout;

    public ClassicsHeader(OverScrollLayout overScrollLayout) {
        super(overScrollLayout.getContext());
        this.overScrollLayout = overScrollLayout;
        this.initView(overScrollLayout.getContext(),null);
    }

    public ClassicsHeader(OverScrollLayout overScrollLayout, String simpleNmae, AttributeSet attrs) {
        super(overScrollLayout.getContext(), attrs);
        this.overScrollLayout = overScrollLayout;
        this.initView(overScrollLayout.getContext(),attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        setMinimumHeight(OverScrollUtils.dipToPx(getContext(), 80));

        LinearLayout layout = new LinearLayout(context);
        layout.setId(android.R.id.widget_frame);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        mHeaderText = new TextView(context);
        mHeaderText.setText(REFRESH_HEADER_PULLDOWN);
        mHeaderText.setTextColor(Color.parseColor("#666666"));
        mHeaderText.setTextSize(16);

        mLastUpdateText = new TextView(context);
        mLastUpdateText.setTextColor(Color.parseColor("#666666"));
        mLastUpdateText.setTextSize(12);
        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mHeaderText, lpHeaderText);
        LinearLayout.LayoutParams lpUpdateText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mLastUpdateText, lpUpdateText);

        RelativeLayout.LayoutParams lpHeaderLayout = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_IN_PARENT);
        addView(layout, lpHeaderLayout);

        mProgressView = new ImageView(context);
        mProgressView.animate().setInterpolator(new LinearInterpolator());
        RelativeLayout.LayoutParams lpProgress = new RelativeLayout.LayoutParams(OverScrollUtils.dipToPx(getContext(), 20), OverScrollUtils.dipToPx(getContext(), 20));
        lpProgress.rightMargin = OverScrollUtils.dipToPx(getContext(), 20);
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
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        setLastUpdateTime(new Date(System.currentTimeMillis()));
    }

    //<editor-fold desc="API">
    public ClassicsHeader setProgressBitmap(Bitmap bitmap) {
        mProgressDrawable = null;
        mProgressView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsHeader setProgressDrawable(Drawable drawable) {
        mProgressDrawable = null;
        mProgressView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsHeader setProgressResource(@DrawableRes int resId) {
        mProgressDrawable = null;
        mProgressView.setImageResource(resId);
        return this;
    }

    public ClassicsHeader setArrowBitmap(Bitmap bitmap) {
        mArrowView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsHeader setArrowDrawable(Drawable drawable) {
        mArrowView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsHeader setArrowResource(@DrawableRes int resId) {
        mArrowView.setImageResource(resId);
        return this;
    }

    public ClassicsHeader setLastUpdateTime(Date time) {
        mLastTime = time;
        mLastUpdateText.setText(mFormat.format(time));
        return this;
    }

    public ClassicsHeader setTimeFormat(DateFormat format) {
        mFormat = format;
        mLastUpdateText.setText(mFormat.format(mLastTime));
        return this;
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

    public void setTextAndIconColor (int color) {
        mHeaderText.setTextColor(color);
        mLastUpdateText.setTextColor(color);
        mArrowView.setColorFilter(color);
        mProgressView.setColorFilter(color);
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
        setLastUpdateTime(new Date());
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
