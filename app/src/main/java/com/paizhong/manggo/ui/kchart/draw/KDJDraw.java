package com.paizhong.manggo.ui.kchart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.target.KDJ;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * KDJ 实现类
 */
public class KDJDraw extends BaseDraw<KDJ> {

    public KDJDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable KDJ lastPoint, @NonNull KDJ curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, mPurpleLinePaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, mOrangeLinePaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, mBlueLinePaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        KDJ point = (KDJ) view.getItem(position);
        String text = "";

        text = "J=" + view.formatValue(point.getJ());
        y += Math.abs(mBlueLinePaint.getFontMetrics().ascent);
        canvas.drawText(text, x, y, mBlueLinePaint);
        x += (mBlueLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "D=" + view.formatValue(point.getD());
        canvas.drawText(text, x, y, mOrangeLinePaint);
        x += (mOrangeLinePaint.measureText(text) + DeviceUtils.dip2px(getContext(), 4));

        text = "K=" + view.formatValue(point.getK());
        canvas.drawText(text, x, y, mPurpleLinePaint);
    }

    @Override
    public float getMaxValue(KDJ point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(KDJ point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

}
