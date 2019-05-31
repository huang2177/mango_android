package com.paizhong.manggo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.paizhong.manggo.R;

/**
 * Created by guanglong
 */
public class TitleText extends android.support.v7.widget.AppCompatTextView {

    private Context mContext;

    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    public TitleText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TitleText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public TitleText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mIndicatorDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        mIndicatorDrawable.setColor(ContextCompat.getColor(mContext, R.color.color_FEBC18));
        mIndicatorDrawable.setBounds(getWidth() / 6, getHeight() * 2 / 3, getWidth() * 5 / 6, getHeight() * 6 / 5);
        mIndicatorDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}
