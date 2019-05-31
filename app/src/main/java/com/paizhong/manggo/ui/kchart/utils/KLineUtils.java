package com.paizhong.manggo.ui.kchart.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by zab on 2018/4/20 0020.
 */

public class KLineUtils {
    private static DecimalFormat mFormat2 = new DecimalFormat("#0.00");
    private static DecimalFormat mFormat1 = new DecimalFormat("#0.0");
    private static DecimalFormat format = new DecimalFormat("#0");


    public static String makeNum(String s) {
        String b = s;
        s = b.replace(",", "");
        return s;
    }

    /**
     * 保留两位小数字符串
     *
     * @param d
     * @return
     */
    public static String getFloatStr2(double d) {
        try {
            return mFormat2.format(d);
        } catch (Exception e) {
            return "0.00";
        }
    }




    /**
     * 整数字符串
     *
     * @param f
     * @return
     */
    public static String getIntegerStr(double f) {
        try {
            return format.format(f);
        } catch (Exception e) {
            return "0";
        }
    }

    public static double getDouble(String str) {
        try {
            return getRound(Double.parseDouble(str), 4);
        } catch (Exception e) {
            return 0.00;
        }
    }


    public static double getDoubleZ(String str) {
        try {
            int indexOf = str.indexOf(".");
            if (indexOf != -1){
                return  getRound(Double.parseDouble(str), str.length() - indexOf - 1);
            }else {
                return getRound(Double.parseDouble(str), 4);
            }
        } catch (Exception e) {
            return 0.00;
        }
    }

    public static double getDouble(double num) {
        try {
            return getRound(num, 4);
        } catch (Exception e) {
            return 0.00;
        }
    }

    /**
     * 返回整数（大于等于该数的那个最近值）
     *
     * @param d
     * @return
     */
    public static int getRoundCeilingInt(double d) {
        BigDecimal decimal = new BigDecimal(d);
        BigDecimal bigDecimal = decimal.setScale(0, BigDecimal.ROUND_UP);
        return bigDecimal.intValue();
    }

    /**
     * 对double数据进行取精度(四舍五入)
     *
     * @param d
     * @param newScale
     * @return
     */
    public static double getRound(double d, int newScale) {
        BigDecimal decimal = new BigDecimal(d);
        BigDecimal bigDecimal = decimal.setScale(newScale, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }


    public static String NVL(Object obj, String value) {
        try {
            if (obj == null) {
                return value;
            }
            if (String.valueOf(obj).trim().equals("")) {
                return value;
            }
            if (String.valueOf(obj).trim().equalsIgnoreCase("null")) {
                return value;
            }
            return String.valueOf(obj);
        } catch (Exception e) {
            return value;
        }
    }


    /**
     * 去掉无效小数点
     * 去掉小数点最后补齐的0
     * 100.01 ＝ 100.01
     * 100.10 ＝ 100.1
     * 100.00 ＝ 100
     * 100 ＝ 100
     * @param d
     * @return
     */
    public static String moveLast0(double d) {
        String str = String.valueOf(d);
        return moveLast0(str);
    }

    public static String moveLast0(String str) {
        try {
            if (str == null)
                return "";
            BigDecimal bigDecimal = new BigDecimal(str);
            str = bigDecimal.toString();
            if (str.contains(".")) {
                String[] ss = str.split("\\.");
                if (Integer.parseInt(ss[1]) > 0) {
                    if (ss[1].endsWith("0")) {
                        int index = ss[1].lastIndexOf("0");
                        str = ss[0] + "."+ss[1].substring(0, index);
                        if (str.endsWith("0")) {
                            return moveLast0(str);
                        }
                    } else {
                        str = ss[0] + "." + ss[1];
                    }
                } else {
                    str = ss[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
