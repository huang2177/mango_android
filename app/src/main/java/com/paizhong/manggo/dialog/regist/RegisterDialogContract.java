package com.paizhong.manggo.dialog.regist;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.bean.zj.UserZJCheckBean;
import com.paizhong.manggo.bean.zj.UserZJRegisterBean;

public interface RegisterDialogContract {

    interface View extends BaseView{
        void loginAppSucc(LoginUserBean loginUserBean);
        void bindUserZjCheck(UserZJCheckBean userCheckBean);
        void getUserZjRegister(UserZJRegisterBean userZJRegisterBean);
        void smsSuccess(boolean isSuc);
        void bindUserZjSmsCaptcha(String resultToken, String base64Img);
    }

    interface Presenter {
        void sendSms(String phone);
        void loginApp(String phone, String smsCode, String nickName);
        void getUserZjCheck(String mobile, String codeType);
        void getUserZjRegister(String mobile, String password);
        void getUserZjLogin(String mobile, String password);
        void getUserZjSmsCaptcha();
        void getForgetSmsCode(String mobile, String captcha, String token);
        void getUserZjForgot(String mobile, String password, String smsCode, String token);
    }
}
