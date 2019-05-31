package com.paizhong.manggo.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class NumberUtil {
    private static DecimalFormat mFormat2 = new DecimalFormat("#0.00");


    /**
     * 解决double 计算的小数点错误
     *
     * @param num
     * @return
     */
    public static BigDecimal getBigDecimal(double num) {
        return new BigDecimal(Double.toString(num));
    }


    /**
     * 加法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double add(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.add(big2).doubleValue();
    }


    /**
     * 加法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double addScale(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.add(big2).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 减法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double subtract(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.subtract(big2).doubleValue();
    }


    public static int subtractInt(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.subtract(big2).intValue();
    }

    public static int subtractInt(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.subtract(big2).intValue();
    }

    /**
     * 减法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double subtract(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.subtract(big2).doubleValue();
    }

    /**
     * 乘法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double multiply(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.multiply(big2).doubleValue();
    }

    public static double multiply(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.multiply(big2).doubleValue();
    }

    public static int multiplyInt(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.multiply(big2).intValue();
    }

    /**
     * 除法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double divide(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static int divideInt(double num1, double num2) {
        BigDecimal big1 = getBigDecimal(num1);
        BigDecimal big2 = getBigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static double divide(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String divideStr(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
    }

    public static double divide_0(String num1, String num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.divide(big2, 2, BigDecimal.ROUND_UP).doubleValue();
    }

    public static BigDecimal divide(String num1, String num2, int scal) {
        return new BigDecimal(num1).divide(new BigDecimal(num2), scal, BigDecimal.ROUND_HALF_UP);
    }


    public static int getInt(String num1, int placeholder) {
        if (TextUtils.isEmpty(num1)){
            return placeholder;
        }
        try {
            return Integer.parseInt(num1);
        } catch (Exception e) {
            e.printStackTrace();
            return placeholder;
        }
    }

    public static double getDouble(String num) {
        try {
            return new BigDecimal(num).setScale(BigDecimal.ROUND_CEILING, BigDecimal.ROUND_DOWN).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }


    public static double getDouble(double num) {
        return new BigDecimal(num).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 保留两位小数字符串
     *
     * @param d
     * @return
     */
    public static String getFloatStr4(double d) {
        try {
            return mFormat2.format(d);
        } catch (Exception e) {
            return "0.0000";
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if (TextUtils.isEmpty(s)){
            return "";
        }
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
