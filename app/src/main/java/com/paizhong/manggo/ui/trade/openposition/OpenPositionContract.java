package com.paizhong.manggo.ui.trade.openposition;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public interface OpenPositionContract {

    interface View extends BaseView {
        String getTimeStr();
        float getNewLinePrice();
        String getClosePrice();

        void bindProductList(List<ProductListBean> productList);

        void bindVipUser(boolean isVipUser);

        void bindZJQuotation(String productID,String yestodayClosePrice);
        //timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1H=一个小时 4H=四个小时 1D=日线  week=周线
        void bindZJKlineQuotation(String timeCode,String productID ,KLineBean kLineBean);
    }
    interface Presenter {
        void getProductList();
        void queryVipUser();
        void forbiddenUser(String isPhone);

        void getAGTime(String productID,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//银
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
