package com.faucet.quickutils.views.overscroll.footer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 经典下拉尾巴
 * Created by sunsha on 2017/11/21.
 */
public class ClassicsFooter extends NestedRelativeLayout implements OverScrollLayout.OnPullListener {

    public static String REFRESH_HEADER_PULLDOWN = "上拉可以加载更多";
    public static String REFRESH_HEADER_REFRESHING = "正在加载...";
    public static String REFRESH_HEADER_RELEASE = "释放立即加载";
    public static String REFRESH_HEADER_FINISH = "加载完成";
    public static String REFRESH_HEADER_FAILED = "加载失败";


    protected TextView mHeaderText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected ProgressDrawable mProgressDrawable;
    private OverScrollLayout overScrollLayout;
    private LinearLayout nomoreView;

    public ClassicsFooter(Context context, OverScrollLayout overScrollLayout) {
        super(context);
        this.overScrollLayout = overScrollLayout;
        this.initView(context, null);
    }

    public ClassicsFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
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

        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layout.addView(mHeaderText, lpHeaderText);
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
        mArrowView.setVisibility(GONE);
        mHeaderText.setText(REFRESH_HEADER_REFRESHING);
        mProgressView.setVisibility(GONE);
        mArrowView.setImageResource(R.mipmap.rl_arrow_up);
        mProgressDrawable = new ProgressDrawable();
        mProgressDrawable.setColor(Color.parseColor("#666666"));
        mProgressView.setImageDrawable(mProgressDrawable);

        nomoreView = new LinearLayout(getContext());
//        nomoreView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.background));
        nomoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, OverScrollUtils.dipToPx(getContext(), 80)));
        nomoreView.setGravity(Gravity.CENTER);
        View line1 = new View(getContext());
        line1.setBackground(new ColorDrawable(Color.parseColor("#cdcdcd")));
        line1.setLayoutParams(new ViewGroup.LayoutParams(200, 1));
        TextView textView = new TextView(getContext());
        textView.setText("我是有底线的");
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(16);
        textView.setPadding(10,0,10,0);
        View line2 = new View(getContext());
        line2.setBackground(new ColorDrawable(Color.parseColor("#cdcdcd")));
        line2.setLayoutParams(new ViewGroup.LayoutParams(200, 1));
        nomoreView.addView(line1);
        nomoreView.addView(textView);
        nomoreView.addView(line2);
        addView(nomoreView);
        nomoreView.setVisibility(GONE);

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

    //<editor-fold desc="API">
    public ClassicsFooter setProgressBitmap(Bitmap bitmap) {
        mProgressDrawable = null;
        mProgressView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsFooter setProgressDrawable(Drawable drawable) {
        mProgressDrawable = null;
        mProgressView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsFooter setProgressResource(@DrawableRes int resId) {
        mProgressDrawable = null;
        mProgressView.setImageResource(resId);
        return this;
    }

    public ClassicsFooter setArrowBitmap(Bitmap bitmap) {
        mArrowView.setImageBitmap(bitmap);
        return this;
    }

    public ClassicsFooter setArrowDrawable(Drawable drawable) {
        mArrowView.setImageDrawable(drawable);
        return this;
    }

    public ClassicsFooter setArrowResource(@DrawableRes int resId) {
        mArrowView.setImageResource(resId);
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
        if (nomoreView.getVisibility() == VISIBLE) {
            mArrowView.setVisibility(GONE);
        } else {
            mArrowView.setVisibility(VISIBLE);
        }
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
        if (b) {
            overScrollLayout.setLoadMoreEnable(false);
            mHeaderText.setVisibility(GONE);
            mArrowView.setVisibility(GONE);
            mProgressView.setVisibility(GONE);
            nomoreView.setVisibility(VISIBLE);
        } else {
            mHeaderText.setVisibility(VISIBLE);
            mArrowView.setVisibility(VISIBLE);
            mProgressView.setVisibility(GONE);
            nomoreView.setVisibility(GONE);
            overScrollLayout.setLoadMoreEnable(true);
        }
    }

    @Override
    public void loadMoreComplete(boolean b) {
        setNoMore(b);
        overScrollLayout.loadMoreComplete();
    }
}
