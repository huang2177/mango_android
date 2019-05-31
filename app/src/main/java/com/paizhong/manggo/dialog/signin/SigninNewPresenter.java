package com.paizhong.manggo.dialog.signin;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.other.SignBean;
import com.paizhong.manggo.bean.other.SigninListBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by zab on 2018/12/18 0018.
 */
public class SigninNewPresenter extends BasePresenter<SigninNewContract.View> implements SigninNewContract.Presenter{



    //签到
    @Override
    public void saveSign() {
        toSubscribe(HttpManager.getApi().saveSign(), new HttpSubscriber<SignBean>() {
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
            protected void _onNext(SignBean signBean) {
                super._onNext(signBean);
                mView.bindSign(signBean);
            }
        });
    }

    //签到
    @Override
    public void saveSign2() {
        toSubscribe(HttpManager.getApi().saveSign(), new HttpSubscriber<SignBean>() {
            @Override
            protected void _onNext(SignBean signBean) {
                super._onNext(signBean);
                mView.bindSign(signBean);
            }
        });
    }


    //幸运抽奖
    @Override
    public void drawPrize(String userId) {
        toSubscribe(HttpManager.getApi().drawPrize(userId), new HttpSubscriber<Integer>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindDrawPrize(-1);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(Integer integer) {
                super._onNext(integer);
                mView.bindDrawPrize(integer);
            }
        });
    }
}
