package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.bean.CrossBean;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.utils.MinuteHourLineUtils;

import java.text.NumberFormat;

public class CrossView extends View {
    //手势控制
    private GestureDetector gestureDetector;
    private OnMoveListener onMoveListener;

    private CrossBean crossBean;

    //左边价格字体宽度
    private float mLeftPriceTextWidth;

    private NumberFormat mFormat = NumberFormat.getNumberInstance();

    private float strokeWidth = 0.001f;//边框线宽度
    private int frameColor = Color.parseColor("#008AAD");//边框色值
    private int backGroundColor = Color.parseColor("#008AAD");//背景色
    private int textColor = Color.parseColor("#FFFFFF");//字体颜色
    private float textSize;//字体大小

    private String mTypeCode;
    private float mTimeStartX;
    private float mTimeEndX;
    private float mTimeTextHeight;


    public void drawLine(CrossBean bean) {
        this.crossBean = bean;
        postInvalidate();
    }

    public void setLeftPriceTextWidth(float leftPriceTextWidth) {
        this.mLeftPriceTextWidth = leftPriceTextWidth;
    }

    public CrossView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFormat.setMaximumFractionDigits(4);
        mFormat.setMinimumFractionDigits(0);
        textSize = getResources().getDimension(R.dimen.dimen_10sp);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动时，通知到接口
                if (onMoveListener != null) {
                    onMoveListener.onCrossMove(e2.getX(), e2.getY());
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    /**
     * 设置移动监听
     *
     * @param onMoveListener
     */
    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() > mLeftPriceTextWidth) {
            if (gestureDetector != null) {
                gestureDetector.onTouchEvent(event);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setVisibility(GONE);
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCrossLine(canvas);
    }

    /**
     * //根据x,y画十字线
     *
     * @param canvas
     */
    private void drawCrossLine(Canvas canvas) {
        if (canvas == null) return;
        if (crossBean == null) return;
        int crossLineColor = Color.parseColor("#5A7F88");
        float crossLineWidth = 1f;
        float textHeight = MinuteHourLineUtils.getFontHeight(textSize);
        float yHeight = getHeight() - textHeight;
        //当该点没有数据的时候，不画
        if (crossBean.x < 0 || crossBean.y < 0) return;
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(crossLineColor);
        p.setStrokeWidth(crossLineWidth);
        p.setStyle(Paint.Style.FILL);

        Paint measurePaint = new Paint();
        measurePaint.setAntiAlias(true);
        measurePaint.setTextSize(textSize);
        measurePaint.setColor(textColor);
        String drawPriceStr;

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(4);//最大多少位数有效
        format.setMinimumFractionDigits(0);//最小多少位有效
        drawPriceStr = KLineUtils.makeNum(format.format(Double.parseDouble(crossBean.price)));

        float priceWidth = MinuteHourLineUtils.getTextWidth(measurePaint, drawPriceStr) + 10;
        float percentWidth = MinuteHourLineUtils.getTextWidth(measurePaint,
                MinuteHourLineUtils.getPercentString(crossBean.percent)) + 10;
        //横线
        canvas.drawLine(mLeftPriceTextWidth + percentWidth , crossBean.y, getWidth() - priceWidth, crossBean.y, p);
        //竖线
        canvas.drawLine(crossBean.x + mLeftPriceTextWidth, 30, crossBean.x + mLeftPriceTextWidth, yHeight, p);
        //2, 写时间
        drawTimeTextWithRect(canvas, crossBean.x, crossBean.getTime(), p);
        //1, 写价格
        drawPriceTextWithRect(canvas, crossBean.y, drawPriceStr, p);
        //3，写百分比
        drawPercentTextWithRect(canvas, crossBean.y, crossBean.percent, p);
        p.reset();
    }

    /**
     * 写文字，并且为文字带上背景，等于在文字后方画上一个Rect
     */
    private void drawPriceTextWithRect(Canvas canvas, float y, String text, Paint p) {
        p.setTextSize(textSize);
        p.setColor(textColor);
        float textWidth = MinuteHourLineUtils.getTextWidth(p, text) + 10;
        Paint rp = new Paint();
        rp.setColor(backGroundColor);
        rp.setStyle(Paint.Style.FILL);
        rp.setStrokeWidth(strokeWidth);

        float textHeight = MinuteHourLineUtils.getFontHeight(textSize);
        float startY = y - textHeight / 2;
        float endY = y + textHeight / 2;
        if (startY < 0) {
            startY = 0f;
            endY = startY + textHeight;
        } else if (endY > getHeight()) {
            endY = getHeight();
            startY = endY - textHeight;
        }
        canvas.drawRect(getWidth() - textWidth, startY, getWidth() - 1f, endY, rp);
        rp.setColor(Color.BLACK);
        rp.setStyle(Paint.Style.STROKE);
        //2，再画黑框
        canvas.drawRect(getWidth() - textWidth, startY, getWidth() - 1f, endY, rp);
        p.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(text, getWidth() - 5f, endY - textHeight * 1 / 4, p);
    }

    /**
     * 写时间，并且带框
     */
    private void drawTimeTextWithRect(Canvas canvas, float x, String time, Paint p) {
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(textSize);
        p.setColor(textColor);
        float textWidth = MinuteHourLineUtils.getTextWidth(p, time) + 20;
        float y = 0;
        Paint rp = new Paint();
        rp.setColor(backGroundColor);
        rp.setStyle(Paint.Style.FILL);
        rp.setStrokeWidth(strokeWidth);
        //1,先画白底
        mTimeStartX = x - textWidth / 2;
        mTimeEndX = x + textWidth / 2;
        if (mTimeStartX < 0) {
            mTimeStartX = 0.8f;
            mTimeEndX = mTimeStartX + textWidth;
        }
        if (mTimeEndX > (getWidth() - mLeftPriceTextWidth)) {
            mTimeEndX = (getWidth() - mLeftPriceTextWidth) - 0.8f;
            mTimeStartX = mTimeEndX - textWidth;
        }
        mTimeTextHeight = MinuteHourLineUtils.getFontHeight(textSize);
        canvas.drawRect(mLeftPriceTextWidth + mTimeStartX, y, mLeftPriceTextWidth + mTimeEndX, y + mTimeTextHeight, rp);
        rp.setColor(Color.BLACK);
        rp.setStyle(Paint.Style.STROKE);
        //2，再画黑框
        canvas.drawRect(mLeftPriceTextWidth + mTimeStartX, y, mLeftPriceTextWidth + mTimeEndX, y + mTimeTextHeight, rp);
        //3，写文字
        canvas.drawText(time, mLeftPriceTextWidth + mTimeStartX + 10, y + mTimeTextHeight * 3 / 4, p);
    }

    private void drawPercentTextWithRect(Canvas canvas, float y, double percent, Paint p) {
        p.setTextSize(textSize);
        p.setColor(textColor);
        String text = MinuteHourLineUtils.getPercentString(percent);
        float textWidth = MinuteHourLineUtils.getTextWidth(p, text) + 10;
        Paint rp = new Paint();
        rp.setColor(backGroundColor);
        rp.setStyle(Paint.Style.FILL);
        rp.setStrokeWidth(strokeWidth);
        float textHeight = MinuteHourLineUtils.getFontHeight(textSize);

        float startY = y - textHeight / 2;
        float endY = y + textHeight / 2;
        if (startY < 0) {
            startY = 0f;
            endY = startY + textHeight;
        } else if (endY > getHeight()) {
            endY = getHeight();
            startY = endY - textHeight;
        }
        if (mTimeStartX < textWidth + mLeftPriceTextWidth) {
            if (startY < mTimeTextHeight) {
                startY = mTimeTextHeight ;
                endY = startY + textHeight;
            }
        }
        canvas.drawRect(mLeftPriceTextWidth, startY, textWidth + mLeftPriceTextWidth, endY, rp);
        rp.setColor(frameColor);
        rp.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mLeftPriceTextWidth, startY, textWidth + mLeftPriceTextWidth, endY, rp);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, 5f + mLeftPriceTextWidth, endY - 6f, p);
    }

    public void setTypeCode(String typeCode) {
        this.mTypeCode = typeCode;
    }

    private boolean isHGNI() {
        return !TextUtils.isEmpty(mTypeCode) && TextUtils.equals("HGNI", mTypeCode);
    }

}
