package com.paizhong.manggo.dialog.placeordermarket;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public interface PlaceOrderMarketContract {

    interface View extends BaseView {
        void bindHttpError();
        void bindSingleProduct(List<ProductListBean> productList);
        void bindProductTime(List<ProductTimeLimitBean> productTimeLimit);
        void bindUserInfo(UserZJBean userZJBean);
        void bindUserInfoRefresh(UserZJBean userZJBean);
        void bindPlaceOrder(int type, int score, String couScore);
    }

    interface Presenter {

       void getProductList();

       void getProductTime();

       void getUserInfo(String token);

       void getUserInfoRefresh(String token);

       void getHangUpOrder(String token, String id, String productId, int quantity, int flag, int profitLimit, int lossLimit, int amount, int holdNight,
                           String productName, double fee, String code, String nickName, String floatNum, String registerAmount);
    }
}
