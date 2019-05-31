package com.paizhong.manggo.ui.follow.rank;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/6/4 0004 12:20
 */
public class RankPresenter extends BasePresenter<RankContract.View> implements RankContract.Presenter {

    @Override
    public void getRankList(final boolean showLoading) {
        toSubscribe(HttpManager.getApi().getRankList(), new HttpSubscriber<List<RankListBean>>() {
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
            public void onNext(List<RankListBean> rankListBeans) {
                if (rankListBeans == null || rankListBeans.isEmpty()) {
                    return;
                }
                for (int i = 0; i < rankListBeans.size(); i++) {
                    RankListBean bean = rankListBeans.get(i);
                    bean.mProfitRate = bean.profitRate.contains(".")
                            ? bean.profitRate.substring(0, bean.profitRate.indexOf("."))
                            : bean.profitRate;
                }
                mView.bindRankList(rankListBeans);
            }
        });
    }
}
