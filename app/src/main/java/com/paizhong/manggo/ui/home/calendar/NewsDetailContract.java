package com.paizhong.manggo.ui.home.calendar;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.home.CalendarDetailBean;

/**
 * Created by zab on 2018/6/29 0029.
 */
public interface NewsDetailContract {

    interface View extends BaseView {
        void bindNewsDetail(CalendarDetailBean calendarBean);
    }

    interface Presenter {
        void getNewsDetail(String id);
    }
}
