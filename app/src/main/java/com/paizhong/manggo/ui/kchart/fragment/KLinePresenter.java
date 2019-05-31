package com.paizhong.manggo.ui.kchart.fragment;


import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zq on 2017/11/7.
 */

public class KLinePresenter extends BasePresenter<KLineContract.View> implements KLineContract.Presenter {


    //1 分线 timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1h=一个小时 4H=四个小时 1D=日线  week=周线
    @Override
    public void getZJKlineQuotation_1(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1, kLineBean);
                }
            });
        }
    }

    //5分线
    @Override
    public void getZJKlineQuotation_5(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5, kLineBean);
                }
            });
        }
    }

    //15分线
    @Override
    public void getZJKlineQuotation_15(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, kLineBean);
                }
            });
        }
    }

    //30分钟线
    @Override
    public void getZJKlineQuotation_30(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30, kLineBean);
                }
            });
        }
    }

    //1小时线
    @Override
    public void getZJKlineQuotation_1h(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H, kLineBean);
                }
            });
        }
    }


    //2小时线
    @Override
    public void getZJKlineQuotation_2h(int type, boolean mIsCKZS, String lineType, String code, String productID) {
        toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_2H, kLineBean);
            }
        });
    }

    // 4小时线
    @Override
    public void getZJKlineQuotation_4h(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H, kLineBean);
                }
            });
        }
    }

    //1天线
    @Override
    public void getZJKlineQuotation_1d(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (mIsCKZS) { //参考数据
            toSubscribe(HttpManager.getApi().getCKSJKlineQuotation(type,code,lineType,true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D, kLineBean);
                }
            });
        }
    }

    //一周线
    @Override
    public void getZJKlineQuotation_week(int type,boolean mIsCKZS,String lineType, String code ,String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, kLineBean);
                }
            });
        }
    }
}
