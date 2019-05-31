package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.CalendarBean;
import com.paizhong.manggo.ui.home.calendar.NewsDetailActivity;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.home.module.adapter.NewsAdapter;
import com.paizhong.manggo.utils.WeekUtil;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Des: 财经日历 Module
 * Created by hs on 2018/6/28 0028 15:19
 */
public class CalendarModule extends BaseHomeModule<ItemHomePresenter> implements BaseRecyclerAdapter.OnItemClick {

    @BindView(R.id.child_recycle)
    RecyclerView mRecycler;

    private NewsAdapter mAdapter;
    private int year, month, day;

    public CalendarModule(Context context) {
        super(context, null);
        initRecycle();
        onRefresh(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_calander_news;
    }

    /**
     * 初始化列表RecycleView
     */
    private void initRecycle() {
        Date today = new Date();
        day = WeekUtil.getToDay();
        year = WeekUtil.getYear(today);
        month = WeekUtil.getCurrentMonth(today);

        mAdapter = new NewsAdapter(mActivity);
        mRecycler.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecycler.setLayoutManager(layoutManager);

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        String _day = day < 10 ? "0" + day : String.valueOf(day);
        String _month = month < 10 ? "0" + month : String.valueOf(month);
        mPresenter.getCalendarList(year + "", _month + "" + _day, (isEmpty() && !isRefresh));
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void bindCalendarList(List<CalendarBean> list) {
        mAdapter.clearFooterView();
        mAdapter.notifyDataChanged(true, list);
        mRecycler.scrollToPosition(0);
    }

    @Override
    public void bindCalendarEmpty() {
        mAdapter.clearData();
        if (mAdapter.getFootersCount() == 0) {
            TextView emptyView = (TextView) mInflater.inflate(R.layout.view_empty_layout, null);
            emptyView.setText("暂无数据!");
            mAdapter.addFooterView(emptyView);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
        intent.putExtra("id", mAdapter.getData().get(position).dataId);
        mContext.startActivity(intent);
    }


    public void setDate(int year, int month, int day) {
        this.year = year;
        this.day = day;
        this.month = month;
    }
}
