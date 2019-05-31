package com.paizhong.manggo.events;

/**
 * Des:
 * Created by huang on 2018/9/11 0011 12:13
 */
public class NewGuideEvent {
    public int code; // 0 新人专享dialog消失  1 新人代金券下单成功   2 登录dialog消失

    public NewGuideEvent(int code) {
        this.code = code;
    }
}
