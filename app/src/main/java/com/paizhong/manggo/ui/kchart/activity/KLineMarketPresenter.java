package com.paizhong.manggo.ui.kchart.activity;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.List;

/**
 * Created by zab on 2018/8/16 0016.
 */
public class KLineMarketPresenter extends BasePresenter<KLineMarketContract.View> implements KLineMarketContract.Presenter{


    @Override
    public void getATPriceQuotation(String code) {
      //单个行情
      toSubscribe(HttpManager.getApi().getReal(code, true), new HttpSubscriber<MarketHQBean>() {
          @Override
          protected void _onNext(MarketHQBean marketAtBean) {
              super._onNext(marketAtBean);
              if (marketAtBean !=null){
                  mView.bindATPriceQuotation(marketAtBean);
              }
          }
      });
    }


    //单个行情（参考数据）
    @Override
    public void getOtherReal(int type, String code) {
       toSubscribe(HttpManager.getApi().getOtherReal(type, code, true), new HttpSubscriber<MarketHQBean>() {
           @Override
           protected void _onNext(MarketHQBean marketHQBean) {
               super._onNext(marketHQBean);
               if (marketHQBean !=null){
                   mView.bindATPriceQuotation(marketHQBean);
               }
           }
       });
    }


    //单个产品信息
    @Override
    public void getSingleProduct(String id) {
      toSubscribe(HttpManager.getApi().getSingleProduct(id), new HttpSubscriber<List<ProductListBean>>() {
          @Override
          protected void _onNext(List<ProductListBean> productList) {
              super._onNext(productList);
              if (productList !=null  && productList.size() > 0){
                   mView.bindSingleProduct(productList.get(0).isClose());
              }
          }
      });
    }


    //持仓
    @Override
    public void getSignOrderList(String token, String productID) {
       toSubscribe(HttpManager.getApi().getSignOrderList(token, productID, true), new HttpSubscriber<List<PositionListBean>>() {

           @Override
           protected void _onError(String message, int code) {
               super._onError(message, code);
               mView.bindOrderList(0);
           }

           @Override
           protected void _onNext(List<PositionListBean> positionList) {
               super._onNext(positionList);
               if (positionList !=null){
                   mView.bindOrderList(positionList.size());
               }else {
                   mView.bindOrderList(0);
               }
           }
       });
    }
}
