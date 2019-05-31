package com.paizhong.manggo.dialog.positionzyzs;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.AtBaseBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class PositionZyzsPresenter extends BasePresenter<PositionZyzsContract.View> implements PositionZyzsContract.Presenter {


    //止损止盈
    @Override
    public void getProfitLossLimit(String token, String secret_access_key, String orderId, int profitLimit, int lossLimit ,String ticketId,String orderNo) {
        toSubscribe(HttpManager.getApi().getProfitLossLimit(token, secret_access_key, orderId, profitLimit, lossLimit,ticketId,orderNo), new HttpSubscriber<AtBaseBean>() {
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
            protected void _onNext(AtBaseBean atBaseBean) {
                super._onNext(atBaseBean);
                mView.bindProfitLossLimit(atBaseBean);
            }
        });
    }
}
