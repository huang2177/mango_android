package com.paizhong.manggo.ui.setting.name;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.google.gson.JsonNull;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class SettingNamePresenter extends BasePresenter<SettingNameContract.View> implements SettingNameContract.Presenter{

    @Override
    public void updateNickName(String userId,final String nickName,int genius,String phone) {
        toSubscribe(HttpManager.getApi().updateNickName(userId, nickName,genius,phone), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
                mView.stopLoading();
                mView.bindNickName(nickName);
            }
        });
    }
}
