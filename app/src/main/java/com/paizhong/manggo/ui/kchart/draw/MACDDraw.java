package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.target.MACD;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * MACD 实现类
 */
public class MACDDraw extends BaseDraw<MACD> {

    public MACDDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable MACD lastPoint, @NonNull MACD curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMACD());
        view.drawChildLine(canvas, mOrangeLinePaint, lastX, lastPoint.getDIF(), curX, curPoint.getDIF());
        view.drawChildLine(canvas, mBlueLinePaint, lastX, lastPoint.getDEA(), curX, curPoint.getDEA());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        MACD point = (MACD) view.getItem(position);
        String text = "";

        // MACD
        text = "MACD=" + view.formatValue(point.getMACD());
        y += Math.abs(mGreenLinePaint.getFontMetrics().ascent);
        canvas.drawText(text, x, y, point.getMACD()>=0?mRedLinePaint:mGreenLinePaint);
        x += (mGreenLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        // DEA
        text = "DEA=" + view.formatValue(point.getDEA());
        canvas.drawText(text, x, y, mBlueLinePaint);
        x += (mBlueLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        // DIF
        text = "DIF=" + view.formatValue(point.getDIF());
        canvas.drawText(text, x, y, mOrangeLinePaint);
        x += (mOrangeLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "MACD(9, 12, 26)";
        canvas.drawText(text, x, y, mGreyLinePaint);
    }

    @Override
    public float getMaxValue(MACD point) {
        return Math.max(point.getMACD(), Math.max(point.getDEA(), point.getDIF()));
    }

    @Override
    public float getMinValue(MACD point) {
        return Math.min(point.getMACD(), Math.min(point.getDEA(), point.getDIF()));
    }

    /**
     * 画macd
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, IKChartView view, float x, float macd) {
        macd = view.getChildY(macd);
        int r = mCandleWidth / 2;
        if (macd > view.getChildY(0)) {
            canvas.drawRect(x - r, view.getChildY(0), x + r, macd - (macd - view.getChildY(0) )/5, greenPaint);
        } else {
            canvas.drawRect(x - r, macd + (view.getChildY(0) - macd)/5, x + r, view.getChildY(0), redPaint);
        }
    }
}
