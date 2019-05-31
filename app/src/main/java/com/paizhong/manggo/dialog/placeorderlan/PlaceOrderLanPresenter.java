package com.paizhong.manggo.dialog.placeorderlan;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Created by zab on 2018/9/6 0006.
 */
public class PlaceOrderLanPresenter extends BasePresenter<PlaceOrderLanContract.View> implements PlaceOrderLanContract.Presenter{

    //单个产品信息接口
    @Override
    public void getSingleProduct(String productID) {
        toSubscribe(HttpManager.getApi().getSingleProduct(productID), new HttpSubscriber<List<ProductListBean>>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindHttpError();
            }
            @Override
            protected void _onNext(List<ProductListBean> productListBeans) {
                super._onNext(productListBeans);
                if (productListBeans !=null && productListBeans.size() > 0){
                    mView.bindSingleProduct(productListBeans.get(0));
                }else {
                    mView.bindHttpError();
                }
            }
        });
    }



    //单个产品时间和做单比例
    @Override
    public void getSingleTime(String code) {
        toSubscribe(HttpManager.getApi().getSingleTime(code,true), new HttpSubscriber<ProductTimeLimitBean>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindSingleTime(null);
            }
            @Override
            protected void _onNext(ProductTimeLimitBean productTimeLimitBean) {
                mView.bindSingleTime(productTimeLimitBean);
            }
        });
    }


    //是不是牛人
    @Override
    public void getValidRole() {
        toSubscribe(HttpManager.getApi().getValidRole(), new HttpSubscriber<Boolean>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindValidRole(false);
            }

            @Override
            protected void _onNext(Boolean aBoolean) {
                super._onNext(aBoolean);
                mView.bindValidRole(aBoolean);
            }
        });
    }


    //资金信息
    @Override
    public void getUserInfo(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.bindHttpError();
            }

            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                if (userZJBean !=null && userZJBean.isSuccess()){
                    mView.bindUserInfo(userZJBean);
                }else {
                    mView.bindHttpError();
                }
            }
        });
    }

    //刷新资金
    @Override
    public void getUserInfoRefresh(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                if (userZJBean !=null && userZJBean.isSuccess()){
                    mView.bindUserInfoRefresh(userZJBean);
                }
            }
        });
    }


    //单个行情接口
    @Override
    public void getReal(String type) {
        //单个行情
        toSubscribe(HttpManager.getApi().getReal(type, true), new HttpSubscriber<MarketHQBean>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindHttpError();
            }

            @Override
            protected void _onNext(MarketHQBean marketAtBean) {
                super._onNext(marketAtBean);
                mView.bindATPriceQuotation(marketAtBean);
            }
        });
    }




    /**
     * 现金下单
     * @param token 	交易所token
     * @param id   	大产品id
     * @param secret_access_key 	交易所secret_access_key
     * @param productId 产品id
     * @param quantity 建仓数量
     * @param flag 0-买涨，1:买跌 允许值: 0, 1
     * @param profitLimit
     * @param lossLimit
     * @param amount    	下单金额
     * @param holdNight   是否持仓 0：:不持仓 1：持仓
     * @param productName
     * @param fee   	交易手续费
     * @param code
     * @param followType  是否跟买 0-否 1-是
     * @param nickName
     */
    @Override
    public void getCashPlaceOrder(String token, String id, String secret_access_key, String productId, int quantity, int flag, int profitLimit,
                                  int lossLimit, int amount, int holdNight, String productName, double fee, String code, int followType, String nickName) {

        toSubscribe(HttpManager.getApi().getCashPlaceOrder(token, id, secret_access_key, productId, quantity, flag, profitLimit, lossLimit, amount,
                holdNight, productName, fee, code, followType, nickName), new HttpSubscriber<CashPlaceOrderBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean cashPlaceOrderBean) {
                super._onNext(cashPlaceOrderBean);
                if (cashPlaceOrderBean !=null){
                    if (cashPlaceOrderBean.isSuccess()){
                        mView.bindPlaceOrder(0,cashPlaceOrderBean.score,cashPlaceOrderBean.tScore);
                    }else {
                        mView.showErrorMsg(cashPlaceOrderBean.desc,null);
                    }
                }
            }
        });
    }


    /**
     *代金券下单
     * @param token  交易所token
     * @param id     大产品id
     * @param secret_access_key 交易所secret_access_key
     * @param productId 产品id
     * @param quantity  建仓数量
     * @param flag  0-买涨，1:买跌 允许值: 0, 1
     * @param profitLimit
     * @param lossLimit
     * @param couponId  体验券id
     * @param amount
     * @param productName
     * @param fee    交易手续费
     * @param code
     */
    @Override
    public void getVoucherPlaceOrder(String token, String id, String secret_access_key, String productId, int quantity, int flag, int profitLimit,
                                     int lossLimit, String couponId, int amount, String productName, double fee, String code) {

        toSubscribe(HttpManager.getApi().getVoucherPlaceOrder(token, id, secret_access_key, productId, quantity, flag, profitLimit, lossLimit,
                couponId, amount, productName, fee, code), new HttpSubscriber<CashPlaceOrderBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean cashPlaceOrderBean) {
                super._onNext(cashPlaceOrderBean);
                if (cashPlaceOrderBean !=null){
                    if (cashPlaceOrderBean.isSuccess()){
                        mView.bindPlaceOrder(1,cashPlaceOrderBean.score,cashPlaceOrderBean.tScore);
                    }else {
                        mView.showErrorMsg(cashPlaceOrderBean.desc,null);
                    }
                }
            }

        });
    }



    //挂单下单
    @Override
    public void getHangUpOrder(String token, String id, String productId, int quantity, int flag, int profitLimit, int lossLimit, int amount,
                               int holdNight, String productName, double fee, String code, String nickName, String floatNum, String registerAmount) {
        toSubscribe(HttpManager.getApi().getHangUpOrder(token, id, productId, quantity, flag, profitLimit, lossLimit, amount, holdNight, productName, fee
                , code, nickName, floatNum, registerAmount), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
                mView.bindPlaceOrder(6,0,null);
            }
        });
    }

}
