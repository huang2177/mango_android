package com.paizhong.manggo.ui.follow.contract;

import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.List;

/**
 * Des: 用于VLayout的adapter请求数据
 * Created by huang on 2018/8/27 0027 16:54
 */
public class ItemFollowPresenter extends BasePresenter<ItemFollowContract.View> implements ItemFollowContract.Presenter {
    @Override
    public void getValidRole() {
        toSubscribe(HttpManager.getApi().getValidRole(), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onNext(Boolean aBoolean) {
                mView.bindValidRole(aBoolean);
            }
        });
    }

    @Override
    public void selectBanner(final String bannerType) {
        toSubscribe(HttpManager.getApi().selectBanner(bannerType), new HttpSubscriber<List<BannerBean>>() {
            @Override
            protected void _onNext(List<BannerBean> bannerBeans) {
                if (bannerBeans != null && !bannerBeans.isEmpty()) {
                    mView.bindBanner(bannerBeans, bannerType);
                }
            }
        });
    }

    @Override
    public void getRankList() {
        toSubscribe(HttpManager.getApi().getRankList(), new HttpSubscriber<List<RankListBean>>() {
            @Override
            protected void _onNext(List<RankListBean> rankListBeans) {
                if (rankListBeans == null || rankListBeans.isEmpty()) {
                    return;
                }
                for (int i = 0; i < rankListBeans.size(); i++) {
                    RankListBean bean = rankListBeans.get(i);
                    bean.mProfitRate = bean.profitRate.contains(".")
                            ? bean.profitRate.substring(0, bean.profitRate.indexOf("."))
                            : bean.profitRate;
                }
                mView.bindRankList(rankListBeans);
            }
        });
    }

    public void getRecordList() {
        toSubscribe(HttpManager.getApi().getRecordList(), new HttpSubscriber<List<NoticeListBean>>() {
            @Override
            protected void _onNext(List<NoticeListBean> list) {
                if (!list.isEmpty()) {
                    mView.bindRecordList(list);
                }
            }
        });
    }

    @Override
    public void addConcern(String concernPhone, final int isConcern, final int position) {
        toSubscribe(HttpManager.getApi().addConcern(concernPhone, isConcern + ""), new HttpSubscriber() {
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(Object o) {
                int isConcerned = isConcern == 0 ? 1 : 0;
                mView.bindConcern(position, isConcerned);
            }
        });
    }

    @Override
    public void getOrderList(final boolean empty, final String proName) {
        toSubscribe(HttpManager.getApi().getOrderList(proName), new HttpSubscriber<List<FollowListBean>>() {
            @Override
            public void onStart() {
                if (empty) {
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }

            @Override
            public void onNext(List<FollowListBean> list) {
                if (list == null || list.isEmpty()) {
                    mView.bindOrderEmpty(proName);
                    return;
                }
                parseOrder(list);
                mView.bindOrderList(proName, list);
            }
        });
    }

    /***
     * 精华帖
     */
    @Override
    public void getBestOrderList(final String name) {
        toSubscribe(HttpManager.getApi().getBestOrderList(name), new HttpSubscriber<List<FollowListBean>>() {
            @Override
            public void onNext(List<FollowListBean> list) {
                if (list == null) {
                    return;
                }
                parseOrder(list);
                mView.bindBestOrderList(name, list);
            }
        });
    }

    private void parseOrder(List<FollowListBean> list) {
        for (FollowListBean bean : list) {
            int selectPrice = bean.quantity > 1 ? bean.amount / bean.quantity : bean.amount;

            bean.mFee = bean.fee + "元";
            bean.mCostPrice = bean.amount + "元";
            bean.mFellowCount = String.valueOf(bean.fellowCount);
            bean.mScore = String.valueOf(bean.score * bean.fellowCount);
            bean.mProductName = KLineDataUtils.getProductName(String.valueOf(bean.typeId));
            bean.mCurrentTime = TimeUtil.getFriendTimeOffer((bean.currentTime - bean.openGetTime));
            bean.mProductMsg = TradeUtils.getProductMsg(bean.typeId, selectPrice) + " " + bean.quantity + "手";
            bean.mProfitRate = bean.profitRate != null && bean.profitRate.contains(".")
                    ? bean.profitRate.substring(0, bean.profitRate.indexOf("."))
                    : bean.profitRate;

            //浮动点位
            bean.mFloatPrice = handleFloatPoint(bean);
            bean.floatPrice = bean.mFloatPrice > 0 ? "+" + bean.mFloatPrice : String.valueOf(bean.mFloatPrice);
            mView.refreshDialog(bean.typeId, bean.stockIndex, bean.changeValue, bean.time);
        }
    }

    /**
     * 计算浮动盈亏
     *
     * @param bean
     */
    private int handleFloatPoint(FollowListBean bean) {
        if (TextUtils.isEmpty(bean.stockIndex) || TextUtils.isEmpty(bean.openPrice)) {
            return 0;
        }
        int floatPoint = NumberUtil.subtractInt(bean.stockIndex, bean.openPrice);
        return bean.flag == 1
                ? (floatPoint == 0 ? floatPoint : -floatPoint) : floatPoint;
    }
}
