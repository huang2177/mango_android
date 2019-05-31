package com.paizhong.manggo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.paizhong.manggo.R;

/**
 * Created by zab on 2018/8/1 0001.
 */
public class MyViewPager extends ViewPager {
    /**
     * 是否需要重新测量子View
     */
    private boolean needReMeasure;

    /**
     * 是否可以滑动
     */
    private boolean isCanScroll = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);
        isCanScroll = typedArray.getBoolean(R.styleable.MyViewPager_isCanScrolled, true);
        needReMeasure = typedArray.getBoolean(R.styleable.MyViewPager_needReMeasure, false);
        typedArray.recycle();
    }

    /**
     * 解决ViewPager高度默认为零的情况
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (needReMeasure) {
            int height = 0;
            //下面遍历所有child的高度
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) //采用最大的view的高度。
                    height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (isCanScroll) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (isCanScroll) {
                return super.onTouchEvent(ev);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}
