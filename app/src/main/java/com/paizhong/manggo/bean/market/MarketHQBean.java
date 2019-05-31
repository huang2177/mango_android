package com.paizhong.manggo.bean.market;

/**
 * 行情列表bean
 * Created by zab on 2018/4/2 0002.
 */

public class MarketHQBean {
    /**
     * high : 2999 最高
     * code : AG
     * low : 2982 //最低
     * change : -1.0 涨跌值
     * name : GL银
     * remark : 11
     * time : 2018-08-21 15:44:51
     * changeRate : -0.03% 涨跌幅
     * rise : 2
     * close : 2996 //收盘
     * point : 2995 //当前
     * open : 2982 开盘
     */

    public String id;
    public String name;
    public String code;
    public String time;
    public String point;
    public String change;
    public String changeRate;
    public String open;
    public String close;
    public String high;
    public String low;
    public String remark;
    public int rise = 2;

    public int toBuy;
    public int buyUp;
    public String timeStr;

    public boolean is_close = false; //休市状态

    public boolean isClose(){
        return is_close;
    }
}
