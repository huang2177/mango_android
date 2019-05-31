package com.paizhong.manggo.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.paizhong.manggo.utils.Logs;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 智能刷新布局 重写 防止事件分发时 引起的崩溃
 * Created by huang on 2017/5/26.
 */
public class FixRefreshLayout extends SmartRefreshLayout {

    private String TAG = "FixRefreshLayout";

    public FixRefreshLayout(Context context) {
        this(context, null);
    }

    public FixRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            Logs.e(TAG, "meet a Exception in SmartRefreshLayout");
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        try {
            return super.dispatchTouchEvent(e);
        } catch (Exception e1) {
            Logs.e(TAG, "meet a Exception in SmartRefreshLayout");
            return false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        } catch (Exception e) {
            Logs.e(TAG, "meet a Exception in SmartRefreshLayout");
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        }catch (Exception e){
            Logs.e(TAG, "meet a Exception in SmartRefreshLayout");
        }
    }

    public boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }
}