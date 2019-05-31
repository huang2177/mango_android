package com.paizhong.manggo.ui.follow.detail.weekcount;


import com.github.mikephil.charting.data.PieEntry;
import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.ProTypeBean;
import com.paizhong.manggo.bean.follow.WeekCountBean;
import com.paizhong.manggo.bean.follow.WeekCountEntry;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Des:
 * Created by hs on 2018/6/4 0004 12:20
 */
public class WeekCountPresenter extends BasePresenter<WeekCountContract.View> implements WeekCountContract.Presenter {

    @Override
    public void getWeekCount(String phone) {
        toSubscribe(HttpManager.getApi().getWeekCount(phone), new HttpSubscriber<WeekCountBean>() {
            @Override
            public void onNext(WeekCountBean bean) {

                List<ProTypeBean> proTypeBeans = Arrays.asList(new ProTypeBean(R.color.color_DADCDE, "GL银", bean.AgCount)
                        , new ProTypeBean(R.color.color_DF76F4, "GL大豆", bean.SoybeanCount)
                        , new ProTypeBean(R.color.color_FE7171, "GL镍", bean.NlCount)
                        , new ProTypeBean(R.color.color_A67142, "GL小麦", bean.WheatCount)
                        , new ProTypeBean(R.color.color_F5A623, "GL铜", bean.CuCount)
                        , new ProTypeBean(R.color.color_7ED321, "GL玉米", bean.CornCount));
                mView.bindProType(proTypeBeans);


                int allCount = bean.AgCount + bean.CuCount + bean.NlCount + bean.SoybeanCount + bean.WheatCount + bean.CornCount;
                List<PieEntry> map = new ArrayList<>();
                if (allCount != 0) {
                    map.add(new PieEntry((float) bean.AgCount / allCount)); //"GL银",
                    map.add(new PieEntry((float) bean.CuCount / allCount));//"GL铜",
                    map.add(new PieEntry((float) bean.NlCount / allCount)); //"GL镍",
                    map.add(new PieEntry((float) bean.SoybeanCount / allCount));//"GL大豆",
                    map.add(new PieEntry((float) bean.WheatCount / allCount));//"GL小麦",
                    map.add(new PieEntry((float) bean.CornCount / allCount));//"GL玉米",
                } else {
                    map.add(new PieEntry(1f));
                }
                mView.bindPieChart(map, String.valueOf("共" + allCount + "单"));

                List<WeekCountEntry> list = new ArrayList<>();
                list.add(new WeekCountEntry("全部", bean.AllWinCount + "单", bean.AllLossCount + "单", getSingProData(bean.AllWinCount, bean.AllLossCount)));
                list.add(new WeekCountEntry("银", bean.AgWinCount + "单", bean.AgLossCount + "单", getSingProData(bean.AgWinCount, bean.AgLossCount)));
                list.add(new WeekCountEntry("铜", bean.CuWinCount + "单", bean.CuLossCount + "单", getSingProData(bean.CuWinCount, bean.CuLossCount)));
                list.add(new WeekCountEntry("镍", bean.NlWinCount + "单", bean.NlLossCount + "单", getSingProData(bean.NlWinCount, bean.NlLossCount)));
                list.add(new WeekCountEntry("大豆", bean.SoybeanWinCount + "单", bean.SoybeanLossCount + "单", getSingProData(bean.SoybeanWinCount, bean.SoybeanLossCount)));
                list.add(new WeekCountEntry("小麦", bean.WheatWinCount + "单", bean.WheatLossCount + "单", getSingProData(bean.WheatWinCount, bean.WheatLossCount)));
                list.add(new WeekCountEntry("玉米", bean.CornWinCount + "单", bean.CornLossCount + "单", getSingProData(bean.CornWinCount, bean.CornLossCount)));
                mView.bindWeekCount(list);

                String weekProfit = ((int) (bean.profitRate * 100)) + "%";
                String weekRate = ((int) (bean.profitOrderNum * 100)) + "%";
                String recentTen = bean.tenWinNum + "盈" + bean.tenLossNum + "亏";

                mView.bindOtherCount(weekProfit, weekRate, recentTen);
            }

            private List<PieEntry> getSingProData(int winCount, int lossCount) {
                List<PieEntry> map = new ArrayList<>();
                if (winCount + lossCount != 0) {
                    map.add(new PieEntry((float) winCount / (winCount + lossCount)));
                    map.add(new PieEntry((float) lossCount / (winCount + lossCount)));
                } else {
                    map.add(new PieEntry(1f));
                }
                return map;
            }
        });
    }
}
