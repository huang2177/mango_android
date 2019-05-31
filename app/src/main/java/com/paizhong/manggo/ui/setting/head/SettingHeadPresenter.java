package com.paizhong.manggo.ui.setting.head;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.other.HeadImageBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.google.gson.JsonNull;

import java.util.List;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class SettingHeadPresenter extends BasePresenter<SettingHeadContract.View> implements SettingHeadContract.Presenter{

    @Override
    public void getImageList(final boolean show,String phone) {
        toSubscribe(HttpManager.getApi().getImageList(phone), new HttpSubscriber<List<HeadImageBean>>() {
            @Override
            protected void _onStart() {
                super._onStart();
                if (show){
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(List<HeadImageBean> strings) {
                super._onNext(strings);
                mView.bindImageList(strings);
            }
        });
    }

    @Override
    public void updateHeadPic(String userId,final String userPic,int genius,String phone) {
        toSubscribe(HttpManager.getApi().updateHeadPic(userId, userPic,genius,phone), new HttpSubscriber<JsonNull>() {

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
                mView.bindUpdateHeadPic(userPic);
            }
        });
    }
}
