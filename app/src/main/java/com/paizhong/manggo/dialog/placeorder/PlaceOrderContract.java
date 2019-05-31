package com.paizhong.manggo.dialog.placeorder;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public interface PlaceOrderContract {

    interface View extends BaseView {
        String getTimeStr();
        float getNewLinePrice();
        void bindHttpError();
        void bindSingleProduct(ProductListBean productBean);
        void bindUserInfo(UserZJBean userBean);
        void bindUserInfoRefresh(UserZJBean userBean);
        void bindValidRole(Boolean validRole);
        void bindPlaceOrder(int type, int score ,String couScore);
        void bindSingleTime(ProductTimeLimitBean timeLimitBean);
        void bindATPriceQuotation(MarketHQBean marketBean);


        void bindZJQuotation(String productID,String yestodayClosePrice);
        //timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1H=一个小时 4H=四个小时 1D=日线  week=周线
        void bindZJKlineQuotation(String timeCode,String productID ,KLineBean kLineBean);
    }

    interface Presenter {
       void getSingleProduct(String productID);
       void getUserInfo(String token);
       void getUserInfoRefresh(String token);
       void getValidRole();
       void getSingleTime(String code);
       void getReal(String type);

       void getCashPlaceOrder(String token,String id,String secret_access_key,String productId,int quantity,int flag,int profitLimit,int lossLimit,
                              int amount,int holdNight,String productName,double fee,String code,int followType,String nickName);

       void getVoucherPlaceOrder(String token,String id,String secret_access_key,String productId,int quantity,int flag,int profitLimit,int lossLimit,
                                 String couponId,int amount,String productName,double fee,String code);

       void getCashFollow(String token,String id,String secret_access_key,String productId,int quantity,int flag,int profitLimit,int lossLimit,
                          int amount,int holdNight,String productName,double fee,String code,String orderNo,String fellowerPhone,int followType);

       void getHangUpOrder(String token,String id,String productId,int quantity,int flag,int profitLimit,int lossLimit,int amount,int holdNight,
                           String productName,double fee,String code,String nickName,String floatNum,String registerAmount);


       void getUpdateHangUp(String token,String id,String registerAmount,int profitLimit,int lossLimit,String floatNum,int quantity,double fee,int amount,int flag,int holdNight,String  product_id);

        void getAGTime(String productID, String yestodayClosePrice, List<MinuteHourBean> dataList, ArrayList<String> mDateList);//银
        void getCUTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//铜
        void getNLTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//镍
        void getZSTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//大豆
        void getZWTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//小麦
        void getZCTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//玉米


        void getZJKlineQuotation_1(String lineType,String productID);
        void getZJKlineQuotation_5(String lineType,String productID);
        void getZJKlineQuotation_15(String lineType, String productID);
        void getZJKlineQuotation_30(String lineType, String productID);
        void getZJKlineQuotation_1h(String lineType, String productID);
        void getZJKlineQuotation_4h(String lineType, String productID);
        void getZJKlineQuotation_1d(String lineType,  String productID);
        void getZJKlineQuotation_week(String lineType, String productID);
    }
}
