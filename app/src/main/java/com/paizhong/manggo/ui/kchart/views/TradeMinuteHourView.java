package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.bean.CrossBean;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineChartController;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.utils.MinuteHourLineUtils;
import com.paizhong.manggo.ui.trade.TradeFragment;
import com.paizhong.manggo.utils.DeviceUtils;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 分时view
 */
public class TradeMinuteHourView extends View implements OnMoveListener {
    //总宽
    private float mWidth;
    //总高
    private float mHeight;

    //排除时间的控件高度
    private float mExcludeTimeHeight;

    //分时图数据
    private ArrayList<MinuteHourBean> mMinuteHourBeans;

    //分时图时间数据
    private ArrayList<String> mMinuteTimes;

    //Y轴最高值
    private double yMax;
    //Y轴最低值
    private double yMin;
    //分时线昨收
    private double yesterdayClose;
    //x轴每两点距离
    private float xUnit;
    //所有价格
    private float[] prices;

    //所有均线数据
    private float[] averages;

    //分时图与时间的偏移量
    public static final float OFFSET = 28f;

    //十字线布局
    private CrossView crossView;

    //行情气泡布局
    private PopupView mPopupView;

    //是否提供外界触摸事件(同时首页逻辑也用到此参数)
    private boolean isTouchEnabled;

    //最新价格横线
    private boolean isNewLinePrice = false;

    //左边价格字体宽度
    private float mLeftPriceTextWidth;
    //字体与轴线的偏移量
    private static final float TEXTOFFSET = 5f;

    //边缘字体大小
    private float mFrameTextSize;

    //商品类型
    private String mTypeCode;

    //最高点坐标
    private float[] mMaxRectPoint = new float[2];

    //最低点坐标
    private float[] mMinRectPoint = new float[2];

    //最高价格
    private float mMaxPrice;

    //最低价格
    private float mMinPrice;

    //行情气泡是否已展示
    private boolean mIsShow = true;

    public void setTouchEnabled(boolean touchEnabled) {
        isTouchEnabled = touchEnabled;
    }

    private int bgColor;
    private int solidLineColor;//实线
    private int dottedLineColor;//横虚线色值
    private int priceLine; //均线颜色
    private int paintColor;//阴影颜色
    private void setColorStyle(Context context){
        this.bgColor =  ContextCompat.getColor(getContext(), R.color.color_ffffff);
        this.solidLineColor =  ContextCompat.getColor(context, R.color.color_dcdcdc);
        this.dottedLineColor = ContextCompat.getColor(context, R.color.color_d8d7d7);
        this.priceLine = ContextCompat.getColor(context, R.color.color_00A3CC);
        this.paintColor =  ContextCompat.getColor(context, R.color.color_C2EEED);
    }

    public TradeMinuteHourView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorStyle(context);
        //关闭硬件加速，不然虚线显示为实线了
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.MinuteHourView);
        if (typedArray != null) {
            isTouchEnabled = typedArray.getBoolean(R.styleable.MinuteHourView_isTouchEnabled, false);
            isNewLinePrice = typedArray.getBoolean(R.styleable.MinuteHourView_isNewLinePrice,false);
            typedArray.recycle();
        }
    }

    //刷新分时图
    public void setDataAndInvalidate(ArrayList<MinuteHourBean> minuteHourBeans, String typeCode, ArrayList<String> minuteTimes, double yc) {
        if (minuteHourBeans != null && minuteHourBeans.size() > 0) {
            this.mMinuteHourBeans = minuteHourBeans;
            this.yesterdayClose = yc;
            this.mMinuteTimes = minuteTimes;
            this.mTypeCode = typeCode;
            postInvalidate();
        }
    }

    public void setCrossView(CrossView crossView, String typeCode) {
        this.crossView = crossView;
        //crossView.setTypeCode(typeCode);
        crossView.setOnMoveListener(this);
    }


    public void setPopupView(PopupView popupView) {
        this.mPopupView = popupView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1,初始化需要的数据
        initWidthAndHeight();
        //2，画网格
        drawGrid(canvas);
        //3,画阴影
        drawLinearFill(canvas);
        //4,均线
        drawAverageLine(canvas);
        //5，画价格线
        drawPriceLine(canvas);
        //6, 画文字
        drawText(canvas);

        if (isNewLinePrice){
            drawNewPriceLine(canvas);
        }
    }

    private void drawLinearFill(Canvas canvas) {
        if (canvas == null) return;
        if (mMinuteHourBeans == null || mMinuteHourBeans.size() <= 0) {
            return;
        }
        //int paintColor = Color.parseColor("#C2EEED");
        int color = (125 << 24) | (paintColor & 0xffffff);

        Path filled = new Path();
        //filled.moveTo(mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET);
        filled.moveTo(1, mExcludeTimeHeight - TEXTOFFSET);
        //filled.lineTo(mLeftPriceTextWidth + TEXTOFFSET, (float) getY(mMinuteHourBeans.get(0).getPrice()));
        filled.lineTo(1, (float) getY(mMinuteHourBeans.get(0).getPrice()));

        for (int i = 0; i < mMinuteHourBeans.size(); i++) {
            //filled.lineTo(i * xUnit + mLeftPriceTextWidth + TEXTOFFSET, (float) getY(mMinuteHourBeans.get(i).getPrice()));
            filled.lineTo(i * xUnit, (float) getY(mMinuteHourBeans.get(i).getPrice()));
        }
        //filled.lineTo(mMinuteHourBeans.size() * xUnit + mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET);
        filled.lineTo(mMinuteHourBeans.size() * xUnit + 1, mExcludeTimeHeight - TEXTOFFSET);
        filled.close();
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        if (Build.VERSION.SDK_INT >= 18) {
            canvas.save();
            canvas.clipPath(filled);
            canvas.drawColor(color);
            canvas.restore();
        } else {
            // save
            Paint.Style previous = p.getStyle();
            int previousColor = p.getColor();
            // set
            p.setStyle(Paint.Style.FILL);
            p.setColor(color);
            canvas.drawPath(filled, p);
            // restore
            p.setColor(previousColor);
            p.setStyle(previous);
        }
    }

    private void initWidthAndHeight() {
        mHeight = getMeasuredHeight();
        mExcludeTimeHeight = mHeight - OFFSET;
        mWidth = getMeasuredWidth();
        init();
    }

    private void init() {
        if (mMinuteHourBeans == null || mMinuteTimes == null) {
            return;
        }
        mFrameTextSize = getResources().getDimension(R.dimen.dimen_10sp);
        String measureTextStr = "0000.000";

        if (mMinuteHourBeans.size() > 0) {
            if (!TextUtils.isEmpty(mTypeCode) && TextUtils.equals("HGNI", mTypeCode)) {
                measureTextStr = KLineUtils.getFloatStr2(mMinuteHourBeans.get(0).getPrice());
            } else {
                measureTextStr = KLineUtils.getIntegerStr(mMinuteHourBeans.get(0).getPrice());
            }
        }
        mLeftPriceTextWidth = MinuteHourLineUtils.getPaintWidth(mFrameTextSize, measureTextStr) + 8.5f;
        if (crossView != null) {
            //crossView.setLeftPriceTextWidth(mLeftPriceTextWidth + TEXTOFFSET);
            crossView.setLeftPriceTextWidth(1);
        }
        xUnit = (mWidth - mLeftPriceTextWidth - TEXTOFFSET) / (float) mMinuteTimes.size();
        //计算最大最小值
        prices = new float[mMinuteHourBeans.size()];
        averages = new float[mMinuteHourBeans.size()];
        for (int i = 0; i < mMinuteHourBeans.size(); i++) {
            prices[i] = (float) mMinuteHourBeans.get(i).getPrice();
            averages[i] = (float) mMinuteHourBeans.get(i).average;
            if (i == 0) {
                yMax = mMinuteHourBeans.get(i).getPrice();
                yMin = mMinuteHourBeans.get(i).getPrice();
            }
            yMax = mMinuteHourBeans.get(i).getPrice() > yMax ? mMinuteHourBeans.get(i).getPrice() : yMax;
            if (mMinuteHourBeans.get(i).getPrice() != 0)
                yMin = mMinuteHourBeans.get(i).getPrice() < yMin ? mMinuteHourBeans.get(i).getPrice() : yMin;
        }
        //updatePopWinLocation();
    }





    private float newLinePrice = 0;
    private float changeValue;
    private String mTimeStr;

    public void setNewLinePrice(float newLinePrice,float changeValue ,String mTimeStr){
        this.newLinePrice = newLinePrice;
        this.changeValue = changeValue;
        this.mTimeStr = mTimeStr;
    }

    public float getNewLinePrice(){
        return newLinePrice;
    }

    public String getTimeStr(){
        return mTimeStr;
    }

    private void drawNewPriceLine(Canvas canvas) {
        if (canvas == null) return;
        if (newLinePrice == 0 ){
            return;
        }
        float yUnit = (float) getY(newLinePrice);

        int lineColor = Color.parseColor("#FF5376");//实线色值
        if (changeValue < 0) {
            lineColor = Color.parseColor("#00CE64");//实线色值
        }
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setColor(lineColor);
        p.setStrokeWidth(DeviceUtils.dip2px(getContext(), 0.5f));//线条粗细
        p.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        canvas.drawLine(0, yUnit, mWidth, yUnit, p);


        p.setTextSize(getResources().getDimension(R.dimen.dimen_12sp));
        String newPriceStr = KLineUtils.getIntegerStr(newLinePrice);
        float textWidth = MinuteHourLineUtils.getTextWidth(p, newPriceStr);
        float fontHeight = MinuteHourLineUtils.getFontHeight(getResources().getDimension(R.dimen.dimen_12sp));


        p.setColor(lineColor);
        Path path = new Path();
        //path.moveTo(mWidth - textWidth - 5f - fontHeight, yUnit);
        path.moveTo(mWidth - textWidth, yUnit);

        //path.lineTo(mWidth - textWidth - fontHeight, yUnit  - fontHeight / 2);
        path.lineTo(mWidth - textWidth, yUnit  - fontHeight / 2);

        path.lineTo(mWidth, yUnit  - fontHeight / 2);
        path.lineTo(mWidth, yUnit  + fontHeight / 2);

        //path.lineTo(mWidth - textWidth - fontHeight, yUnit  + fontHeight / 2);
        path.lineTo(mWidth - textWidth , yUnit  + fontHeight / 2);

        //path.lineTo(mWidth - textWidth - 5f - fontHeight, yUnit);
        path.lineTo(mWidth - textWidth , yUnit);
        canvas.drawPath(path, p);
        p.setColor(Color.WHITE);
        //canvas.drawText(newPriceStr, mWidth - textWidth - fontHeight / 2, yUnit  + fontHeight / 4 + 1f, p);
        canvas.drawText(newPriceStr, mWidth - textWidth, yUnit  + fontHeight / 4 + 1f, p);
    }


    private void drawPopupViewTinyCircle(Canvas canvas, float centerX, float centerY) {
        if (canvas == null) return;
        int innerColor = Color.parseColor("#FEBC18");
        int outerColor = Color.parseColor("#FFEAB3");
        int innerRadius = DeviceUtils.dip2px(getContext(), 4.f);
        int outerRadius = DeviceUtils.dip2px(getContext(), 6.f);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(outerColor);
        canvas.drawCircle(centerX, centerY, outerRadius, paint);

        paint.setColor(innerColor);
        canvas.drawCircle(centerX, centerY, innerRadius, paint);

        paint.reset();
    }

    private void drawGrid(Canvas canvas) {
        if (canvas == null) return;
        if (mMinuteHourBeans == null || mMinuteHourBeans.size() <= 0) return;
        //int solidLineColor = Color.parseColor(solidLineColor);//实线色值
        //int dottedLineColor = Color.parseColor("#d8d7d7");//横虚线色值
        canvas.drawColor(bgColor);
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setPathEffect(new DashPathEffect(
                new float[]{8, 8, 8, 8}, 1));
        p.setAntiAlias(true);
        p.setColor(dottedLineColor);
        //中间横虚线
        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, (mExcludeTimeHeight - TEXTOFFSET) * 2 / 4, mWidth, (mExcludeTimeHeight - TEXTOFFSET) * 2 / 4, p);
        canvas.drawLine(0, (mExcludeTimeHeight - TEXTOFFSET) * 2 / 4, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), (mExcludeTimeHeight - TEXTOFFSET) * 2 / 4, p);

        //中间竖虚线
        //canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 2 / 4 + mLeftPriceTextWidth + TEXTOFFSET, 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 2 / 4 + mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 2 / 4, 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 2 / 4 , mExcludeTimeHeight - TEXTOFFSET, p);
        //横虚线
        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4, mWidth, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4, p);
        canvas.drawLine(0, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4, p);

        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4, mWidth, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4, p);
        canvas.drawLine(0, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4, p);

        //竖虚线
        //canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 3 / 4 + mLeftPriceTextWidth + TEXTOFFSET, 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 3 / 4 + mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 3 / 4 , 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 3 / 4 , mExcludeTimeHeight - TEXTOFFSET, p);

        //canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 1 / 4 + mLeftPriceTextWidth + TEXTOFFSET, 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 1 / 4 + mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine((mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 1 / 4 , 0, (mWidth - mLeftPriceTextWidth - TEXTOFFSET) * 1 / 4 , mExcludeTimeHeight - TEXTOFFSET, p);

        p.reset();
        p.setColor(solidLineColor);
        //四周线
        //下
        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET, mWidth, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine(0, mExcludeTimeHeight - TEXTOFFSET, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), mExcludeTimeHeight - TEXTOFFSET, p);
        //上
        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, 0, mWidth, 0, p);
        canvas.drawLine(0, 0, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), 0, p);
        //右
        //canvas.drawLine(mWidth - 1, 0, mWidth - 1, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine(mWidth - (mLeftPriceTextWidth + TEXTOFFSET), 0, mWidth - (mLeftPriceTextWidth + TEXTOFFSET), mExcludeTimeHeight - TEXTOFFSET, p);
        //左
        //canvas.drawLine(mLeftPriceTextWidth + TEXTOFFSET, 0, mLeftPriceTextWidth + TEXTOFFSET, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawLine(1, 0, 1, mExcludeTimeHeight - TEXTOFFSET, p);
        p.reset();
    }


    private void drawAverageLine(Canvas canvas) {
        int color = Color.parseColor("#FFBE00");//均线色值
        if (canvas == null) return;
        if (averages == null || averages.length == 0) return;
        float[] maxAndMinAverages = MinuteHourLineUtils.getMaxAndMin(averages);
        if (maxAndMinAverages[0] == 0.01 && maxAndMinAverages[1] == 0.01)
            return;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setColor(color);
        p.setStrokeWidth(1.2f);
        double[] maxAndMin = MinuteHourLineUtils.getMaxAndMinByYd(yMax, yMin, yesterdayClose);
//        canvas.drawLines(getLines(averages, xUnit, mExcludeTimeHeight - TEXTOFFSET, (float) maxAndMin[0], (float) maxAndMin[1],
//                false, 0, mLeftPriceTextWidth + TEXTOFFSET, false), p);
        canvas.drawLines(getLines(averages, xUnit, mExcludeTimeHeight - TEXTOFFSET, (float) maxAndMin[0], (float) maxAndMin[1],
                false, 0, 1, false), p);

        p.reset();
    }


    private void drawPriceLine(Canvas canvas) {
        //int color = Color.parseColor("#00A3CC");//分时线色值
        if (canvas == null) return;
        if (prices == null || prices.length == 0) return;
        double[] maxAndMin = MinuteHourLineUtils.getMaxAndMinByYd(yMax, yMin, yesterdayClose);
        float tmax = 0f;
        float tmin = 0f;

        if (prices != null) {
            for (float f : prices) {
                tmax = tmax > f ? tmax : f;
                tmin = tmin < f ? tmin : f;
            }
        }
        //如果数组中值全为0，则不画该线
        if (tmax == 0 && tmin == 0)
            return;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setColor(priceLine);
        p.setStrokeWidth(2.0f);
//        canvas.drawLines(getLines(prices, xUnit, mExcludeTimeHeight - TEXTOFFSET, (float) maxAndMin[0], (float) maxAndMin[1],
//                false, 0, mLeftPriceTextWidth + TEXTOFFSET, true), p);

        canvas.drawLines(getLines(prices, xUnit, mExcludeTimeHeight - TEXTOFFSET, (float) maxAndMin[0], (float) maxAndMin[1],
                false, 0, 1, true), p);
        p.reset();
        //进入分时图详情才展示
        if (!isTouchEnabled) {
            drawMaxAndMinPriceRect(canvas);
        }
    }

    private void drawMaxAndMinPriceRect(Canvas canvas) {
        int colorMax = Color.parseColor("#FF5376");//最高价格点折线颜色值
        int colorMin = Color.parseColor("#37CD80");//最低价格点折线颜色值

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setColor(colorMax);
        p.setStrokeWidth(2.0f);

        float rectWidth = 100.f;
        float rectHeight = 40.f;
        float rectRadius = 5.f;
        float maxPointX = mMaxRectPoint[0];
        float maxPointY = mMaxRectPoint[1];
        float minPointX = mMinRectPoint[0];
        float minPointY = mMinRectPoint[1];
        float bottomY = mExcludeTimeHeight - TEXTOFFSET;//底部Y坐标

        float[] maxLines;
        RectF rMax = new RectF();
        if (maxPointY < 100.f) {
            if (mWidth - maxPointX < 200.f) {
                maxLines = new float[]{maxPointX, maxPointY, maxPointX - 80, maxPointY};//横线坐标
                rMax.left = maxPointX - 80 - rectWidth;
                rMax.right = maxPointX - 80;
            } else {
                maxLines = new float[]{maxPointX, maxPointY, maxPointX + 80, maxPointY};//横线坐标
                rMax.left = maxPointX + 80;
                rMax.right = maxPointX + 80 + rectWidth;
            }
            rMax.top = maxPointY - rectHeight / 2;
            rMax.bottom = maxPointY + rectHeight / 2;

        } else {
            maxLines = new float[]{maxPointX, maxPointY, maxPointX, maxPointY - 40,//竖线坐标
                    maxPointX, maxPointY - 40, maxPointX + 80, maxPointY - 40};//横线坐标
            rMax.top = maxPointY - 40 - rectHeight / 2;
            rMax.bottom = maxPointY - 40 + rectHeight / 2;
            rMax.left = maxPointX + 80;
            rMax.right = maxPointX + 80 + rectWidth;
        }


        //绘制最高点折线
        canvas.drawLines(maxLines, p);
        //绘制最高点圆角矩形
        canvas.drawRoundRect(rMax, rectRadius, rectRadius, p);

        float[] minLines;
        RectF rMin = new RectF();
        if (bottomY - minPointY < 100.f) {
            if (mWidth - minPointX < 200.f) {
                minLines = new float[]{minPointX, minPointY, minPointX - 80, minPointY};
                rMin.left = minPointX - 80 - rectWidth;
                rMin.right = minPointX - 80;
            } else {
                minLines = new float[]{minPointX, minPointY, minPointX + 80, minPointY};
                rMin.left = minPointX + 80;
                rMin.right = minPointX + 80 + rectWidth;
            }
            rMin.top = minPointY - rectHeight / 2;
            rMin.bottom = minPointY + rectHeight / 2;
        } else {
            minLines = new float[]{minPointX, minPointY, minPointX, minPointY + 40,
                    minPointX, minPointY + 40, minPointX + 80, minPointY + 40};
            rMin.left = minPointX + 80;
            rMin.top = minPointY + 40 - rectHeight / 2;
            rMin.right = minPointX + 80 + rectWidth;
            rMin.bottom = minPointY + 40 + rectHeight / 2;
        }

        p.setColor(colorMin);
        //绘制最低点折线
        canvas.drawLines(minLines, p);
        //绘制最低点圆角矩形
        canvas.drawRoundRect(rMin, rectRadius, rectRadius, p);

        //绘制文本
        p.setColor(Color.WHITE);
        p.setTextSize(mFrameTextSize);
        String newMaxPrice = "0";
        String newMinPrice = "0";
        newMaxPrice = KLineUtils.getIntegerStr(mMaxPrice);
        newMinPrice = KLineUtils.getIntegerStr(mMinPrice);
        float textMaxWidth = MinuteHourLineUtils.getTextWidth(p, newMaxPrice);
        float textMinWidth = MinuteHourLineUtils.getTextWidth(p, newMinPrice);
        float marginTop = MinuteHourLineUtils.getTextStartY(mFrameTextSize, rectHeight);

        if (maxPointY < 100.f) {
            if (mWidth - maxPointX < 200.f) {
                canvas.drawText(newMaxPrice, maxPointX - 80 - rectWidth + (rectWidth - textMaxWidth) / 2, maxPointY + marginTop, p);
            } else {
                canvas.drawText(newMaxPrice, maxPointX + 80 + (rectWidth - textMaxWidth) / 2, maxPointY + marginTop, p);
            }

        } else {
            canvas.drawText(newMaxPrice, maxPointX + 80 + (rectWidth - textMaxWidth) / 2, maxPointY - rectHeight + marginTop, p);
        }

        if (bottomY - minPointY < 100) {
            if (mWidth - minPointX < 200.f) {
                canvas.drawText(newMinPrice, minPointX - 80 - rectWidth + (rectWidth - textMinWidth) / 2, minPointY + marginTop, p);
            } else {
                canvas.drawText(newMinPrice, minPointX + 80 + (rectWidth - textMinWidth) / 2, minPointY + marginTop, p);
            }
        } else {
            canvas.drawText(newMinPrice, minPointX + 80 + (rectWidth - textMinWidth) / 2, minPointY + rectHeight + marginTop, p);
        }
        p.reset();
    }

    /**
     * 传入价格和
     *
     * @param prices   价格
     * @param xUnit    x轴每两点距离
     * @param height   控件高度
     * @param max      价格最大值
     * @param min      价格最小值
     * @param fromZero 是否从0开始，意思是，假如y轴为0是否画出该点（比如在SMA5、SMA10中，并不是由0开始）
     * @return
     */
    private float[] getLines(float[] prices, float xUnit, float height, float max, float min, boolean fromZero, float y, float xOffset, boolean fromPriceLine) {
        float[] result = new float[prices.length * 4];
        float yUnit = (max - min) / height;
        for (int i = 0; i < prices.length - 1; i++) {
            //排除起点为0的点
            if (!fromZero && prices[i] == 0) continue;
            result[i * 4] = xOffset + i * xUnit;
            result[i * 4 + 1] = y + height - (prices[i] - min) / yUnit;

            result[i * 4 + 2] = xOffset + (i + 1) * xUnit;
            result[i * 4 + 3] = y + height - (prices[i + 1] - min) / yUnit;
        }
        if (!isTouchEnabled) {
            if (fromPriceLine) {
                //获取最高价格点坐标
                setMaxPoint(prices, xUnit, height, min, y, xOffset, yUnit);
                //获取最低价格点坐标
                setMinPoint(prices, xUnit, height, min, y, xOffset, yUnit);
            }
        }
        return result;
    }

    public void setMaxPoint(float[] arr, float xUnit, float height, float min, float y, float xOffset, float yUnit) {
        int maxIndex = 0;
        float maxX;
        float maxY;
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > arr[maxIndex]) {
                maxIndex = x;
            }
        }
        maxX = xOffset + maxIndex * xUnit;
        maxY = y + height - (arr[maxIndex] - min) / yUnit;
        mMaxPrice = arr[maxIndex];
        mMaxRectPoint = new float[]{maxX, maxY};

    }

    private void setMinPoint(float[] arr, float xUnit, float height, float min, float y, float xOffset, float yUnit) {
        int minIndex = 0;
        float minX;
        float minY;
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < arr[minIndex]) {
                minIndex = x;
            }
        }
        minX = xOffset + minIndex * xUnit;
        minY = y + height - (arr[minIndex] - min) / yUnit;
        mMinPrice = arr[minIndex];
        mMinRectPoint = new float[]{minX, minY};
    }


    protected void drawText(Canvas canvas) {
        if (mMinuteHourBeans == null || mMinuteTimes == null) {
            return;
        }
        drawYPercentAndPrice(canvas);
        drawXTime(canvas);

        // TODO: 2017/8/2 zq 坐标点暂写死，正式需要接口获取
//        float centerX = 600;
//        float centerY = mExcludeTimeHeight - TEXTOFFSET;
//        //绘制行情气泡对应的时间轴上的小圆
//        drawPopupViewTinyCircle(canvas, centerX, centerY);
    }

    /**
     * 分时线：Y轴涨跌幅
     *
     * @param canvas
     */
    public void drawYPercentAndPrice(Canvas canvas) {
        if (canvas == null) return;
        //计算出最大涨跌幅
        float percentOffset = 8f;

        double[] maxAndMin = MinuteHourLineUtils.getMaxAndMinByYd(yMax, yMin, yesterdayClose);
        double max = maxAndMin[0];
        double min = maxAndMin[1];

        double upPercent = (max - yesterdayClose) / yesterdayClose;
        double downPercent = (min - yesterdayClose) / yesterdayClose;
        double maxPercent = Math.abs(upPercent) > Math.abs(downPercent) ? upPercent : downPercent;
        double halfPercent = maxPercent / 2d;
        //计算出最大价格
        double diff = Math.abs(max - yesterdayClose) > Math.abs(min - yesterdayClose) ?
                Math.abs(max - yesterdayClose) : Math.abs(min - yesterdayClose);
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(4);
        format.setMinimumFractionDigits(0);
        String p1;
        String p2;
        String p3;
        String p4;
        if (isHGNI()) {
            p1 = KLineUtils.makeNum(format.format(yesterdayClose + diff));
            p2 = KLineUtils.makeNum(format.format(yesterdayClose + diff / 2));
            p3 = KLineUtils.makeNum(format.format(yesterdayClose - diff / 2));
            p4 = KLineUtils.makeNum(format.format(yesterdayClose - diff));
        } else {
            p1 = String.valueOf((int) (yesterdayClose + diff));
            p2 = String.valueOf((int) (yesterdayClose + diff / 2));
            p3 = String.valueOf((int) (yesterdayClose - diff / 2));
            p4 = String.valueOf((int) (yesterdayClose - diff));
        }
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(mFrameTextSize);
        p.setTextAlign(Paint.Align.LEFT);
        float halfSizeHeight = (Math.abs(p.ascent()) - p.descent()) / 2;
        //最大涨幅(价格)
        p.setColor(getTextColorAsh(1, 0));
        if (!isTouchEnabled) {
            //canvas.drawText(MinuteHourLineUtils.getPercentString(Math.abs(maxPercent)), mWidth - percentOffset, OFFSET - TEXTOFFSET, p);
            canvas.drawText(MinuteHourLineUtils.getPercentString(Math.abs(maxPercent)), 0, OFFSET - TEXTOFFSET, p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText(p1, 0, OFFSET - TEXTOFFSET, p);
        canvas.drawText(p1, mWidth - mLeftPriceTextWidth, OFFSET - TEXTOFFSET, p);


        //一半涨幅(价格)
        p.setTextAlign(Paint.Align.LEFT);
        if (!isTouchEnabled) {
            //canvas.drawText(MinuteHourLineUtils.getPercentString(Math.abs(halfPercent)), mWidth - percentOffset, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4 + halfSizeHeight, p);
            canvas.drawText(MinuteHourLineUtils.getPercentString(Math.abs(halfPercent)), 0, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4 + halfSizeHeight, p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText(p2, 0, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4 + halfSizeHeight, p);
        canvas.drawText(p2, mWidth - mLeftPriceTextWidth, (mExcludeTimeHeight - TEXTOFFSET) * 1 / 4 + halfSizeHeight, p);

        //中间0%
        p.setColor(getTextColorAsh(0, 0));
        p.setTextAlign(Paint.Align.LEFT);
        if (!isTouchEnabled) {
            //canvas.drawText("0.00%", mWidth - percentOffset, (mExcludeTimeHeight - TEXTOFFSET) / 2 + halfSizeHeight, p);
            canvas.drawText("0.00%", 0, (mExcludeTimeHeight - TEXTOFFSET) / 2 + halfSizeHeight, p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText(isHGNI() ? KLineUtils.makeNum(format.format(yesterdayClose)) : String.valueOf((int) (yesterdayClose)), 0, (mExcludeTimeHeight - TEXTOFFSET) / 2 + halfSizeHeight, p);
        canvas.drawText(isHGNI() ? KLineUtils.makeNum(format.format(yesterdayClose)) : String.valueOf((int) (yesterdayClose)), mWidth - mLeftPriceTextWidth, (mExcludeTimeHeight - TEXTOFFSET) / 2 + halfSizeHeight, p);

        //跌幅一半
        p.setColor(getTextColorAsh(0, 1));
        p.setTextAlign(Paint.Align.LEFT);
        if (!isTouchEnabled) {
            //canvas.drawText("-" + MinuteHourLineUtils.getPercentString(Math.abs(halfPercent)), mWidth - percentOffset, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4 + halfSizeHeight, p);
            canvas.drawText("-" + MinuteHourLineUtils.getPercentString(Math.abs(halfPercent)), 0, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4 + halfSizeHeight, p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText(p3, 0, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4 + halfSizeHeight, p);
        canvas.drawText(p3, mWidth - mLeftPriceTextWidth, (mExcludeTimeHeight - TEXTOFFSET) * 3 / 4 + halfSizeHeight, p);

        //最下面跌幅
        p.setTextAlign(Paint.Align.LEFT);
        if (!isTouchEnabled) {
            //canvas.drawText("-" + MinuteHourLineUtils.getPercentString(Math.abs(maxPercent)), mWidth - percentOffset, mExcludeTimeHeight - TEXTOFFSET, p);
            canvas.drawText("-" + MinuteHourLineUtils.getPercentString(Math.abs(maxPercent)), 0, mExcludeTimeHeight - TEXTOFFSET, p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        //canvas.drawText(p4, 0, mExcludeTimeHeight - TEXTOFFSET, p);
        canvas.drawText(p4, mWidth - mLeftPriceTextWidth, mExcludeTimeHeight - TEXTOFFSET, p);
        p.reset();
    }

    /**
     * 获取价格显示的颜色，curr>change是红，等于是黑，小于是绿
     * 平是灰色
     *
     * @param curr   当前价
     * @param change 变化颜色的价格
     * @return
     */
    public static int getTextColorAsh(double curr, double change) {
        if (curr == change)
            return Color.parseColor("#878787");
        if (curr < change)
            return Color.parseColor("#37CD80");
        return Color.parseColor("#FF5376");
    }

    /**
     * 分时图：X轴时间
     *
     * @param canvas
     */
    private void drawXTime(Canvas canvas) {
        if (canvas == null) return;
        if (mMinuteHourBeans == null || mMinuteHourBeans.size() == 0) {
            return;
        }
        try {
            int timeTextColor = Color.parseColor("#B2B2B2");//时间字体颜色
            int size = mMinuteTimes.size();
            String leftTime = mMinuteTimes.get(0);
            String leftCenterTime = mMinuteTimes.get(size / 4);
            String centerTime = mMinuteTimes.get(size / 2);
            String rightCenterTime = mMinuteTimes.get(size * 3 / 4);
            String rightTime = mMinuteTimes.get(size - 1);
            //左边日期
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setTextSize(mFrameTextSize);
            p.setColor(timeTextColor);
            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText(leftTime, 0 + mLeftPriceTextWidth, mHeight - 2, p);
            canvas.drawText(leftTime, 0 , mHeight - 2, p);

            //左中日期
            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText(leftCenterTime, (mWidth - mLeftPriceTextWidth) / 4 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, leftCenterTime) / 2 + mLeftPriceTextWidth, mHeight - 2, p);
            canvas.drawText(leftCenterTime, (mWidth - mLeftPriceTextWidth) / 4 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, leftCenterTime) / 2 , mHeight - 2, p);

            //中间日期
            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText(centerTime, (mWidth - mLeftPriceTextWidth) / 2 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, centerTime) / 2 + mLeftPriceTextWidth, mHeight - 2, p);
            canvas.drawText(centerTime, (mWidth - mLeftPriceTextWidth) / 2 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, centerTime) / 2 , mHeight - 2, p);

            //右中日期
            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText(rightCenterTime, (mWidth - mLeftPriceTextWidth) * 3 / 4 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, rightCenterTime) / 2 + mLeftPriceTextWidth, mHeight - 2, p);
            canvas.drawText(rightCenterTime, (mWidth - mLeftPriceTextWidth) * 3 / 4 - MinuteHourLineUtils.getPaintWidth(mFrameTextSize, rightCenterTime) / 2 , mHeight - 2, p);

            //右边日期
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(rightTime, mWidth - mLeftPriceTextWidth, mHeight - 2, p);
        } catch (Exception e) {
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TradeFragment.getInstance().setCanScroll(false);
                if (!isTouchEnabled)
                    setMinuteTouchListener(event);
                if (crossView != null) {
                    if (event.getX() - (mLeftPriceTextWidth + TEXTOFFSET) > 0) {
                        if (crossView.getVisibility() == View.GONE) {
                            onCrossMove(event.getX() - (mLeftPriceTextWidth + TEXTOFFSET), event.getY());
                        }
                    } else {
                        if (crossView.getVisibility() == View.GONE) {
                            onCrossMove(0, event.getY());
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isTouchEnabled)
                    setMinuteTouchListener(event);
                if (crossView != null) {
                    if (event.getX() - (mLeftPriceTextWidth + TEXTOFFSET) > 0) {
                        onCrossMove(event.getX() - (mLeftPriceTextWidth + TEXTOFFSET), event.getY());
                    } else {
                        onCrossMove(0, event.getY());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // TODO: 2017/8/2 zq 坐标点暂写死，正式需要接口获取
                TradeFragment.getInstance().setCanScroll(true);
                if (!isTouchEnabled && KLineChartController.getInstance() != null && KLineChartController.getInstance().getOnActivityMinuteLineOnTouchListener() != null) {
                    KLineChartController.getInstance().getOnActivityMinuteLineOnTouchListener().onNothingSelected();
                }
                if (crossView != null) {
                    crossView.setVisibility(GONE);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                TradeFragment.getInstance().setCanScroll(true);
                break;
        }
        return true;
    }


    private void setMinuteTouchListener(MotionEvent event) {
        if (mMinuteHourBeans == null || mMinuteHourBeans.size() == 0) return;
        if (KLineChartController.getInstance() != null && KLineChartController.getInstance().getOnActivityMinuteLineOnTouchListener() != null) {
            float x = 0;
            if (event.getX() - (mLeftPriceTextWidth + TEXTOFFSET) > 0) {
                x = event.getX() - (mLeftPriceTextWidth + TEXTOFFSET);
            }
            int position = (int) Math.rint(new Double(x) / new Double(xUnit));
            MinuteHourBean minuteHourBean;
            if (position < mMinuteHourBeans.size()) {
                minuteHourBean = mMinuteHourBeans.get(position);
            } else {
                minuteHourBean = mMinuteHourBeans.get(mMinuteHourBeans.size() - 1);
            }
            KLineChartController.getInstance().getOnActivityMinuteLineOnTouchListener().onValueSelected(minuteHourBean);
        }
    }

    @Override
    public void onCrossMove(float x, float y) {
        try {
            if (crossView == null || mMinuteHourBeans == null || mMinuteHourBeans.size() == 0)
                return;
            int position = (int) Math.rint(new Double(x) / new Double(xUnit));
            if (position < mMinuteHourBeans.size()) {
                MinuteHourBean minuteHourBean = mMinuteHourBeans.get(position);
                float cy = (float) getY(minuteHourBean.getPrice());
                CrossBean bean = new CrossBean(position * xUnit, cy);
                bean.price = minuteHourBean.getPrice() + "";
                bean.time = minuteHourBean.getTime();
                bean.percent = minuteHourBean.getPercent();
                crossView.drawLine(bean);
                if (crossView.getVisibility() == GONE)
                    crossView.setVisibility(VISIBLE);
            } else {
                MinuteHourBean minuteHourBean = mMinuteHourBeans.get(mMinuteHourBeans.size() - 1);
                float cy = (float) getY(minuteHourBean.getPrice());
                CrossBean bean = new CrossBean((mMinuteHourBeans.size() - 1) * xUnit, cy);
                bean.price = minuteHourBean.getPrice() + "";
                bean.time = minuteHourBean.getTime();
                bean.percent = minuteHourBean.getPercent();
                crossView.drawLine(bean);
                if (crossView.getVisibility() == GONE)
                    crossView.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
        }
    }

    //获取价格对应的Y轴
    public double getY(double price) {
        double[] maxAndMin = MinuteHourLineUtils.getMaxAndMinByYd(yMax, yMin, yesterdayClose);
        if (price == maxAndMin[0]) return 0;
        if (price == maxAndMin[1]) return mExcludeTimeHeight - TEXTOFFSET;
        return mExcludeTimeHeight - TEXTOFFSET - (new Float(price) - maxAndMin[1]) / ((maxAndMin[0] - maxAndMin[1]) / (mExcludeTimeHeight - TEXTOFFSET));
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


    private boolean isHGNI() {
        return !TextUtils.isEmpty(mTypeCode) && TextUtils.equals("HGNI", mTypeCode);
    }

}
