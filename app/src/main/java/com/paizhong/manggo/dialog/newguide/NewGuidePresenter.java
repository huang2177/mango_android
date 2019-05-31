package com.paizhong.manggo.dialog.newguide;


import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProducItemBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Created by zab on 2018/5/24 0024.
 */

public class NewGuidePresenter extends BasePresenter<NewGuideContract.View> implements NewGuideContract.Presenter {


    /***
     * 单个产品的规格Id
     */
    @Override
    public void getSingleProduct(String productId) {
        toSubscribe(HttpManager.getApi().getSingleProduct(productId), new HttpSubscriber<List<ProductListBean>>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindProductError();
            }

            @Override
            protected void _onNext(List<ProductListBean> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                for (ProducItemBean itemsBean: list.get(0).items) {
                    int amount = itemsBean.price / Constant.ZJ_PRICE_COMPANY;
                    if (amount == 100) {
                        mView.bindSingleProduct(itemsBean.id);
                        break;
                    }
                }
            }
        });
    }


    /***
     * 获取新手券Id
     */
    @Override
    public void getNewUserTicket(String token) {
        toSubscribe(HttpManager.getApi().getNewUserTicket(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onError(String message, int code) {
                mView.bindProductError();
            }

            @Override
            protected void _onNext(UserZJBean userZJBean) {
                mView.bindNewUser();
                mView.bindProductError();
                if (userZJBean != null && userZJBean.isSuccess()
                        && userZJBean.data != null
                        && userZJBean.data.coupons != null
                        && userZJBean.data.coupons.size() > 0) {

                    for (UserZJBean.DataBean.CouponsBean couponsBean: userZJBean.data.coupons) {
                        int amount = couponsBean.amount / Constant.ZJ_PRICE_COMPANY;
                        if (amount == 100) {
                            mView.bindNewUserTicketId(couponsBean.id);
                            break;
                        }
                    }
                }
            }
        });
    }


    //单个产品行情
    @Override
    public void getSingleMarket(String code) {
        toSubscribe(HttpManager.getApi().getReal(code, true), new HttpSubscriber<MarketHQBean>() {
            @Override
            protected void _onNext(MarketHQBean marketZJBean) {
                if (marketZJBean != null && !TextUtils.isEmpty(marketZJBean.point)) {
                    mView.bindSingleMarket(marketZJBean);
                }
            }
        });
    }

    //代金券建仓
    @Override
    public void getVoucherPlaceOrder(String token
            , String id, String secret_access_key, String productId, int quantity
            , int flag, int profitLimit, int lossLimit, String couponId, int amount, String productName, double fee, String code) {

        toSubscribe(HttpManager.getApi().getVoucherPlaceOrder(token, id, secret_access_key, productId, quantity
                , flag, profitLimit, lossLimit, couponId, amount, productName, fee, code), new HttpSubscriber<CashPlaceOrderBean>() {

            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean bean) {
                mView.stopLoading();
                if (bean != null && bean.isSuccess()) {
                    mView.bindCashPlaceOrder(bean);
                } else {
                    mView.showErrorMsg(bean.desc, null);
                }
            }
        });
    }
}
