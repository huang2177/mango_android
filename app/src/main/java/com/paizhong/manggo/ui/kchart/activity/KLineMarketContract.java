package com.paizhong.manggo.ui.kchart.activity;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;

/**
 * Created by zab on 2018/8/16 0016.
 */
public interface KLineMarketContract {

    interface View extends BaseView {
        void bindATPriceQuotation(MarketHQBean marketBean);

        void bindSingleProduct(boolean close);

        void bindOrderList(int positNum);
    }

    interface Presenter {
        //单个产品行情
        void getATPriceQuotation(String code);

        void getOtherReal(int type,String code);

        //单个产品信息
        void getSingleProduct(String id);

        void getSignOrderList(String token,String productID);
    }
}
