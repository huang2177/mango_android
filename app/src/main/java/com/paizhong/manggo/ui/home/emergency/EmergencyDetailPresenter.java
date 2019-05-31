package com.paizhong.manggo.ui.home.emergency;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.home.CalendarDetailBean;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.bean.home.EmergencyDetailBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/6/29 0029.
 */
public class EmergencyDetailPresenter extends BasePresenter<EmergencyContract.View> implements EmergencyContract.Presenter {

    @Override
    public void getEmergencyDetail(String url) {
        toSimpSubscribe(HttpManager.getApi().getEmergencyDetail(url, true), new HttpSubscriber<EmergencyDetailBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }

            @Override
            protected void _onNext(EmergencyDetailBean bean) {
                if (bean != null) {
                    mView.bindEmergencyDetail(bean);
                }
            }
        });
    }
}
