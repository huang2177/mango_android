package com.paizhong.manggo.bean.trade;

/**
 * Created by zab on 2018/5/29 0029.
 */

public class CapitalListBean {

    /**
     * order_no : 18042708093742
     * hold_fee : 0
     * delivery : 0
     * amount : 800
     * quantity : 1
     * flag : 0   // 0涨 1 跌
     * fee : 70
     * open_time : 2018-04-27T09:34:08.000Z
     * close_time : 2018-04-27T09:36:21.000Z
     * type : 6
     * product_name : GL铜0.02吨
     * close_profit : -56
     * close_price : 43038
     * coupon_id : null
     * product_id : 10110
     * id : 113387
     * open_price : 43066
     */

    public boolean iOpen = false;
    public String hold_fee;
    public String delivery;
    public String quantity;
    public int flag;
    public String open_time;
    public String close_time;
    public String product_name;
    public String close_profit;
    public String close_price;
    public String coupon_id;
    public String product_id;
    public String open_price;
    public String lossLimit;
    public String profit;

    //公共参数
    public String order_no;
    public String fee;
    public String id;
    public String amount;
    public int type;
    public String typeName;
    public int tabSelect;

        /*
        id    Number
        type --0-充值 1-提现
        amount    -- 实际支付金额,单位分
        order_amoun -- 订单金额,单位分
        fee    Number    手续费,单位分
        payee_name    订单列表
        order_no    支付订单号
        time_paid 订单支付完成时的时间
        transaction_no    第三方支付返回的交易流水号
        remark    备注
        status    0-成功 1-失败 2-待支付 3-系统超时 4-申请 5-驳回
        fail_reason     提现或失败的原因
        create_time     创建时间
        */

    public String order_amoun;
    public String payee_name;
    public String time_paid;
    public String transaction_no;
    public String remark;
    public int status;
    public String fail_reason;
    public String create_time;
}
