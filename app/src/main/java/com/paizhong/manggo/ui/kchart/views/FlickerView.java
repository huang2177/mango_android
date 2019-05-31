package com.paizhong.manggo.ui.kchart.views;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;

import com.paizhong.manggo.R;


/**
 * 闪电图上闪烁的圆
 */

public class FlickerView extends View {
    public FlickerView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.shape_circle_72b3ff);
        PropertyValuesHolder pvhOutA = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.2f);
        PropertyValuesHolder pvhOutX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f);
        PropertyValuesHolder pvOutY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f);
        ObjectAnimator ani = ObjectAnimator.ofPropertyValuesHolder(this, pvhOutA, pvhOutX, pvOutY);
        ani.setRepeatCount(ObjectAnimator.INFINITE);
        ani.setRepeatMode(ObjectAnimator.REVERSE);
        ani.setDuration(800);
        ani.start();
    }
}
