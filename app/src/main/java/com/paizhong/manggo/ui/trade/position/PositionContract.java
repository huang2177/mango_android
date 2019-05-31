package com.paizhong.manggo.ui.trade.position;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.PositionListBean;

import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public interface PositionContract {
    interface View extends BaseView {
        boolean getCheck(String key);

        void refreshDialog(String productID, String orderId, String stockIndex,double changValue, double floatingPrice);

        void bindPositionList(List<PositionListBean> positionList);

    }
    interface Presenter {
      void getPositionList(String token);

      void getOrderHoldNight(String token,String secret_access_key,String orderId,String orderNo,boolean holdNight);
    }
}
