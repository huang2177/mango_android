package com.paizhong.manggo.bean.zj;

/**
 * 资金配置bean
 * Created by zab on 2018/4/29 0029.
 */

public class RechargeConfigZJBean {

     public int code;
     public RechargeConfigListBean data;
     public String desc;

    public boolean isSuccess(){
        return code == 0;
    }
}
