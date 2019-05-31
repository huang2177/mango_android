package com.paizhong.manggo.events;

/**
 * Des:
 * Created by hs on 2018/7/16 0016 14:49
 */
public class CancelFollowEvent {
    public int code; // 0 牛人详情  1 跟买广场
    public int position; //跟买广场所对应的position

    public CancelFollowEvent(int eventCode) {
        code = eventCode;
    }

    public CancelFollowEvent(int eventCode, int position) {
        code = eventCode;
        this.position = position;
    }
}
