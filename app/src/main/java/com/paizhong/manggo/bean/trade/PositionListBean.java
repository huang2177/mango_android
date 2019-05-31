package com.paizhong.manggo.bean.trade;

/**
 * Created by zab on 2018/8/22 0022.
 */
public class PositionListBean {


    /**
     * order_no : 18083021984558
     * flu_price : 10
     * hold_fee : 10
     * flag : 1
     * profit_limit_price : 2798
     * fee : 70
     * type : 11
     * hold_night : 1
     * product_id : 10107
     * loss_limit_price : 3018
     * profit_limit : 200
     * id : 11
     * open_price : 2958
     * changeRate : -1.04%
     * close : 2977
     * amount : 800
     * quantity : 1
     * loss_limit : 75
     * create_time : 2018-08-30 16:07:21
     * stockIndex : 2946.0
     * floatingPrice : 1.2
     * user_id : 1084646
     * name : GL银0.1kg
     * changValue : -31.0
     * status : 0
     */

    public String order_no; //订单号
    public String flu_price;  //浮动价格
    public String hold_fee; //--过夜费 单位分
    public int flag;
    public String fee;
    public String type; //产品id
    public int hold_night;
    public int product_id;
    public int profit_limit_price;
    public int loss_limit_price;
    public String id; //订单id
    public String open_price;
    public String amount;
    public int quantity;
    public String create_time;
    public double floatingPrice;
    public String user_id;
    public String name;
    public int status;
    public int profit_limit;
    public int loss_limit;
    public String coupon_id;
    public String close; //收盘价
    public String stockIndex;
    public double changValue;
    public String changeRate;

    public boolean check;
    public boolean typeTag;
    public String flu_dw; //浮动点位
    public String kgNum;
}
