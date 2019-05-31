package com.paizhong.manggo.bean.zj;

/**
 * 检测 交易所是否已注册
 * Created by zab on 2018/4/17 0017.
 */

public class UserZJCheckBean {

    public int code;
    public String desc;
    public AiPeiErrorBean data;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }

    public static class AiPeiErrorBean {
        public boolean isregister;
    }

}
