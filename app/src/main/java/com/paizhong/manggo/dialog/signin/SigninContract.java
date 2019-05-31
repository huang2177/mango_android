package com.paizhong.manggo.dialog.signin;
import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.other.SigninListBean;
import com.paizhong.manggo.bean.user.IntegralBean;

import java.util.List;

/**
 * Created by zab on 2018/8/13 0013.
 */
public interface SigninContract {
    interface View extends BaseView {
        void bindSign();
        void bindUserScore(IntegralBean integralBean);
        void bindSigninList(SigninListBean signinBean);
        void bindSigninError();
    }

    interface Presenter {
        void saveSign();

        void getUserIntegral(String token,String userId,String phone);

        void getSigninList();
    }
}
