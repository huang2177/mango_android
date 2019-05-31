package com.paizhong.manggo.bean.zj;

/**
 * 图片验证码
 * Created by zab on 2018/5/14 0014.
 */

public class UserZJSmsCaptchaBean {

    public ResultDataBean result;
    public String token;

    public class ResultDataBean{
        public int code;
        public DataBean data;
        public String desc;

        public boolean isSuccess(){
            return code == 0 ? true : false;
        }
    }

    public class DataBean {
        public String src;
    }
}
