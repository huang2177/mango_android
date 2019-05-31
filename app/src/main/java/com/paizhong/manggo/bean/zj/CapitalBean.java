package com.paizhong.manggo.bean.zj;

import com.paizhong.manggo.bean.trade.CapitalListBean;

import java.util.List;

/**
 * 交易 - 资金 - 交易记录
 * Created by zab on 2018/4/5 0005.
 */

public class CapitalBean {

    public int code;
    public String desc;
    public List<CapitalListBean> data;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }
}
