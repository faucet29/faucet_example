package com.faucet.quickutils.core.controler;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faucet.quickutils.R;
import com.faucet.quickutils.utils.SizeUtils;
import com.faucet.quickutils.utils.StringUtils;

public class BaseBarActivity extends BaseActivity {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(getActivityContentViewId());
        setAutoAdjustTopInsideToVisible(true);
    }

    protected int getActivityContentViewId() {
        return R.layout.activity_base_bar;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, (FrameLayout) findViewById(R.id.content_view), true);

        if(mImmersionBar != null) {
            mImmersionBar.keyboardEnable(true).init();  //解决软键盘与底部输入框冲突问题
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View view = findViewById(R.id.view_bar);
        if (view != null) {
            if(mImmersionBar != null) {
                mImmersionBar.statusBarView(view).init();
            }
        }
    }

    public void setAutoAdjustTopInsideToVisible(boolean adjust) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        if (adjust) {
            params.addRule(RelativeLayout.BELOW, R.id.widget_toolbar);
        } else {
            params.addRule(RelativeLayout.BELOW, RelativeLayout.TRUE);
        }
        frameLayout.setLayoutParams(params);
    }

    public View toolBarBackgroundView() {
        return findViewById(R.id.widget_toolbar);
    }

    private View getDefaultToolbar () {
        if(view == null) {
            view = getLayoutInflater().inflate(R.layout.view_default_toolbar, null);
        }
        return view;
    }

    public void setDefaultToolbar(String title, boolean addBack){
        setCustomToolbar(null);
        TextView toolbarTitle = getDefaultToolbar().findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
        if(addBack) {
            getDefaultToolbar().findViewById(R.id.ib_bar_back).setVisibility(View.VISIBLE);
            getDefaultToolbar().findViewById(R.id.ib_bar_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            getDefaultToolbar().findViewById(R.id.ib_bar_back).setVisibility(View.GONE);
        }
    }

    /**
     * view为null,则使用默认
     * @param view
     */
    public void setCustomToolbar(View view) {
        RelativeLayout rootView = getView(R.id.toolbar);
        rootView.removeAllViews();
        if(view == null) {
            view = getDefaultToolbar();
        }
        rootView.addView(view);
    }

    public void setBarShadowVisible(boolean isVisible) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isVisible) {
                toolBarBackgroundView().setElevation(3);
            } else {
                toolBarBackgroundView().setElevation(0);
            }
        }
    }

    public void setToolBarAlpha(int value) {
        toolBarBackgroundView().getBackground().mutate().setAlpha(value);
    }

    public void setToolbarColor(int raw){
        RelativeLayout widget_toolbar = getView(R.id.widget_toolbar);
        widget_toolbar.setBackgroundColor(raw);
    }

    public void setToolbarBackgroundRaw(int raw){
        RelativeLayout widget_toolbar = getView(R.id.widget_toolbar);
        widget_toolbar.setBackgroundResource(raw);
    }

    public void setBottomLineVisible(boolean isVisible){
        View view_line = getView(R.id.view_line);
        if(isVisible){
            view_line.setVisibility(View.VISIBLE);
        }else{
            view_line.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title){
        setDefaultToolbar(title, false);
    }

    public TextView getTitleView () {
        TextView toolbarTitle = getDefaultToolbar().findViewById(R.id.toolbar_title);
        return toolbarTitle;
    }

    public TextView getBackView () {
        TextView backView = getDefaultToolbar().findViewById(R.id.ib_bar_back);
        return backView;
    }

    public void setCustomBack (int draw, String content) {
        Drawable drawable= getResources().getDrawable(draw);
        if(drawable!=null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView)getDefaultToolbar().findViewById(R.id.ib_bar_back)).setCompoundDrawables(drawable,null,null,null);
            ((TextView)getDefaultToolbar().findViewById(R.id.ib_bar_back)).setCompoundDrawablePadding(SizeUtils.px2dp(this, getResources().getDimensionPixelSize(R.dimen.dp_5)));
        }
        if(!StringUtils.isEmpty(content)) {
            ((TextView)getDefaultToolbar().findViewById(R.id.ib_bar_back)).setText(content);
        } else {
            ((TextView)getDefaultToolbar().findViewById(R.id.ib_bar_back)).setText("");
        }
    }

    public void addTextRightButton (String content, View.OnClickListener listener) {
        addTextRightButton(content, null, listener);
    }

    public void addTextRightButton (String content, Integer color, View.OnClickListener listener) {
        LinearLayout rootView = getDefaultToolbar().findViewById(R.id.ll_right);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(this);
        textView.setLayoutParams(lp);
        textView.setOnClickListener(listener);
        textView.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_5), 0, getResources().getDimensionPixelSize(R.dimen.dp_5), 0);
        textView.setMaxLines(1);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(content);
        if (color == null) {
            textView.setTextColor(Color.parseColor("#333333"));
        } else {
            textView.setTextColor(color);
        }
        textView.setTextSize(SizeUtils.px2sp(this, getResources().getDimensionPixelSize(R.dimen.sp_16)));
        rootView.addView(textView, 0);
    }

    public void addImageRightButton (int draw, View.OnClickListener listener) {
        LinearLayout rootView = getDefaultToolbar().findViewById(R.id.ll_right);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(listener);
        imageView.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_5), 0, getResources().getDimensionPixelSize(R.dimen.dp_5), 0);
        imageView.setImageResource(draw);
        rootView.addView(imageView, 0);
    }

    public void clearRightButton () {
        LinearLayout rootView = getDefaultToolbar().findViewById(R.id.ll_right);
        rootView.removeAllViews();
    }

    public void setToolbarVisible(boolean isVisible){
        RelativeLayout widget_toolbar = getView(R.id.widget_toolbar);
        if(isVisible){
            widget_toolbar.setVisibility(View.VISIBLE);
        }else{
            widget_toolbar.setVisibility(View.GONE);
        }
    }
}
