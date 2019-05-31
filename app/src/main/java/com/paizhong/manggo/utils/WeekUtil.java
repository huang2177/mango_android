package com.paizhong.manggo.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Des: 日期 ，周 工具类
 * Created by hs on 2018/6/28 0028 17:02
 */
public class WeekUtil {

    /**
     * 获取当前日期所在周的日期集合
     *
     * @return
     */
    public static List<Date> getWeekList() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
//        if (dayWeek == 1) {
//            dayWeek = 8;
//        }
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        cal.setTime(mondayDate);

        cal.add(Calendar.DATE, 5 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        cal.setTime(sundayDate);

        return findDates(mondayDate, sundayDate);
    }

    /**
     * 获取当前(上，下)周的日期范围如：...,-1上一周，0本周，1下一周...
     */
    public static List<Date> getWeekDays(int i) {
        Calendar calendar1 = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayOfWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 获得当前日期是一个星期的第几天
        int day = calendar1.get(Calendar.DAY_OF_WEEK);

        //获取当前日期前（下）x周同星几的日期
        calendar1.add(Calendar.DATE, 7 * i);

        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);

        Date beginDate = calendar1.getTime();
        calendar1.add(Calendar.DATE, 6);
        Date endDate = calendar1.getTime();
        return findDates(beginDate, endDate);
    }

    private static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList();
        lDate.add(dBegin);

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    /**
     * 根据时间获取日期
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据时间获取月份
     *
     * @param date
     * @return
     */
    public static int getCurrentMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 根据时间获取年分
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 根据今天日期
     *
     * @return
     */
    public static int getToDay() {
        return getDayOfMonth(new Date());
    }
}
