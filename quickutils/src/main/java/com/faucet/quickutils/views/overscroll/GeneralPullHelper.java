package com.faucet.quickutils.views.overscroll;

import android.content.Context;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * support general view to pull refresh
 * Created by sunsh on 2017/11/16.
 */
class GeneralPullHelper {
    private final OverScrollLayout prl;

    /**
     * default values
     */
    private final int minimumFlingVelocity;
    private final int maximumVelocity;
    private final int touchSlop;

    /**
     * is Being Dragged
     * - use by pullRefreshLayout to know is drag Vertical
     */
    boolean isDragVertical;

    /**
     * is Drag Horizontal
     * - use by pullRefreshLayout to know is drag Horizontal
     */
    boolean isDragHorizontal;

    /**
     * is moving direct down
     * - use by pullRefreshLayout to get moving direction
     */
    boolean isDragMoveTrendDown;

    /**
     * is Refresh Layout has moved
     * - use by prl to get know the layout moved
     */
    boolean isLayoutDragMoved;

    /**
     * is Disallow Intercept
     * - use by prl to get know need to Intercept event
     */
    boolean isDisallowIntercept;
    private boolean lastDisallowIntercept;

    /**
     * is ReDispatch TouchEvent
     */
    private boolean isReDispatchMoveEvent;

    /**
     * is Dispatch Touch Cancel
     */
    private boolean isDispatchTouchCancel;

    /**
     * is touch direct down
     * - use by pullRefreshLayout to get drag state
     */
    int dragState;

    /**
     * first touch point x
     */
    private int actionDownPointX;

    /**
     * first touch point y
     */
    private int actionDownPointY;

    /**
     * last Layout Move Distance
     */
    private int lastMoveDistance = Integer.MAX_VALUE;

    /**
     * motion event child consumed
     */
    private final int[] childConsumed = new int[2];
    private int lastChildConsumedY;

    /**
     * active pointer id
     */
    private int activePointerId;

    /**
     * last drag MotionEvent y
     */
    private int lastDragEventY;

    /**
     * touchEvent velocityTracker
     */
    private VelocityTracker velocityTracker;

    GeneralPullHelper(OverScrollLayout pullRefreshLayout, Context context) {
        this.prl = pullRefreshLayout;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        minimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();
    }

    boolean dispatchTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = ev.getPointerId(0);
                actionDownPointX = (int) (ev.getX() + 0.5f);
                lastDragEventY = actionDownPointY = (int) (ev.getY() + 0.5f);

                isLayoutDragMoved = false;
                isDisallowIntercept = false;
                lastDisallowIntercept = false;

                prl.onStartScroll();
                prl.dispatchSuperTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isDisallowIntercept) {
                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    if (ev.findPointerIndex(activePointerId) == -1) {
                        break;
                    }
                    int tempY = (int) (ev.getY(pointerIndex) + 0.5f);
                    if (lastDisallowIntercept) {
                        lastDragEventY = tempY;
                    }
                    int deltaY = lastDragEventY - tempY;
                    lastDragEventY = tempY;

                    if (!isDragVertical || !prl.isTargetNestedScrollingEnabled() || (!prl.isMoveWithContent && prl.moveDistance != 0)) {
                        dellDirection(deltaY);
                    }
                    int movingX = (int) (ev.getX(pointerIndex) + 0.5f) - actionDownPointX;
                    int movingY = (int) (ev.getY(pointerIndex) + 0.5f) - actionDownPointY;
                    if (!isDragVertical && Math.abs(movingY) > touchSlop && Math.abs(movingY) > Math.abs(movingX)) {
                        final ViewParent parent = prl.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }

                        isDragVertical = true;
                        reDispatchMoveEventDrag(ev, deltaY);
                        lastDragEventY = (int) ev.getY(pointerIndex);
                    } else if (!isDragVertical && !isDragHorizontal && Math.abs(movingX) > touchSlop && Math.abs(movingX) > Math.abs(movingY)) {
                        isDragHorizontal = true;
                    }

                    if (isDragVertical) {
                        // ---------- | make sure that the pullRefreshLayout is moved|----------
                        if (lastMoveDistance == Integer.MAX_VALUE) {
                            lastMoveDistance = prl.moveDistance;
                        }
                        if (lastMoveDistance != prl.moveDistance) {
                            isLayoutDragMoved = true;
                        }
                        lastMoveDistance = prl.moveDistance;

                        reDispatchMoveEventDragging(ev, deltaY);

                        // make sure that can nested to work or the targetView is move with content
                        // dell the touch logic
                        if (!prl.isTargetNestedScrollingEnabled() || !prl.isMoveWithContent) {
                            if (!prl.isMoveWithContent && prl.isTargetNestedScrollingEnabled()) {
                                // when nested scroll the nested event is delay than this logic
                                // so we need adjust the deltaY
                                deltaY = (isDragMoveTrendDown ? -1 : 1) * Math.abs(deltaY);
                            }
                            prl.onPreScroll(deltaY, childConsumed);
                            deltaY = prl.parentOffsetInWindow[1] >= Math.abs(deltaY) ? 0 : deltaY;
                            prl.onScroll(deltaY - (childConsumed[1] - lastChildConsumedY));
                            lastChildConsumedY = childConsumed[1];

                            // -------------------| event reset |--------------------
                            if (!prl.isMoveWithContent) {
                                ev.offsetLocation(0, childConsumed[1]);
                            }
                        }
                    }
                }
                lastDisallowIntercept = isDisallowIntercept;
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                lastDragEventY = (int) ev.getY(index);
                activePointerId = ev.getPointerId(index);
                reDispatchPointDownEvent();
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                lastDragEventY = (int) ev.getY(ev.findPointerIndex(activePointerId));
                reDispatchPointUpEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                dragState = 0;// get know the touchState first

                velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
                float velocityY = (isDragMoveTrendDown ? 1 : -1) * Math.abs(VelocityTrackerCompat.getYVelocity(velocityTracker, activePointerId));
                if (!prl.isTargetNestedScrollingEnabled() && isDragVertical && (Math.abs(velocityY) > minimumFlingVelocity)) {
                    prl.onPreFling(-(int) velocityY);
                }
                recycleVelocityTracker();

                reDispatchUpEvent(ev);
            case MotionEvent.ACTION_CANCEL:
                prl.onStopScroll();

                isReDispatchMoveEvent = false;
                isDispatchTouchCancel = false;
                isDragHorizontal = false;
                isDragVertical = false;

                lastMoveDistance = Integer.MAX_VALUE;
                lastChildConsumedY = 0;
                childConsumed[1] = 0;
                activePointerId = -1;
                dragState = 0;
                break;
        }
        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }
        return prl.dispatchSuperTouchEvent(ev);
    }

    void dellDirection(int offsetY) {
        if (offsetY < 0) {
            dragState = 1;
            isDragMoveTrendDown = true;
        } else if (offsetY > 0) {
            dragState = -1;
            isDragMoveTrendDown = false;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void reDispatchPointDownEvent() {
        if (!prl.isMoveWithContent && isLayoutDragMoved && prl.moveDistance == 0) {
            childConsumed[1] = 0;
            lastChildConsumedY = 0;
        }
    }

    private void reDispatchPointUpEvent(MotionEvent event) {
        if (!prl.isMoveWithContent && isLayoutDragMoved && prl.moveDistance == 0 && childConsumed[1] != 0) {
            prl.dispatchSuperTouchEvent(getReEvent(event, MotionEvent.ACTION_CANCEL));
        }
    }

    private void reDispatchMoveEventDrag(MotionEvent event, int movingY) {
        if ((!prl.isTargetNestedScrollingEnabled() || !prl.isMoveWithContent) && (movingY > 0 && prl.moveDistance > 0 || movingY < 0 && prl.moveDistance < 0
                || (isDragHorizontal && (prl.moveDistance != 0 || !prl.isTargetAbleScrollUp() && movingY < 0 || !prl.isTargetAbleScrollDown() && movingY > 0)))) {
            isDispatchTouchCancel = true;
            prl.dispatchSuperTouchEvent(getReEvent(event, MotionEvent.ACTION_CANCEL));
        }
    }

    private void reDispatchMoveEventDragging(MotionEvent event, int movingY) {
        if ((!prl.isTargetNestedScrollingEnabled() || !prl.isMoveWithContent) && isDispatchTouchCancel && !isReDispatchMoveEvent
                && ((movingY > 0 && prl.moveDistance > 0) || (movingY < 0 && prl.moveDistance < 0))) {
            isReDispatchMoveEvent = true;
            prl.dispatchSuperTouchEvent(getReEvent(event, MotionEvent.ACTION_DOWN));
        }
    }

    private void reDispatchUpEvent(MotionEvent event) {
        if ((!prl.isTargetNestedScrollingEnabled() || !prl.isMoveWithContent) && isDragVertical && isLayoutDragMoved) {
            if (!prl.isTargetAbleScrollDown() && !prl.isTargetAbleScrollUp()) {
                prl.dispatchSuperTouchEvent(getReEvent(event, MotionEvent.ACTION_CANCEL));
            } else if (prl.targetView instanceof ViewGroup) {
                ViewGroup vp = (ViewGroup) prl.targetView;
                for (int i = 0; i < vp.getChildCount(); i++) {
                    vp.getChildAt(i).dispatchTouchEvent(getReEvent(event, MotionEvent.ACTION_CANCEL));
                }
            }
        }
    }

    private MotionEvent getReEvent(MotionEvent event, int action) {
        MotionEvent reEvent = MotionEvent.obtain(event);
        reEvent.setAction(action);
        return reEvent;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int actionIndex = MotionEventCompat.getActionIndex(ev);
        if (ev.getPointerId(actionIndex) == activePointerId) {
            final int newPointerIndex = actionIndex == 0 ? 1 : 0;
            lastDragEventY = (int) ev.getY(newPointerIndex);
            activePointerId = ev.getPointerId(newPointerIndex);

            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }
}
