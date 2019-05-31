package com.paizhong.manggo.ui.trade.hangup;

import android.text.TextUtils;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.HangUpListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class HangUpPresenter extends BasePresenter<HangUpContract.View> implements HangUpContract.Presenter {


    //挂单列表
    @Override
    public void getHangUpOrderList(final boolean showLoad) {
       toSubscribe(HttpManager.getApi().getHangUpOrderList(), new HttpSubscriber<List<HangUpListBean>>() {
           @Override
           protected void _onStart() {
               super._onStart();
               if (showLoad){
                   mView.showLoading("");
               }
           }

           @Override
           protected void _onCompleted() {
               super._onCompleted();
               mView.stopLoading();
           }

           @Override
           protected void _onNext(List<HangUpListBean> hangUpOrderBeans) {
               super._onNext(hangUpOrderBeans);
               if (hangUpOrderBeans !=null && hangUpOrderBeans.size() > 0){
                   Collections.sort(hangUpOrderBeans, new Comparator<HangUpListBean>() {
                       @Override
                       public int compare(HangUpListBean o1, HangUpListBean o2) {
                           // o1 - o2 升序（从小到大） o2 - 01 降序（从大到小）
                           int compareIndex_1;
                           int compareIndex_2;
                           if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,o1.typeId)){ //银
                               compareIndex_1 = 0;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,o1.typeId)){//铜
                               compareIndex_1 = 1;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,o1.typeId)){//镍
                               compareIndex_1 = 2;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,o1.typeId)){//大豆
                               compareIndex_1 = 3;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,o1.typeId)){//小麦
                               compareIndex_1 = 4;
                           }else {
                               compareIndex_1 = 5;
                           }

                           if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,o2.typeId)){ //银
                               compareIndex_2 = 0;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,o2.typeId)){//铜
                               compareIndex_2 = 1;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,o2.typeId)){//镍
                               compareIndex_2 = 2;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,o2.typeId)){//大豆
                               compareIndex_2 = 3;
                           }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,o2.typeId)){//小麦
                               compareIndex_2 = 4;
                           }else {
                               compareIndex_2 = 5;
                           }
                           return compareIndex_1 - compareIndex_2;
                       }
                   });


                   for (int i= 0 ; i < hangUpOrderBeans.size() ; i ++){
                       HangUpListBean hangUpBean = hangUpOrderBeans.get(i);
                       hangUpBean.amount = String.valueOf((int) Double.parseDouble(hangUpBean.amount));
                       hangUpBean.kgNum = TradeUtils.getProductMsg(hangUpBean.typeId,(Integer.parseInt(hangUpBean.amount)/hangUpBean.quantity))+" "+hangUpBean.quantity+"手";
                       hangUpBean.createTime = TimeUtil.getStringByFormat(Long.parseLong(hangUpBean.createTime),TimeUtil.dateFormatMDHM);
                       hangUpBean.registerAmount = String.valueOf((int) Double.parseDouble(hangUpBean.registerAmount));
                       if (i == 0){
                           hangUpBean.typeTag = true;
                       }else {
                           hangUpBean.typeTag =  !TextUtils.equals(hangUpBean.typeId,hangUpOrderBeans.get(i-1).typeId);
                       }
                   }
               }
               mView.bindHangUpOrderList(hangUpOrderBeans);
           }
       });
    }

    //行情
    @Override
    public void getMarketList() {
       toSubscribe(HttpManager.getApi().getZJMarketList(true), new HttpSubscriber<List<MarketHQBean>>() {
           @Override
           protected void _onNext(List<MarketHQBean> marketHQBeanList) {
               super._onNext(marketHQBeanList);
               mView.bindMarketList(marketHQBeanList);
           }
       });
    }



    @Override
    public void getHangUpHistoryList(final boolean showLoad, int pageSize,final int pageIndex) {
       toSubscribe(HttpManager.getApi().getHangUpHistoryList(pageSize, pageIndex), new HttpSubscriber<List<HangUpListBean>>() {
           @Override
           protected void _onStart() {
               super._onStart();
               if (showLoad){
                   mView.showLoading("");
               }
           }

           @Override
           protected void _onCompleted() {
               super._onCompleted();
               mView.stopLoading();
           }

           @Override
           protected void _onNext(List<HangUpListBean> hangUpListBeans) {
               super._onNext(hangUpListBeans);
               if (hangUpListBeans !=null && hangUpListBeans.size() > 0){
                   for (HangUpListBean hangUpBean : hangUpListBeans){
                       hangUpBean.productName = KLineDataUtils.getProductName(hangUpBean.typeId);
                       hangUpBean.amount = String.valueOf((int) Double.parseDouble(hangUpBean.amount));
                       hangUpBean.registerAmount = String.valueOf((int) Double.parseDouble(hangUpBean.registerAmount));
                       hangUpBean.createTime = TimeUtil.getStringByFormat(Long.parseLong(hangUpBean.createTime),TimeUtil.dateFormatYMDHMS);
                       if (hangUpBean.status == 2 ||hangUpBean.status == 3){
                           hangUpBean.registerFinishTime = TimeUtil.getStringByFormat(Long.parseLong(hangUpBean.registerFinishTime),TimeUtil.dateFormatYMDHMS);
                           hangUpBean.openPrice = String.valueOf((int) Double.parseDouble(hangUpBean.openPrice));
                       }
                   }
               }
               mView.bindHangUpHistoryList(pageIndex,hangUpListBeans);
           }
       });
    }


    //撤单
    @Override
    public void getCancelHangUp(final String id) {
      toSubscribe(HttpManager.getApi().getCancelHangUp(id), new HttpSubscriber<JsonNull>() {
          @Override
          protected void _onStart() {
              super._onStart();
              mView.showLoading("");
          }

          @Override
          protected void _onError(String message, int code) {
              super._onError(message, code);
              mView.showErrorMsg(message,null);
          }

          @Override
          protected void _onCompleted() {
              super._onCompleted();
              mView.stopLoading();
          }

          @Override
          protected void _onNext(JsonNull jsonNull) {
              super._onNext(jsonNull);
              mView.bindCancelHangUp(id);
          }
      });
    }
}
