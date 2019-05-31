package com.paizhong.manggo.ui.main;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.other.UpdateAppBean;

/**
 * Created by zab on 2018/8/15 0015.
 */
public interface MainContract {

    interface View extends BaseView {
       void bindUpdateApp(UpdateAppBean updateAppBean);
        void bindValidIsUpPop();
    }
    interface Presenter {
        void updateApp();
        void validIsUpPop();
    }
}
