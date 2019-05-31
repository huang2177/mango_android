package com.paizhong.manggo.ui.home.emergency;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.home.CalendarDetailBean;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.bean.home.EmergencyDetailBean;

/**
 * Created by zab on 2018/6/29 0029.
 */
public interface EmergencyContract {

    interface View extends BaseView {
        void bindEmergencyDetail(EmergencyDetailBean bean);
    }

    interface Presenter {
        void getEmergencyDetail(String url);
    }
}
