package com.paizhong.manggo.ui.market;
import android.text.TextUtils;

import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class MarketPresenter extends BasePresenter<MarketContract.View> implements MarketContract.Presenter {
    //行请
    @Override
    public void getZJMarketList() {
        toSubscribe(HttpManager.getApi().getZJMarketList(true), new HttpSubscriber<List<MarketHQBean>>() {

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<MarketHQBean> marketBeanList) {
                super._onNext(marketBeanList);
                if (AppApplication.getConfig().mIsAuditing && marketBeanList !=null && marketBeanList.size() > 0){
                    Iterator<MarketHQBean> iterator = marketBeanList.iterator();
                    while (iterator.hasNext()){
                        MarketHQBean marketHQBean = iterator.next();
                        if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,marketHQBean.remark)
                                || TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,marketHQBean.remark)
                                || TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,marketHQBean.remark)){
                            iterator.remove();
                        }
                    }
                }
                mView.bindZJMarketList(ViewConstant.SIMP_TAB_ITEM_ZERO,marketBeanList);
            }
        });
    }


    //行情 参考数据
    @Override
    public void getZJCKSJMarketList(final int tabType) {
        toSubscribe(HttpManager.getApi().getZJCKSJMarketList(true,tabType), new HttpSubscriber<List<MarketHQBean>>() {

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<MarketHQBean> marketBeanList) {
                super._onNext(marketBeanList);
                if (marketBeanList !=null && marketBeanList.size() >0){
                    List<MarketHQBean> frontList = mView.getFrontData(tabType);
                    if(frontList!=null && frontList.size()>0 && frontList.size() == marketBeanList.size()){
                        for (int i = 0; i < frontList.size(); i++) {
                            MarketHQBean marketHQBean = marketBeanList.get(i);
                            double front = Double.valueOf(frontList.get(i).point);
                            double now = Double.valueOf(marketHQBean.point);
                            if(front > now){
                                marketHQBean.rise=0;
                            }else if(front == now){
                                marketHQBean.rise=2;
                            }else if(front < now){
                                marketHQBean.rise=1;
                            }
                        }
                    }
                    mView.bindZJMarketList(tabType,marketBeanList);
                }
            }
        });
    }
}
