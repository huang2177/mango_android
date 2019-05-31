package com.paizhong.manggo.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.paizhong.manggo.utils.DeviceUtils;

/**
 * Description: K 线图详情买涨买跌闪烁加载组件
 */
public class FlickerProgressBar extends ProgressBar {

    static final int DEF_ANIM_DURING = 2000;
    static final int DEF_FLICKER_WIDTH = 24;
    static final int DEF_START_GRADIENT = 0x80FFFFFF;
    static final int DEF_END_GRADIENT = 0x0FFFFFFF;

    private ValueAnimator mAnimator;

    private Paint mLeftPaint;
    private Paint mRightPaint;

    private LinearGradient mLeftShader;
    private LinearGradient mRightShader;

    private float round;// 0-1

    private int mWidth;

    private int mHeight;

    public FlickerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLeftCursor(canvas);

        drawRightCursor(canvas);
    }

    /**
     * 绘制左侧加载光标
     *
     * @param canvas
     */
    private void drawLeftCursor(Canvas canvas) {
        int progress = getProgress();
        int max = getMax();
        if (progress <= 0)
            return;

        float leftPercent = progress * 1.0f / max;

        int left = (int) (mWidth * leftPercent * round -
                DeviceUtils.dip2px(getContext(), DEF_FLICKER_WIDTH));
        int right = left + DeviceUtils.dip2px(getContext(), DEF_FLICKER_WIDTH);

        mLeftShader = new LinearGradient(left, mHeight, right, mHeight
                , DEF_END_GRADIENT, DEF_START_GRADIENT, Shader.TileMode.CLAMP);
        mLeftPaint.setShader(mLeftShader);

        canvas.drawRect(left, 0, right, mHeight, mLeftPaint);
    }

    /**
     * 绘制右侧加载光标
     *
     * @param canvas
     */
    private void drawRightCursor(Canvas canvas) {
        int progress = getProgress();
        int max = getMax();
        if (progress >= max)
            return;

        float leftPercent = progress * 1.0f / max;
        float rightPercent = (1 - progress * 1.0f / max);

        int left = (int) (mWidth * leftPercent + mWidth * rightPercent * (1 - round));
        int right = left + DeviceUtils.dip2px(getContext(), DEF_FLICKER_WIDTH);

        mRightShader = new LinearGradient(left, mHeight, right, mHeight
                , DEF_START_GRADIENT, DEF_END_GRADIENT, Shader.TileMode.CLAMP);
        mRightPaint.setShader(mRightShader);

        canvas.drawRect(left, 0, right, mHeight, mRightPaint);
    }

    public void startAnimation() {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(0, 1);
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    round = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setDuration(DEF_ANIM_DURING);
        }

        if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stopAnimation() {
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.end();
        }
    }

}
