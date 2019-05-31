package com.paizhong.manggo.dialog.placeordermarket;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public class PlaceOrderMarketPresenter extends BasePresenter<PlaceOrderMarketContract.View> implements PlaceOrderMarketContract.Presenter {


    //全部产品信息
    @Override
    public void getProductList() {
        toSubscribe(HttpManager.getApi().getProductList(true), new HttpSubscriber<List<ProductListBean>>() {
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
            protected void _onNext(List<ProductListBean> productListBeanList) {
                super._onNext(productListBeanList);
                mView.bindSingleProduct(productListBeanList);
            }
        });
    }


    //全部做单比例
    @Override
    public void getProductTime() {
       toSubscribe(HttpManager.getApi().getProductTime(true), new HttpSubscriber<List<ProductTimeLimitBean>>() {
           @Override
           public void onError(Throwable e) {
               super.onError(e);
               mView.bindHttpError();
           }

           @Override
           protected void _onNext(List<ProductTimeLimitBean> productTimeLimitBeans) {
               super._onNext(productTimeLimitBeans);
               mView.bindProductTime(productTimeLimitBeans);
           }
       });
    }


    //资金信息
    @Override
    public void getUserInfo(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
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
