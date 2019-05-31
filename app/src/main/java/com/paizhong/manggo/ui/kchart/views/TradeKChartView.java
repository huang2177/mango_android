package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.util.AttributeSet;
import com.paizhong.manggo.ui.kchart.draw.SMADraw;

/**
 * 专业 K 线图
 */
public class TradeKChartView extends TradeBaseKChart {

    public TradeKChartView(Context context) {
        this(context, null);
    }

    public TradeKChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TradeKChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addMainDraw("SMA", new SMADraw(getContext()));
    }

    @Override
    public void onLeftSide() {
    }

    @Override
    public void onRightSide() {
    }

}
