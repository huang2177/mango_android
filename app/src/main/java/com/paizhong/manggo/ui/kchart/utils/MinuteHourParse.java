package com.paizhong.manggo.ui.kchart.utils;


import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;

import java.util.ArrayList;

public class MinuteHourParse {
    private ArrayList<MinuteHourBean> mDataList = new ArrayList<>();
    private ArrayList<String> mDateList = new ArrayList<>();

    public synchronized ArrayList<String> getmDateList() {
        return mDateList;
    }

    public synchronized ArrayList<MinuteHourBean> getmDataList() {
        return mDataList;
    }

    /********** 测试**********/

    public synchronized void parseMinutes() {
        mDataList.clear();
        mDateList.clear();
        for (int i = 0; i < 50; i++) {
            mDateList.add(20170101 + i + "");
        }
        for (int i = 0; i < 50; i++) {
            MinuteHourBean minuteHourBean = new MinuteHourBean();
            minuteHourBean.time = mDateList.get(i);
            int num = (int) (Math.random() * 100) + 50;
            minuteHourBean.price = num ;
            minuteHourBean.percent =
                    ((minuteHourBean.getPrice() - Double.parseDouble("75")) / Double.parseDouble("75"));
            mDataList.add(minuteHourBean);
        }
        for (int i = 0; i < mDataList.size(); i++) {
            MinuteHourBean bean = mDataList.get(i);
            if (i != 0) {
                bean.total = bean.getPrice() + mDataList.get(i - 1).total;
                bean.average = (int) (bean.total / (i + 1));
            } else {
                bean.total = bean.getPrice();
                bean.average = (int) (bean.total / (i + 1));
            }
        }
    }
}
