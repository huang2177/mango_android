package com.paizhong.manggo.dialog.sendmsg;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;


public class SendMsgPresenter extends BasePresenter<SendMsgContract.View> implements SendMsgContract.Presenter{

    //发送消息
    @Override
    public void getMsgProducer(String phone, String content, String name) {
        toSubscribe(HttpManager.getApi().msgProducer(phone, content, name), new HttpSubscriber<JsonNull>() {
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
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
                mView.bindMsgProducer();
            }
        });
    }
}
