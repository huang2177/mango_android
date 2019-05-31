package com.paizhong.manggo.events;

/**
 * Des:
 * Created by huang on 2018/8/30 0030 14:58
 */
public class BankListEvent {
    public int code; // 1绑卡 2解绑
    public String account;
    public int position;

    public BankListEvent(int code, String account) {
        this.code = code;
        this.account = account;
    }

    public BankListEvent(int code, String account, int position) {
        this.code = code;
        this.account = account;
        this.position = position;
    }
}
