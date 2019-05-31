package com.paizhong.manggo.bean.zj;

/**
 * Created by zab on 2018/5/10 0010.
 */

public class ZJBaseBean {
    public int code;
    public String desc;

    public boolean isSuccess(){
        return code == 0;
    }
}
