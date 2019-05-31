package com.paizhong.manggo.ui.kchart.fragment;



import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zq on 2017/11/7.
 */

public interface MinuteHourContract {
    interface View extends BaseView {
        void bindZJQuotation();
        String getTimeStr();
        float getNewLinePrice();
    }

    interface Presenter {
        void getCKSJQuotation(int type,String code,String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);

        void getAGTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//银
        void getCUTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//铜
        void getNLTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//镍
        void getZSTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//大豆
        void getZWTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//小麦
        void getZCTime(String yestodayClosePrice,List<MinuteHourBean> dataList, ArrayList<String> mDateList);//玉米
    }
}
