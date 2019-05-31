package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Paint;

import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.internal.IChartDraw;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * draw基类 创建画笔等
 */
public abstract class BaseDraw<T> implements IChartDraw<T> {

    protected float mTextSize = 0;
    //candle的宽度
    protected int mCandleWidth = 0;
    //candle线的宽度
    protected int mCandleLineWidth = 0;
    //线的宽度
    protected int mLineWidth = 0;
    protected Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint greyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected Paint mPurpleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mOrangeLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mBlueLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mGreenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mRedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mGreyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected Context mContext;

    public BaseDraw(Context context) {
        mCandleWidth = DeviceUtils.dip2px(context, 5);
        mCandleLineWidth = DeviceUtils.dip2px(context, 1);
        mLineWidth = DeviceUtils.dip2px(context, 0.5f);
        mTextSize = context.getResources().getDimension(R.dimen.dimen_10sp);
        redPaint.setColor(context.getResources().getColor(R.color.color_ff5376));
        greenPaint.setColor(context.getResources().getColor(R.color.color_00ce64));
        greyPaint.setColor(context.getResources().getColor(R.color.color_b2b2b2));

        mTextSize = context.getResources().getDimension(R.dimen.dimen_8sp);
        mPurpleLinePaint.setColor(context.getResources().getColor(R.color.color_cf5ee6));
        mPurpleLinePaint.setStrokeWidth(mLineWidth);
        mPurpleLinePaint.setTextSize(mTextSize);

        mOrangeLinePaint.setColor(context.getResources().getColor(R.color.color_ff9300));
        mOrangeLinePaint.setStrokeWidth(mLineWidth);
        mOrangeLinePaint.setTextSize(mTextSize);

        mBlueLinePaint.setColor(context.getResources().getColor(R.color.color_4990e2));
        mBlueLinePaint.setStrokeWidth(mLineWidth);
        mBlueLinePaint.setTextSize(mTextSize);

        mGreenLinePaint.setColor(context.getResources().getColor(R.color.color_00ce64));
        mGreenLinePaint.setStrokeWidth(mLineWidth);
        mGreenLinePaint.setTextSize(mTextSize);

        mRedLinePaint.setColor(context.getResources().getColor(R.color.color_ff5376));
        mRedLinePaint.setStrokeWidth(mLineWidth);
        mRedLinePaint.setTextSize(mTextSize);

        mGreyLinePaint.setColor(context.getResources().getColor(R.color.color_b2b2b2));
        mGreyLinePaint.setStrokeWidth(mLineWidth);
        mGreyLinePaint.setTextSize(mTextSize);

        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String formatValue(float value) {
        return String.valueOf(value);
    }

}
