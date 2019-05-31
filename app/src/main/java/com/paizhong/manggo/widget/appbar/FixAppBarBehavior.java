package com.paizhong.manggo.widget.appbar;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.paizhong.manggo.R;

import java.lang.reflect.Field;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * Des:修复AppBarLayout嵌套在滑动过程中卡顿 闪烁
 * Created by hs on 2018/7/9 0009 17:39
 */
public class FixAppBarBehavior extends AppBarLayout.Behavior {

    private OnScrollListener mListener;

    public FixAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (ev.getAction() == ACTION_DOWN) {
            Object scroller = getSuperSuperField(this);
            if (scroller instanceof OverScroller) {
                OverScroller overScroller = (OverScroller) scroller;
                overScroller.abortAnimation();
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private Object getSuperSuperField(Object paramClass) {
        Field field;
        Object object = null;
        try {
            field = paramClass.getClass().getSuperclass().getSuperclass().getDeclaredField("mScroller");
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception ignored) {
        }
        return object;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout parent, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyConsumed > 0 && mListener != null) {
            mListener.onScrollUp();
        } else if (dyConsumed < 0 && mListener != null) {
            mListener.onScrollDown();
        }
        super.onNestedScroll(parent, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    public interface OnScrollListener {
        void onScrollUp();

        void onScrollDown();
    }
}
