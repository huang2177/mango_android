package com.paizhong.manggo.ui.home.calendar;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.home.CalendarDetailBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/6/29 0029.
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter {

    @Override
    public void getNewsDetail(String id) {
        toSimpSubscribe(HttpManager.getApi().getNewsDetail(id,true), new HttpSubscriber<CalendarDetailBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }

            @Override
            protected void _onNext(CalendarDetailBean calendarBean) {
                if (calendarBean == null) {
                    return;
                }
                mView.bindNewsDetail(calendarBean);
            }
        });
    }
}
