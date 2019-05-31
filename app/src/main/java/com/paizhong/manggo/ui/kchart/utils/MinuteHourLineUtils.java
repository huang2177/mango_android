package com.paizhong.manggo.ui.kchart.utils;

import android.graphics.Paint;
import android.graphics.Rect;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

public class MinuteHourLineUtils {

    /**
     * 计算出Y轴显示价格的最大最小值
     *
     * @param max
     * @param min
     * @param yd
     * @return
     */
    public synchronized static double[] getMaxAndMinByYd(double max, double min, double yd) {
        double rate = 1.075;
        double limit = Math.abs(max - yd) > Math.abs(yd - min) ? Math.abs(max - yd) : Math.abs(yd - min);
        limit = limit * rate;
        double[] result = new double[2];
        result[0] = yd + limit;
        result[1] = yd - limit;
        return result;
    }

    /**
     * 获取该画笔写出文字的宽度
     *
     * @param paint
     * @return
     */
    public static float getTextWidth(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * 获取百分比字符
     *
     * @param d
     * @return
     */
    public static String getPercentString(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        return df.format(d * 100) + "%";
    }

    /**
     * 获取字体宽度
     *
     * @param textSize
     * @param str
     * @return
     */
    public static int getPaintWidth(float textSize, String str) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

    /**
     * 获取字体高度
     *
     * @param fontSize
     * @return
     */
    public static float getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }


    /**
     * 获取字体居中后到控件顶部的间距
     *
     * @return
     */
    public static float getTextStartY(float fontSize, float height) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        return height/2 + fm.descent - (fm.descent - fm.ascent) / 2;
    }


    /**
     * 获取数组中最大最小值
     *
     * @param list
     * @return
     */
    public synchronized static float[] getMaxAndMin(float[] list) {
        if (list == null || list.length == 0) return new float[]{0, 0};
        float max = 0, min = 0;
        float[] temp = list.clone();
        Arrays.sort(temp);
        max = temp[temp.length - 1];
        min = temp[0];
        return new float[]{max, min};
    }


    /**
     * 四舍五入  保留位数
     *
     * @param v
     * @param scale
     * @return
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
