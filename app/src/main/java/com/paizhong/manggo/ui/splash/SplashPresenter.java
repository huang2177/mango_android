package com.paizhong.manggo.ui.splash;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{

    //屏蔽接口
    @Override
    public void getAuditing() {
        toSubscribe(HttpManager.getApi().selectShield(), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindAuditing(false);
            }

            @Override
            protected void _onNext(Boolean aBoolean) {
                super._onNext(aBoolean);
                mView.bindAuditing(aBoolean);
            }
        });
    }
}
