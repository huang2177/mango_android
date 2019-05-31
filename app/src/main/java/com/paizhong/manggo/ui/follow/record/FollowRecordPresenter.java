package com.paizhong.manggo.ui.follow.record;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.FollowRecordBean;
import com.paizhong.manggo.bean.follow.RecordRankBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.List;

/**
 * Created by zab on 2018/7/3 0003.
 */
public class FollowRecordPresenter extends BasePresenter<FollowRecordContract.View> implements FollowRecordContract.Presenter {


    //跟买盈利
    @Override
    public void getWeekProfit(String customerPhone) {
        toSubscribe(HttpManager.getApi().getWeekProfit(customerPhone), new HttpSubscriber<RecordRankBean>() {
            @Override
            protected void _onNext(RecordRankBean recordRankBean) {
                mView.bindWeekProfit(recordRankBean);
            }
        });
    }

    //发起跟买持仓
    @Override
    public void getOrderPosition() {
        toSubscribe(HttpManager.getApi().getOrderPosition(), new HttpSubscriber<List<FollowRecordBean>>() {
            @Override
            protected void _onCompleted() {
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<FollowRecordBean> recordBeanList) {
                if (recordBeanList == null || recordBeanList.isEmpty()) {
                    mView.bindOrderEmpty();
                    return;
                }
                for (FollowRecordBean recordBean: recordBeanList) {
                    int selectPrice = recordBean.quantity > 1 ? recordBean.amount / recordBean.quantity : recordBean.amount;
                    recordBean.mProductMsg = TradeUtils.getProductMsg(recordBean.typeId, selectPrice) + " " + recordBean.quantity + "手";
                    recordBean.productName = KLineDataUtils.getProductName(String.valueOf(recordBean.typeId));

                    //浮动盈亏 = （当前点位 - 建仓点位）* 点位波动价格 * 手数  //买跌加负号
                    double closePrice = NumberUtil.multiply(NumberUtil.multiply(NumberUtil.subtract(recordBean.stockIndex, recordBean.openPrice), TradeUtils.getProductPrice(recordBean.typeId, selectPrice)), recordBean.quantity);
                    recordBean.closeProfit = recordBean.flag == 1 ? (closePrice > 0 || closePrice < 0 ? String.valueOf(-closePrice) : String.valueOf(closePrice)) : String.valueOf(closePrice);
                    recordBean.mCloseProfit = Double.parseDouble(recordBean.closeProfit);

                    //浮动点位
                    int v = NumberUtil.subtractInt(recordBean.stockIndex, recordBean.openPrice);
                    recordBean.mFloatPrice = recordBean.flag == 1 ? (v == 0 ? v : -v) : v;
                    recordBean.floatPrice = recordBean.mFloatPrice > 0 ? "+" + recordBean.mFloatPrice : String.valueOf(recordBean.mFloatPrice);
                    //recordBean.fee = recordBean.fee+"元";
                    //mView.refreshDialog(recordBean.typeId,recordBean.stockIndex,recordBean.changeValue,ViewConstant.SIMP_TAB_ITEM_ONE);

                    recordBean.score = String.valueOf(Integer.parseInt(recordBean.score) * recordBean.fellowCount);
                }

                mView.bindOrderPosition(1, ViewConstant.SIMP_TAB_ITEM_ONE, 1, recordBeanList);
            }
        });
    }

    //发起跟买平仓
    @Override
    public void getOrderPositionUp(String phone, int pageSize, final int pageIndex) {
        toSubscribe(HttpManager.getApi().getOrderEveningUp(phone, pageSize, pageIndex), new HttpSubscriber<List<FollowRecordBean>>() {
            @Override
            protected void _onCompleted() {
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<FollowRecordBean> recordBeanList) {
                if (recordBeanList == null || recordBeanList.isEmpty()) {
                    mView.bindOrderEmpty();
                    return;
                }
                for (FollowRecordBean recordBean: recordBeanList) {
                    int selectPrice = recordBean.quantity > 1 ? recordBean.amount / recordBean.quantity : recordBean.amount;
                    recordBean.mProductMsg = TradeUtils.getProductMsg(recordBean.typeId, selectPrice) + " " + recordBean.quantity + "手";
                    recordBean.productName = KLineDataUtils.getProductName(String.valueOf(recordBean.typeId));

                    //浮动点位
                    int v = NumberUtil.subtractInt(recordBean.closePrice, recordBean.openPrice);
                    recordBean.mFloatPrice = recordBean.flag == 1 ? (v == 0 ? v : -v) : v;
                    recordBean.floatPrice = recordBean.mFloatPrice > 0 ? "+" + recordBean.mFloatPrice : String.valueOf(recordBean.mFloatPrice);

                    recordBean.score = String.valueOf(Integer.parseInt(recordBean.score) * recordBean.fellowCount);
                }
                mView.bindOrderPosition(pageIndex, ViewConstant.SIMP_TAB_ITEM_ONE, 2, recordBeanList);
            }
        });
    }


    //我的跟买 持仓
    @Override
    public void getMyFellowOrder() {
        toSubscribe(HttpManager.getApi().getMyOrderPosition(), new HttpSubscriber<List<FollowRecordBean>>() {
            @Override
            protected void _onCompleted() {
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<FollowRecordBean> recordBeanList) {
                if (recordBeanList == null || recordBeanList.isEmpty()) {
                    mView.bindOrderEmpty();
                    return;
                }
                for (FollowRecordBean recordBean: recordBeanList) {
                    int selectPrice = recordBean.quantity > 1 ? recordBean.amount / recordBean.quantity : recordBean.amount;
                    recordBean.mProductMsg = TradeUtils.getProductMsg(recordBean.typeId, selectPrice) + " " + recordBean.quantity + "手";
                    recordBean.productName = KLineDataUtils.getProductName(String.valueOf(recordBean.typeId));

                    //浮动盈亏 = （当前点位 - 建仓点位）* 点位波动价格 * 手数  //买跌加负号
                    double closePrice = NumberUtil.multiply(NumberUtil.multiply(NumberUtil.subtract(recordBean.stockIndex, recordBean.openPrice), TradeUtils.getProductPrice(recordBean.typeId, selectPrice)), recordBean.quantity);
                    recordBean.closeProfit = recordBean.flag == 1 ? (closePrice > 0 || closePrice < 0 ? String.valueOf(-closePrice) : String.valueOf(closePrice)) : String.valueOf(closePrice);
                    recordBean.mCloseProfit = Double.parseDouble(recordBean.closeProfit);

                    //浮动点位
                    int v = NumberUtil.subtractInt(recordBean.stockIndex, recordBean.openPrice);
                    recordBean.mFloatPrice = recordBean.flag == 1 ? (v == 0 ? v : -v) : v;
                    recordBean.floatPrice = recordBean.mFloatPrice > 0 ? "+" + recordBean.mFloatPrice : String.valueOf(recordBean.mFloatPrice);

                    //recordBean.fee = recordBean.fee+"元";
                    //mView.refreshDialog(recordBean.typeId,recordBean.stockIndex,recordBean.changeValue,ViewConstant.SIMP_TAB_ITEM_TWO);
                }

                mView.bindOrderPosition(1, ViewConstant.SIMP_TAB_ITEM_TWO, 1, recordBeanList);
            }
        });
    }

    //我的跟买 平仓
    @Override
    public void getMyFellowOrderUp(int pageSize, final int pageIndex) {
        toSubscribe(HttpManager.getApi().getMyOrderEvenUp(pageSize, pageIndex), new HttpSubscriber<List<FollowRecordBean>>() {
            @Override
            protected void _onCompleted() {
                mView.bindStopShowLoad();
            }

            @Override
            protected void _onNext(List<FollowRecordBean> recordBeanList) {
                if (recordBeanList == null || recordBeanList.isEmpty()) {
                    mView.bindOrderEmpty();
                    return;
                }
                for (FollowRecordBean recordBean: recordBeanList) {
                    int selectPrice = recordBean.quantity > 1 ? recordBean.amount / recordBean.quantity : recordBean.amount;
                    recordBean.mProductMsg = TradeUtils.getProductMsg(recordBean.typeId, selectPrice) + " " + recordBean.quantity + "手";
                    recordBean.productName = KLineDataUtils.getProductName(String.valueOf(recordBean.typeId));

                    //浮动点位
                    int v = NumberUtil.subtractInt(recordBean.closePrice, recordBean.openPrice);
                    recordBean.mFloatPrice = recordBean.flag == 1 ? (v == 0 ? v : -v) : v;
                    recordBean.floatPrice = recordBean.mFloatPrice > 0 ? "+" + recordBean.mFloatPrice : String.valueOf(recordBean.mFloatPrice);
                }
                mView.bindOrderPosition(pageIndex, ViewConstant.SIMP_TAB_ITEM_TWO, 2, recordBeanList);
            }
        });
    }
}
