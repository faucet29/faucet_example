package com.faucet.quickutils.views.overscroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.core.widget.ScrollerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.faucet.quickutils.R;


/**
 * Created by sunsh on 2017/11/16.
 */
public class OverScrollLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
    private final NestedScrollingParentHelper parentHelper;
    private final NestedScrollingChildHelper childHelper;
    private final int[] parentScrollConsumed = new int[2];
    final int[] parentOffsetInWindow = new int[2];

    /**
     * view children
     * - use by showGravity to dell onLayout
     */
    View headerView;
    View footerView;

    /**
     * - use by generalHelper to dell cancel event
     */
    View targetView;

    private View pullContentLayout;

    //-------------------------START| values part |START-----------------------------

    /**
     * trigger distance
     * - use by showGravity to control the layout move
     */
    int refreshTriggerDistance = -1;
    int loadTriggerDistance = -1;

    /**
     * max drag distance
     */
    private int pullDownMaxDistance = -1;
    private int pullUpMaxDistance = -1;

    /**
     * refresh Animation total during
     */
    private int refreshAnimationDuring = 180;

    /**
     * reset animation total during
     */
    private int resetAnimationDuring = 400;

    /**
     * over scroll top start offset
     */
    private int topOverScrollMaxTriggerOffset = 60;

    /**
     * over scroll bottom start offset
     */
    private int bottomOverScrollMaxTriggerOffset = 60;

    /**
     * over scroll min during
     */
    private int overScrollMinDuring = 65;

    /**
     * target view id
     */
    private int targetViewId = -1;

    /**
     * the ratio for final distance for drag
     */
    private float dragDampingRatio = 0.6F;

    /**
     * over scroll adjust value
     */
    private float overScrollAdjustValue = 1F;

    /**
     * move distance ratio for over scroll
     */
    private float overScrollDampingRatio = 0.35F;

    /**
     * switch
     */
    private boolean pullRefreshEnable = true;
    private boolean pullTwinkEnable = true;
    private boolean pullLoadMoreEnable = false;
    private boolean autoLoadingEnable = false;

    /**
     * dispatch control
     * dispatchTouchAble:is able prl dispatch touch event
     * dispatchPullTouchAble:is able prl dell pull logic
     * dispatchChildrenEventAble:is the views in prl able to dispatch touch event
     */
    private boolean dispatchTouchAble = true;
    private boolean dispatchPullTouchAble = true;
    private boolean dispatchChildrenEventAble = true;

    /**
     * move with
     * isMoveWithContent:- use by generalHelper dell touch logic
     */
    private boolean isMoveWithFooter = true;
    private boolean isMoveWithHeader = true;
    boolean isMoveWithContent = true;

    /**
     * view front
     */
    private boolean isHeaderFront = false;
    private boolean isFooterFront = false;

    //--------------------START|| values can modify in the lib only ||START------------------

    /**
     * current refreshing state 1:refresh 2:loadMore
     */
    private int refreshState = 0;

    /**
     * last Scroll Y
     */
    private int lastScrollY = 0;

    /**
     * over scroll state
     */
    private int overScrollState = 0;

    /**
     * drag move distance
     * - use by generalHelper dell touch logic
     */
    int moveDistance = 0;

    /**
     * final scroll distance
     */
    private float finalScrollDistance = 0;

    /**
     * make sure header or footer hold trigger one time
     */
    private boolean pullStateControl = true;

    /**
     * refreshing state trigger
     */
    private boolean isHoldingTrigger = false;
    private boolean isHoldingFinishTrigger = false;
    private boolean isResetTrigger = false;
    private boolean isOverScrollTrigger = false;
    private boolean isAutoLoadingTrigger = false;

    /**
     * refresh with action
     */
    private boolean refreshWithAction = true;

    /**
     * is scroll able when view scroll back
     */
    private boolean isScrollAbleViewBackScroll = false;

    /**
     * is is target nested
     */
    private boolean isTargetNested = false;

    private boolean isAttachWindow = false;

    //--------------------END|| values can modify int class only ||END------------------
    //--------------------END| values part |END------------------

    private final ShowGravity showGravity;
    private final GeneralPullHelper generalPullHelper;

    private OnRefreshListener onRefreshListener;
    private OnDragIntercept onDragIntercept;

    private ScrollerCompat scroller;
    private Interpolator scrollInterpolator;

    private ValueAnimator startRefreshAnimator;
    private ValueAnimator resetHeaderAnimator;
    private ValueAnimator startLoadMoreAnimator;
    private ValueAnimator resetFooterAnimator;
    private ValueAnimator overScrollAnimator;

    private Interpolator animationMainInterpolator;
    private Interpolator animationOverScrollInterpolator;

    private Runnable delayHandleActionRunnable;
    private boolean noMore;
    private boolean resetHeaderEnable = true;
    private boolean resetFooterEnable = true;

    public OverScrollLayout(Context context) {
        this(context, null);
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        showGravity = new ShowGravity(this);
        generalPullHelper = new GeneralPullHelper(this, context);

        parentHelper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        loadAttribute(context, attrs);
    }

    private void loadAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OverScrollLayout);
        pullRefreshEnable = ta.getBoolean(R.styleable.OverScrollLayout_prl_refreshEnable, pullRefreshEnable);
        pullLoadMoreEnable = ta.getBoolean(R.styleable.OverScrollLayout_prl_loadMoreEnable, pullLoadMoreEnable);
        pullTwinkEnable = ta.getBoolean(R.styleable.OverScrollLayout_prl_twinkEnable, pullTwinkEnable);
        autoLoadingEnable = ta.getBoolean(R.styleable.OverScrollLayout_prl_autoLoadingEnable, autoLoadingEnable);
        isHeaderFront = ta.getBoolean(R.styleable.OverScrollLayout_prl_headerFront, isHeaderFront);
        isFooterFront = ta.getBoolean(R.styleable.OverScrollLayout_prl_footerFront, isFooterFront);

        refreshTriggerDistance = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_refreshTriggerDistance, refreshTriggerDistance);
        loadTriggerDistance = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_loadTriggerDistance, loadTriggerDistance);

        pullDownMaxDistance = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_pullDownMaxDistance, pullDownMaxDistance);
        pullUpMaxDistance = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_pullUpMaxDistance, pullUpMaxDistance);

        resetAnimationDuring = ta.getInt(R.styleable.OverScrollLayout_prl_resetAnimationDuring, resetAnimationDuring);
        refreshAnimationDuring = ta.getInt(R.styleable.OverScrollLayout_prl_refreshAnimationDuring, refreshAnimationDuring);
        overScrollMinDuring = ta.getInt(R.styleable.OverScrollLayout_prl_overScrollMinDuring, overScrollMinDuring);

        dragDampingRatio = ta.getFloat(R.styleable.OverScrollLayout_prl_dragDampingRatio, dragDampingRatio);

        overScrollAdjustValue = ta.getFloat(R.styleable.OverScrollLayout_prl_overScrollAdjustValue, overScrollAdjustValue);
        overScrollDampingRatio = ta.getFloat(R.styleable.OverScrollLayout_prl_overScrollDampingRatio, overScrollDampingRatio);
        topOverScrollMaxTriggerOffset = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_topOverScrollMaxTriggerOffset, OverScrollUtils.dipToPx(context, topOverScrollMaxTriggerOffset));
        bottomOverScrollMaxTriggerOffset = ta.getDimensionPixelOffset(R.styleable.OverScrollLayout_prl_downOverScrollMaxTriggerOffset, OverScrollUtils.dipToPx(context, bottomOverScrollMaxTriggerOffset));

        showGravity.headerShowGravity = ta.getInteger(R.styleable.OverScrollLayout_prl_headerShowGravity, ShowGravity.FOLLOW);
        showGravity.footerShowGravity = ta.getInteger(R.styleable.OverScrollLayout_prl_footerShowGravity, ShowGravity.FOLLOW);

        targetViewId = ta.getResourceId(R.styleable.OverScrollLayout_prl_targetId, targetViewId);

        headerView = initRefreshView(context, ta.getString(R.styleable.OverScrollLayout_prl_headerClass), ta.getResourceId(R.styleable.OverScrollLayout_prl_headerViewId, -1));
        footerView = initRefreshView(context, ta.getString(R.styleable.OverScrollLayout_prl_footerClass), ta.getResourceId(R.styleable.OverScrollLayout_prl_footerViewId, -1));

        ta.recycle();
    }

    private View initRefreshView(Context context, String className, int viewId) {
        View v = OverScrollUtils.parseClassName(context, className);
        if (v == null) {
            if (viewId != -1) {
                v = LayoutInflater.from(context).inflate(viewId, null, false);
            }
        }
        return v;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initContentView();
        dellNestedScrollCheck(); // make sure that targetView able to scroll after targetView has set
        readyScroller();
    }

    private void initContentView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) != headerView && getChildAt(i) != footerView) {
                pullContentLayout = getChildAt(i);
                break;
            }
        }
        if (pullContentLayout == null) {
            throw new RuntimeException("OverScrollLayout should have a child");
        }

        // ---------| targetView ready |----------
        if (targetViewId != -1) {
            targetView = findViewById(targetViewId);
        }
        if (targetView == null) {
            targetView = pullContentLayout;
        }

        setHeaderView(headerView);
        setFooterView(footerView);
    }

    public boolean dispatchSuperTouchEvent(MotionEvent ev) {
        return dispatchChildrenEventAble && super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {//dell Exception - java.lang.IllegalArgumentException: pointerIndex out of range
            return dispatchTouchAble && ((!dispatchPullTouchAble && super.dispatchTouchEvent(ev)) || generalPullHelper.dispatchTouchEvent(ev));
        } catch (IllegalArgumentException ex) {
            // ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if (headerView != null && refreshTriggerDistance == -1) {
            refreshTriggerDistance = headerView.getMeasuredHeight();
        }
        if (footerView != null && loadTriggerDistance == -1) {
            loadTriggerDistance = footerView.getMeasuredHeight();
        }

        if (pullDownMaxDistance == -1) {
            pullDownMaxDistance = getMeasuredHeight();
        }
        if (pullUpMaxDistance == -1) {
            pullUpMaxDistance = getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        showGravity.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        layoutContentView();
    }

    private void layoutContentView() {
        MarginLayoutParams lp = (MarginLayoutParams) pullContentLayout.getLayoutParams();
        pullContentLayout.layout(getPaddingLeft() + lp.leftMargin
                , getPaddingTop() + lp.topMargin
                , getPaddingLeft() + lp.leftMargin + pullContentLayout.getMeasuredWidth()
                , getPaddingTop() + lp.topMargin + pullContentLayout.getMeasuredHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachWindow = true;
        handleAction();
    }

    @Override
    protected void onDetachedFromWindow() {
        isAttachWindow = false;

        cancelAllAnimation();
        abortScroller();

        startRefreshAnimator = null;
        resetHeaderAnimator = null;
        startLoadMoreAnimator = null;
        resetFooterAnimator = null;
        overScrollAnimator = null;

        delayHandleActionRunnable = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -1);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        if ((android.os.Build.VERSION.SDK_INT >= 21 || !(targetView instanceof AbsListView)) && (targetView == null || ViewCompat.isNestedScrollingEnabled(targetView))) {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    @Override
    public void computeScroll() {
        boolean isFinish = scroller == null || !scroller.computeScrollOffset() || scroller.isFinished();
        if (!isFinish) {
            int currY = scroller.getCurrY();
            int currScrollOffset = currY - lastScrollY;
            lastScrollY = currY;

            if (pullTwinkEnable && ((overScrollFlingState() == 1 && overScrollBackDell(1, currScrollOffset))
                    || (overScrollFlingState() == 2 && overScrollBackDell(2, currScrollOffset)))) {
                return;
            } else if (isScrollAbleViewBackScroll && (pullContentLayout instanceof ListView)) {
                // ListView scroll back scroll to normal
                ListViewCompat.scrollListBy((ListView) pullContentLayout, currScrollOffset);
            }

            if (onScrollerScrollChangeListener != null)
                onScrollerScrollChangeListener.onScrollChange(moveDistance);
            if (!isOverScrollTrigger && !isTargetAbleScrollUp() && currScrollOffset < 0 && moveDistance >= 0) {
                overScrollDell(1, currScrollOffset);
            } else if (!isOverScrollTrigger && !isTargetAbleScrollDown() && currScrollOffset > 0 && moveDistance <= 0) {
                overScrollDell(2, currScrollOffset);
            }

            // invalidate View ,the method invalidate() sometimes not work , so i use ViewCompat.postInvalidateOnAnimation(this) instead of invalidate()
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * overScroll Back Dell
     *
     * @param tempDistance temp move distance
     * @return need continue
     */
    private boolean overScrollBackDell(int type, int tempDistance) {
        if ((type == 1 && finalScrollDistance > moveDistance * 2) || (type == 2 && finalScrollDistance < moveDistance * 2)) {
            cancelAllAnimation();
            if ((type == 1 && moveDistance <= tempDistance) || (type == 2 && moveDistance >= tempDistance)) {
                dellScroll(-moveDistance);
                return kindsOfViewsToNormalDell(type, tempDistance);
            }
            dellScroll(-tempDistance);
            return false;
        } else {
            abortScroller();
            handleAction();
            return true;
        }
    }

    /**
     * kinds of view dell back scroll to normal state
     */
    private boolean kindsOfViewsToNormalDell(int type, int tempDistance) {
        if (!dispatchChildrenEventAble) {
            return false;
        }

        final int sign = type == 1 ? 1 : -1;
        int velocity = (int) (sign * Math.abs(scroller.getCurrVelocity()));

        if (targetView instanceof ScrollView && !isScrollAbleViewBackScroll) {
            ((ScrollView) targetView).fling(velocity);
        } else if (targetView instanceof WebView && !isScrollAbleViewBackScroll) {
            ((WebView) targetView).flingScroll(0, velocity);
        } else if (targetView instanceof RecyclerView && !isTargetNestedScrollingEnabled() && !isScrollAbleViewBackScroll) {
            ((RecyclerView) targetView).fling(0, velocity);
        } else if (targetView instanceof NestedScrollView && !isTargetNestedScrollingEnabled() && !isScrollAbleViewBackScroll) {
            ((NestedScrollView) targetView).fling(velocity);
        } else if (!OverScrollUtils.canChildScrollUp(targetView) && !OverScrollUtils.canChildScrollDown(targetView)
                || (targetView instanceof ListView && !isScrollAbleViewBackScroll)
                || targetView instanceof RecyclerView
                || targetView instanceof NestedScrollView) {
            // this case just dell overScroll normal,without any operation
        } else {
            // the target is able to scrollUp or scrollDown but have not the fling method
            // ,so dell the view just like normal view
            overScrollDell(type, tempDistance);
            return true;
        }
        isScrollAbleViewBackScroll = true;
        return false;
    }

    /**
     * dell over scroll to move children
     */
    private void startOverScrollAnimation(int type, int distanceMove) {
        distanceMove = type == 1 ? Math.max(-topOverScrollMaxTriggerOffset, distanceMove) : Math.min(bottomOverScrollMaxTriggerOffset, distanceMove);
        int finalDistance = scroller.getFinalY() - scroller.getCurrY();
        abortScroller();
        cancelAllAnimation();

        if (overScrollAnimator == null) {
            if (animationOverScrollInterpolator == null) {
                animationOverScrollInterpolator = new LinearInterpolator();
            }
            overScrollAnimator = getAnimator(distanceMove, 0, overScrollAnimatorUpdate, overScrollAnimatorListener, animationOverScrollInterpolator);
        } else {
            overScrollAnimator.setIntValues(distanceMove, 0);
        }
        overScrollAnimator.setDuration(getOverScrollTime(finalDistance));
        overScrollAnimator.start();
    }

    private void onTopOverScroll() {
        overScrollState = 1;
    }

    private void onBottomOverScroll() {
        overScrollState = 2;
        autoLoadingTrigger();
    }

    private void autoLoadingTrigger() {
        if (!noMore && !isAutoLoadingTrigger && autoLoadingEnable && !isHoldingTrigger && onRefreshListener != null) {
            isAutoLoadingTrigger = true;
            onRefreshListener.onLoading();
        }
    }

    private void readyScroller() {
        if (scroller == null && (pullTwinkEnable || autoLoadingEnable)) {
            if (targetView instanceof RecyclerView) {
                scroller = ScrollerCompat.create(getContext(), scrollInterpolator == null
                        ? scrollInterpolator = getRecyclerDefaultInterpolator() : scrollInterpolator);
                return;
            }
            scroller = ScrollerCompat.create(getContext());
        }
    }

    private Interpolator readyMainInterpolator() {
        if (animationMainInterpolator == null) {
            animationMainInterpolator = new ViscousInterpolator();
        }
        return animationMainInterpolator;
    }

    private Interpolator getRecyclerDefaultInterpolator() {
        return new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            }
        };
    }

    /**
     * dell the nestedScroll
     *
     * @param distanceY move distance of Y
     */
    private void dellScroll(float distanceY) {
        if (checkMoving(distanceY) || distanceY == 0) {
            return;
        }
        int tempDistance = (int) (moveDistance + distanceY);
        tempDistance = Math.min(tempDistance, pullDownMaxDistance);
        tempDistance = Math.max(tempDistance, -pullUpMaxDistance);

        if (!pullTwinkEnable && ((isRefreshing() && tempDistance < 0) || (isLoading() && tempDistance > 0))) {
            if (moveDistance == 0) {
                return;
            }
            tempDistance = 0;
        }
        if ((pullLoadMoreEnable && tempDistance <= 0) || (pullRefreshEnable && tempDistance >= 0) || pullTwinkEnable) {
            if (pullTwinkEnable)
                moveChildren(tempDistance);
            else {
                moveDistance = 0;
                return;
            }
        } else {
            moveDistance = 0;
            return;
        }

        if (onScrollerScrollChangeListener != null)
            onScrollerScrollChangeListener.onScrollChange(moveDistance);

        if (moveDistance >= 0 && headerView != null && pullTwinkEnable) {
            onHeaderPullChange();
            if (!isHoldingTrigger && moveDistance >= refreshTriggerDistance) {
                if (pullStateControl) {
                    pullStateControl = false;
                    onHeaderPullHoldTrigger();
                }
                return;
            }
            if (!isHoldingTrigger && !pullStateControl) {
                pullStateControl = true;
                onHeaderPullHoldUnTrigger();
            }
            return;
        }
        if (footerView == null) {
            return;
        }
        onFooterPullChange();
        if (!isHoldingTrigger && moveDistance <= -loadTriggerDistance) {
            if (pullStateControl) {
                pullStateControl = false;
                onFooterPullHoldTrigger();
            }
            return;
        }
        if (!isHoldingTrigger && !pullStateControl) {
            pullStateControl = true;
            onFooterPullHoldUnTrigger();
        }
    }

    /**
     * check before header down and footer up moving
     *
     * @param distanceY just make sure the move direct
     * @return need intercept
     */
    private boolean checkMoving(float distanceY) {
        return (((distanceY > 0 && moveDistance == 0) || moveDistance > 0) && onDragIntercept != null && !onDragIntercept.onHeaderDownIntercept())
                || (((distanceY < 0 && moveDistance == 0) || moveDistance < 0) && onDragIntercept != null && !onDragIntercept.onFooterUpIntercept());
    }

    private void overScrollDell(int type, int offset) {
        if (pullTwinkEnable // if pullTwinkEnable is true , while fling back the target is able to over scroll just intercept that
                && ((!isTargetAbleScrollUp() && isTargetAbleScrollDown()) && moveDistance < 0
                || (isTargetAbleScrollUp() && !isTargetAbleScrollDown()) && moveDistance > 0)) {
            return;
        }

        if (type == 1) {
            onTopOverScroll();
        } else {
            onBottomOverScroll();
        }

        if (!pullTwinkEnable) {
            abortScroller();
            return;
        }

        isOverScrollTrigger = true;
        startOverScrollAnimation(type, offset);
    }

    /**
     * decide on the action refresh or loadMore
     */
    private void handleAction() {
        if (pullRefreshEnable && headerView != null && !isLoading() && !isResetTrigger && moveDistance >= refreshTriggerDistance) {
            startRefresh(moveDistance, true);
        } else if (pullLoadMoreEnable && footerView != null && !isRefreshing() && !isResetTrigger && moveDistance <= -loadTriggerDistance) {
            startLoadMore(moveDistance, true);
        } else if ((!isHoldingTrigger && moveDistance > 0) || (isRefreshing() && (moveDistance < 0 || isResetTrigger))) {
            resetHeaderView(moveDistance);
        } else if ((!isHoldingTrigger && moveDistance < 0) || (isLoading() && moveDistance > 0) || isResetTrigger) {
            resetFootView(moveDistance);
        }
    }

    private void startRefresh(int headerViewHeight, final boolean withAction) {
        if (refreshTriggerDistance == -1) {
            return;
        }

        cancelAllAnimation();
        if (!isHoldingTrigger && onHeaderPullHolding()) {
            isHoldingTrigger = true;
        }
        if (headerViewHeight == refreshTriggerDistance) {
            refreshStartAnimationListener.onAnimationEnd(null);
            return;
        }
        if (startRefreshAnimator == null) {
            startRefreshAnimator = getAnimator(headerViewHeight, refreshTriggerDistance, headerAnimationUpdate, refreshStartAnimationListener, readyMainInterpolator());
        } else {
            startRefreshAnimator.setIntValues(headerViewHeight, refreshTriggerDistance);
        }
        refreshWithAction = withAction;
        startRefreshAnimator.setDuration(refreshAnimationDuring);
        startRefreshAnimator.start();
    }

    private void resetHeaderView(int headerViewHeight) {
        if (resetHeaderEnable) {
            cancelAllAnimation();
            if (headerViewHeight == 0) {
                resetHeaderAnimationListener.onAnimationStart(null);
                resetHeaderAnimationListener.onAnimationEnd(null);
                return;
            }
            if (resetHeaderAnimator == null) {
                resetHeaderAnimator = getAnimator(headerViewHeight, 0, headerAnimationUpdate, resetHeaderAnimationListener, readyMainInterpolator());
            } else {
                resetHeaderAnimator.setIntValues(headerViewHeight, 0);
            }
            resetHeaderAnimator.setDuration(resetAnimationDuring);
            resetHeaderAnimator.start();
        }
    }

    private void resetRefreshState() {
        if (isHoldingFinishTrigger && onHeaderPullReset()) ;
        if (footerView != null) {
            footerView.setVisibility(VISIBLE);
        }
        resetState();
    }

    private void startLoadMore(int loadMoreViewHeight, boolean withAction) {
        if (loadTriggerDistance == -1) {
            return;
        }

        cancelAllAnimation();
        if (!isHoldingTrigger && onFooterPullHolding()) {
            isHoldingTrigger = true;
        }
        if (loadMoreViewHeight == -loadTriggerDistance) {
            loadingStartAnimationListener.onAnimationEnd(null);
            return;
        }
        if (startLoadMoreAnimator == null) {
            startLoadMoreAnimator = getAnimator(loadMoreViewHeight, -loadTriggerDistance, footerAnimationUpdate, loadingStartAnimationListener, readyMainInterpolator());
        } else {
            startLoadMoreAnimator.setIntValues(loadMoreViewHeight, -loadTriggerDistance);
        }
        refreshWithAction = withAction;
        startLoadMoreAnimator.setDuration(refreshAnimationDuring);
        startLoadMoreAnimator.start();
    }

    private void resetFootView(int loadMoreViewHeight) {
        if (resetFooterEnable) {
            cancelAllAnimation();
            if (loadMoreViewHeight == 0) {
                resetFooterAnimationListener.onAnimationStart(null);
                resetFooterAnimationListener.onAnimationEnd(null);
                return;
            }
            if (resetFooterAnimator == null) {
                resetFooterAnimator = getAnimator(loadMoreViewHeight, 0, footerAnimationUpdate, resetFooterAnimationListener, readyMainInterpolator());
            } else {
                resetFooterAnimator.setIntValues(loadMoreViewHeight, 0);
            }
            resetFooterAnimator.setDuration(resetAnimationDuring);
            resetFooterAnimator.start();
        }
    }

    private void resetLoadMoreState() {
        if (isHoldingFinishTrigger && onFooterPullReset()) ;
        if (headerView != null) {
            headerView.setVisibility(VISIBLE);
        }
        resetState();
    }

    private void resetState() {
        isHoldingFinishTrigger = false;
        isAutoLoadingTrigger = false;
        isHoldingTrigger = false;
        pullStateControl = true;
        isResetTrigger = false;
        refreshState = 0;
    }

    private ValueAnimator getAnimator(int firstValue, int secondValue, ValueAnimator.AnimatorUpdateListener updateListener, Animator.AnimatorListener animatorListener, Interpolator interpolator) {
        ValueAnimator animator = ValueAnimator.ofInt(firstValue, secondValue);
        animator.addUpdateListener(updateListener);
        animator.addListener(animatorListener);
        animator.setInterpolator(interpolator);
        return animator;
    }

    private void abortScroller() {
        if (scroller != null && !scroller.isFinished()) {
            scroller.abortAnimation();
        }
    }

    private void cancelAnimation(ValueAnimator animator) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    private long getOverScrollTime(int distance) {
        float ratio = Math.abs((float) distance / OverScrollUtils.getWindowHeight(getContext()));
        return Math.max(overScrollMinDuring, (long) ((Math.pow(2000 * ratio, 0.44)) * overScrollAdjustValue));
    }

    private void dellNestedScrollCheck() {
        View target = targetView;
        while (target != pullContentLayout) {
            if (!(target instanceof NestedScrollingChild)) {
                isTargetNested = false;
                return;
            }
            target = (View) target.getParent();
        }
        isTargetNested = (target instanceof NestedScrollingChild);
    }

    private void removeDelayRunnable() {
        if (delayHandleActionRunnable != null) {
            removeCallbacks(delayHandleActionRunnable);
        }
    }

    /**
     * the fling may execute after onStopNestedScroll , so while overScrollBack try delay to handle action
     */
    private Runnable getDelayHandleActionRunnable() {
        return new Runnable() {
            public void run() {
                if (!pullTwinkEnable || (scroller != null && scroller.isFinished() && overScrollState == 0)) {
                    handleAction();
                }
            }
        };
    }

    private void setViewFront(boolean firstFront, boolean secondFront, View firstView, View secondView) {
        if (firstFront) {
            bringViewToFront(firstView);
        } else {
            bringViewToFront(pullContentLayout);
            if (secondFront) {
                bringViewToFront(secondView);
            }
        }
    }

    private void bringViewToFront(View view) {
        if (view != null) {
            view.bringToFront();
        }
    }

    private boolean dellDetachComplete() {
        // if the refreshLayout is detach window just mark the trigger state
        // to dell reAttachWindow , both the same as loadingComplete
        if (!isAttachWindow) {
            isResetTrigger = true;
            isHoldingFinishTrigger = true;
            return true;
        }
        return false;
    }

    private boolean nestedAble(View target) {
        return isTargetNestedScrollingEnabled() || !(target instanceof NestedScrollingChild);
    }

    /**
     * - use by generalHelper to dell touch logic
     *
     * @return
     */
    boolean isTargetNestedScrollingEnabled() {
        return isTargetNested && ViewCompat.isNestedScrollingEnabled(targetView);
    }

    private int overScrollFlingState() {
        if (moveDistance == 0) {
            return 0;
        }
        if (!generalPullHelper.isDragMoveTrendDown) {
            if (moveDistance > 0) {
                return 1;
            } else if (moveDistance < 0) {
                return -1; // scroller fling unable
            }
        } else {
            if (moveDistance < 0) {
                return 2;
            } else if (moveDistance > 0) {
                return -1; // scroller fling unable
            }
        }
        return 0;
    }

    private View getRefreshView(View v) {
        LayoutParams lp = v.getLayoutParams();
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v);
        }
        if (lp == null) {
            lp = new LayoutParams(-1, -2);
            v.setLayoutParams(lp);
        }
        return v;
    }

    private void onHeaderPullChange() {
        if (headerView != null && headerView instanceof OnPullListener && !isLoading()) {
            ((OnPullListener) headerView).onPullChange((float) moveDistance / refreshTriggerDistance);
        }
    }

    private void onHeaderPullHoldTrigger() {
        if (headerView instanceof OnPullListener) {
            ((OnPullListener) headerView).onPullHoldTrigger();
        }
    }

    private void onHeaderPullHoldUnTrigger() {
        if (headerView instanceof OnPullListener) {
            ((OnPullListener) headerView).onPullHoldUnTrigger();
        }
    }

    private boolean onHeaderPullHolding() {
        if (headerView instanceof OnPullListener) {
            ((OnPullListener) headerView).onPullHolding();
            return true;
        }
        return false;
    }

    private boolean onHeaderPullFinish() {
        if (headerView != null && headerView instanceof OnPullListener) {
            ((OnPullListener) headerView).onPullFinish();
            return true;
        }
        return false;
    }

    private boolean onHeaderPullReset() {
        if (headerView != null && headerView instanceof OnPullListener) {
            ((OnPullListener) headerView).onPullReset();
            return true;
        }
        return false;
    }

    private void onFooterPullChange() {
        if (footerView != null && footerView instanceof OnPullListener && !isRefreshing()) {
            ((OnPullListener) footerView).onPullChange((float) moveDistance / loadTriggerDistance);
        }
    }

    private void onFooterPullHoldTrigger() {
        if (footerView instanceof OnPullListener) {
            ((OnPullListener) footerView).onPullHoldTrigger();
        }
    }

    private void onFooterPullHoldUnTrigger() {
        if (footerView instanceof OnPullListener) {
            ((OnPullListener) footerView).onPullHoldUnTrigger();
        }
    }

    private boolean onFooterPullHolding() {
        if (footerView instanceof OnPullListener) {
            ((OnPullListener) footerView).onPullHolding();
            return true;
        }
        return false;
    }

    private boolean onFooterPullFinish() {
        if (footerView != null && footerView instanceof OnPullListener) {
            ((OnPullListener) footerView).onPullFinish();
            return true;
        }
        return false;
    }

    private boolean onFooterPullReset() {
        if (footerView != null && footerView instanceof OnPullListener) {
            ((OnPullListener) footerView).onPullReset();
            return true;
        }
        return false;
    }

    public boolean isTargetAbleScrollUp() {
        return OverScrollUtils.canChildScrollUp(targetView);
    }

    public boolean isTargetAbleScrollDown() {
        return OverScrollUtils.canChildScrollDown(targetView);
    }

    /**
     * state animation
     */
    private final AnimatorListenerAdapter resetHeaderAnimationListener = new PullAnimatorListenerAdapter() {
        protected void animationStart() {
            if (isResetTrigger && isRefreshing() && !isHoldingFinishTrigger && onHeaderPullFinish()) {
                isHoldingFinishTrigger = true;
            }
        }

        protected void animationEnd() {
            if (isResetTrigger) {
                resetRefreshState();
            }
        }
    };

    private final AnimatorListenerAdapter resetFooterAnimationListener = new PullAnimatorListenerAdapter() {
        protected void animationStart() {
            if (isResetTrigger && isLoading() && !isHoldingFinishTrigger && onFooterPullFinish()) {
                isHoldingFinishTrigger = true;
            }
        }

        protected void animationEnd() {
            if (isResetTrigger) {
                resetLoadMoreState();
            }
        }
    };

    private final AnimatorListenerAdapter refreshStartAnimationListener = new PullAnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (refreshState == 0) {
                refreshState = 1;
                if (footerView != null) {
                    footerView.setVisibility(GONE);
                }
                if (onRefreshListener != null && refreshWithAction) {
                    onRefreshListener.onRefresh();
                }
            }
        }
    };

    private final AnimatorListenerAdapter loadingStartAnimationListener = new PullAnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (refreshState == 0) {
                refreshState = 2;
                if (headerView != null) {
                    headerView.setVisibility(GONE);
                }
                if (!noMore && onRefreshListener != null && refreshWithAction && !isAutoLoadingTrigger) {
                    onRefreshListener.onLoading();
                }
            }
        }
    };

    private final AnimatorListenerAdapter overScrollAnimatorListener = new PullAnimatorListenerAdapter() {
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            onNestedScrollAccepted(null, null, 2);
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            handleAction();
            onStopNestedScroll(null);
            overScrollState = 0;
            isOverScrollTrigger = false;
        }
    };

    /**
     * animator update listener
     */
    private final ValueAnimator.AnimatorUpdateListener headerAnimationUpdate = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            if (onScrollerScrollChangeListener != null)
                onScrollerScrollChangeListener.onScrollChange((Integer) animation.getAnimatedValue());
            moveChildren((Integer) animation.getAnimatedValue());
            onHeaderPullChange();
        }
    };

    private final ValueAnimator.AnimatorUpdateListener footerAnimationUpdate = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            if (onScrollerScrollChangeListener != null)
                onScrollerScrollChangeListener.onScrollChange((Integer) animation.getAnimatedValue());
            moveChildren((Integer) animation.getAnimatedValue());
            onFooterPullChange();
        }
    };

    private final ValueAnimator.AnimatorUpdateListener overScrollAnimatorUpdate = new ValueAnimator.AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            int offsetY = (int) ((Integer) animation.getAnimatedValue() * overScrollDampingRatio);
            if (ViewCompat.isNestedScrollingEnabled(targetView)) {
                dispatchNestedScroll(0, 0, 0, offsetY, parentOffsetInWindow);
            }
            onScrollAny(offsetY + parentOffsetInWindow[1]);
        }
    };

    void onStartScroll() {
        abortScroller();
        cancelAllAnimation();
        isScrollAbleViewBackScroll = false;
    }

    void onPreScroll(int dy, int[] consumed) {
        if (dy > 0 && moveDistance > 0) {
            if (dy > moveDistance) {
                consumed[1] += moveDistance;
                dellScroll(-moveDistance);
                return;
            }
            consumed[1] += dy;
            dellScroll(-dy);
        } else if (dy < 0 && moveDistance < 0) {
            if (dy < moveDistance) {
                consumed[1] += moveDistance;
                dellScroll(-moveDistance);
                return;
            }
            consumed[1] += dy;
            dellScroll(-dy);
        }
    }

    void onScroll(int dy) {
        if ((generalPullHelper.isDragMoveTrendDown && !isTargetAbleScrollUp()) || (!generalPullHelper.isDragMoveTrendDown && !isTargetAbleScrollDown())) {
            onScrollAny(dy);
        }
    }

    private void onScrollAny(int dy) {
        if (dy < 0 && dragDampingRatio < 1 && pullDownMaxDistance > 0 && moveDistance - dy > pullDownMaxDistance * dragDampingRatio) {
            dy = (int) (dy * (1 - (moveDistance / (float) pullDownMaxDistance)));
        } else if (dy > 0 && dragDampingRatio < 1 && pullUpMaxDistance > 0 && -moveDistance + dy > pullUpMaxDistance * dragDampingRatio) {
            dy = (int) (dy * (1 - (-moveDistance / (float) pullUpMaxDistance)));
        } else {
            dy = (int) (dy * dragDampingRatio);
        }
        dellScroll(-dy);
    }

    void onStopScroll() {
        removeDelayRunnable();
        if (!pullTwinkEnable) {
            handleAction();
        } else if ((overScrollFlingState() == 1 || overScrollFlingState() == 2) && !isOverScrollTrigger) {
            if (delayHandleActionRunnable == null) {
                delayHandleActionRunnable = getDelayHandleActionRunnable();
            }
            postDelayed(delayHandleActionRunnable, 50);
        } else if (scroller != null && scroller.isFinished()) {
            handleAction();
        }

        // just make that custom header and footer easier
        if (generalPullHelper.isLayoutDragMoved) {
            if (isRefreshing() || moveDistance > 0) {
                onHeaderPullChange();
            } else if (isLoading() || moveDistance < 0) {
                onFooterPullChange();
            }
        }
    }

    void onPreFling(float velocityY) {
        if ((pullTwinkEnable || autoLoadingEnable) && overScrollFlingState() != -1) {
            readyScroller();
            lastScrollY = 0;
            scroller.fling(0, 0, 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            finalScrollDistance = scroller.getFinalY() - scroller.getCurrY();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (nestedAble(target)) {
            generalPullHelper.dellDirection(dy);
            if (isMoveWithContent) {
                onPreScroll(dy, consumed);
            }
            final int[] parentConsumed = parentScrollConsumed;
            if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
                consumed[0] += parentConsumed[0];
                consumed[1] += parentConsumed[1];
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (nestedAble(target)) {
            dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, parentOffsetInWindow);
            if (isMoveWithContent) {
                onScroll(dyUnconsumed + parentOffsetInWindow[1]);
            }
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        parentHelper.onStopNestedScroll(child);
        stopNestedScroll();
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (nestedAble(target)) {
            onPreFling(velocityY);
        }
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public interface OnPullListener {
        void onPullChange(float percent);

        void onPullHoldTrigger();

        void onPullHoldUnTrigger();

        void onPullHolding();

        void onPullFinish();

        void onPullReset();

        void setNoMore(boolean b);

        void loadMoreComplete(boolean b);
    }

    public interface OnDragIntercept {
        boolean onHeaderDownIntercept();

        boolean onFooterUpIntercept();
    }

    public static class OnDragInterceptAdapter implements OnDragIntercept {
        public boolean onHeaderDownIntercept() {
            return true;
        }

        public boolean onFooterUpIntercept() {
            return true;
        }
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoading();
    }

    private OnScrollerScrollChangeListener onScrollerScrollChangeListener;

    public void setOnScrollerScrollChangeListener(OnScrollerScrollChangeListener o) {
        this.onScrollerScrollChangeListener = o;
    }

    public interface OnScrollerScrollChangeListener {
        void onScrollChange(int distance);
    }


    public static class OnRefreshListenerAdapter implements OnRefreshListener {
        public void onRefresh() {
        }

        public void onLoading() {
        }
    }

    private class PullAnimatorListenerAdapter extends AnimatorListenerAdapter {
        private boolean isCancel;

        public void onAnimationStart(Animator animation) {
            if (!isAttachWindow) {
                return;
            }
            animationStart();
        }

        public void onAnimationCancel(Animator animation) {
            isCancel = true;
        }

        public void onAnimationEnd(Animator animation) {
            if (!isAttachWindow) {
                return;
            }
            if (!isCancel) {
                animationEnd();
            }
            isCancel = false;
        }

        protected void animationStart() {
        }

        protected void animationEnd() {
        }
    }

    // ------------------| open api |------------------

    /**
     * move children
     */
    public final void moveChildren(int distance) {
        moveDistance = distance;
        if (moveDistance <= 0 && !isTargetAbleScrollDown()) {
            autoLoadingTrigger();
        }
        if (isMoveWithFooter) {
            showGravity.dellFooterMoving(moveDistance);
        }
        if (isMoveWithHeader) {
            showGravity.dellHeaderMoving(moveDistance);
        }
        if (isMoveWithContent) {
            ViewCompat.setTranslationY(pullContentLayout, moveDistance);
        }
    }

    public final void cancelAllAnimation() {
        cancelAnimation(overScrollAnimator);
        cancelAnimation(startRefreshAnimator);
        cancelAnimation(resetHeaderAnimator);
        cancelAnimation(startLoadMoreAnimator);
        cancelAnimation(resetFooterAnimator);
        removeDelayRunnable();
    }

    public void refreshComplete() {
        if (targetView != null) {
            if (targetView instanceof AbsListView) {
                if (((ListView) targetView).getAdapter() instanceof BaseAdapter)
                    ((BaseAdapter) ((ListView) targetView).getAdapter()).notifyDataSetChanged();
                if (((ListView) targetView).getAdapter() instanceof HeaderViewListAdapter)
                    ((BaseAdapter) ((HeaderViewListAdapter) ((ListView) targetView).getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
            }
            if (targetView instanceof RecyclerView)
                ((RecyclerView) targetView).getAdapter().notifyDataSetChanged();
        }
        noMore = false;
        if (footerView != null) ((OnPullListener) footerView).setNoMore(noMore);
        if (!dellDetachComplete() && !isLoading()) {
            isResetTrigger = true;
            if (resetHeaderAnimator != null && resetHeaderAnimator.isRunning()) {
                resetHeaderAnimationListener.onAnimationStart(null);
                return;
            }
            resetHeaderView(moveDistance);
        }
    }

    public void loadMoreComplete() {
        if (targetView != null) {
            if (targetView instanceof AbsListView) {
                if (((ListView) targetView).getAdapter() instanceof BaseAdapter)
                    ((BaseAdapter) ((ListView) targetView).getAdapter()).notifyDataSetChanged();
                if (((ListView) targetView).getAdapter() instanceof HeaderViewListAdapter)
                    ((BaseAdapter) ((HeaderViewListAdapter) ((ListView) targetView).getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
            }
            if (targetView instanceof RecyclerView)
                ((RecyclerView) targetView).getAdapter().notifyDataSetChanged();
        }
        if (!dellDetachComplete() && !isRefreshing()) {
            isResetTrigger = true;
            if (resetFooterAnimator != null && resetFooterAnimator.isRunning()) {
                resetFooterAnimationListener.onAnimationStart(null);
                return;
            }
            resetFootView(moveDistance);
        }
    }

    public void loadMoreComplete(boolean noMore) {
        if (targetView != null) {
            if (targetView instanceof AbsListView)
                ((BaseAdapter) ((ListView) targetView).getAdapter()).notifyDataSetChanged();
            if (targetView instanceof RecyclerView)
                ((RecyclerView) targetView).getAdapter().notifyDataSetChanged();
        }
        this.noMore = noMore;
        if (getFooterView() != null)
            ((OverScrollLayout.OnPullListener) getFooterView()).loadMoreComplete(noMore);
    }


    public void autoLoading() {
        autoLoading(true);
    }

    public void autoLoading(boolean withAction) {
        if (!pullLoadMoreEnable || isHoldingTrigger || footerView == null) {
            return;
        }
        startLoadMore(moveDistance, withAction);
    }

    public void autoRefresh() {
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                OverScrollLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
                startRefresh();
                return false;
            }
        });
    }

    public void startRefresh() {
        if (pullRefreshEnable && !isLoading() && headerView != null) {
            if (targetView instanceof ScrollView) {
                ((ScrollView) targetView).fullScroll(ScrollView.FOCUS_UP);
            } else if (targetView instanceof RecyclerView) {
                ((LinearLayoutManager) ((RecyclerView) targetView).getLayoutManager()).scrollToPositionWithOffset(0, 0);
            } else if (targetView instanceof NestedScrollView && !isTargetNestedScrollingEnabled() && !isScrollAbleViewBackScroll) {
                ((NestedScrollView) targetView).fullScroll(NestedScrollView.FOCUS_UP);
            } else if (targetView instanceof AbsListView) {
                ((AbsListView) targetView).setSelection(0);
            }
            cancelAllAnimation();
            resetState();
            startRefresh(moveDistance, true);
        }
    }

    public void requestPullDisallowInterceptTouchEvent(boolean b) {
        generalPullHelper.isDisallowIntercept = b;
        requestDisallowInterceptTouchEvent(b);
    }

    public void setHeaderView(View header) {
        if (headerView != null && headerView != header) {
            removeView(headerView);
        }
        headerView = header;
        if (header == null) {
            return;
        }
        addView(getRefreshView(header));

        if (!isHeaderFront) {
            setViewFront(false, isFooterFront, null, footerView);
        }
    }

    public void setFooterView(View footer) {
        if (footerView != null && footerView != footer) {
            removeView(footerView);
        }
        footerView = footer;
        if (footer == null) {
            return;
        }
        addView(getRefreshView(footer));

        if (!isFooterFront) {
            setViewFront(false, isHeaderFront, null, headerView);
        }
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
        cancelTouchEvent();
        dellNestedScrollCheck();
        if ((targetView instanceof RecyclerView) && (pullTwinkEnable || autoLoadingEnable)) {
            if (scrollInterpolator == null) {
                scroller = ScrollerCompat.create(getContext(), scrollInterpolator = getRecyclerDefaultInterpolator());
            }
        }
    }

    public void setLoadMoreEnable(boolean loadEnable) {
        this.pullLoadMoreEnable = loadEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.pullRefreshEnable = refreshEnable;
    }

    public void setTwinkEnable(boolean twinkEnable) {
        this.pullTwinkEnable = twinkEnable;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnDragIntercept(OnDragIntercept onDragIntercept) {
        this.onDragIntercept = onDragIntercept;
    }

    public void setScrollInterpolator(Interpolator interpolator) {
        this.scrollInterpolator = interpolator;
        scroller = ScrollerCompat.create(getContext(), scrollInterpolator);
    }

    public void setRefreshTriggerDistance(int refreshTriggerDistance) {
        this.refreshTriggerDistance = refreshTriggerDistance;
    }

    public void setLoadTriggerDistance(int loadTriggerDistance) {
        this.loadTriggerDistance = loadTriggerDistance;
    }

    public void setPullDownMaxDistance(int pullDownMaxDistance) {
        this.pullDownMaxDistance = pullDownMaxDistance;
    }

    public void setPullUpMaxDistance(int pullUpMaxDistance) {
        this.pullUpMaxDistance = pullUpMaxDistance;
    }

    public void setOverScrollAdjustValue(float overScrollAdjustValue) {
        this.overScrollAdjustValue = overScrollAdjustValue;
    }

    public void setTopOverScrollMaxTriggerOffset(int topOverScrollMaxTriggerOffset) {
        this.topOverScrollMaxTriggerOffset = topOverScrollMaxTriggerOffset;
    }

    public void setBottomOverScrollMaxTriggerOffset(int bottomOverScrollMaxTriggerOffset) {
        this.bottomOverScrollMaxTriggerOffset = bottomOverScrollMaxTriggerOffset;
    }

    public void setOverScrollMinDuring(int overScrollMinDuring) {
        this.overScrollMinDuring = overScrollMinDuring;
    }

    public void setOverScrollDampingRatio(float overScrollDampingRatio) {
        this.overScrollDampingRatio = overScrollDampingRatio;
    }

    public void setRefreshAnimationDuring(int refreshAnimationDuring) {
        this.refreshAnimationDuring = refreshAnimationDuring;
    }

    public void setResetAnimationDuring(int resetAnimationDuring) {
        this.resetAnimationDuring = resetAnimationDuring;
    }

    public void setDragDampingRatio(float dragDampingRatio) {
        this.dragDampingRatio = dragDampingRatio;
    }

    public void setAutoLoadingEnable(boolean ableAutoLoading) {
        autoLoadingEnable = ableAutoLoading;
    }

    public void setRefreshShowGravity(@ShowGravity.ShowState int headerShowGravity, @ShowGravity.ShowState int footerShowGravity) {
        setHeaderShowGravity(headerShowGravity);
        setFooterShowGravity(footerShowGravity);
    }

    public void setHeaderShowGravity(@ShowGravity.ShowState int headerShowGravity) {
        showGravity.headerShowGravity = headerShowGravity;
        requestLayout();
    }

    public void setFooterShowGravity(@ShowGravity.ShowState int footerShowGravity) {
        showGravity.footerShowGravity = footerShowGravity;
        requestLayout();
    }

    public void setHeaderFront(boolean headerFront) {
        if (isHeaderFront != headerFront) {
            isHeaderFront = headerFront;
            setViewFront(isHeaderFront, isFooterFront, headerView, footerView);
        }
    }

    public void setFooterFront(boolean footerFront) {
        if (isFooterFront != footerFront) {
            isFooterFront = footerFront;
            setViewFront(isFooterFront, isHeaderFront, footerView, headerView);
        }
    }


    public void setMoveWithFooter(boolean moveWithFooter) {
        this.isMoveWithFooter = moveWithFooter;
    }

    public void setMoveWithContent(boolean moveWithContent) {
        this.isMoveWithContent = moveWithContent;
    }

    public void setMoveWithHeader(boolean moveWithHeader) {
        this.isMoveWithHeader = moveWithHeader;
    }

    public final void cancelTouchEvent() {
        if (generalPullHelper.dragState != 0) {
            super.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
        }
    }

    public void setDispatchTouchAble(boolean dispatchTouchAble) {
        this.dispatchTouchAble = dispatchTouchAble;
    }

    public void setDispatchPullTouchAble(boolean dispatchPullTouchAble) {
        this.dispatchPullTouchAble = dispatchPullTouchAble;
    }

    public void setDispatchChildrenEventAble(boolean dispatchChildrenEventAble) {
        this.dispatchChildrenEventAble = dispatchChildrenEventAble;
    }

    public void setAnimationMainInterpolator(Interpolator animationMainInterpolator) {
        this.animationMainInterpolator = animationMainInterpolator;
    }

    public void setAnimationOverScrollInterpolator(Interpolator animationOverScrollInterpolator) {
        this.animationOverScrollInterpolator = animationOverScrollInterpolator;
    }

    public void setResetHeaderEnable(boolean b) {
        this.resetHeaderEnable = b;
    }

    public boolean isResetHeaderEnable() {
        return this.resetHeaderEnable;
    }

    public void setResetFooterEnable(boolean b) {
        this.resetFooterEnable = b;
    }

    public boolean isResetFooterEnable() {
        return this.resetFooterEnable;
    }

    public final int getMoveDistance() {
        return moveDistance;
    }

    public <T extends View> T getHeaderView() {
        return (T) headerView;
    }

    public <T extends View> T getFooterView() {
        return (T) footerView;
    }

    public <T extends View> T getTargetView() {
        return (T) targetView;
    }

    public int getRefreshTriggerDistance() {
        return refreshTriggerDistance;
    }

    public int getLoadTriggerDistance() {
        return loadTriggerDistance;
    }

    public int getPullDownMaxDistance() {
        return pullDownMaxDistance;
    }

    public int getPullUpMaxDistance() {
        return pullUpMaxDistance;
    }

    public boolean isOverScrollUp() {
        return overScrollState == 1;
    }

    public boolean isOverScrollDown() {
        return overScrollState == 2;
    }

    public boolean isLoadMoreEnable() {
        return pullLoadMoreEnable;
    }

    public boolean isRefreshEnable() {
        return pullRefreshEnable;
    }

    public boolean isTwinkEnable() {
        return pullTwinkEnable;
    }

    public boolean isRefreshing() {
        return (refreshState == 0 && startRefreshAnimator != null && startRefreshAnimator.isRunning()) || refreshState == 1;
    }

    public boolean isLoading() {
        return (refreshState == 0 && startLoadMoreAnimator != null && startLoadMoreAnimator.isRunning()) || refreshState == 2;
    }

    public boolean isDragDown() {
        return generalPullHelper.dragState == 1;
    }

    public boolean isDragUp() {
        return generalPullHelper.dragState == -1;
    }

    public boolean isDragMoveTrendDown() {
        return generalPullHelper.isDragMoveTrendDown;
    }

    public boolean isHoldingTrigger() {
        return isHoldingTrigger;
    }

    public boolean isHoldingFinishTrigger() {
        return isHoldingFinishTrigger;
    }

    public boolean isOverScrollTrigger() {
        return isOverScrollTrigger;
    }

    public boolean isAutoLoadingTrigger() {
        return isAutoLoadingTrigger;
    }

    public boolean isDragVertical() {
        return generalPullHelper.isDragVertical;
    }

    public boolean isDragHorizontal() {
        return generalPullHelper.isDragHorizontal;
    }

    public boolean isLayoutDragMoved() {
        return generalPullHelper.isLayoutDragMoved;
    }

    public boolean isLayoutMoving() {
        if (startRefreshAnimator != null && startRefreshAnimator.isRunning()) {
            return true;
        } else if (resetHeaderAnimator != null && resetHeaderAnimator.isRunning()) {
            return true;
        } else if (startLoadMoreAnimator != null && startLoadMoreAnimator.isRunning()) {
            return true;
        } else if (resetFooterAnimator != null && resetFooterAnimator.isRunning()) {
            return true;
        } else if (overScrollAnimator != null && overScrollAnimator.isRunning()) {
            return true;
        }
        return false;
    }
}