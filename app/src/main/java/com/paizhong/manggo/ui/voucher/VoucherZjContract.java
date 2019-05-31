package com.paizhong.manggo.ui.voucher;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.zj.VoucherKyZJBean;

import java.util.List;

/**
 * Created by zab on 2018/6/28 0028.
 */
public interface VoucherZjContract {
    interface View extends BaseView {
        void bindUserVoucherZjList(List<VoucherKyZJBean> voucherList);

        void bindUserGqVoucherZjList(int page,int listSize,List<VoucherKyZJBean> voucherList);
    }
    interface Presenter {
        void getUserVoucherZjList(String token,String userId);

        void getHistoryCouponsList(String token,int page,int pageSize);
    }
}
