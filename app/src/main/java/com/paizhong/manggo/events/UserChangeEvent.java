package com.paizhong.manggo.events;

/**
 * Created by zab on 2018/6/11 0011.
 */

public class UserChangeEvent {
    // type 0头像  1名字
    public int type;
    public String phone;
    public String content;

    public UserChangeEvent(int type, String phone, String content){
        this.type = type;
        this.phone = phone;
        this.content = content;
    }
}
