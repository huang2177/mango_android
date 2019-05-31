package com.paizhong.manggo.ui.trade.openposition;

import android.text.TextUtils;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class OpenPositionPresenter extends BasePresenter<OpenPositionContract.View> implements OpenPositionContract.Presenter {


    //产品信息接口
    @Override
    public void getProductList() {
       toSubscribe(HttpManager.getApi().getProductList(true), new HttpSubscriber<List<ProductListBean>>() {
           @Override
           protected void _onNext(List<ProductListBean> productListBeans) {
               super._onNext(productListBeans);
               if (productListBeans !=null && productListBeans.size() > 0){
                   mView.bindProductList(productListBeans);
               }
           }
       });
    }


    //查询是不是vip用户
    @Override
    public void queryVipUser() {
        toSubscribe(HttpManager.getApi().queryVipUser(), new HttpSubscriber<Integer>() {
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindVipUser(false);
            }

            @Override
            protected void _onNext(Integer integer) {
                super._onNext(integer);
                //status 0 不是vip  1 是vip
                if (integer !=null){
                    mView.bindVipUser((integer == 1));
                }else {
                    mView.bindVipUser(false);
                }
            }
        });
    }


    //禁言
    @Override
    public void forbiddenUser(String isPhone) {
       toSubscribe(HttpManager.getApi().forbiddenUser(isPhone), new HttpSubscriber<JsonNull>() {
           @Override
           protected void _onStart() {
               super._onStart();
               mView.showLoading("");
           }

           @Override
           protected void _onError(String message, int code) {
               super._onError(message, code);
               mView.stopLoading();
               mView.showErrorMsg(message,null);
           }

           @Override
           protected void _onNext(JsonNull jsonNull) {
               super._onNext(jsonNull);
               mView.stopLoading();
               mView.showErrorMsg("禁言成功",null);
           }
       });
    }


    //银----------------分时线-------------------------------
    @Override
    public void getAGTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getAGTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //铜
    @Override
    public void getCUTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getCUTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //镍
    @Override
    public void getNLTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getNLTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //大豆
    @Override
    public void getZSTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZSTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //小麦
    @Override
    public void getZWTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZWTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //玉米
    @Override
    public void getZCTime(final String productID,final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZCTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(productID,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }



    //1==================k线================
    //1 分线 timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1h=一个小时 4H=四个小时 1D=日线  week=周线
    @Override
    public void getZJKlineQuotation_1(String lineType, final String productID) {
         if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1,productID, kLineBean);
                }
            });
        }
    }

    //5分线
    @Override
    public void getZJKlineQuotation_5(String lineType, final String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_5,productID,kLineBean);
                }
            });
        }
    }

    //15分线
    @Override
    public void getZJKlineQuotation_15(String lineType, final String productID) {
       if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15,productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_15, productID,kLineBean);
                }
            });
        }
    }

    //30分钟线
    @Override
    public void getZJKlineQuotation_30(String lineType, final String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_30,productID ,kLineBean);
                }
            });
        }
    }

    //1小时线
    @Override
    public void getZJKlineQuotation_1h(String lineType, final String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID, kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1H,productID ,kLineBean);
                }
            });
        }
    }

    // 4小时线
    @Override
    public void getZJKlineQuotation_4h(String lineType,  final String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_4H,productID ,kLineBean);
                }
            });
        }
    }

    //1天线
    @Override
    public void getZJKlineQuotation_1d(String lineType, final String productID) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_1D,productID ,kLineBean);
                }
            });
        }
    }

    //一周线
    @Override
    public void getZJKlineQuotation_week(String lineType, final String productID) {
         if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, productID)) {//银
            toSubscribe(HttpManager.getApi().getAGKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, productID)) {//铜
            toSubscribe(HttpManager.getApi().getCUKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK, productID,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, productID)) {//镍
            toSubscribe(HttpManager.getApi().getNLKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, productID)) {//大豆
            toSubscribe(HttpManager.getApi().getZSKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, productID)) {//小麦
            toSubscribe(HttpManager.getApi().getZWKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK,productID ,kLineBean);
                }
            });
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, productID)) {//玉米
            toSubscribe(HttpManager.getApi().getZCKline(lineType, true), new HttpSubscriber<KLineBean>() {
                @Override
                protected void _onNext(KLineBean kLineBean) {
                    mView.bindZJKlineQuotation(ViewConstant.KLINE_TAB_WEEK,productID ,kLineBean);
                }
            });
        }
    }



    //分时线数据处理
    private void updateData(String productID,String yestodayClosePrice,KLineBean kLineBean,List<MinuteHourBean> dataList, final ArrayList<String> dateList){
        if (kLineBean == null
                || kLineBean.dataList == null
                || kLineBean.dataList.size() == 0
                || kLineBean.dateList == null
                || kLineBean.dateList.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(yestodayClosePrice)){
            yestodayClosePrice = mView.getClosePrice();
        }
        if (TextUtils.isEmpty(yestodayClosePrice)){
            return;
        }
        dataList.clear();
        dateList.clear();

        for (int i = 0; i < kLineBean.dateList.size(); i++) {
            dateList.add(kLineBean.dateList.get(i));
        }
        for (int i = 0; i < kLineBean.dataList.size(); i++) {
            MinuteHourBean minuteHourBean = new MinuteHourBean();
            minuteHourBean.time = dateList.get(i);
            minuteHourBean.price = KLineUtils.getDouble(kLineBean.dataList.get(i).get(0));
            minuteHourBean.percent = (minuteHourBean.getPrice() - KLineUtils.getDouble(yestodayClosePrice)) / KLineUtils.getDouble(yestodayClosePrice);
            dataList.add(minuteHourBean);
        }

        String timeStr = TimeUtil.getStringToString(mView.getTimeStr(), TimeUtil.dateFormatYMDHMS, TimeUtil.dateFormatHM);
        float newLinePrice = mView.getNewLinePrice();
        if (!TextUtils.isEmpty(timeStr) && newLinePrice > 0){
            MinuteHourBean lastHourBean = dataList.get(dataList.size() - 1);
            if (TextUtils.equals(timeStr,lastHourBean.time)){
                lastHourBean.price = newLinePrice;
                lastHourBean.percent = (lastHourBean.getPrice() - KLineUtils.getDouble(yestodayClosePrice)) / KLineUtils.getDouble(yestodayClosePrice);
            }else {
                MinuteHourBean minuteHourBean = new MinuteHourBean();
                minuteHourBean.price = newLinePrice;
                minuteHourBean.time = timeStr;
                minuteHourBean.percent = (minuteHourBean.getPrice() - KLineUtils.getDouble(yestodayClosePrice)) / KLineUtils.getDouble(yestodayClosePrice);
                dataList.add(minuteHourBean);
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            MinuteHourBean bean = dataList.get(i);
            if (i != 0) {
                bean.total = bean.getPrice() + dataList.get(i - 1).total;
                //bean.total = NumberUtil.divide(bean.getPrice(),dataList.get(i - 1).total);
                bean.average = (int) (bean.total / (i + 1));
                //bean.average = NumberUtil.divide(bean.total,i+1);
            } else {
                bean.total = bean.getPrice();
                bean.average = (int) (bean.total / (i + 1));
                //bean.average = NumberUtil.divide(bean.total,i+1);
            }
        }

        mView.bindZJQuotation(productID,yestodayClosePrice);
    }
}
