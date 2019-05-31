package com.paizhong.manggo.ui.voucher;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.zj.VoucherGqZJBean;
import com.paizhong.manggo.bean.zj.VoucherKyZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.TimeUtil;
import java.util.List;

/**
 * Created by zab on 2018/6/28 0028.
 */
public class VoucherZjPresenter extends BasePresenter<VoucherZjContract.View> implements VoucherZjContract.Presenter{
    //可用代金券列表
    @Override
    public void getUserVoucherZjList(String token, String userId) {
        toSubscribe(HttpManager.getApi().getUserVoucherZjList(token, userId), new HttpSubscriber<List<VoucherKyZJBean>>() {

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(List<VoucherKyZJBean> voucherKyZJBeans) {
                super._onNext(voucherKyZJBeans);
                if (voucherKyZJBeans !=null && voucherKyZJBeans.size() > 0){
                    for (VoucherKyZJBean zjBean : voucherKyZJBeans){
                        zjBean.amount = zjBean.amount / Constant.ZJ_PRICE_COMPANY;
                        zjBean.end_time  = TimeUtil.timeZoneConversionYMD(zjBean.end_time);
                    }
                }
                mView.bindUserVoucherZjList(voucherKyZJBeans);
            }
        });
    }



    @Override
    public void getHistoryCouponsList(String token,final int page, int pageSize) {
        toSubscribe(HttpManager.getApi().getHistoryCouponsList(token, page, pageSize), new HttpSubscriber<VoucherGqZJBean>() {

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(VoucherGqZJBean voucherGqZJBean) {
                super._onNext(voucherGqZJBean);
                if (voucherGqZJBean !=null
                        && voucherGqZJBean.isSuccess()
                        && voucherGqZJBean.data !=null
                        && voucherGqZJBean.data.size() > 0){

                    for (VoucherKyZJBean zjBean : voucherGqZJBean.data) {
                        zjBean.amount = zjBean.amount / Constant.ZJ_PRICE_COMPANY;
                        zjBean.end_time  = TimeUtil.timeZoneConversionYMD(zjBean.end_time);
                    }
                    mView.bindUserGqVoucherZjList(page,voucherGqZJBean.data.size(),voucherGqZJBean.data);
                }else {
                    mView.bindUserGqVoucherZjList(page,0,null);
                }
            }
        });
    }
}
