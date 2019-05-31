package com.paizhong.manggo.bean.trade;

import java.util.List;

/**
 * 交易-建仓 产品列表
 * Created by zab on 2018/4/3 0003.
 */

public class OpenPositionBean {

    /**
     * img : http://image100.oss-cn-shanghai.aliyuncs.com/EciAr1522132598259?x-oss-process=style/large
     * name : GL银
     * close_msg : 此产品尚未开放
     * colse_status : 0 休市  1不休市
     * id : 11
     * items : [{"flu_price":10,"hold_fee":10,"img":"http://image100.oss-cn-shanghai.aliyuncs.com/EciAr1522132598259?x-oss-process=style/large","max_order_num":30,"fee":70,"fall_points":"[75,30,40,50,60]","type":11,"points":0,"update_time":"2018-04-11T03:43:55.000Z","is_deleted":0,"price":800,"flu_price_display":0,"id":10107,"create_time":"2018-03-27T06:38:57.000Z","cat_name":"GL银","flu_unit":"10","is_close":0,"source_type":11,"is_show":1,"cat_close_msg":"此产品尚未开放","min_order_num":10,"name":"GL银0.1kg","close_msg":"","shop_goods_number":0,"cat_is_close":0,"rise_points":"[75,30,40,60,100]"},{"flu_price":100,"hold_fee":120,"img":"http://image100.oss-cn-shanghai.aliyuncs.com/EciAr1522132598259?x-oss-process=style/large","max_order_num":30,"fee":1000,"fall_points":"[76,30,40,50,60]","type":11,"points":0,"update_time":"2018-04-11T03:44:00.000Z","is_deleted":0,"price":10000,"flu_price_display":0,"id":10108,"create_time":"2018-03-27T06:41:31.000Z","cat_name":"GL银","flu_unit":"10","is_close":0,"source_type":11,"is_show":1,"cat_close_msg":"此产品尚未开放","min_order_num":10,"name":"GL银1kg","close_msg":"","shop_goods_number":0,"cat_is_close":0,"rise_points":"[76,30,40,60,100]"},{"flu_price":1000,"hold_fee":600,"img":"http://image100.oss-cn-shanghai.aliyuncs.com/EciAr1522132598259?x-oss-process=style/large","max_order_num":30,"fee":4800,"fall_points":"[76,30,40,50,60]","type":11,"points":0,"update_time":"2018-04-11T03:44:05.000Z","is_deleted":0,"price":50000,"flu_price_display":0,"id":10109,"create_time":"2018-03-27T06:42:42.000Z","cat_name":"GL银","flu_unit":"10","is_close":0,"source_type":11,"is_show":1,"cat_close_msg":"此产品尚未开放","min_order_num":10,"name":"GL银5kg","close_msg":"","shop_goods_number":0,"cat_is_close":0,"rise_points":"[76,30,40,60,100]"}]
     */

    public int shouType = 1; //1 2 3
    public String img;
    public String name;
    public String close_msg;
    public int colse_status;
    public int is_close; //1休市   0开市
    public int id;
    public String closingPrice;

    public String changeValue;//涨跌值
    public String changeRange;//涨跌幅
    public String stockIndex;//最新价格
    public int buyUp;
    public int toBuy;
    public int rise;//0 跌  1涨
    public boolean isUp = true;

    public String createTime;
    public String modifyTime;

    public String srartEndTimeStr;

    public List<ItemsBean> items;


    public boolean isClose(){
        return is_close == 1 ? true : false;
    }
    public static class ItemsBean {
        /**
         * flu_price : 10
         * hold_fee : 10
         * img : http://image100.oss-cn-shanghai.aliyuncs.com/EciAr1522132598259?x-oss-process=style/large
         * max_order_num : 30
         * fee : 70
         * fall_points : [75,30,40,50,60]
         * type : 11
         * points : 0
         * update_time : 2018-04-11T03:43:55.000Z
         * is_deleted : 0
         * price : 800
         * flu_price_display : 0
         * id : 10107
         * create_time : 2018-03-27T06:38:57.000Z
         * cat_name : GL银
         * flu_unit : 10
         * is_close : 0
         * source_type : 11
         * is_show : 1
         * cat_close_msg : 此产品尚未开放
         * min_order_num : 10
         * name : GL银0.1kg
         * close_msg :
         * shop_goods_number : 0
         * cat_is_close : 0
         * rise_points : [75,30,40,60,100]
         */

        public int flu_price;
        public int hold_fee;
        public String img;
        public int max_order_num;
        public double fee;
        public String fall_points;
        public int type;
        public int points;
        public String update_time;
        public int is_deleted;
        public int price;
        public int flu_price_display;
        public int id;
        public String create_time;
        public String cat_name;
        public String flu_unit;
        public int is_close;
        public int source_type;
        public int is_show;
        public String cat_close_msg;
        public int min_order_num;
        public String name;
        public String close_msg;
        public int shop_goods_number;
        public int cat_is_close;
        public String rise_points;

        public String flu_yk; //每点浮动盈亏
    }
}
