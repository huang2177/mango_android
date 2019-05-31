package com.paizhong.manggo.dialog.market;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import java.util.List;

/**
 * Created by zab on 2018/9/5 0005.
 */
public class MarketPresenter extends BasePresenter<MarketContract.View> implements MarketContract.Presenter {

    @Override
    public void getMarketList() {
        toSubscribe(HttpManager.getApi().getZJMarketList(true), new HttpSubscriber<List<MarketHQBean>>() {
            @Override
            protected void _onNext(List<MarketHQBean> marketHQBeanList) {
                super._onNext(marketHQBeanList);
                mView.bindMarketList(marketHQBeanList);
            }
        });
    }
}
