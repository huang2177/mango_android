package com.paizhong.manggo.bean.other;

/**
 * 下单页面参数bean
 * Created by zab on 2018/8/27 0027.
 */
public class PlaceOrderBean {
    //1 正常下单  2无 3跟单下单 4跟单行情下单（首先展示行情）5挂单下单 6修改挂单
    public int placeOrderType;
    public String productID;  //产品id
    public String productName;

    public int mrShouNum = 1;  //购买手数手数

    public double ballance; //账户金额
    public boolean mBuyState = true; //购买方向

    public String stockIndex;//当前点位
    public String changeValue;//涨跌值
    public int buyUp;
    public int toBuy;

    public int selectGgNum; //当前选择的规格 1 2 3 规格
    public double selectPrice;//当前选择规格金额

    public double feeGg1; //每手手续费 规格1
    public double feeGg2; //每手手续费 规格2
    public double feeGg3; //每手手续费 规格3

    public int priceGg1; //规格1 对应金额
    public int priceGg2; //规格2 对应金额
    public int priceGg3; //规格3 对应金额


    public int voucherGgNum1; //规格1 代金券数量
    public int voucherGgNum2; //规格2 代金券数量
    public int voucherGgNum3; //规格3 代金券数量

    public String voucherGgId1;//规格1 代金券id
    public String voucherGgId2;//规格2 代金券id
    public String voucherGgId3;//规格3 代金券id

    public String productIdGg1;//规格1 id;
    public String productIdGg2;//规格2 id;
    public String productIdGg3;//规格3 id;

    public String productIdSelect;//当前选择的规格id


    public String closingPrice;//收盘价
    public int profitLimit = 0;//跟单下单 止盈
    public int lossLimit = 0;//跟单下单 止损

    public boolean validRole; // true 牛人 false 不是牛人
    public String orderNo; //订单id
    public String fellowerPhone; //被跟单人手机号

    public int holdNight = 1; //持仓过夜 0不持仓 1持仓
    public String hangUpID;
    public String hangUpPrice;
    public String hangUpNum;

    //初始化数据
    public void initData(){
      this.placeOrderType = 1;
      this.productID = "";
      this.mrShouNum = 1;
      this.mBuyState = true;
      this.closingPrice = "";
      this.profitLimit = 0;
      this.lossLimit = 0;

      this.selectGgNum = 1;
      this.voucherGgId1 = "";
      this.voucherGgId2 = "";
      this.voucherGgId3 = "";

      this.productIdGg1 = "";
      this.productIdGg2 = "";
      this.productIdGg3 = "";

      this.productIdSelect ="";
      this.stockIndex = "";

      this.voucherGgNum1 = 0;
      this.voucherGgNum2 = 0;
      this.voucherGgNum3 = 0;

      this.ballance = 0;

      this.priceGg1 = 0;
      this.priceGg2 = 0;
      this.priceGg3 = 0;

      this.feeGg1 = 0;
      this.feeGg2 = 0;
      this.feeGg3 = 0;

      this.validRole = false;
      this.toBuy = 0;
      this.buyUp = 0;
      this.stockIndex ="";
      this.changeValue = "";
      this.holdNight = 1;
      this.hangUpPrice ="";
      this.hangUpNum = "";
    }
}
