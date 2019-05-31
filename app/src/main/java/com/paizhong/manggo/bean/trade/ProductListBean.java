package com.paizhong.manggo.bean.trade;

import java.util.List;

/**
 * 交易产品列表 tab bean
 * Created by zab on 2018/8/20 0020.
 */
public class ProductListBean {

    /**
     * img : http://image100.oss-cn-shanghai.aliyuncs.com/NNjzR1530175397119?x-oss-process=style/large
     * close_hour : 2
     * colse_status : 1
     * name : GL玉米
     * weekend_open : 0
     * is_close : 0
     * id : 14
     * open_hour : 8
     * close_minute : 20
     * items : []
     * product_cat_id : 14
     * open_minute : 0
     */

    public String img;
    public int close_hour;
    public int colse_status;
    public String name;
    public int weekend_open;
    public int is_close; //1休市   0开市
    public String id;
    public int open_hour;
    public int close_minute;
    public int product_cat_id;
    public int open_minute;
    public List<ProducItemBean> items;

    public boolean isClose(){
        return is_close == 1;
    }
}
