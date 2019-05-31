package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.TradeChanceBean;
import com.paizhong.manggo.ui.home.chance.TradeDetailActivity;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.home.module.adapter.TradeChanceAdapter;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 交易机会 Module
 * Created by hs on 2018/6/28 0028 15:19
 */
public class ChanceModule extends BaseHomeModule<ItemHomePresenter> implements BaseRecyclerAdapter.OnItemClick {

    @BindView(R.id.trade_chance_rv)
    RecyclerView mRecycler;

    private int pageIndex = 1;
    private TradeChanceAdapter mAdapter;


    public ChanceModule(Context context) {
        super(context, null);
        initView();
        onRefresh(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_trade_chance;
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.selectTradeChance(pageIndex);
    }

    @Override
    public void bindTradeChance(List<TradeChanceBean> list, int pageIndex) {
        mAdapter.clearFooterView();
        mAdapter.notifyDataChanged((pageIndex == 1), list);

        mRecycler.scrollToPosition(0);
    }

    /**
     * 初始化列表RecycleView
     */
    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecycler.setLayoutManager(layoutManager);

        mAdapter = new TradeChanceAdapter(mActivity);
        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void bindTradeEmpty() {
        if (mAdapter.getFootersCount() == 0 && pageIndex == 1) {
            mAdapter.clearData();
            TextView emptyView = (TextView) mInflater.inflate(R.layout.view_empty_layout, null);
            emptyView.setText("暂无数据!");
            mAdapter.addFooterView(emptyView);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        TradeChanceBean bean = mAdapter.getData().get(position);
        if (DeviceUtils.isFastDoubleClick() || bean == null) {
            return;
        }
        Intent intent = new Intent(mContext, TradeDetailActivity.class);
        intent.putExtra("type", bean.type);
        intent.putExtra("tradeId", bean.tradeOpportunityId);
        mContext.startActivity(intent);
    }
}
