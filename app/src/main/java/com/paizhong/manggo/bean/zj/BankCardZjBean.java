package com.paizhong.manggo.bean.zj;

import android.text.TextUtils;

/**
 * Created by zab on 2018/4/20 0020.
 */

public class BankCardZjBean {

    /**
     * code : 0
     * data : [150]
     * desc : success
     */

    public String code;
    public String realCode;
    public String desc;
    public String message;
    //public JsonObject data;

    public boolean isSuccess(){
        return TextUtils.equals("0",code);
    }
}
