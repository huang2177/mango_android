package com.paizhong.manggo.events;

/**
 * 登录退出相关
 * Created by zab on 2018/4/11 0011.
 */

public class AppUserAccountEvent {
    public int code; // 0退出 1登录
    public boolean flag; //是否是新手

    public AppUserAccountEvent(int code){
        this.code = code;
        this.flag = false;
    }

    public AppUserAccountEvent(int code, boolean flag){
        this.code = code;
        this.flag = flag;
    }
}
