package com.paizhong.manggo.ui.user;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.bean.zj.UserZJBean;

/**
 * Created by zab on 2018/8/15 0015.
 */
public interface UserContract {

    interface View extends BaseView {
        void bindUserIntegral(IntegralBean bean);
        void bindValidRole(boolean validRole);
        void updateHeadPic(String userPic);
        void bindUserInfo(UserZJBean userZJBean);
        void bindPositionUserMoney(Double countAmount, Double countPostAmount , Double countAmountBj);
        void bindUpdateApp(UpdateAppBean updateAppBean);
    }
    interface Presenter {
        void getIntegral(String token, String userId, String phone);
        void getValidRole(boolean mIShow);
        void getUserInfo(String token);
        void getPositionUserMoney(double amount, String token);
        void updateApp();
    }
}
