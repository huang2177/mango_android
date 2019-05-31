package com.paizhong.manggo.bean.trade;

/**
 * Created by zab on 2018/8/30 0030.
 */
public class CashPlaceOrderBean {
    public int code;
    public String desc;
    public int score;
    public String tScore;

    public boolean isSuccess() {
        return code == 0;
    }
}
