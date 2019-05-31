package com.paizhong.manggo.ui.kchart.internal;


import com.paizhong.manggo.ui.kchart.internal.target.BOLL;
import com.paizhong.manggo.ui.kchart.internal.target.EMA;
import com.paizhong.manggo.ui.kchart.internal.target.SMA;

/**
 * 分时图顶部实体
 */
public interface Candle extends SMA, EMA, BOLL {

    /**
     * 开盘
     */
    float getOpenVal();

    /**
     * 收盘
     */
    float getCloseVal();

    /**
     * 最高
     */
    float getHighVal();

    /**
     * 最低
     */
    float getLowVal();

    /**
     * 涨跌值
     * @return
     */
    String getPriceNum();

    /**
     * 涨跌值
     * @return
     */
    String getPercentage();
}
