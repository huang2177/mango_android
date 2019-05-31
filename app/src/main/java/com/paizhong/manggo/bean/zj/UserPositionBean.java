package com.paizhong.manggo.bean.zj;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zab on 2018/9/5 0005.
 */
public class UserPositionBean {

    @SerializedName("a")
    public double amount;  //金额
    @SerializedName("c")
    public String coupon_id; //代金券id
    @SerializedName("f")
    public double floatingPrice; //浮动盈亏
}
