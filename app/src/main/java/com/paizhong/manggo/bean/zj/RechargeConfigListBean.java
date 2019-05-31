package com.paizhong.manggo.bean.zj;

import java.util.List;

/**
 * Created by zab on 2018/7/17 0017.
 */
public class RechargeConfigListBean {
    /**
     * amount : [109,1300,1500,25000]
     * maximumAmount : 100000
     * message :
     * minimumAmount : 0
     * channels : [{"id":1,"channel":"alipay_qr","name":"支付宝","is_disable":0,"disable_msg":""},{"id":2,"channel":"quick_h5","name":"银行卡支付","is_disable":0,"disable_msg":""},{"id":3,"channel":"quick_h5","name":"银行卡支付2","is_disable":1,"disable_msg":"渠道维护中，暂停使用"}]
     */


    public int maximumAmount;
    public String message;
    public int minimumAmount;
    public List<String> amount;
    public List<ChannelsZJBean> channels;
}
