package com.paizhong.manggo.events;

/**
 * 交易所登录成功
 * Created by zab on 2018/4/17 0017.
 */

public class JysAccountEvent {
    public int code; //1 中金  2 中金登陆超时
    public int typeCode; //0 登录  1注册

    public JysAccountEvent(int code, int typeCode){
        this.code = code;
        this.typeCode = typeCode;
    }
}
