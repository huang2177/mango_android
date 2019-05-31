package com.paizhong.manggo.ui.kchart.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;


import com.paizhong.manggo.R;

import java.util.ArrayList;

public class RippleBackground extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 60000;
    private static final float DEFAULT_SCALE = 6.0f;
    private static final int DEFAULT_FILL_TYPE = 0;

    private float rippleStrokeWidth;
    private float rippleRadius;
    private int rippleDurationTime;
    private int rippleAmount;
    private int rippleDelay;
    private float rippleScale;
    private int rippleType;
    private Paint paint;
    private boolean animationRunning = false;
    private AnimatorSet animatorSet;
    private ArrayList<Animator> animatorList;
    private LayoutParams rippleParams;
    private ArrayList<RippleView> rippleViewList = new ArrayList<>();

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
        rippleStrokeWidth = typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.dimen_2dp));
        rippleRadius = typedArray.getDimension(R.styleable.RippleBackground_rb_radius, getResources().getDimension(R.dimen.dimen_4dp));
        rippleDurationTime = typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
        rippleAmount = typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount, DEFAULT_RIPPLE_COUNT);
        rippleScale = typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
        rippleType = typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
        typedArray.recycle();
        this.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));


    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = (int) ((rippleRadius + rippleStrokeWidth) * 2 * rippleScale);
        int measureHeight = measureWidth;
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }*/


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void init(int rippleColor) {
        rippleDelay = rippleDurationTime / rippleAmount;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0;
            paint.setStyle(Paint.Style.FILL);
        } else
            paint.setStyle(Paint.Style.STROKE);
        paint.setColor(rippleColor);

//        rippleParams = new LayoutParams((int) (2 * (rippleRadius + rippleStrokeWidth)), (int) (2 * (rippleRadius + rippleStrokeWidth)));
        rippleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList = new ArrayList<>();

        for (int i = 0; i < rippleAmount; i++) {
            RippleView rippleView = new RippleView(getContext());
            addView(rippleView, rippleParams);
            rippleViewList.add(rippleView);

//            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale);
//            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
//            scaleYAnimator.setStartDelay(i * rippleDelay);
//            scaleYAnimator.setDuration(rippleDurationTime);
//            animatorList.add(scaleYAnimator);
//            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale);
//            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
//            scaleXAnimator.setStartDelay(i  * rippleDelay);
//            scaleXAnimator.setDuration(rippleDurationTime);
//            animatorList.add(scaleXAnimator);
//            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 0.6f, 0f);
//            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
//            alphaAnimator.setStartDelay(i * rippleDelay);
//            alphaAnimator.setDuration(rippleDurationTime);
//            animatorList.add(alphaAnimator);

            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0.65f, 0f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.0f, rippleScale);
            PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1.0f, rippleScale);
            final ObjectAnimator ani = ObjectAnimator.ofPropertyValuesHolder(rippleView, pvhX, pvhY, pvhZ);
            ani.setRepeatCount(ObjectAnimator.INFINITE);
            ani.setRepeatMode(ObjectAnimator.RESTART);
            ani.setStartDelay(i * rippleDelay);
            ani.setDuration(rippleDurationTime - i * rippleDelay);
            animatorList.add(ani);

        }
//        animatorSet.playTogether(animatorList);
        animatorSet.playSequentially(animatorList);
        startRippleAnimation();
    }

    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(getResources().getColor(android.R.color.transparent));
            int radius = (Math.min(getWidth(), getHeight())) / 2;
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawCircle(radius, radius, radius - rippleStrokeWidth, paint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measureWidth = (int) ((rippleRadius + rippleStrokeWidth));
            int measureHeight = measureWidth;
            // 计算自定义的ViewGroup中所有子控件的大小
//            measureChildren(widthMeasureSpec, heightMeasureSpec);

            // 设置自定义的控件MyViewGroup的大小
            setMeasuredDimension(measureWidth, measureHeight);
        }

        /*@Override
        protected void onDetachedFromWindow() {
            stopRippleAnimation();
            super.onDetachedFromWindow();
        }*/
    }

    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            for (RippleView rippleView : rippleViewList) {
                rippleView.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animatorSet.end();
            animationRunning = false;
        }
    }

    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }
}
