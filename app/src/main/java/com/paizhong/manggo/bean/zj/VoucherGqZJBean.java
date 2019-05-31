package com.paizhong.manggo.bean.zj;

import java.util.List;

/**
 * Created by zab on 2018/6/29 0029.
 */
public class VoucherGqZJBean {
    public int code;
    public String desc;
    public List<VoucherKyZJBean> data;

    public boolean isSuccess(){
        return code == 0 ? true : false;
    }
}
