package com.paizhong.manggo.ui.account.register;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.user.LoginUserBean;

/**
 * Created by zab on 2018/3/28 0028.
 */

public interface RegistContract {
    interface View extends BaseView {
        void loginAppSucc(LoginUserBean loginUserBean);
    }
    interface Presenter {
        void sendSms(String phone);
        void registApp(String phone,String smsCode,String nickName);
    }
}
