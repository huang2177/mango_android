package com.paizhong.manggo.ui.follow.detail.weekcount;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.WeekCountEntry;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.List;


/**
 * Des: (牛人详情) 本周盈利统计 Adapter
 * Created by hs on 2018/5/28 0028 16:57
 */
public class ProfitCountAdapter extends BaseRecyclerAdapter<ProfitCountAdapter.ViewHolder, WeekCountEntry> {
    public static final int[] PIE_COLORS1 = {Color.parseColor("#008EFF")};
    public static final int[] PIE_COLORS = {Color.parseColor("#F74F54")
            , Color.parseColor("#1AC47A")};

    public ProfitCountAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_profit_count, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, WeekCountEntry data) {
        holder.tvCount1.setText(data.winCount);
        holder.tvCount2.setText(data.lossCount);
        setPieChart(data.map, data.proName, viewHolder);
    }


    /**
     * 设置饼图样式
     *
     * @param pieValues
     * @param title
     */
    public void setPieChart(List<PieEntry> pieValues, String title, ViewHolder holder) {
        holder.mPieChart.setUsePercentValues(true);//设置使用百分比
        holder.mPieChart.getDescription().setEnabled(false);//设置描述
        holder.mPieChart.setCenterText(title);//设置环中的文字
        holder.mPieChart.setCenterTextSize(12f);//设置环中文字的大小
        holder.mPieChart.setRotationAngle(120f);//设置旋转角度
        holder.mPieChart.setDrawEntryLabels(false);
        holder.mPieChart.getLegend().setEnabled(false); //设置不需要图例
        holder.mPieChart.setTransparentCircleRadius(0f);
        holder.mPieChart.setHoleRadius(70f);

        setPieChartData(holder.mPieChart, pieValues);
        holder.mPieChart.animateX(1000, Easing.EasingOption.EaseInOutQuad);//数据显示动画
    }

    /**
     * 设置饼图数据源
     */
    private void setPieChartData(PieChart pieChart, List<PieEntry> pieEntries) {

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setSliceSpace(0f);//设置饼块之间的间隔
        dataSet.setSelectionShift(0f);//设置饼块选中时偏离饼图中心的距离

        dataSet.setColors(pieEntries.size() == 1 ? PIE_COLORS1 : PIE_COLORS);//设置饼块的颜色

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


    class ViewHolder extends RecyclerView.ViewHolder {
        PieChart mPieChart;
        TextView tvCount1, tvCount2;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            initView();
        }

        private void initView() {
            tvCount1 = itemView.findViewById(R.id.tv_pro_count1);
            tvCount2 = itemView.findViewById(R.id.tv_pro_count2);
            mPieChart = itemView.findViewById(R.id.item_pie_chart);
        }
    }
}
