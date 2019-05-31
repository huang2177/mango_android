package com.paizhong.manggo.bean.zj;

/**
 * Des:
 * Created by huang on 2018/8/30 0030 13:42
 */
public class BankListBean {

    /**
     * id : null
     * phone : 15378412400
     * account : 6217003580002365460
     * remark : null
     * createTime : null
     * modifyTime : null
     */

    public Object id;
    public String phone;
    public String account;
    public Object remark;
    public Object createTime;
    public Object modifyTime;


    public BankListBean(String account) {
        this.account = account;
    }
}
