package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.internal.Candle;
import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.target.BOLL;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * BOLL实现类
 */
public class BOLLDraw extends BaseDraw<Candle> {

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BOLLDraw(Context context) {
        super(context);
        mTextPaint.setColor(context.getResources().getColor(R.color.color_323232));
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.dimen_10sp));
        mSelectorPaint.setColor(context.getResources().getColor(R.color.color_ff9300));
        mSelectorPaint.setAlpha(200);
    }

    @Override
    public void drawTranslated(@Nullable Candle lastPoint, @NonNull Candle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getHighVal(), curPoint.getLowVal(), curPoint.getOpenVal(), curPoint.getCloseVal());

        view.drawMainLine(canvas, mPurpleLinePaint, lastX, lastPoint.getUpper(), curX, curPoint.getUpper());
        view.drawMainLine(canvas, mOrangeLinePaint, lastX, lastPoint.getMiddle(), curX, curPoint.getMiddle());
        view.drawMainLine(canvas, mBlueLinePaint, lastX, lastPoint.getLower(), curX, curPoint.getLower());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        BOLL point = (BOLL) view.getItem(position);

        String text = "";
        text = "DN=" + view.formatValue(point.getLower());
        y += Math.abs(mBlueLinePaint.getFontMetrics().ascent);
        canvas.drawText(text, x, y, mBlueLinePaint);
        x += (mBlueLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "MB=" + view.formatValue(point.getMiddle());
        canvas.drawText(text, x, y, mOrangeLinePaint);
        x += mOrangeLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4);

        text = "UP=" + view.formatValue(point.getUpper());
        canvas.drawText(text, x, y, mPurpleLinePaint);
    }

    @Override
    public float getMaxValue(Candle point) {
        float upper = point.getUpper();
        float middle = point.getMiddle();
        float lower = point.getLower();

        if (Float.isNaN(upper)) upper = Float.MIN_VALUE;
        if (Float.isNaN(middle)) middle = Float.MIN_VALUE;
        if (Float.isNaN(lower)) lower = Float.MIN_VALUE;

        return Math.max(point.getHighVal(), Math.max(Math.max(upper, middle), lower));
    }

    @Override
    public float getMinValue(Candle point) {
        float upper = point.getUpper();
        float middle = point.getMiddle();
        float lower = point.getLower();

        if (Float.isNaN(upper)) upper = Float.MAX_VALUE;
        if (Float.isNaN(middle)) middle = Float.MAX_VALUE;
        if (Float.isNaN(lower)) lower = Float.MAX_VALUE;

        return Math.min(point.getLowVal(), Math.min(Math.min(upper, middle), lower));
    }

    /**
     * 绘制蜡烛图
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(IKChartView view, Canvas canvas, float x, float high, float low, float open, float close) {
        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        int r = mCandleWidth / 2;
        int lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            canvas.drawRect(x - r, close, x + r, open, redPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, redPaint);
        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, greenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, greenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, greyPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, greyPaint);
        }
    }

}
