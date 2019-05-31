package com.paizhong.manggo.bean.zj;

import java.util.List;

/**
 * 支付方式
 * Created by zab on 2018/4/29 0029.
 */

public class PayModeZJBean {


    /**
     * code : 0
     * data : [{"disable_msg":"支付宝提现暂停，请使用银行卡提现","channel":"alipay","name":"支付宝","is_disable":1},{"disable_msg":"春节假期暂停预约提现，2月22日恢复正常提现","channel":"ebank","name":"银行卡","is_disable":0}]
     * desc : success
     */

    public int code;
    public String desc;
    public List<DataBean> data;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }

    public class DataBean {
        /**
         * disable_msg : 支付宝提现暂停，请使用银行卡提现
         * channel : alipay
         * name : 支付宝
         * is_disable : 1 不可用  0可用
         */

        public String disable_msg;
        public String channel;
        public String name;
        public int is_disable;
    }
}
