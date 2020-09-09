package com.faucet.faucetexample.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.faucet.faucetexample.R;
import com.faucet.quickutils.utils.ScreenUtils;
import com.faucet.quickutils.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopupWindowsNew {

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;

    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private RecyclerView mScroller;

    protected Context mContext;
    protected PopupWindow mWindow;
    protected View mRootView;
    protected Drawable mBackground = null;

    private int rootWidth = 0;
    private int rootHeight = 0;
    private int mAnimStyle = ANIM_AUTO;
    private int horizontalPadding = 15;

    public PopupWindowsNew(Context context, int rootWidth, int rootHeight) {
        mContext = context;
        this.rootWidth = rootWidth;
        this.rootHeight = rootHeight;
        mWindow = new PopupWindow(context);

        mWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mWindow.dismiss();

                    return true;
                }

                return false;
            }
        });
        horizontalPadding = SizeUtils.dp2px(context, 15);
        if (rootWidth > 0) {
            mWindow.setWidth(rootWidth);
        }
        if (rootHeight > 0) {
            mWindow.setHeight(rootHeight);
        }
        setRootView();
    }

    private void setRootView() {
        mRootView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.popup_vertical, null);
        mScroller = (RecyclerView) mRootView.findViewById(R.id.scroller);
        mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
        mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        PopItemAdapter popItemAdapter = new PopItemAdapter(list);
        mScroller.setLayoutManager(new GridLayoutManager(mContext, 2));
        mScroller.setAdapter(popItemAdapter);
    }

    private void preShow() {
        if (mRootView == null)
            throw new IllegalStateException(
                    "setContentView was not called with a view to display.");

        if (mBackground == null)
            mWindow.setBackgroundDrawable(new BitmapDrawable());
        else
            mWindow.setBackgroundDrawable(mBackground);

        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);

        mWindow.setContentView(mRootView);
    }

    public void show(View anchor) {
        preShow();

        int xPos, yPos, arrowPos;

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1],
                location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

        // mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
        // LayoutParams.WRAP_CONTENT));

        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (rootHeight == 0) {
            rootHeight = mRootView.getMeasuredHeight();
        }

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        int screenHeight = ScreenUtils.getScreenHeight(mContext);

        if (anchorRect.centerX() <= screenWidth / 2) {
            //在左边
            if (anchorRect.centerX() >  (rootWidth / 2)) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
                xPos = (xPos < horizontalPadding) ? horizontalPadding : xPos;
            } else {
                xPos = horizontalPadding;
            }
        } else {
            if (screenWidth - anchorRect.centerX() >= (rootWidth / 2) + horizontalPadding) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
            } else {
                xPos = screenWidth - rootWidth - horizontalPadding;
            }
        }
        arrowPos = anchorRect.centerX() - xPos;

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom) ? true : false;

        if (onTop) {
            if (rootHeight > dyTop) {
                yPos = horizontalPadding;
            } else {
                yPos = anchorRect.top - rootHeight;
            }
        } else {
            if (rootHeight > dyBottom) {
                yPos = screenHeight - rootHeight;
            } else {
                yPos = anchorRect.bottom;
            }
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
                : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
                : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
                .getLayoutParams();

        param.leftMargin = requestedX - arrowWidth / 2;

        hideArrow.setVisibility(View.INVISIBLE);
    }

    private void setAnimationStyle(int screenWidth, int requestedX,
                                   boolean onTop) {
        int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                        : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
                        : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                        : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_REFLECT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect
                        : R.style.Animations_PopDownMenu_Reflect);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                            : R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4
                        && arrowPos < 3 * (screenWidth / 4)) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                            : R.style.Animations_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
                            : R.style.Animations_PopDownMenu_Right);
                }

                break;
        }
    }
}
