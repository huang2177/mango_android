package com.paizhong.manggo.ui.kchart.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.view.View;


import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.utils.MinuteHourLineUtils;
import com.paizhong.manggo.ui.kchart.utils.OnGetLocationListener;
import com.paizhong.manggo.utils.DeviceUtils;

import java.util.List;

/**
 * 闪电view
 */
public class LightningView extends View {
    private OnGetLocationListener mOnGetLocationListener;

    public void setOnGetLocationListener(OnGetLocationListener onGetLocationListener) {
        mOnGetLocationListener = onGetLocationListener;
    }

    //总宽
    private float mWidth;
    //总高
    private float mHeight;

    //排除时间的控件高度
    private float mExcludeTimeHeight;

    //闪电图数据
    private List<Float> mPrices;

    //闪电图时间数据
    private List<String> mTimes;
    //闪电线昨收
    private double yesterdayClose;
    //x轴每两点距离
    private float xUnit;
    //y轴每两点距离
    private float yUnit;

    //闪电图与时间的偏移量
    public static final float OFFSET = 28f;

    //左边价格字体宽度
    private float mLeftPriceTextWidth;
    //字体与轴线的偏移量
    private static final float TEXTOFFSET = 5f;

    //边缘字体大小
    private float mFrameTextSize;

    //商品类型
    private String mGoodsType;

    private float frac;

    private float castoffPrice;

    private boolean isFirst = false;
    //显示Y轴的价格数量
    private int mYNum = 13;
    //显示X轴的网格数量
    private int mXNum = 25;

    private float mPrePrice;

    public void setYNum(int YNum) {
        mYNum = YNum;
    }

    public void setXNum(int XNum) {
        mXNum = XNum;
    }

    public void updateStyle(boolean mYeSyle){
        if (getContext() !=null){
            setColorStyle(getContext(),mYeSyle);
            postInvalidate();
        }
    }

    private int bgColor;
    private int solidLineColor; //实线色值
    private int dottedLineColor;//横虚线色值
    private int zheLineColor;//折线颜色
    private void setColorStyle(Context context ,boolean mYeSyle){
        this.bgColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_2C3C53) : ContextCompat.getColor(getContext(), R.color.color_ffffff);
        this.solidLineColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_A4A5A6) : ContextCompat.getColor(context, R.color.color_dcdcdc);
        this.dottedLineColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_A4A5A6) : ContextCompat.getColor(context, R.color.color_d8d7d7);
        this.zheLineColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_ffffff) : ContextCompat.getColor(context, R.color.color_00A3CC);
    }

    public LightningView(Context context) {
        super(context);
        setColorStyle(context,AppApplication.getConfig().getYeStyle());
        //关闭硬件加速，不然虚线显示为实线了
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    //刷新闪电图
    public void setDataAndInvalidate(List<Float> prices, String typeCode/*, List<String> times*/, double yc) {
        this.mPrices = prices;
        this.yesterdayClose = yc;
//        this.mTimes = times;
        this.mGoodsType = typeCode;
        frac = 1;
        postInvalidate();
    }

    public void insertPrice(String price/*, String time*/) {
        if (mPrices == null || mPrices.size() <= 0) return;
        float inPrice;
        try {
            inPrice = Float.valueOf(price);
        } catch (Exception e) {
            return;
        }

        if (mPrices.size() > 0) {
            mPrePrice = mPrices.get(mPrices.size() - 1);
            castoffPrice = mPrices.get(0);
            mPrices.remove(0);
//            mTimes.remove(0);
            mPrices.add(inPrice);
//            mTimes.add(time);
        }
        postInvalidate();
        frac = 0;
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frac = animation.getAnimatedFraction();
                postInvalidate();
            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1,初始化需要的数据
        initWidthAndHeight();
        //2，画网格
        drawGrid(canvas);

        drawLine(canvas);

        drawNewPriceLine(canvas);

        drawCircle(canvas);

        drawYPrice(canvas);
        //6, 画文字
//        drawTimeText(canvas);
    }

    private void initWidthAndHeight() {
        mHeight = getMeasuredHeight();
        mExcludeTimeHeight = mHeight/* - OFFSET*/;
        mWidth = getMeasuredWidth();
        init();
    }

    private void init() {
        if (mPrices == null || mPrices.size() <=0) {
            return;
        }
        mFrameTextSize = getResources().getDimension(R.dimen.dimen_10sp);
        String measureTextStr = "0000.000";

        measureTextStr = KLineUtils.getIntegerStr(mPrices.get(0));
        mLeftPriceTextWidth = MinuteHourLineUtils.getPaintWidth(mFrameTextSize, measureTextStr) + 8.5f;
        xUnit = mWidth / (mXNum - 1);
        yUnit = mExcludeTimeHeight / (mYNum - 1);
        if (!isFirst) {
            if (mOnGetLocationListener != null) {
                mOnGetLocationListener.onGetLocation(getCircleX(), getCircleY());
                isFirst = true;
            }
        }
    }

    /**
     * 闪电图网格
     *
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {
        if (canvas == null) return;
        if (mPrices == null || mPrices.size() <= 0) return;
        //int solidLineColor = Color.parseColor("#dcdcdc");//实线色值
        //int dottedLineColor = Color.parseColor("#d8d7d7");//横虚线色值
        canvas.drawColor(bgColor);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setPathEffect(new DashPathEffect(
                new float[]{8, 8, 8, 8}, 1));
        p.setAntiAlias(true);
        p.setColor(dottedLineColor);
        float radix = 1f;

        if (mPrices.size() <= 1){
            return;
        }
        float priceLast = mPrices.get(mPrices.size() - 1);
        float pricePre = mPrices.get(mPrices.size() - 2);
        float changeY = (priceLast - pricePre) * yUnit * frac / radix;
        float changeX = xUnit * frac;
        int gridNum = Math.round(priceLast - pricePre);
        //横虚线
        for (int i = 0; i < (mYNum - 2 + Math.abs(gridNum)); i++) {
            if (priceLast > pricePre) {
                canvas.drawLine(0, yUnit * (i + 1 - gridNum) + changeY, mWidth, yUnit * (i + 1 - gridNum) + changeY, p);
            } else {
                canvas.drawLine(0, yUnit * (i + 1) + changeY, mWidth, yUnit * (i + 1) + changeY, p);
            }
        }
        //竖虚线
        for (int i = 0; i < mXNum - 1; i++) {
            canvas.drawLine(xUnit * (i + 1) - changeX, 0, xUnit * (i + 1) - changeX, mExcludeTimeHeight, p);
        }
        p.reset();
        p.setColor(solidLineColor);
        //四周线
        //下
        canvas.drawLine(0, mExcludeTimeHeight - 2, mWidth, mExcludeTimeHeight - 2, p);
        //上
        canvas.drawLine(0, 0, mWidth, 0, p);
        //右
        canvas.drawLine(mWidth - 1, 0, mWidth - 1, mExcludeTimeHeight, p);
        //左
        canvas.drawLine(0, 0, 0, mExcludeTimeHeight, p);
        p.reset();
    }

    /**
     * 最新价格指示线
     *
     * @param canvas
     */
    private void drawNewPriceLine(Canvas canvas) {
        if (canvas == null) return;
        if (mPrices == null || mPrices.size() <= 0) return;
        float newPrice = mPrices.get(mPrices.size() - 1);
        int lineColor = Color.parseColor("#FF5376");//实线色值
        if (newPrice < yesterdayClose) {
            lineColor = Color.parseColor("#00CE64");//实线色值
        }
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setColor(lineColor);
        p.setStrokeWidth(DeviceUtils.dip2px(getContext(), 0.5f));//线条粗细
        canvas.drawLine(0, yUnit * (mYNum - 1) / 2, mWidth, yUnit * (mYNum - 1) / 2, p);


        p.setTextSize(getResources().getDimension(R.dimen.dimen_12sp));
        String newPriceStr = KLineUtils.getIntegerStr(newPrice);
        float textWidth = MinuteHourLineUtils.getTextWidth(p, newPriceStr);
        float fontHeight = MinuteHourLineUtils.getFontHeight(getResources().getDimension(R.dimen.dimen_12sp));


        p.setColor(lineColor);
        Path path = new Path();
        path.moveTo(mWidth - textWidth - 5f - fontHeight, yUnit * (mYNum - 1) / 2);
        path.lineTo(mWidth - textWidth - fontHeight, yUnit * (mYNum - 1) / 2 - fontHeight / 2);
        path.lineTo(mWidth, yUnit * (mYNum - 1) / 2 - fontHeight / 2);
        path.lineTo(mWidth, yUnit * (mYNum - 1) / 2 + fontHeight / 2);
        path.lineTo(mWidth - textWidth - fontHeight, yUnit * (mYNum - 1) / 2 + fontHeight / 2);
        path.lineTo(mWidth - textWidth - 5f - fontHeight, yUnit * (mYNum - 1) / 2);
        canvas.drawPath(path, p);

        p.setColor(Color.WHITE);
        canvas.drawText(newPriceStr, mWidth - textWidth - fontHeight / 2, yUnit * (mYNum - 1) / 2 + fontHeight / 4 + 1f, p);
    }

    private float startX;
    private float startY;

    /**
     * 闪电图：折线
     *
     * @param canvas
     */
    private void drawLine(final Canvas canvas) {
        if (canvas == null) return;
        if (mPrices == null || mPrices.size() <= 1) {
            return;
        }

        //int color = Color.parseColor("#00A3CC");//线条色值
        final float fineness = DeviceUtils.dip2px(getContext(), 1.5f);//线条细度
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(zheLineColor);
        paint.setStrokeWidth(fineness);
        int count = mPrices.size();
        float radix = 1f;

        //if (GoodsType.HGNI.equals(mGoodsType)) radix = 0.01f;

        for (int i = count - 1; i >= 1; i--) {
            float price = mPrices.get(i);
            float pricePre = mPrices.get(i - 1);
            startX = (i == count - 1) ? xUnit * (mPrices.size() - 1) : startX;
            startY = (i == count - 1) ? yUnit * (mYNum - 1) / 2 : startY;
            float endX;
            float endY;
            if (i == count - 1) {
                endX = startX - xUnit * frac;
                endY = startY + (price - pricePre) / radix * yUnit * frac;
            } else {
                endX = startX - xUnit;
                endY = startY + (price - pricePre) / radix * yUnit;
            }
            canvas.drawLine(startX, startY, endX, endY, paint);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            startY = endY;
            startX = endX;
        }
        if (castoffPrice != 0)
            canvas.drawLine(startX - xUnit * (1 - frac), startY + (mPrices.get(0) - castoffPrice) / radix * yUnit * (1 - frac), startX, startY, paint);
        canvas.restore();
    }

    //画圆
    private void drawCircle(Canvas canvas) {
        if (mPrices == null || mPrices.size() <= 0) return;
        int color = Color.parseColor("#00A3CC");//线条色值
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float fineness = DeviceUtils.dip2px(getContext(), 1f);//线条细度
        float radius = DeviceUtils.dip2px(getContext(), 4f);
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(fineness);
        paint.setStyle(Paint.Style.FILL);
        float x = xUnit * (mPrices.size() - 1);
        float y = yUnit * (mYNum - 1) / 2;
        canvas.drawCircle(x, y, radius, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius / 2, paint);
    }

    /**
     * 闪电图图：X轴时间
     *
     * @param canvas
     */
    private void drawTimeText(Canvas canvas) {
        if (canvas == null) return;
        if (mPrices == null || mPrices.size() == 0) {
            return;
        }
        try {
            int timeTextColor = Color.parseColor("#B2B2B2");//时间字体颜色
            //左边日期
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setTextSize(mFrameTextSize);
            p.setColor(timeTextColor);
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(mTimes.get(0), 0 + mLeftPriceTextWidth, mHeight - 2, p);
            for (int i = 0; i < 5; i++) {
                p.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(mTimes.get(i + 1), (mWidth - mLeftPriceTextWidth) * (i + 1) / 6 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, mTimes.get(i + 1)) / 2 + mLeftPriceTextWidth, mHeight - 2, p);
            }

            //右边日期
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(mTimes.get(mTimes.size() - 1), mWidth, mHeight - 2, p);
        } catch (Exception e) {
        }
    }

    //闪电图：y轴价格
    private void drawYPrice(Canvas canvas) {
        if (canvas == null) return;
        if (mPrices == null || mPrices.size() == 0) return;
        float radix = 1f;
        try {
            int priceTextColor = Color.parseColor("#00CE64");
            if (mPrices.get(mPrices.size() - 1) + (mYNum - 1) / 2 > yesterdayClose)
                priceTextColor = Color.parseColor("#FF5376");
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setTextSize(getResources().getDimension(R.dimen.dimen_9sp));
            p.setColor(priceTextColor);
            p.setTextAlign(Paint.Align.LEFT);
            float newPrice = mPrices.get(mPrices.size() - 1);
            //float changeY = (newPrice - mPrePrice) * yUnit * frac;
            //int gridNum = Math.round(newPrice - mPrePrice);
            float halfSizeHeight = (Math.abs(p.ascent()) - p.descent()) / 2;
            //最高价格

            String price = KLineUtils.getIntegerStr(newPrice + ((mYNum - 1) / 2) * radix);
            float textWidth = MinuteHourLineUtils.getTextWidth(p, price);
            canvas.drawText(price, mWidth - textWidth - TEXTOFFSET, 4f + halfSizeHeight * 2, p);

            //中间价格
            for (int i = 0; i < mYNum - 2; i++) {
                float price_num = newPrice + ((mYNum - 3) / 2 - i) * radix;
                if (price_num >= yesterdayClose) {
                    p.setColor(Color.parseColor("#FF5376"));
                } else {
                    p.setColor(Color.parseColor("#00CE64"));
                }

                if (i != (mYNum - 3) / 2) {
                    p.setTextAlign(Paint.Align.LEFT);
                        float textWidth_minute = MinuteHourLineUtils.getTextWidth(p, KLineUtils.getIntegerStr(price_num));
                        canvas.drawText(KLineUtils.getIntegerStr(price_num), mWidth - textWidth_minute - TEXTOFFSET, mExcludeTimeHeight * (i + 1) / (mYNum - 1) + halfSizeHeight, p);
                }
            }
            //最低价格
            String price_min = KLineUtils.getIntegerStr(newPrice - ((mYNum - 1) / 2) * radix);
            float textWidth_min = MinuteHourLineUtils.getTextWidth(p, price_min);
            canvas.drawText(price_min, mWidth - textWidth_min - TEXTOFFSET, mExcludeTimeHeight - 4f, p);
        } catch (Exception e) {

        }
    }

    public float getExcludeTimeHeight() {
        return mExcludeTimeHeight - TEXTOFFSET;
    }

    public float getTrueWidth() {
        return mWidth - (mLeftPriceTextWidth + TEXTOFFSET);
    }

    public float getxUnit() {
        return xUnit;
    }

    public float getmLeftPriceTextWidth() {
        return mLeftPriceTextWidth + TEXTOFFSET;
    }

    public float getyUnit() {
        return yUnit;
    }

    public float getCircleX() {
        return xUnit * (mPrices.size() - 1);
    }

    public float getCircleY() {
        return yUnit * (mYNum - 1) / 2;
    }
}
