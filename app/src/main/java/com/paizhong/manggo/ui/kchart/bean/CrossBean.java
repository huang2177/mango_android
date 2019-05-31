package com.paizhong.manggo.ui.kchart.bean;

public class CrossBean {
    //价格x轴
    public float x;
    //价格y轴
    public float y;
    //价格
    public String price;
    //时间
    public String time;

    //百分比
    public double percent ;

    public CrossBean(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String getTime() {
        return time;
    }

}
