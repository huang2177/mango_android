package com.paizhong.manggo.ui.kchart.bean;


import com.paizhong.manggo.ui.kchart.internal.KLine;

/**
 * K线实体
 */
public class KLineEntity implements KLine {

    @Override
    public float getOpenVal() {
        return open;
    }

    @Override
    public float getHighVal() {
        return high;
    }

    @Override
    public float getLowVal() {
        return low;
    }

    @Override
    public String getPriceNum() {
        return priceNum;
    }

    @Override
    public String getPercentage() {
        return percentage;
    }

    @Override
    public float getCloseVal() {
        return close;
    }

    @Override
    public float getSMA5() {
        return sma5;
    }

    @Override
    public float getSMA10() {
        return sma10;
    }

    @Override
    public float getSMA20() {
        return sma20;
    }

    @Override
    public float getDEA() {
        return dea;
    }

    @Override
    public float getDIF() {
        return dif;
    }

    @Override
    public float getMACD() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getRsi6() {
        return rsi6;
    }

    @Override
    public float getRsi12() {
        return rsi12;
    }

    @Override
    public float getRsi24() {
        return rsi24;
    }

    @Override
    public float getUpper() {
        return up;
    }

    @Override
    public float getMiddle() {
        return mb;
    }

    @Override
    public float getLower() {
        return dn;
    }

    @Override
    public float getEMA5() {
        return ema5;
    }

    @Override
    public float getEMA10() {
        return ema10;
    }

    @Override
    public float getEMA20() {
        return ema20;
    }

    /* 时间 */
    public String dateStr;
    /* 最高 */
    public float high;
    /* 最低 */
    public float low;
    /* 开盘 */
    public float open;
    /* 收盘 */
    public float close;
    /* 成交量 */
    public float volume;

    /* 涨跌值 */
    public String priceNum;

    /* 涨跌 幅 */
    public String percentage;

    // SMA 指标 
    public float sma5;
    public float sma10;
    public float sma20;

    // EMA 指标
    public float ema5;
    public float ema10;
    public float ema20;

    // MACD 指标
    public float dea;
    public float dif;
    public float macd;

    // KDJ 指标
    public float k;
    public float d;
    public float j;

    // RSI 指标
    public float rsi6;
    public float rsi12;
    public float rsi24;

    // BOLL 指标
    public float up;
    public float mb;
    public float dn;

}
