package com.paizhong.manggo.ui.home.contract;

import android.text.TextUtils;

import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.home.CalendarBean;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.bean.home.HotProBean;
import com.paizhong.manggo.bean.home.NewBuyInfoBean;
import com.paizhong.manggo.bean.home.ProfitBean;
import com.paizhong.manggo.bean.home.RedPacketBean;
import com.paizhong.manggo.bean.home.ReportBean;
import com.paizhong.manggo.bean.home.TradeChanceBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.TradeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Des: 用于Module请求数据
 * Created by huang on 2018/8/27 0027 16:54
 */
public class ItemHomePresenter extends BasePresenter<ItemHomeContract.View> implements ItemHomeContract.Presenter {
    private List<CalendarBean> newCalendarBeans = new ArrayList<>();

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
    public void getReporting() {
        toSubscribe(HttpManager.getApi().getReporting(), new HttpSubscriber<List<ReportBean>>() {
            @Override
            protected void _onNext(List<ReportBean> reportBeans) {
                if (reportBeans != null && !reportBeans.isEmpty()) {
                    mView.bindReporting(reportBeans);
                }
            }
        });
    }

    @Override
    public void getHotProduct() {
        toSubscribe(HttpManager.getApi().getHotProduct(), new HttpSubscriber<HotProBean>() {
            @Override
            protected void _onNext(HotProBean bean) {
                if (bean != null) {
                    mView.bindHotProduct(bean);
                }
            }
        });
    }

    @Override
    public void getZJMarketList() {
        toSubscribe(HttpManager.getApi().getZJMarketList(true), new HttpSubscriber<List<MarketHQBean>>() {

            @Override
            protected void _onNext(List<MarketHQBean> marketBeanList) {
                if (marketBeanList != null && !marketBeanList.isEmpty()) {
                    mView.bindZJMarket(marketBeanList);
                } else {
                    mView.bindZJMarketEmpty();
                }
            }
        });
    }

    @Override
    public void getInitTicket(String phone, final boolean isLoginSucc) {
        toSubscribe(HttpManager.getApi().getInitTicket(phone), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onNext(Boolean bool) {
                mView.bindInitTicket(bool, isLoginSucc);
            }
        });
    }

    @Override
    public void getLastFellowInfo() {
        toSubscribe(HttpManager.getApi().getLastFellowInfo(), new HttpSubscriber<List<NewBuyInfoBean>>() {

            @Override
            protected void _onNext(List<NewBuyInfoBean> beans) {
                if (beans == null || beans.isEmpty()) {
                    mView.bindFellowInfoEmpty();
                    return;
                }
                NewBuyInfoBean bean = beans.get(0);

                int selectPrice = bean.quantity > 1 ? bean.amount / bean.quantity : bean.amount;

                bean.fee = bean.fee + "元";
                bean.mCostPrice = bean.amount + "元";
                bean.mFellowCount = String.valueOf(bean.fellowCount);
                bean.mScore = String.valueOf(bean.score * bean.fellowCount);
                bean.mProductName = KLineDataUtils.getProductName(String.valueOf(bean.typeId));
                bean.mCurrentTime = TimeUtil.getFriendTimeOffer((bean.currentTime - bean.openGetTime));
                bean.mProductMsg = TradeUtils.getProductMsg(bean.typeId, selectPrice) + " " + bean.quantity + "手";
                bean.mProfitRate = bean.profitRate.contains(".")
                        ? bean.profitRate.substring(0, bean.profitRate.indexOf("."))
                        : bean.profitRate;

                //浮动点位
                bean.mFloatPrice = handleFloatPoint(bean);
                bean.floatPrice = bean.mFloatPrice > 0 ? "+" + bean.mFloatPrice : String.valueOf(bean.mFloatPrice);

                mView.refreshDialog(bean.typeId, bean.stockIndex, bean.changeValue, bean.time);
                mView.bindLastFellowInfo(bean);
            }

            /**
             * 计算浮动盈亏
             */
            private int handleFloatPoint(NewBuyInfoBean bean) {
                int floatPoint = NumberUtil.subtractInt(bean.stockIndex, bean.openPrice);
                return bean.flag == 1
                        ? (floatPoint == 0 ? floatPoint : -floatPoint)
                        : floatPoint;
            }
        });
    }

    @Override
    public void selectTradeChance(final int pageIndex) {
        toSubscribe(HttpManager.getApi().selectTradeChance(pageIndex), new HttpSubscriber<List<TradeChanceBean>>() {
            @Override
            protected void _onNext(List<TradeChanceBean> beans) {

                if (beans == null || beans.isEmpty()) {
                    mView.bindTradeEmpty();
                } else {
                    mView.bindTradeChance(beans, pageIndex);
                }
            }
        });
    }

    @Override
    public void queryRedPacketInfo() {
        toSubscribe(HttpManager.getApi().queryRedPacketInfo(), new HttpSubscriber<RedPacketBean>() {
            @Override
            protected void _onNext(RedPacketBean bean) {
                mView.bindRedPacketInfo(bean);
            }
        });
    }

    @Override
    public void queryProfitRank(final int page) {
        toSubscribe(HttpManager.getApi().selectRanking(page), new HttpSubscriber<List<ProfitBean>>() {
            @Override
            protected void _onNext(List<ProfitBean> bean) {
                if (bean != null && !bean.isEmpty()) {
                    mView.bindProfitRank(bean, page);
                }else {
                    mView.bindProfitEmpty();
                }
            }
        });
    }

    @Override
    public void queryUserScore() {
        toSubscribe(HttpManager.getApi().getUserIntegral(AppApplication.getConfig().getAppToken()
                , AppApplication.getConfig().getUserId()
                , AppApplication.getConfig().getMobilePhone()), new HttpSubscriber<IntegralBean>() {
            @Override
            protected void _onNext(IntegralBean bean) {
                if (bean != null) {
                    mView.bindUserScore(bean);
                }
            }
        });
    }

    @Override
    public void getEmergencyDetail(String method, final boolean isShowLoading) {
        toSubscribe(HttpManager.getApi().getEmergency(method, "android", true), new HttpSubscriber<List<EmergencyBean>>() {

            @Override
            protected void _onNext(List<EmergencyBean> data) {
                if (data == null) return;
                Iterator<EmergencyBean> iterator = data.iterator();
                while (iterator.hasNext()) {
                    EmergencyBean next = iterator.next();
                    if (!TextUtils.isEmpty(next.redirect_url)) {
                        iterator.remove();
                    }
                }
                mView.bindEmergencyDetail(data);
            }

        });
    }

    @Override
    public void getCalendarList(String year, String date, final boolean empty) {
        toSimpSubscribe(HttpManager.getApi().getCalendarList(year, date, true), new HttpSubscriber<List<CalendarBean>>() {

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.bindCalendarEmpty();
            }


            @Override
            protected void _onNext(List<CalendarBean> calendarBeans) {
                newCalendarBeans.clear();
                Observable.from(calendarBeans)
                        .filter(new Func1<CalendarBean, Boolean>() {
                            @Override
                            public Boolean call(CalendarBean calendarBean) {
                                return calendarBean.star > 2;
                            }
                        })
                        .subscribe(new Action1<CalendarBean>() {
                            @Override
                            public void call(CalendarBean calendarBean) {
                                newCalendarBeans.add(calendarBean);
                            }
                        });

                if (newCalendarBeans.size() == 0) {
                    mView.bindCalendarEmpty();
                    return;
                }
                mView.bindCalendarList(newCalendarBeans);
            }
        });
    }
}
