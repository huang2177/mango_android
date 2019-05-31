package com.paizhong.manggo.ui.setting.main;

import com.paizhong.manggo.base.BaseView;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface SettingMainContract {

    interface View extends BaseView {
        void outLogin();
        void bindValidRole(boolean validRole);
        void updateHeadPic(String userPic);
    }
    interface Presenter {
        void outLogin(String phone);
        void getValidRole(boolean mIShow);
    }
}
