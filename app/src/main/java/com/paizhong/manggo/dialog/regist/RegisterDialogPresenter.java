package com.paizhong.manggo.dialog.regist;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.bean.zj.UserZJCheckBean;
import com.paizhong.manggo.bean.zj.UserZJRegisterBean;
import com.paizhong.manggo.bean.zj.UserZJSmsBean;
import com.paizhong.manggo.bean.zj.UserZJSmsCaptchaBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

public class RegisterDialogPresenter extends BasePresenter<RegisterDialogContract.View> implements RegisterDialogContract.Presenter{

    /**
     * 发送验证码
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
                mView.smsSuccess(true);
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
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            public void onNext(LoginUserBean loginUserBean) {
                mView.loginAppSucc(loginUserBean);
            }
        });
    }

    //检查中金 账号是否在交易所 注册过
    @Override
    public void getUserZjCheck(String mobile, String codeType) {
        toSubscribe(HttpManager.getApi().getUserZjCheck(mobile, codeType), new HttpSubscriber<UserZJCheckBean>() {

            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(UserZJCheckBean userZJCheckBean) {
                mView.stopLoading();
                mView.bindUserZjCheck(userZJCheckBean);
            }
        });
    }

    //中金 注册交易所
    @Override
    public void getUserZjRegister(String mobile, String password) {
        toSubscribe(HttpManager.getApi().getZjRegister(mobile, password), new HttpSubscriber<UserZJRegisterBean>() {
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
                mView.stopLoading();
            }

            @Override
            protected void _onNext(UserZJRegisterBean userZJRegisterBean) {
                super._onNext(userZJRegisterBean);
                mView.getUserZjRegister(userZJRegisterBean);
            }
        });
    }

    //中金 登录
    @Override
    public void getUserZjLogin(String mobile, String password) {
        toSubscribe(HttpManager.getApi().getUserZjLogin(mobile, password), new HttpSubscriber<UserZJRegisterBean>() {
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
                mView.stopLoading();
            }

            @Override
            protected void _onNext(UserZJRegisterBean userZJRegisterBean) {
                super._onNext(userZJRegisterBean);
                mView.getUserZjRegister(userZJRegisterBean);
            }
        });
    }

    //获取图片验证码
    @Override
    public void getUserZjSmsCaptcha() {
        toSubscribe(HttpManager.getApi().getUserZjSmsCaptcha(), new HttpSubscriber<UserZJSmsCaptchaBean>() {

            @Override
            protected void _onNext(UserZJSmsCaptchaBean captchaBean) {
                super._onNext(captchaBean);
                if (captchaBean !=null
                        && captchaBean.result !=null
                        && captchaBean.result.isSuccess()
                        && captchaBean.result.data !=null){
                    mView.bindUserZjSmsCaptcha(captchaBean.token,captchaBean.result.data.src);
                }
            }
        });
    }

    //获取验证码  含token 验证码
    @Override
    public void getForgetSmsCode(String mobile, String captcha ,String token) {
        toSubscribe(HttpManager.getApi().getForgetSmsCode(mobile, captcha,token), new HttpSubscriber<UserZJSmsBean>() {
            @Override
            protected void _onNext(UserZJSmsBean userZjSmsBean) {
                super._onNext(userZjSmsBean);
            }
        });
    }

    //忘记、修改密码
    @Override
    public void getUserZjForgot(String mobile, String password, String smsCode,String token) {
        toSubscribe(HttpManager.getApi().getUserZjForgot(mobile, password, smsCode,token), new HttpSubscriber<UserZJRegisterBean>() {
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
                mView.stopLoading();
            }

            @Override
            protected void _onNext(UserZJRegisterBean userZJRegisterBean) {
                super._onNext(userZJRegisterBean);
                mView.getUserZjRegister(userZJRegisterBean);
            }

        });
    }

}
