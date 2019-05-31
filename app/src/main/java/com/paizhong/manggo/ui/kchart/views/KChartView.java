package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.util.AttributeSet;

import com.paizhong.manggo.ui.kchart.draw.BOLLDraw;
import com.paizhong.manggo.ui.kchart.draw.EMADraw;
import com.paizhong.manggo.ui.kchart.draw.KDJDraw;
import com.paizhong.manggo.ui.kchart.draw.MACDDraw;
import com.paizhong.manggo.ui.kchart.draw.RSIDraw;
import com.paizhong.manggo.ui.kchart.draw.SMADraw;

/**
 * 专业 K 线图
 */
public class KChartView extends BaseKChart {

    public KChartView(Context context) {
        this(context, null);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addMainDraw("SMA", new SMADraw(getContext()));
        addMainDraw("EMA", new EMADraw(getContext()));
        addMainDraw("BOLL", new BOLLDraw(getContext()));

        addChildDraw("MACD", new MACDDraw(getContext()));
        addChildDraw("KDJ", new KDJDraw(getContext()));
        addChildDraw("RSI", new RSIDraw(getContext()));
    }

    @Override
    public void onLeftSide() {
    }

    @Override
    public void onRightSide() {
    }

}
