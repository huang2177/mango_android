package com.paizhong.manggo.bean.zj;

import java.util.List;

/**
 * 中金绑卡列表
 * Created by zab on 2018/4/20 0020.
 */

public class BankCardZjListBean {

    /**
     * code : 0
     * data : [{"channel":"ebank","real_name":"唐斌","id":144,"account":"6214851215197075"},{"channel":"ebank","real_name":"唐斌","id":152,"account":"6722353453535345"}]
     * desc : success
     */

    public int code;
    public String desc;
    public String phone;//手机号
    public String idCard;//身份证
    public List<DataBean> data;
    public boolean isSuccess(){
        return code == 0;
    }

    public static class DataBean {
        /**
         * channel : ebank
         * real_name : 唐斌
         * id : 144
         * account : 6214851215197075
         */

        public String channel;
        public String real_name;
        public String id;  //卡列表id
        public String account; //银行卡号
    }
}
