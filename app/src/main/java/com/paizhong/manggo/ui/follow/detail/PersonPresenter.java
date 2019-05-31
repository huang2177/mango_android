package com.paizhong.manggo.ui.follow.detail;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.PersonInfoBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Des:
 * Created by hs on 2018/6/4 0004 12:20
 */
public class PersonPresenter extends BasePresenter<PersonContract.View> implements PersonContract.Presenter {

    @Override
    public void getPersonInfo(String phone) {
        toSubscribe(HttpManager.getApi().getPersonInfo(phone), new HttpSubscriber<PersonInfoBean>() {

            @Override
            public void onNext(PersonInfoBean infoBean) {
                if (infoBean == null) {
                    return;
                }
                infoBean.profit = infoBean.profit.contains(".")
                        ? infoBean.profit.substring(0, infoBean.profit.indexOf("."))
                        : infoBean.profit;
                mView.bindPersonInfo(infoBean);
            }
        });
    }

    @Override
    public void addConcern(String concernPhone, int isConcern) {
        toSubscribe(HttpManager.getApi().addConcern(concernPhone, isConcern + ""), new HttpSubscriber() {
            @Override
            protected void _onNext(Object o) {
                mView.bindConcern();
            }
        });
    }
}
