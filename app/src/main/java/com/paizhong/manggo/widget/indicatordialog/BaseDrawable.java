package com.paizhong.manggo.widget.indicatordialog;

import android.graphics.drawable.Drawable;


public abstract class BaseDrawable extends Drawable {

    public final int arrowDirection;

    public BaseDrawable(@IndicatorBuilder.ARROWDIRECTION int arrowDirection) {
        this.arrowDirection = arrowDirection;
    }
}
