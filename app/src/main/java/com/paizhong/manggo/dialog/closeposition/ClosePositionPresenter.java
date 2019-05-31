package com.paizhong.manggo.dialog.closeposition;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.AtBaseBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class ClosePositionPresenter extends BasePresenter<ClosePositionContract.View> implements ClosePositionContract.Presenter {


    //平仓
    @Override
    public void getTransfer(String token, String orderId, String secret_access_key) {
       toSubscribe(HttpManager.getApi().getTransfer(token, orderId, secret_access_key), new HttpSubscriber<AtBaseBean>() {
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
           protected void _onNext(AtBaseBean atBaseBean) {
               super._onNext(atBaseBean);
               mView.bindTransfer(atBaseBean);
           }
       });
    }
}
