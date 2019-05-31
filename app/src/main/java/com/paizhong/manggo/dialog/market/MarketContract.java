package com.paizhong.manggo.dialog.market;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;

import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public interface MarketContract {

    interface View extends BaseView {
        void bindMarketList(List<MarketHQBean> marketList);
    }

    interface Presenter {
      void getMarketList();
    }
}
