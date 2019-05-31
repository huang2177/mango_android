package com.paizhong.manggo.bean.zj;

/**
 * 入金
 * Created by zab on 2018/5/2 0002.
 */

public class RechargeMoneyZJBean {


    /**
     * code : 0
     * desc : success
     * data : {"id":2540,"amount":51825,"channel":"alipay_qr","paid":false,"extra":{"qr_code":"https://qr.alipay.com/bax03484sxqjr3abghkc20a1"}}
     */

    public int code;
    public String desc;
    public DataBean data;

    public boolean isSuccess(){
        return code == 0;
    }

    //true 没有实名
    public boolean isNoVerifyCard(){
        return code == 3004;
    }
    public class DataBean {
        /**
         * id : 2540
         * amount : 51825
         * channel : alipay_qr
         * paid : false
         * extra : {"qr_code":"https://qr.alipay.com/bax03484sxqjr3abghkc20a1"}
         */

        public int id;
        public int amount;
        public String channel;
        public boolean paid;
        public ExtraBean extra;

        public class ExtraBean {
            /**
             * qr_code : https://qr.alipay.com/bax03484sxqjr3abghkc20a1
             */

            public String qr_code;

            public String pay_url;


        }
    }
}
