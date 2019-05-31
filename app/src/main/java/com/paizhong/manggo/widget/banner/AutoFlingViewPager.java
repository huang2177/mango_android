package com.paizhong.manggo.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

public class AutoFlingViewPager extends ViewPager {

    private int mAutoFlingTime = 4000;
    private final int HANDLE_FLING = 0;
    private Handler mHandler;

    public AutoFlingViewPager(Context context) {
        super(context);
    }

    public AutoFlingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoFlingTime(int autoFlingTime) {
        mAutoFlingTime = autoFlingTime;
    }

    public void setDuration(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(getContext());
            fixedSpeedScroller.setDuration(duration);
            field.set(this, fixedSpeedScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    public void start() {
        stop();
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    next();
                    start();
                }
            };
        }
        Message message = mHandler.obtainMessage(HANDLE_FLING);
        mHandler.sendMessageDelayed(message, mAutoFlingTime);
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.removeMessages(HANDLE_FLING);
        }
    }

    private void next() {
        BaseAutoFlingAdapter<?> adapter = (BaseAutoFlingAdapter<?>) getAdapter();
        if (adapter.getRealCount() > 0) {
            int nextIndex = getCurrentItem() + 1 % adapter.getRealCount();
            this.setCurrentItem(nextIndex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            start();
        } else {
            stop();
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
