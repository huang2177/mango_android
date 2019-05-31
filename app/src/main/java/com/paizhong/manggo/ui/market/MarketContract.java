package com.paizhong.manggo.ui.market;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;

import java.util.List;

/**
 * Created by zab on 2018/8/15 0015.
 */
public interface MarketContract {

    interface View extends BaseView {
        void bindZJMarketList(int tabType , List<MarketHQBean> marketBeanList);
        void bindStopShowLoad();
        List<MarketHQBean> getFrontData(int frontType);
    }
    interface Presenter {
        void getZJMarketList();
        void getZJCKSJMarketList(int tabType);
    }
}
