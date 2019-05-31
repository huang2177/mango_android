package com.paizhong.manggo.bean.zj;
import java.util.List;

/**
 * 中金 账户信息
 * Created by zab on 2018/4/17 0017.
 */

public class UserZJBean {


    /**
     * code : 0
     * data : {"nick":"51淘金","ballance":5000000,"coupons":[{"amount":800,"name":"8元体验券(注册送券)","end_time":"2018-04-18T20:00:00.000Z","begin_time":"2018-04-12T05:01:49.000Z","id":1327}],"phone":"13062789856","userId":1001752,"points":0}
     * desc : success
     */

    public int code;
    public DataBean data;
    public String desc;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }

    public class DataBean {
        /**
         * nick : 51淘金
         * ballance : 5000000
         * coupons : [{"amount":800,"name":"8元体验券(注册送券)","end_time":"2018-04-18T20:00:00.000Z","begin_time":"2018-04-12T05:01:49.000Z","id":1327}]
         * phone : 13062789856
         * userId : 1001752
         * points : 0
         */

        public String nick;
        public double ballance;
        public String phone;
        public String userId;
        public String points;
        public List<CouponsBean> coupons;


        public class CouponsBean {
            /**
             * amount : 800
             * name : 8元体验券(注册送券)
             * end_time : 2018-04-18T20:00:00.000Z
             * begin_time : 2018-04-12T05:01:49.000Z
             * id : 1327
             */

            public int amount;
            public String name;
            public String end_time;
            public String begin_time;
            public String id;
        }
    }
}
