package com.paizhong.manggo.ui.follow.detail.weekcount;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.follow.ProTypeBean;
import com.paizhong.manggo.bean.follow.WeekCountEntry;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 本周统计
 * Created by huang on 2018/9/3 0003 14:17
 */
public class WeekCountFragment extends BaseFragment<WeekCountPresenter> implements WeekCountContract.View {
    public static final int[] PIE_COLORS1 = {Color.parseColor("#008EFF")};
    public static final int[] PIE_COLORS = {Color.parseColor("#DADCDE")//银
            , Color.parseColor("#F5A623")//铜
            , Color.parseColor("#FE7171")//镍
            , Color.parseColor("#DF76F4")
            , Color.parseColor("#A67142")
            , Color.parseColor("#7ED321")};


    @BindView(R.id.week_hit_rate)
    TextView tvWeekRate;
    @BindView(R.id.week_profit)
    TextView tvWeekProfit;
    @BindView(R.id.week_nearly_ten)
    TextView tvNearlyTen;
    @BindView(R.id.week_recycle1)
    RecyclerView mRecycler1;
    @BindView(R.id.week_recycle2)
    RecyclerView mRecycler2;
    @BindView(R.id.week_count_pie)
    PieChart mPieChart;

    private static String mPhone;
    public static WeekCountFragment mFragment;

    private ProTypeAdapter mProTypeAdapter;
    private ProfitCountAdapter mCountAdapter;

    public static WeekCountFragment getInstance(String phone) {
        if (mFragment == null) {
            mFragment = new WeekCountFragment();
        }
        mPhone = phone;
        return mFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_statist;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initRecycle();
        refreshView();
    }

    private void initRecycle() {
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        mRecycler1.setLayoutManager(manager);
        mProTypeAdapter = new ProTypeAdapter(mActivity);
        mRecycler1.setAdapter(mProTypeAdapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(mActivity);
        mRecycler2.setNestedScrollingEnabled(false);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler2.setLayoutManager(manager1);

        mCountAdapter = new ProfitCountAdapter(mActivity);
        mRecycler2.setAdapter(mCountAdapter);
    }


    public void refreshView() {
        mPresenter.getWeekCount(mPhone);
    }

    @Override
    public void bindWeekCount(List<WeekCountEntry> bean) {
        mCountAdapter.addData(bean);
    }

    @Override
    public void bindOtherCount(String weekProfit, String weekRate, String recentTen) {
        tvWeekProfit.setText(weekProfit);
        tvWeekRate.setText(weekRate);
        tvNearlyTen.setText(recentTen);
    }

    @Override
    public void bindPieChart(List<PieEntry> map, String allCount) {
        setPieChart(map, allCount);
    }

    @Override
    public void bindProType(List<ProTypeBean> beans) {
        mProTypeAdapter.notifyDataChanged(true, beans);
    }

    /**
     * 设置饼图样式
     *
     * @param pieValues
     * @param title
     */
    public void setPieChart(List<PieEntry> pieValues, String title) {
        mPieChart.setUsePercentValues(true);//设置使用百分比
        mPieChart.getDescription().setEnabled(false);//设置描述
        mPieChart.setCenterText(title);//设置环中的文字
        mPieChart.setCenterTextSize(13f);//设置环中文字的大小
        mPieChart.setRotationAngle(120f);//设置旋转角度
        mPieChart.setDrawEntryLabels(false);
        mPieChart.getLegend().setEnabled(false); //设置不需要图例
        mPieChart.setHoleRadius(70f);
        mPieChart.setTransparentCircleRadius(0f);

        setPieChartData(mPieChart, pieValues);
        mPieChart.animateX(1000, Easing.EasingOption.EaseInOutQuad);//数据显示动画
    }

    /**
     * 设置饼图数据源
     */
    private void setPieChartData(PieChart pieChart, List<PieEntry> pieValues) {

        PieDataSet dataSet = new PieDataSet(pieValues, "");
        dataSet.setSliceSpace(0f);//设置饼块之间的间隔
        dataSet.setSelectionShift(0f);//设置饼块选中时偏离饼图中心的距离

        dataSet.setColors(pieValues.size() == 1 ? PIE_COLORS1 : PIE_COLORS);//设置饼块的颜色

        dataSet.setValueLinePart1OffsetPercentage(0f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0f);
        dataSet.setValueLinePart2Length(0f);
        dataSet.setValueLineColor(Color.TRANSPARENT);//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(0f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }
}
