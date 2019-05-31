package com.paizhong.manggo.ui.follow.follower;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.MyFollowerBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/6/4 0004 12:20
 */
public class MyFollowerPresenter extends BasePresenter<MyFollowerContract.View> implements MyFollowerContract.Presenter {

    @Override
    public void getFollowerList(final int pageIndex, int pageSize, final boolean showLoading) {
        toSubscribe(HttpManager.getApi().getMyFollowerList(pageIndex + "", pageSize + "")
                , new HttpSubscriber<List<MyFollowerBean>>() {
                    @Override
                    public void onStart() {
                        if (showLoading) {
                            mView.showLoading("");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.stopLoading();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.stopLoading();
                    }

                    @Override
                    public void onNext(List<MyFollowerBean> followerBeans) {
                        if (followerBeans == null || followerBeans.isEmpty()) {
                            mView.bindFollowerEmpty();
                            return;
                        }
                        mView.bindFollowerList(pageIndex, followerBeans);
                    }
                });
    }
}
