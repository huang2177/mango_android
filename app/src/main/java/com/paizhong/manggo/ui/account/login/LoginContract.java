package com.paizhong.manggo.ui.account.login;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.user.LoginUserBean;

/**
 * Created by zab on 2018/3/28 0028.
 */

public interface LoginContract {

    interface View extends BaseView {
        void loginAppSucc(LoginUserBean loginUserBean);
    }
    interface Presenter {
        void sendSms(String phone);
        void loginApp(String phone,String smsCode,String nickName);
    }
}
