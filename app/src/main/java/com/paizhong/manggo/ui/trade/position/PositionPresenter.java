package com.paizhong.manggo.ui.trade.position;


import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.AtBaseBean;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class PositionPresenter extends BasePresenter<PositionContract.View> implements PositionContract.Presenter {


    @Override
    public void getPositionList(String token) {
        toSubscribe(HttpManager.getApi().getPositionList(token,true), new HttpSubscriber<List<PositionListBean>>() {
            @Override
            protected void _onNext(List<PositionListBean> positionList) {
                super._onNext(positionList);
                if (positionList !=null && positionList.size() > 0){
                    Collections.sort(positionList, new Comparator<PositionListBean>() {
                        @Override
                        public int compare(PositionListBean o1, PositionListBean o2) {
                            // o1 - o2 升序（从小到大） o2 - 01 降序（从大到小）
                            int compareIndex_1;
                            int compareIndex_2;
                            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,o1.type)){ //银
                                compareIndex_1 = 0;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,o1.type)){//铜
                                compareIndex_1 = 1;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,o1.type)){//镍
                                compareIndex_1 = 2;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,o1.type)){//大豆
                                compareIndex_1 = 3;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,o1.type)){//小麦
                                compareIndex_1 = 4;
                            }else {
                                compareIndex_1 = 5;
                            }

                            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,o2.type)){ //银
                                compareIndex_2 = 0;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,o2.type)){//铜
                                compareIndex_2 = 1;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,o2.type)){//镍
                                compareIndex_2 = 2;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,o2.type)){//大豆
                                compareIndex_2 = 3;
                            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,o2.type)){//小麦
                                compareIndex_2 = 4;
                            }else {
                                compareIndex_2 = 5;
                            }
                            return compareIndex_1 - compareIndex_2;
                        }
                    });

                    for (int i = 0 ; i < positionList.size() ; i ++){
                        PositionListBean listBean = positionList.get(i);
                        listBean.name = KLineDataUtils.getProductName(listBean.type);
                        listBean.amount = String .valueOf(NumberUtil.divideInt(Double.parseDouble(listBean.amount) , Constant.ZJ_PRICE_COMPANY));//建仓价
                        listBean.create_time = listBean.create_time.substring(5,listBean.create_time.length());
                        if (TextUtils.isEmpty(listBean.coupon_id)){
                            listBean.fee = NumberUtil.divideStr(listBean.fee,String.valueOf(Constant.ZJ_PRICE_COMPANY));
                        }else {
                            listBean.fee = "0";
                        }
                        listBean.kgNum = TradeUtils.getProductMsg(listBean.type,(Integer.parseInt(listBean.amount) / listBean.quantity))+" "+listBean.quantity+"手";

                        //浮动点位等于建仓价减去 最新价
                        if (listBean.flag == 0){//0涨  1跌
                            int v = -NumberUtil.subtractInt(listBean.open_price, listBean.stockIndex);
                            listBean.flu_dw = v > 0 ? "+"+v : String.valueOf(v);
                        }else {
                            int v = NumberUtil.subtractInt(listBean.open_price, listBean.stockIndex);
                            listBean.flu_dw = v > 0 ? "+"+v : String.valueOf(v);
                        }

                        if (i == 0){
                            listBean.typeTag = true;
                        }else {
                            listBean.typeTag =  !TextUtils.equals(listBean.type,positionList.get(i-1).type);
                        }
                        listBean.check = mView.getCheck(listBean.order_no);
                        mView.refreshDialog(listBean.type,listBean.id,listBean.stockIndex,listBean.changValue,listBean.floatingPrice);
                    }

                    mView.bindPositionList(positionList);
                }else {
                    mView.bindPositionList(null);
                }
            }
        });
    }



    //设置持仓过夜状态
    @Override
    public void getOrderHoldNight(String token, String secret_access_key, String orderId,String orderNo, boolean holdNight) {
        toSubscribe(HttpManager.getApi().getOrderHoldNight(token, secret_access_key, orderId,orderNo, holdNight), new HttpSubscriber<AtBaseBean>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(AtBaseBean atBaseBean) {
                super._onNext(atBaseBean);
                mView.showErrorMsg((atBaseBean !=null && atBaseBean.isSuccess() ? "设置成功" :atBaseBean.desc ),null);
            }
        });
    }
}
