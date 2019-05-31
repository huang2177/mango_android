package com.paizhong.manggo.ui.trade.capital;


import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.trade.CapitalListBean;
import com.paizhong.manggo.bean.zj.CapitalBean;
import com.paizhong.manggo.bean.zj.CapitalJsonBean;
import com.paizhong.manggo.bean.zj.UserPositionBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TimeUtil;

import java.util.List;

/**
 * Created by zab on 2018/3/28 0028.
 */

public class CapitalPresenter extends BasePresenter<CapitalContract.View> implements CapitalContract.Presenter{


    //资金信息
    @Override
    public void getUserInfo(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                mView.bindUserInfo(userZJBean);
            }
        });
    }


    //持仓单资金信息
    @Override
    public void getPositionUserMoney(final double amount, String token) {
        toSubscribe(HttpManager.getApi().getUserMoney(token, true), new HttpSubscriber<List<UserPositionBean>>() {

            @Override
            protected void _onNext(List<UserPositionBean> userPositionList) {
                super._onNext(userPositionList);
                if (userPositionList != null && userPositionList.size() > 0){

                    double countPostAmount = 0; //持仓资产
                    double countAmountBj = 0; //本金
                    for (UserPositionBean zjBean : userPositionList){
                        if (!TextUtils.isEmpty(zjBean.coupon_id) && !TextUtils.equals("0",zjBean.coupon_id)){
                            if (zjBean.floatingPrice > 0){
                                countPostAmount += zjBean.floatingPrice;
                            }
                        }else {
                            countPostAmount += (zjBean.amount  + zjBean.floatingPrice);
                            countAmountBj += zjBean.amount;
                        }
                    }
                    mView.bindPositionUserMoney(NumberUtil.addScale(countPostAmount,amount),   NumberUtil.getDouble(countPostAmount),countAmountBj);
                }else {
                    mView.bindPositionUserMoney(null,null,null);
                }
            }
        });
    }



    //今天平仓记录
    @Override
    public void getTodayOrderBill(final boolean showLoad, String token, int page, int page_size,long open_time,long close_time) {
        toSubscribe(HttpManager.getApi().getTodayOrderBill(token, page, page_size,open_time,close_time), new HttpSubscriber<CapitalBean>() {
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
                if (showLoad){
                    mView.stopLoading();
                }
            }

            @Override
            protected void _onNext(CapitalBean capitalBeans) {
                super._onNext(capitalBeans);
                if (ViewConstant.SIMP_TAB_ITEM_ZERO == mView.getTabSelect()){
                    if (capitalBeans !=null  && capitalBeans.isSuccess() && capitalBeans.data !=null && capitalBeans.data.size() > 0){

                        for (CapitalListBean listBean : capitalBeans.data){
                            listBean.tabSelect = ViewConstant.SIMP_TAB_ITEM_ZERO;
                            listBean.amount = String.valueOf(NumberUtil.divideInt(Double.parseDouble(listBean.amount),Constant.ZJ_PRICE_COMPANY));
                            if (TextUtils.isEmpty(listBean.coupon_id)){
                                listBean.close_profit = String.valueOf(NumberUtil.divide(listBean.close_profit,String.valueOf(Constant.ZJ_PRICE_COMPANY)));
                                listBean.fee = String.valueOf(NumberUtil.divide(listBean.fee,String.valueOf(Constant.ZJ_PRICE_COMPANY)));
                            }else {
                                double close_profit = NumberUtil.divide(listBean.close_profit, String.valueOf(Constant.ZJ_PRICE_COMPANY));
                                listBean.close_profit = String.valueOf(close_profit);
                                listBean.fee = "0";
                            }

                            if (TextUtils.isEmpty(listBean.hold_fee)){
                                listBean.hold_fee = "0";
                            }else {
                                listBean.hold_fee = String.valueOf(NumberUtil.divide(listBean.hold_fee,String.valueOf(Constant.ZJ_PRICE_COMPANY)));
                            }
                            listBean.open_time = TimeUtil.timeZoneConversion( listBean.open_time);
                            listBean.close_time = TimeUtil.timeZoneConversion( listBean.close_time);

                            listBean.open_price = String.valueOf(Integer.parseInt(listBean.open_price)/ Constant.ZJ_PRICE_COMPANY);
                            listBean.close_price = String.valueOf(Integer.parseInt(listBean.close_price) / Constant.ZJ_PRICE_COMPANY);
                            if (listBean.product_name.contains("GL铜")){
                                listBean.product_name = "GL铜";
                            }else if (listBean.product_name.contains("GL银")){
                                listBean.product_name = "GL银";
                            }else if (listBean.product_name.contains("GL镍")){
                                listBean.product_name = "GL镍";
                            }else if (listBean.product_name.contains("GL大豆")){
                                listBean.product_name = "GL大豆";
                            }else if (listBean.product_name.contains("GL小麦")){
                                listBean.product_name = "GL小麦";
                            }else if (listBean.product_name.contains("GL玉米")){
                                listBean.product_name = "GL玉米";
                            }

                            if (listBean.type == 2){
                                listBean.typeName = "建仓";
                            }else if (listBean.type == 3){
                                listBean.typeName = "止盈解约";
                            }else if (listBean.type == 4){
                                listBean.typeName = "止损解约";
                            }else if (listBean.type == 5){
                                listBean.typeName = "盈利解约";
                            }else if (listBean.type == 6){
                                listBean.typeName = "亏损解约";
                            }else if (listBean.type == 8){
                                listBean.typeName = "体验券解约";
                            }else if (listBean.type == 9){
                                listBean.typeName = "零盈亏解约";
                            }
                        }
                        mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_ZERO,capitalBeans.data);
                    }else {
                        mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_ZERO,null);
                    }
                }
                if (showLoad){
                    mView.stopLoading();
                }
            }
        });
    }



    //提现记录
    @Override
    public void getWithdrawalList(final boolean showLoad, String token, int page, int page_size) {
        toSubscribe(HttpManager.getApi().getWithdrawalList(token, page, page_size), new HttpSubscriber<CapitalJsonBean>() {
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
                if (showLoad){
                    mView.stopLoading();
                }
            }

            @Override
            protected void _onNext(CapitalJsonBean capitalBean) {
                super._onNext(capitalBean);
                if (ViewConstant.SIMP_TAB_ITEM_ONE  != mView.getTabSelect()){
                    return;
                }

                if (capitalBean !=null
                        && capitalBean.isSuccess()
                        && capitalBean.data !=null
                        && capitalBean.data.data !=null
                        && capitalBean.data.data.size() > 0){


                    for (CapitalListBean listBean : capitalBean.data.data){
                        listBean.tabSelect = ViewConstant.SIMP_TAB_ITEM_ONE;
                        listBean.amount = String.valueOf(NumberUtil.divide(listBean.amount,String.valueOf(Constant.ZJ_PRICE_COMPANY)));
                        listBean.create_time = TimeUtil.timeZoneConversion(listBean.create_time);
                        if (listBean.status == 0){
                            listBean.typeName = "出金成功";
                        }else if (listBean.status == 1 ){
                            listBean.typeName = "出金失败";
                        }else if (listBean.status == 3){
                            listBean.typeName = "系统超时";
                        }else if (listBean.status == 4) {
                            listBean.typeName = "出金申请";
                        }else if (listBean.status == 5) {
                            listBean.typeName = "出金驳回";
                        }
                    }
                    mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_ONE,capitalBean.data.data);
                }else {
                    mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_ONE,null);
                }
            }
        });
    }


    //充值记录
    @Override
    public void getRechargeList(final boolean showLoad, String token, int page, int page_size) {
        toSubscribe(HttpManager.getApi().getRechargeList(token, page, page_size), new HttpSubscriber<CapitalJsonBean>() {
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
                if (showLoad){
                    mView.stopLoading();
                }
            }

            @Override
            protected void _onNext(CapitalJsonBean capitalBean) {
                super._onNext(capitalBean);
                if (ViewConstant.SIMP_TAB_ITEM_TWO != mView.getTabSelect()){
                    return;
                }

                if (capitalBean !=null
                        && capitalBean.isSuccess()
                        && capitalBean.data !=null
                        && capitalBean.data.data !=null
                        && capitalBean.data.data.size() > 0){

                    for (CapitalListBean listBean : capitalBean.data.data){
                        listBean.tabSelect = ViewConstant.SIMP_TAB_ITEM_TWO;
                        listBean.amount = String.valueOf(NumberUtil.divide(listBean.amount,String.valueOf(Constant.ZJ_PRICE_COMPANY)));
                        listBean.create_time = TimeUtil.timeZoneConversion(listBean.create_time);
                        if (listBean.status == 0){
                            listBean.typeName = "充值";
                        }else {
                            listBean.typeName = "待支付";
                        }
                    }
                    mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_TWO, capitalBean.data.data);
                }else {
                    mView.bindTodayOrderBill(ViewConstant.SIMP_TAB_ITEM_TWO,null);
                }
            }
        });
    }
}
