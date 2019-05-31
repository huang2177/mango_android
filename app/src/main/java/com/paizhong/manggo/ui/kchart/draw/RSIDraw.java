package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.target.RSI;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * RSI实现类
 */
public class RSIDraw extends BaseDraw<RSI> {

    public RSIDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable RSI lastPoint, @NonNull RSI curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, mPurpleLinePaint, lastX, lastPoint.getRsi6(), curX, curPoint.getRsi6());
        view.drawChildLine(canvas, mOrangeLinePaint, lastX, lastPoint.getRsi12(), curX, curPoint.getRsi12());
        view.drawChildLine(canvas, mBlueLinePaint, lastX, lastPoint.getRsi24(), curX, curPoint.getRsi24());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        RSI point = (RSI) view.getItem(position);

        String text = "";

        text = "RSI24=" + view.formatValue(point.getRsi24());
        y += Math.abs(mBlueLinePaint.getFontMetrics().ascent);
        canvas.drawText(text, x, y, mBlueLinePaint);
        x += (mBlueLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "RSI12=" + view.formatValue(point.getRsi12());
        canvas.drawText(text, x, y, mOrangeLinePaint);
        x += (mOrangeLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "RSI6=" + view.formatValue(point.getRsi6());
        canvas.drawText(text, x, y, mPurpleLinePaint);
    }

    @Override
    public float getMaxValue(RSI point) {
        return Math.max(point.getRsi6(), Math.max(point.getRsi12(), point.getRsi24()));
    }

    @Override
    public float getMinValue(RSI point) {
        return Math.min(point.getRsi6(), Math.min(point.getRsi12(), point.getRsi24()));
    }

}
