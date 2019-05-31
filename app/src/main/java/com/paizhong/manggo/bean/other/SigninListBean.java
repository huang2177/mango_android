package com.paizhong.manggo.bean.other;

import java.util.List;

/**
 * Created by zab on 2018/6/20 0020.
 */
public class SigninListBean {
    public String score; //没签到的积分
    public String tomorrowScore;//签到了的积分
    public int totalDay;//累计签到天数
    public int flag; //1显示图片 0不显示
    public List<SigninItemBean> data;


    public class SigninItemBean{
        public boolean checked;
        public long time;
        public int status;
    }
}
