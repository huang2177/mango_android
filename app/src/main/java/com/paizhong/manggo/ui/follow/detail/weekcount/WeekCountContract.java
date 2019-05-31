package com.paizhong.manggo.ui.follow.detail.weekcount;


import com.github.mikephil.charting.data.PieEntry;
import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.ProTypeBean;
import com.paizhong.manggo.bean.follow.WeekCountEntry;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/5/31 0031 19:08
 */
public class WeekCountContract {
    public interface View extends BaseView {
        void bindProType(List<ProTypeBean> beans);

        void bindWeekCount(List<WeekCountEntry> list);

        void bindPieChart(List<PieEntry> map, String allCount);

        void bindOtherCount(String weekProfit, String  weekRate, String recentTen);
    }

    public interface Presenter {
        void getWeekCount(String phone);
    }
}
