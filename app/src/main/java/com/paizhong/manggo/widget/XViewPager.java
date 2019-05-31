package com.paizhong.manggo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Des:
 * Created by huang on 2019/5/10 0010 13:57
 */
public class XViewPager extends MyViewPager {

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    long lastTime = 0L;
    float lastDownX = 0f;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0) {
            boolean isTouching = false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastDownX = ev.getX();
                    lastTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float nowDownX = ev.getX();
                    long nowTime = System.currentTimeMillis();
                    float diffX = lastDownX - nowDownX;
                    long diffTime = nowTime - lastTime;
                    if (diffX < 150 && diffTime < 50 && (diffX / diffTime) < 3) {
                        lastTime = nowTime;
                        lastDownX = nowDownX;
                        return false;
                    } else {
                        lastTime = nowTime;
                        lastDownX = nowDownX;
                        isTouching = true;
                    }
                case MotionEvent.ACTION_UP:
                    if (isTouching) {
                        return super.onInterceptTouchEvent(ev);
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void setCanScroll(boolean isCanScroll) {
    }

}
