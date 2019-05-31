package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.paizhong.manggo.ui.trade.TradeFragment;

/**
 * 可以滑动和放大的view
 */
public abstract class ScrollAndScaleView extends RelativeLayout implements
        GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {

    //x轴的偏移量
    protected int mScrollX = 0;
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;
    //是否是长按事件
    protected boolean isLongPress = false;

    private OverScroller mScroller;
    //是否在触摸中
    protected boolean touch = false;

    protected float mScaleX = 1;
    //x轴最大的缩放程度
    protected float mScaleXMax = 3f;
    //x轴最小的缩放程度
    protected float mScaleXMin = 0.5f;

    /* 按下后的焦点坐标 */
    protected volatile float currentX = 0;
    protected volatile float currentY = 0;

    protected boolean mScalePage = false;
    public ScrollAndScaleView(Context context) {
        super(context);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isLongPress) {
            scrollBy(Math.round(distanceX), 0);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
        currentX = e.getX();
        currentY = e.getY();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isTouch()) {
            mScroller.fling(mScrollX, 0
                    , Math.round(velocityX / mScaleX), 0,
                    Integer.MIN_VALUE, Integer.MAX_VALUE,
                    0, 0);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (!isTouch()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            } else {
                mScroller.forceFinished(true);
            }
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        scrollTo(mScrollX - Math.round(x / mScaleX), 0);
    }

    @Override
    public void scrollTo(int x, int y) {
        int oldX = mScrollX;
        mScrollX = x;
        checkAndFixScrollX();
        onScrollChanged(mScrollX, 0, oldX, 0);
        invalidate();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mScaleX *= detector.getScaleFactor();
        if (mScaleX < mScaleXMin) {
            mScaleX = mScaleXMin;
        } else if (mScaleX > mScaleXMax) {
            mScaleX = mScaleXMax;
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mScalePage){
                    TradeFragment.getInstance().setCanScroll(false);
                }
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mScalePage){
                    TradeFragment.getInstance().setCanScroll(true);
                }
                isLongPress = false;
                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mScalePage){
                    TradeFragment.getInstance().setCanScroll(true);
                }
                isLongPress = false;
                touch = false;
                invalidate();
                break;
        }
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }


    /**
     * 滑到了最左边
     */
    abstract public void onLeftSide();

    /**
     * 滑到了最右边
     */
    abstract public void onRightSide();

    /**
     * 是否在触摸中
     *
     * @return
     */
    public boolean isTouch() {
        return touch;
    }

    /**
     * 获取位移的最小值
     *
     * @return
     */
    public abstract int getMinScrollX();

    /**
     * 获取位移的最大值
     *
     * @return
     */
    public abstract int getMaxScrollX();

    /**
     * 设置ScrollX
     *
     * @param scrollX
     */
    public void setScrollX(int scrollX) {
        this.mScrollX = scrollX;
        scrollTo(scrollX, 0);
    }

    protected void checkAndFixScrollX() {
        if (mScrollX < getMinScrollX()) {
            mScrollX = getMinScrollX();
            onRightSide();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            onLeftSide();
            mScroller.forceFinished(true);
        }
    }
}
