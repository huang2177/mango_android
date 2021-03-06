package com.paizhong.manggo.ui.account.login;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/3/28 0028.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{

    /**
     * 发送验证码
     * @param phone
     */
    @Override
    public void sendSms(String phone) {
        toSubscribe(HttpManager.getApi().sendSms(phone), new HttpSubscriber<JsonNull>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            public void onNext(JsonNull jsonNull) {
                super.onNext(jsonNull);
            }

        });
    }

    /**
     * 登录
     * @param phone
     * @param smsCode
     * @param nickName
     */
    @Override
    public void loginApp(String phone, String smsCode, String nickName) {
        toSubscribe(HttpManager.getApi().loginApp(phone, smsCode, nickName, ""), new HttpSubscriber<LoginUserBean>() {

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
            protected void _onError(String message) {
                super._onError(message);
                mView.showErrorMsg(message,null);
            }

            @Override
            public void onNext(LoginUserBean loginUserBean) {
                mView.loginAppSucc(loginUserBean);
            }
        });
    }
}
