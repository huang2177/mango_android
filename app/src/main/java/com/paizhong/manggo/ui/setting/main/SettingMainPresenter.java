package com.paizhong.manggo.ui.setting.main;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class SettingMainPresenter extends BasePresenter<SettingMainContract.View> implements SettingMainContract.Presenter{

    @Override
    public void outLogin(String phone) {
        toSubscribe(HttpManager.getApi().outLogin(phone), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
                mView.outLogin();
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
            }
        });
    }

    //判断是不是牛人
    @Override
    public void getValidRole(final boolean mIShow) {
        toSubscribe(HttpManager.getApi().getValidRole(), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                if (mIShow){
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }


            @Override
            protected void _onError(String message) {
                super._onError(message);
                if (mIShow){
                    mView.showErrorMsg("初始化数据失败",null);
                }
            }

            @Override
            protected void _onNext(Boolean aBoolean) {
                super._onNext(aBoolean);
                mView.bindValidRole(aBoolean);
            }
        });
    }

}
