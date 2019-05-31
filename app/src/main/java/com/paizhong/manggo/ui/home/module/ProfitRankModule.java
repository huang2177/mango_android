package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.ProfitBean;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.home.module.adapter.ProfitRankAdapter;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * Des:盈利广场~module
 * Created by huang on 2019/1/14 0014 17:25
 */
public class ProfitRankModule extends BaseHomeModule<ItemHomePresenter> implements OnLoadMoreListener {
    private static final int SIZE = 10;
    @BindView(R.id.profit)
    RecyclerView mRecycler;
    @BindView(R.id.refresh)
    FixRefreshLayout mRefresh;

    private int page;
    private ProfitRankAdapter mAdapter;

    public ProfitRankModule(Context context) {
        super(context);
        initView();
        onRefresh((false));
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_profit;
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    /**
     * 初始化列表RecycleView
     */
    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecycler.setLayoutManager(layoutManager);

        mAdapter = new ProfitRankAdapter(mActivity);
        mRecycler.setAdapter(mAdapter);
        if (mRefresh !=null){
            mRefresh.setOnLoadMoreListener(this);
        }
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        page = 1;
        mPresenter.queryProfitRank(page);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.queryProfitRank(page);
        refreshLayout.finishLoadMore();
    }

    @Override
    public void bindProfitRank(List<ProfitBean> bean, int page) {
        mAdapter.clearFooterView();
        mAdapter.notifyDataChanged((page == 1), bean);

        mRefresh.setEnableLoadMore(bean.size() == SIZE);
        this.page = bean.size() == SIZE ? page + 1 : page;
    }

    @Override
    public void bindProfitEmpty() {
        mRefresh.setEnableLoadMore(false);
        if (page == 1 && mAdapter.getFootersCount() == 0) {
            mAdapter.clearData();
            TextView emptyView = (TextView) mInflater.inflate(R.layout.view_empty_layout, null);
            emptyView.setText("暂无数据!");
            mAdapter.addFooterView(emptyView);
        }
    }
}
