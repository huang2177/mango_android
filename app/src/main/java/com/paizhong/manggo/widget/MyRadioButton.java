package com.paizhong.manggo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.paizhong.manggo.R;


public class MyRadioButton extends RadioButton {

    private double mDrawableSize;// xml文件中设置的大小

    int drawableHeight = 0;
    int drawableWidth = 0;

    public MyRadioButton(Context context) {
        this(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        redius = context.getResources().getDimensionPixelSize(R.dimen.dimen_4dp);
        paint = new Paint();
        paint.setAntiAlias(true);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyRadioButton_rbDrawableSize:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_rbDrawableSize, 0);
                    break;
                case R.styleable.MyRadioButton_rbDrawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_rbDrawableBottom:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_rbDrawableRight:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_rbDrawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            getDrawableSize(left);
            left.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (right != null) {
            getDrawableSize(right);
            right.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (top != null) {
            getDrawableSize(top);
            top.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (bottom != null) {
            getDrawableSize(bottom);
            bottom.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    private void getDrawableSize(Drawable drawable) {
        double height = drawable.getIntrinsicHeight();
        double width = drawable.getIntrinsicWidth();
        if (mDrawableSize == 0) {
            drawableHeight = (int) height;
            drawableWidth = (int) width;
        } else {
            if (height >= width) {
                double offset = height / mDrawableSize;
                drawableHeight = (int) mDrawableSize;
                drawableWidth = (int) (width / offset);
            } else {
                double offset = width / mDrawableSize;
                drawableWidth = (int) mDrawableSize;
                drawableHeight = (int) (height / offset);
            }
        }
    }

    Paint paint = null;
    private boolean showRedCircle = false;
    private int redius = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showRedCircle && paint != null) {
            int right = (getWidth() + drawableWidth) / 2;
            int top = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
            paint.setColor(Color.RED);
            canvas.drawCircle(right, top, redius, paint);
        }
    }

    public void showRedCircle(boolean mShow) {
        if (showRedCircle == mShow) {
            return;
        }
        this.showRedCircle = mShow;
        invalidate();
    }
}
