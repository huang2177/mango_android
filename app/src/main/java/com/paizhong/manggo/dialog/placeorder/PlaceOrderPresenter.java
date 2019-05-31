package com.paizhong.manggo.dialog.placeorder;

import android.text.TextUtils;

import com.google.gson.JsonNull;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public class PlaceOrderPresenter extends BasePresenter<PlaceOrderContract.View> implements PlaceOrderContract.Presenter {

    //单个产品信息接口
    @Override
    public void getSingleProduct(String productID) {
        toSubscribe(HttpManager.getApi().getSingleProduct(productID), new HttpSubscriber<List<ProductListBean>>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindHttpError();
            }
            @Override
            protected void _onNext(List<ProductListBean> productListBeans) {
                super._onNext(productListBeans);
                if (productListBeans !=null && productListBeans.size() > 0){
                    mView.bindSingleProduct(productListBeans.get(0));
                }else {
                    mView.bindHttpError();
                }
            }
        });
    }

    //资金信息
    @Override
    public void getUserInfo(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.bindHttpError();
            }

            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                if (userZJBean !=null && userZJBean.isSuccess()){
                    mView.bindUserInfo(userZJBean);
                }else {
                    mView.bindHttpError();
                }
            }
        });
    }


    //刷新资金
    @Override
    public void getUserInfoRefresh(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                if (userZJBean !=null && userZJBean.isSuccess()){
                    mView.bindUserInfoRefresh(userZJBean);
                }
            }
        });
    }


    //是不是牛人
    @Override
    public void getValidRole() {
      toSubscribe(HttpManager.getApi().getValidRole(), new HttpSubscriber<Boolean>() {

          @Override
          protected void _onError(String message, int code) {
              super._onError(message, code);
              mView.bindValidRole(false);
          }

          @Override
          protected void _onNext(Boolean aBoolean) {
              super._onNext(aBoolean);
              mView.bindValidRole(aBoolean);
          }
      });
    }



    //单个产品时间和做单比例
    @Override
    public void getSingleTime(String code) {
       toSubscribe(HttpManager.getApi().getSingleTime(code,true), new HttpSubscriber<ProductTimeLimitBean>() {
           @Override
           protected void _onError(String message, int code) {
               super._onError(message, code);
               mView.bindSingleTime(null);
           }
           @Override
           protected void _onNext(ProductTimeLimitBean productTimeLimitBean) {
               mView.bindSingleTime(productTimeLimitBean);
           }
       });
    }


    //单个行情接口
    @Override
    public void getReal(String type) {
        //单个行情
        toSubscribe(HttpManager.getApi().getReal(type, true), new HttpSubscriber<MarketHQBean>() {

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.bindHttpError();
            }

            @Override
            protected void _onNext(MarketHQBean marketAtBean) {
                super._onNext(marketAtBean);
                mView.bindATPriceQuotation(marketAtBean);
            }
        });
    }


    /**
     * 现金下单
     * @param token 	交易所token
     * @param id   	大产品id
     * @param secret_access_key 	交易所secret_access_key
     * @param productId 产品id
     * @param quantity 建仓数量
     * @param flag 0-买涨，1:买跌 允许值: 0, 1
     * @param profitLimit
     * @param lossLimit
     * @param amount    	下单金额
     * @param holdNight   是否持仓 0：:不持仓 1：持仓
     * @param productName
     * @param fee   	交易手续费
     * @param code
     * @param followType  是否跟买 0-否 1-是
     * @param nickName
     */
    @Override
    public void getCashPlaceOrder(String token, String id, String secret_access_key, String productId, int quantity, int flag, int profitLimit,
                                  int lossLimit, int amount, int holdNight, String productName, double fee, String code, int followType, String nickName) {

        toSubscribe(HttpManager.getApi().getCashPlaceOrder(token, id, secret_access_key, productId, quantity, flag, profitLimit, lossLimit, amount,
                holdNight, productName, fee, code, followType, nickName), new HttpSubscriber<CashPlaceOrderBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean cashPlaceOrderBean) {
                super._onNext(cashPlaceOrderBean);
                if (cashPlaceOrderBean !=null){
                    if (cashPlaceOrderBean.isSuccess()){
                        mView.bindPlaceOrder(0,cashPlaceOrderBean.score,cashPlaceOrderBean.tScore);
                    }else {
                        mView.showErrorMsg(cashPlaceOrderBean.desc,null);
                    }
                }
            }
        });
    }


    /**
     *代金券下单
     * @param token  交易所token
     * @param id     大产品id
     * @param secret_access_key 交易所secret_access_key
     * @param productId 产品id
     * @param quantity  建仓数量
     * @param flag  0-买涨，1:买跌 允许值: 0, 1
     * @param profitLimit
     * @param lossLimit
     * @param couponId  体验券id
     * @param amount
     * @param productName
     * @param fee    交易手续费
     * @param code
     */
    @Override
    public void getVoucherPlaceOrder(String token, String id, String secret_access_key, String productId, int quantity, int flag, int profitLimit,
                                     int lossLimit, String couponId, int amount, String productName, double fee, String code) {

        toSubscribe(HttpManager.getApi().getVoucherPlaceOrder(token, id, secret_access_key, productId, quantity, flag, profitLimit, lossLimit,
                couponId, amount, productName, fee, code), new HttpSubscriber<CashPlaceOrderBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean cashPlaceOrderBean) {
                super._onNext(cashPlaceOrderBean);
                if (cashPlaceOrderBean !=null){
                    if (cashPlaceOrderBean.isSuccess()){
                        mView.bindPlaceOrder(1,cashPlaceOrderBean.score,cashPlaceOrderBean.tScore);
                    }else {
                        mView.showErrorMsg(cashPlaceOrderBean.desc,null);
                    }
                }
            }

        });
    }


    //跟买现金下单
    @Override
    public void getCashFollow(String token, String id, String secret_access_key, String productId, int quantity, int flag, int profitLimit, int lossLimit,
                              int amount, int holdNight, String productName, double fee, String code, String orderNo, String fellowerPhone, int followType) {

        toSubscribe(HttpManager.getApi().getCashFollow(token, id, secret_access_key, productId, quantity, flag, profitLimit, lossLimit, amount, holdNight, productName, fee,
                code, orderNo, fellowerPhone, followType), new HttpSubscriber<CashPlaceOrderBean>() {

            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(CashPlaceOrderBean cashPlaceOrderBean) {
                super._onNext(cashPlaceOrderBean);
                if (cashPlaceOrderBean !=null){
                    if (cashPlaceOrderBean.isSuccess()){
                        mView.bindPlaceOrder(0,cashPlaceOrderBean.score,cashPlaceOrderBean.tScore);
                    }else {
                        mView.showErrorMsg(cashPlaceOrderBean.desc,null);
                    }
                }
            }
        });
    }



    //挂单下单
    @Override
    public void getHangUpOrder(String token, String id, String productId, int quantity, int flag, int profitLimit, int lossLimit, int amount,
                               int holdNight, String productName, double fee, String code, String nickName, String floatNum, String registerAmount) {
        toSubscribe(HttpManager.getApi().getHangUpOrder(token, id, productId, quantity, flag, profitLimit, lossLimit, amount, holdNight, productName, fee
                , code, nickName, floatNum, registerAmount), new HttpSubscriber<JsonNull>() {
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
                mView.bindPlaceOrder(6,0,null);
            }
        });
    }


    //修改挂单
    @Override
    public void getUpdateHangUp(String token, String id, String registerAmount, int profitLimit, int lossLimit, String floatNum, int quantity,
                                double fee, int amount, int flag, int holdNight,String  product_id) {

        toSubscribe(HttpManager.getApi().getUpdateHangUp(token, id, registerAmount, profitLimit, lossLimit, floatNum, quantity, fee, amount, flag, holdNight,product_id), new HttpSubscriber<JsonNull>() {

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
                mView.bindPlaceOrder(7,0,null);
            }
        });
    }


    //银----------------分时线-------------------------------
    @Override
    public void getAGTime(final String productID, final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
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
