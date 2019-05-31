package com.paizhong.manggo.bean.trade;

/**
 * Created by zab on 2018/8/31 0031.
 */
public class AtBaseBean {
    public int code;
    public String desc;

    public boolean isSuccess() {
        return code == 0;
    }
}
