package com.paizhong.manggo.ui.kchart.bean;

import java.io.Serializable;

public class MinuteHourBean implements Serializable {
    //时间
    public String time;
    //最新价
    public double price;

    //百分比
    public double percent;

    //平均值
    public double average;

    //前面数据和
    public double total;

    public double getPrice() {
        try {
            return price;
        }catch (Exception e){
            return 0.00;
        }
    }

    public String getTime() {
        return time;
    }

    public double getPercent() {
        return percent;
    }

}
