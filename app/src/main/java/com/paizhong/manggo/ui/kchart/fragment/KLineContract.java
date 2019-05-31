package com.paizhong.manggo.ui.kchart.fragment;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.at.KLineBean;

/**
 * Created by zq on 2017/11/7.
 */

public interface KLineContract {
    interface View extends BaseView {
        //timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1H=一个小时 4H=四个小时 1D=日线  week=周线
        void bindZJKlineQuotation(String timeCode, KLineBean kLineBean);
    }

    interface Presenter {
        void getZJKlineQuotation_1(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_5(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_15(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_30(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_1h(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_2h(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_4h(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_1d(int type,boolean mIsCKZS, String lineType, String code ,String productID);
        void getZJKlineQuotation_week(int type,boolean mIsCKZS, String lineType, String code ,String productID);
    }
}
