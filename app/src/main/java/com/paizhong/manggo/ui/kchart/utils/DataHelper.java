package com.paizhong.manggo.ui.kchart.utils;


import com.paizhong.manggo.ui.kchart.bean.KLineEntity;
import java.util.List;

/**
 * 数据辅助类 计算macd rsi等
 * 计算参见:.\src\main\assets\k_kline_target_calculate.png
 * // 公式部分有误仅供参考, 详情查阅百度
 */
public class DataHelper {

    /**
     * 计算RSI
     */
    static void calculateRSI(List<KLineEntity> datas) {
        float rsi6 = 0;
        float rsi12 = 0;
        float rsi24 = 0;
        float rsi6ABSEma = 0;
        float rsi12ABSEma = 0;
        float rsi24ABSEma = 0;
        float rsi6MaxEma = 0;
        float rsi12MaxEma = 0;
        float rsi24MaxEma = 0;
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();
            if (i == 0) {
                rsi6 = 0;
                rsi12 = 0;
                rsi24 = 0;
                rsi6ABSEma = 0;
                rsi12ABSEma = 0;
                rsi24ABSEma = 0;
                rsi6MaxEma = 0;
                rsi12MaxEma = 0;
                rsi24MaxEma = 0;
            } else {
                float Rmax = Math.max(0, closePrice - datas.get(i - 1).getCloseVal());
                float RAbs = Math.abs(closePrice - datas.get(i - 1).getCloseVal());
                rsi6MaxEma = (Rmax + (6f - 1) * rsi6MaxEma) / 6f;
                rsi6ABSEma = (RAbs + (6f - 1) * rsi6ABSEma) / 6f == 0 ? 0.1f : (RAbs + (6f - 1) * rsi6ABSEma) / 6f;

                rsi12MaxEma = (Rmax + (12f - 1) * rsi12MaxEma) / 12f;
                rsi12ABSEma = (RAbs + (12f - 1) * rsi12ABSEma) / 12f == 0 ? 0.1f : (RAbs + (12f - 1) * rsi12ABSEma) / 12f;

                rsi24MaxEma = (Rmax + (24f - 1) * rsi24MaxEma) / 24f;
                rsi24ABSEma = (RAbs + (24f - 1) * rsi24ABSEma) / 24f == 0 ? 0.1f : (RAbs + (24f - 1) * rsi24ABSEma) / 24f;

                rsi6 = (rsi6MaxEma / rsi6ABSEma) * 100;
                rsi12 = (rsi12MaxEma / rsi12ABSEma) * 100;
                rsi24 = (rsi24MaxEma / rsi24ABSEma) * 100;
            }
            point.rsi6 = rsi6;
            point.rsi12 = rsi12;
            point.rsi24 = rsi24;
        }
    }

    /**
     * 计算 KDJ
     */
    static void calculateKDJ(List<KLineEntity> datas) {
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();
            float maxN = 0f;
            float minN = Float.MAX_VALUE;
            for (int index = i; index >= 0; index--) {
                maxN = Math.max(maxN, datas.get(index).getHighVal());
                minN = Math.min(minN, datas.get(index).getLowVal());
            }

            float rsv;
            if (maxN == minN) {
                rsv = 0f;
            } else {
                rsv = 100f * (closePrice - minN) / (maxN - minN);
            }

            float lastK;
            float lastD;
            if (i == 0) {
                lastK = 50f;
                lastD = 50f;
            } else {
                KLineEntity lastPoint = datas.get(i - 1);
                lastK = lastPoint.getK();
                lastD = lastPoint.getD();
            }

            point.k = (rsv + 2f * lastK) / 3f;
            point.d = (lastK + 2f * lastD) / 3f;
            point.j = 3f * lastK - 2f * lastD;
        }
    }

    /**
     * 计算 MACD
     */
    static void calculateMACD(List<KLineEntity> datas) {
        float ema12 = 0;
        float ema26 = 0;
        float dif = 0;
        float dea = 0;
        float macd = 0;

        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();
            if (i == 0) {
                ema12 = closePrice;
                ema26 = closePrice;
            } else {
                // EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
                // EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                ema12 = ema12 * 11f / 13f + closePrice * 2f / 13f;
                ema26 = ema26 * 25f / 27f + closePrice * 2f / 27f;
            }
            // DIF = EMA（12） - EMA（26）
            // 今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
            // 用（DIF-DEA）*2即为MACD柱状图
            dif = ema12 - ema26;
            dea = dea * 8f / 10f + dif * 2f / 10f;
            macd = (dif - dea) * 2f;

            point.dif = dif;
            point.dea = dea;
            point.macd = macd;
        }
    }

    /**
     * 计算 BOLL 需要在计算ma之后进行
     */
    static void calculateBOLL(List<KLineEntity> datas) {
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();
            if (i == 0) {
                point.mb = closePrice;
                point.up = Float.NaN;
                point.dn = Float.NaN;
            } else {
                int n = 20;
                if (i < 20) {
                    n = i + 1;
                }
                float md = 0;
                for (int j = i - n + 1; j <= i; j++) {
                    float c = datas.get(j).getCloseVal();
                    float m = point.getSMA20();
                    float value = c - m;
                    md += value * value;
                }
                md = md / (n - 1);
                md = (float) Math.sqrt(md);
                point.mb = point.getSMA20();
                point.up = point.mb + 2f * md;
                point.dn = point.mb - 2f * md;
            }
        }
    }

    /**
     * 计算 SMA
     */
    static void calculateSMA(List<KLineEntity> datas) {
        float sma5 = 0;
        float sma10 = 0;
        float sma20 = 0;

        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();

            sma5 += closePrice;
            sma10 += closePrice;
            sma20 += closePrice;
            if (i >= 5) {
                sma5 -= datas.get(i - 5).getCloseVal();
                point.sma5 = sma5 / 5f;
            } else {
                point.sma5 = sma5 / (i + 1f);
            }
            if (i >= 10) {
                sma10 -= datas.get(i - 10).getCloseVal();
                point.sma10 = sma10 / 10f;
            } else {
                point.sma10 = sma10 / (i + 1f);
            }
            if (i >= 20) {
                sma20 -= datas.get(i - 20).getCloseVal();
                point.sma20 = sma20 / 20f;
            } else {
                point.sma20 = sma20 / (i + 1f);
            }
        }
    }

    /**
     * 计算EMA
     */
    static void calculateEMA(List<KLineEntity> datas) {
        float ema5 = 0;
        float ema10 = 0;
        float ema20 = 0;
        for (int i = 0; i < datas.size(); i++) {
            KLineEntity point = datas.get(i);
            final float closePrice = point.getCloseVal();
            if (i == 0) {
                ema5 = closePrice;
                ema10 = closePrice;
                ema20 = closePrice;
            } else {
                ema5 = ema5 * 4f / 6f + closePrice * 2f / 6f;
                ema10 = ema10 * 9f / 11f + closePrice * 2f / 11f;
                ema20 = ema20 * 19f / 21f + closePrice * 2f / 21f;
            }
            point.ema5 = ema5;
            point.ema10 = ema10;
            point.ema20 = ema20;
        }
    }

    /**
     * 计算SMA EMA BOLL RSI KDJ MACD
     */
    public static void calculate(List<KLineEntity> datas) {
        calculateSMA(datas);
        calculateEMA(datas);
        calculateMACD(datas);
        calculateBOLL(datas);
        calculateRSI(datas);
        calculateKDJ(datas);
    }

}
