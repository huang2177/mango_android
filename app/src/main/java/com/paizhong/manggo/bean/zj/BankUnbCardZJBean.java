package com.paizhong.manggo.bean.zj;


/**
 * 解绑 银行卡
 * Created by zab on 2018/4/20 0020.
 */

public class BankUnbCardZJBean {


    /**
     * code : 0
     * data : 1
     * desc : success
     */
    public int code;
   // public JsonObject data;
    public String desc;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }

}
