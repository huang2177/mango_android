package com.paizhong.manggo.dialog.placeorderlan;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;


/**
 * Created by zab on 2018/9/6 0006.
 */
public interface PlaceOrderLanContract {

    interface View extends BaseView {
        void bindHttpError();
        void bindSingleProduct(ProductListBean productBean);
        void bindSingleTime(ProductTimeLimitBean timeLimitBean);
        void bindValidRole(Boolean validRole);
        void bindUserInfo(UserZJBean userBean);
        void bindUserInfoRefresh(UserZJBean userBean);
        void bindATPriceQuotation(MarketHQBean marketBean);

        void bindPlaceOrder(int type, int score,String couScore);
    }

    interface Presenter {
        void getSingleProduct(String productID);
        void getSingleTime(String code);
        void getValidRole();
        void getUserInfo(String token);
        void getUserInfoRefresh(String token);
        void getReal(String type);

        void getCashPlaceOrder(String token,String id,String secret_access_key,String productId,int quantity,int flag,int profitLimit,int lossLimit,
                               int amount,int holdNight,String productName,double fee,String code,int followType,String nickName);

        void getVoucherPlaceOrder(String token,String id,String secret_access_key,String productId,int quantity,int flag,int profitLimit,int lossLimit,
                                  String couponId,int amount,String productName,double fee,String code);


        void getHangUpOrder(String token,String id,String productId,int quantity,int flag,int profitLimit,int lossLimit,int amount,int holdNight,
                            String productName,double fee,String code,String nickName,String floatNum,String registerAmount);
    }
}
