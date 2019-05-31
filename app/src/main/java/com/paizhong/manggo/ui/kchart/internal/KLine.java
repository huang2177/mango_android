package com.paizhong.manggo.ui.kchart.internal;


import com.paizhong.manggo.ui.kchart.internal.target.KDJ;
import com.paizhong.manggo.ui.kchart.internal.target.MACD;
import com.paizhong.manggo.ui.kchart.internal.target.RSI;

/**
 * k线实体
 */
public interface KLine extends Candle, MACD, KDJ, RSI {
}
