package com.paizhong.manggo.ui.splash;

import com.paizhong.manggo.base.BaseView;

/**
 * Created by zab on 2018/8/15 0015.
 */
public interface SplashContract {

    interface View extends BaseView {
         void bindAuditing(boolean mIsAuditing);
    }
    interface Presenter {
        void getAuditing();
    }
}
