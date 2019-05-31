package com.paizhong.manggo.bean.zj;

import com.google.gson.JsonObject;

/**
 * 登录/注册交易所
 * Created by zab on 2018/4/17 0017.
 */

public class UserZJRegisterBean {

    /**
     * token : tk:RtFWi-C6CjBOGdpe7u9P8ww0wObho6h9VN2hTyy_mjc
     * secret_access_key : AbNwkWjdZ9YGDezEbK5kkYh2Ksu3OzFD6ca9-IKaKk8nL_ks7-5mhozLXzjerIKY
     */

    public int code;
    public String desc;
    public AiPeiDateBean data;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }

    public class  AiPeiDateBean{
        public String token;
        public String secret_access_key;
        public String userId;

        public String errstr;
        public JsonObject err;
    }
}
