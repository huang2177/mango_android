package com.paizhong.manggo.events;

import com.paizhong.manggo.bean.user.LoginUserBean;

/**
 * 登录退出相关
 * Created by zab on 2018/4/11 0011.
 */

public class AppAccountEvent {
    public int code; // 0退出 1登录
    public LoginUserBean loginUserBean;

    public AppAccountEvent(int code, LoginUserBean loginUserBean){
        this.code = code;
        this.loginUserBean = loginUserBean;
    }
}
