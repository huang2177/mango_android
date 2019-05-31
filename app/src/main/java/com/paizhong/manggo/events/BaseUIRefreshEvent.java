package com.paizhong.manggo.events;

/**
 * Created by zab on 2018/9/11 0011.
 */
public class BaseUIRefreshEvent {
    //1 挂单修改 2刷新资金  3跟买红点 4跟买关注提示
    public int mType;

    public String value1;
    public String value2;
    public String value3;

    public BaseUIRefreshEvent(int type){
       this.mType = type;
    }

    public BaseUIRefreshEvent(int type ,String value1,String value2 ,String value3){
        this.mType = type;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }
}
