package com.paizhong.manggo.dialog.position;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.List;

/**
 * Created by zab on 2018/6/19 0019.
 */
public class PositionPresenter extends BasePresenter<PositionContract.View> implements PositionContract.Presenter{

    //持仓单
    @Override
    public void getSignOrderList(String token,String mProductID) {
      toSubscribe(HttpManager.getApi().getSignOrderList(token,mProductID,true), new HttpSubscriber<List<PositionListBean>>() {
          @Override
          public void onNext(List<PositionListBean> positionList) {
              super.onNext(positionList);
              if (positionList !=null && positionList.size() > 0){
                  for (PositionListBean listBean : positionList){
                      listBean.name = TradeUtils.getProductMsg(listBean.type, Integer.parseInt(listBean.amount) / Constant.ZJ_PRICE_COMPANY/listBean.quantity)+" "+listBean.quantity+"手";
                      listBean.check = mView.getCheck(listBean.id);
                      listBean.create_time = TimeUtil.getStringToString(listBean.create_time, TimeUtil.dateFormatYMDHMS, TimeUtil.dateFormatMDHM);
                  }
              }
              mView.bindOrderList(positionList);
          }
      });
    }



    //批量平仓
    @Override
    public void getTransferList(String token, String orderids, String secret_access_key) {
        toSubscribe(HttpManager.getApi().getTransferList(token, orderids, secret_access_key), new HttpSubscriber<ZJBaseBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(ZJBaseBean baseBean) {
                super._onNext(baseBean);
                mView.bindTransferList(baseBean);
            }
        });
    }
}
