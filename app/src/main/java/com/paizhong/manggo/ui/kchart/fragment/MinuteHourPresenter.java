package com.paizhong.manggo.ui.kchart.fragment;



import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;



public class MinuteHourPresenter extends BasePresenter<MinuteHourContract.View> implements MinuteHourContract.Presenter {


    //参考行情
    @Override
    public void getCKSJQuotation(int type, String code, final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getCKSJQuotation(type, code, true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(true,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }


    //银
    @Override
    public void getAGTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
       toSubscribe(HttpManager.getApi().getAGTime(true), new HttpSubscriber<KLineBean>() {
           @Override
           protected void _onNext(KLineBean kLineBean) {
               super._onNext(kLineBean);
               updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
           }
       });
    }

    //铜
    @Override
    public void getCUTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getCUTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //镍
    @Override
    public void getNLTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getNLTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //大豆
    @Override
    public void getZSTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZSTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //小麦
    @Override
    public void getZWTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZWTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }

    //玉米
    @Override
    public void getZCTime(final String yestodayClosePrice, final List<MinuteHourBean> dataList, final ArrayList<String> mDateList) {
        toSubscribe(HttpManager.getApi().getZCTime(true), new HttpSubscriber<KLineBean>() {
            @Override
            protected void _onNext(KLineBean kLineBean) {
                super._onNext(kLineBean);
                updateData(false,yestodayClosePrice,kLineBean,dataList,mDateList);
            }
        });
    }








    //公共数据处理
    private void updateData(boolean mIsCKZS,String yestodayClosePrice,KLineBean kLineBean,List<MinuteHourBean> dataList, final ArrayList<String> dateList){
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
            if (mIsCKZS){
                minuteHourBean.price = KLineUtils.getDoubleZ(kLineBean.dataList.get(i).get(0));
            }else {
                minuteHourBean.price = KLineUtils.getDouble(kLineBean.dataList.get(i).get(0));
            }
            minuteHourBean.percent = (minuteHourBean.getPrice() - KLineUtils.getDouble(yestodayClosePrice)) / KLineUtils.getDouble(yestodayClosePrice);
            dataList.add(minuteHourBean);
        }
        if (!mIsCKZS){
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

        mView.bindZJQuotation();
    }

}
