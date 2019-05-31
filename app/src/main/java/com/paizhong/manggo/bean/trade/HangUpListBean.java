package com.paizhong.manggo.bean.trade;

/**
 * Created by zab on 2018/8/22 0022.
 */
public class HangUpListBean {
    public int itemViewType;



    /**
     * id : 3
     * phone : 13262645906
     * name : null
     * productName : GL银
     * typeId : 11     //产品id
     * productId : 10107
     * amount : 8.0
     * quantity : 1
     * flag : 0      //0-买涨，1买跌
     * fee : 0.7
     * createTime : 1536551551000
     * floatNum : 5
     * profitLimit : 200
     * lossLimit : 75
     * registerAmount : 2869.0
     * marketPrice : null
     * productCode : 2869
     *
     * registerFinishTime //历史： 创建时间
     * createTime    （进行：创建时间    历史： 挂单时间
     * status 历史： 挂单状态：1、挂单中 2、持仓(挂单成功) 3、平仓 4、撤单 5、挂单失败
     * openPrice 历史： 建仓成功后的建仓价
     */

    public String name;
    public String productName;

    public String marketPrice;
    public String productCode;

    //公共数据
    public String id;
    public String phone;
    public String typeId;
    public String productId;
    public String amount;
    public int quantity;
    public int flag;
    public String fee;
    public String createTime;
    public String floatNum;
    public int profitLimit;
    public int lossLimit;
    public String registerAmount;

    public String registerFinishTime;
    public int holdNight;
    public String remark;
    public int status;
    public String openPrice;

    public boolean typeTag;
    public String kgNum;
    public String stockIndex;
    public double changValue;
}
