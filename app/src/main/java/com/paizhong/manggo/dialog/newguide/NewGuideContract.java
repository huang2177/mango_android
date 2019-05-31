package com.paizhong.manggo.dialog.newguide;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;

/**
 * Created by zab on 2018/5/24 0024.
 */

public interface NewGuideContract {

    interface View extends BaseView {
        void bindProductError();

        void bindNewUser();

        void bindNewUserTicketId(String id);

        void bindSingleProduct(int proId);

        void bindSingleMarket(MarketHQBean marketHQBean);

        void bindCashPlaceOrder(CashPlaceOrderBean placeOrderBean);
    }

    interface Presenter {
        void getSingleMarket(String code);

        void getNewUserTicket(String token);

        void getSingleProduct(String productId);

        void getVoucherPlaceOrder(String token
                , String id, String secret_access_key, String productId, int quantity, int flag
                , int profitLimit, int lossLimit, String couponId
                , int amount, String productName, double fee, String code);
    }
}
