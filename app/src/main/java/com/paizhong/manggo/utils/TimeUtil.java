package com.paizhong.manggo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Des:
 * Created by huang on 2018/8/24 0024 15:12
 */
public class TimeUtil {
    public static String dateM = "MM";
    public static String dateD = "dd";
    public static String dateFormatYMD = "yyyy-MM-dd";
    public static String dateFormat_path = "yyyy_MM_dd_HH_mmss";
    public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static String dateFormatMDHMS = "MM/dd HH:mm:ss";
    public static String dateFormatMDHM = "MM-dd HH:mm";
    public static String dateFormatHM = "HH:mm";

    /**
     * 友好显示时间差
     *
     * @param diff 毫秒
     * @return
     */
    public static String getFriendTimeOffer(long diff) {
        int day = (int) (diff / (24 * 60 * 60 * 1000));
        if (day > 0)
            return day + "天前";
        int time = (int) (diff / (60 * 60 * 1000));
        if (time > 0)
            return time + "小时前";
        int min = (int) (diff / (60 * 1000));
        if (min > 0)
            return min + "分钟前";
        int sec = (int) diff / 1000;
        if (sec > 0)
            return sec + "秒前";
        return "1秒前";
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    //美国时间转换成固定的中文时间
    public static String timeZoneConversionYMD(String time) {
        String newDate = null;
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            df1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df1.parse(time);
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    //美国时间转换成固定的中文时间
    public static String timeZoneConversion(String time) {
        String newDate = null;
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            df1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df1.parse(time);
            return new SimpleDateFormat("MM-dd HH:mm").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static long getDateByFormatYMDate(String strDate) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
        try {
            return addDay(mSimpleDateFormat.parse(strDate), 1).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public static long getDateByFormatYMD(String strDate) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
        try {
            return mSimpleDateFormat.parse(strDate).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 时间相加
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }


    /**
     * 将时间字符串转成其他时间字符串
     * @param dateTime 字符串时间
     * @param dataPattern 当前字符串时间格式
     * @param zhPattern 需要转成的时间格式
     * @return
     */
    public static String getStringToString(String dateTime, String dataPattern,String zhPattern) {
        if (TextUtils.isEmpty(dateTime)){
            return "";
        }
        try {
          return  new SimpleDateFormat(zhPattern).format(new SimpleDateFormat(dataPattern).parse(dateTime));
        }catch (Exception e){
            e.printStackTrace();
        }
        return dateTime;
    }
}
