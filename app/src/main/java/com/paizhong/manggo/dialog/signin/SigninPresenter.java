package com.paizhong.manggo.dialog.signin;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.other.SigninListBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by zab on 2018/8/13 0013.
 */
public class SigninPresenter extends BasePresenter<SigninContract.View> implements SigninContract.Presenter{


    @Override
    public void saveSign() {
        toSubscribe(HttpManager.getApi().saveSign(), new HttpSubscriber<JsonNull>() {
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
                mView.bindSign();
            }
        });
    }

    @Override
    public void getUserIntegral(String token, String userId, String phone) {
        toSubscribe(HttpManager.getApi().getUserIntegral(token, userId, phone), new HttpSubscriber<IntegralBean>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindSigninError();
            }

            @Override
            protected void _onNext(IntegralBean integralBean) {
                super._onNext(integralBean);
                mView.bindUserScore(integralBean);
            }
        });
    }


    @Override
    public void getSigninList() {
        toSubscribe(HttpManager.getApi().getSigninList(), new HttpSubscriber<SigninListBean>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindSigninError();
            }

            @Override
            protected void _onNext(SigninListBean signinBean) {
                super._onNext(signinBean);

                Collections.sort(signinBean.data, new Comparator<SigninListBean.SigninItemBean>() {
                    @Override
                    public int compare(SigninListBean.SigninItemBean o1, SigninListBean.SigninItemBean o2) {
                        Date date1 = new Date(o1.time);
                        Date date2 = new Date(o2.time);
                        // 对日期字段进行升序，如果欲降序可采用after方法
                        if (date1.after(date2)){
                            return 1;
                        }
                        return -1;
                    }
                });
                mView.bindSigninList(signinBean);
            }
        });
    }
}

