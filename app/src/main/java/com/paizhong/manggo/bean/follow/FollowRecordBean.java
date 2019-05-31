package com.paizhong.manggo.bean.follow;

/**
 * Created by zab on 2018/7/2 0002.
 */
public class FollowRecordBean {

    /**
     * nickName : null
     * userPic : null
     * profitRate : null
     * productName : GL镍
     * quantity : 1
     * amount : 8
     * flag : 0  0-买涨 1-买跌
     * openPrice : 93438
     * fee : 0.7
     * openTime : 2018-07-02 10:37:05
     * phone : 13601951992
     * closeProfit : 0
     * floatPrice : 0
     * closePrice : null
     * orderNo : 18070205352426
     * fellowCount : 1
     */

    public String nickName;
    public String userPic;
    public String profitRate;
    public String productName;
    public int quantity;
    public int amount;
    public int flag;
    public String openPrice;
    public String fee;
    public String openTime;
    public String closeTime;
    public String phone;
    public String closeProfit;
    public String floatPrice;
    public String closePrice; //持仓的时候是盈亏
    public String orderNo;
    public int fellowCount;
    public String typeId;       //产品id
    public String fellowerPhone;
    //public String productId; //订单id
    public String stockIndex;
    public String changeValue;
    public String changeRange;
    public String score;
    public String totalAmount;//跟买金额

    public String profitLimit;
    public String lossLimit;

    public int isRanking;//0.不是牛人 1.是牛人

    public int mFloatPrice;
    public String mProductMsg;
    public double mCloseProfit;
}
