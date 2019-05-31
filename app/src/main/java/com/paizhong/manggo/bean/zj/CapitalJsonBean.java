package com.paizhong.manggo.bean.zj;

import com.paizhong.manggo.bean.trade.CapitalListBean;

import java.util.List;

/**
 * 交易 - 资金 - 交易记录
 * Created by zab on 2018/4/5 0005.
 */

public class CapitalJsonBean {

    public int code;
    public String desc;
    public ListJsonBean data;

    public boolean isSuccess(){
        return code == 0;
    }


    public class ListJsonBean{
        public List<CapitalListBean> data;
        public int count;
    }
}
