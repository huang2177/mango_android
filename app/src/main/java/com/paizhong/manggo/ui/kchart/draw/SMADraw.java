package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.paizhong.manggo.ui.kchart.internal.Candle;
import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.KLine;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * 主图的实现类
 */
public class SMADraw extends BaseDraw<Candle> {

    public SMADraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable Candle lastPoint, @NonNull Candle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getHighVal(), curPoint.getLowVal(), curPoint.getOpenVal(), curPoint.getCloseVal());
        //SMA5
        if (lastPoint.getSMA5() != 0) {
            view.drawMainLine(canvas, mPurpleLinePaint, lastX, lastPoint.getSMA5(), curX, curPoint.getSMA5());
        }
        //SMA10
        if (lastPoint.getSMA10() != 0) {
            view.drawMainLine(canvas, mOrangeLinePaint, lastX, lastPoint.getSMA10(), curX, curPoint.getSMA10());
        }
        //SMA20
        if (lastPoint.getSMA20() != 0) {
            view.drawMainLine(canvas, mBlueLinePaint, lastX, lastPoint.getSMA20(), curX, curPoint.getSMA20());
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        Candle point = (KLine) view.getItem(position);
        String text;

        text = "SMA20=" + view.formatValue(point.getSMA20());
        y += Math.abs(mBlueLinePaint.getFontMetrics().ascent);
        canvas.drawText(text, x, y, mBlueLinePaint);
        x += (mBlueLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "SMA10=" + view.formatValue(point.getSMA10());
        canvas.drawText(text, x, y, mOrangeLinePaint);
        x += (mOrangeLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "SMA5=" + view.formatValue(point.getSMA5());
        canvas.drawText(text, x, y, mPurpleLinePaint);
    }

    @Override
    public float getMaxValue(Candle point) {
        return Math.max(point.getHighVal(), Math.max(point.getSMA5(), Math.max(point.getSMA10(), point.getEMA20())));
    }

    @Override
    public float getMinValue(Candle point) {
        return Math.min(point.getLowVal(), Math.min(point.getSMA5(), Math.min(point.getSMA10(), point.getEMA20())));
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
        
        if (open > close) {//实心
            canvas.drawRect(x - r, close, x + r, open, redPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, redPaint);
        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, greenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, greenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close +1, greyPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, greyPaint);
        }
    }

}
