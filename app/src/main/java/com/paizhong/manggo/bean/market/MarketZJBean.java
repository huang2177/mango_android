package com.paizhong.manggo.bean.market;

/**
 *  行情
 * Created by zab on 2018/4/18 0018.
 */

public class MarketZJBean {

    /**
     * sliverMarketId : null
     * investProductId : ZJWLAG
     * investProductName : GL银
     * stockTime : 2018-04-18 10:50:44
     * stockIndex : 3386
     * changeValue : -6.0
     * changeRange : -0.18%
     * openingPrice : 3395
     * closingPrice : 3392
     * highestPrice : 3383
     * floorPrice : 3398
     * lastPrice : 3392
     * todayPrice : 3386
     * state : null
     * remark : 11
     * createTime : null
     * modifyTime : null
     * rise : 0 //0 跌  1涨  2平
     * buyUp : 50
     * toBuy : 50
     */

    public String sliverMarketId;
    public String investProductId;
    public String investProductName;
    public String stockTime;
    public String stockIndex; //最新价格
    public String changeValue; //涨跌值
    public String changeRange; //涨跌幅
    public String openingPrice; //开盘价
    public String closingPrice; //收盘价
    public String highestPrice; //最高价
    public String floorPrice;  //最低价
    public String lastPrice;
    public String todayPrice;
    public String state;
    public int remark;    //id
    public String createTime;
    public String modifyTime;
    public int rise;
    public int buyUp;
    public int toBuy;

}
