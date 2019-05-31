package com.paizhong.manggo.ui.follow.detail.newbuy;


import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.PersonOrderBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/6/4 0004 12:20
 */
public class NewBuyPresenter extends BasePresenter<NewBuyContract.View> implements NewBuyContract.Presenter {

    @Override
    public void getOrderList(String phone, final boolean showLoading) {
        toSubscribe(HttpManager.getApi().getRankingOrder(phone), new HttpSubscriber<List<PersonOrderBean>>() {
            @Override
            public void onStart() {
                if (showLoading) {
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.bindOrderEmpty();
            }

            @Override
            public void onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onNext(List<PersonOrderBean> list) {
                if (list == null || list.isEmpty()) {
                    mView.bindOrderEmpty();
                    return;
                }
                for (PersonOrderBean bean: list) {
                    int selectPrice = bean.quantity > 1 ? bean.amount / bean.quantity : bean.amount;

                    bean.fee = bean.fee + "元";
                    bean.mCostPrice = bean.amount + "元";
                    bean.mFellowCount = String.valueOf(bean.fellowCount);
                    bean.mScore = String.valueOf(bean.score * bean.fellowCount);
                    bean.mProductName = KLineDataUtils.getProductName(String.valueOf(bean.typeId));
                    bean.mProductMsg = TradeUtils.getProductMsg(bean.typeId, selectPrice) + " " + bean.quantity + "手";

                    //浮动点位
                    bean.mFloatPrice = handleFloatPoint(bean);
                    bean.floatPrice = bean.mFloatPrice > 0 ? "+" + bean.mFloatPrice : String.valueOf(bean.mFloatPrice);

                    mView.refreshDialog(String.valueOf(bean.typeId), bean.stockIndex, bean.changeValue, bean.time);

                }
                mView.bindOrderList(list);
            }

            /**
             * 计算浮动盈亏  (0-买涨 1-买跌)
             * @param bean
             * @param selectPrice
             */
            private String handleFloatPrice(PersonOrderBean bean, int selectPrice) {
                // 浮动盈亏 = （当前点位 - 建仓点位）* 点位波动价格 * 手数  (买跌加负号)
                double closePrice = NumberUtil.multiply(NumberUtil.multiply(NumberUtil.subtract(bean.stockIndex, bean.openPrice)
                        , TradeUtils.getProductPrice(bean.typeId, selectPrice))
                        , bean.quantity);
                return bean.flag == 1 ? (closePrice == 0 ? String.valueOf(closePrice) : String.valueOf(-closePrice))
                        : String.valueOf(closePrice);
            }

            /**
             * 计算浮动盈亏
             * @param bean
             */
            private int handleFloatPoint(PersonOrderBean bean) {
                int floatPoint = NumberUtil.subtractInt(bean.stockIndex, bean.openPrice);
                return bean.flag == 1
                        ? (floatPoint == 0 ? floatPoint : -floatPoint)
                        : floatPoint;
            }
        });
    }

}
