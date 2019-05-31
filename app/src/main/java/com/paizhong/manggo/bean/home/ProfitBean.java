package com.paizhong.manggo.bean.home;

/**
 * Des:
 * Created by huang on 2019/1/14 0014 17:35
 */
public class ProfitBean {
    public int amount;
    public int quantity;
    public int flag;
    public String phone;
    public String userPic;
    public String nickName;
    public Long closeTime;
    public Long currentTime;
    public String openPrice;
    public String closePrice;
    public String productName;
    public String productId;
    public String closeProfit;

    public int getQuantity() {
        return quantity == 0 ? 1 : quantity;
    }
}
