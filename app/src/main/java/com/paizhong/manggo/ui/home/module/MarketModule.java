package com.paizhong.manggo.ui.home.module;

import android.content.Context;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.HotProBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.widget.MyViewPager;
import com.paizhong.manggo.widget.banner.IndicatorView;
import com.paizhong.manggo.widget.market.MarketHelper;
import com.paizhong.manggo.widget.recycle.RecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页行情 Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class MarketModule extends BaseHomeModule<ItemHomePresenter> implements HandlerHelper.OnExecuteListener {
    @BindView(R.id.viewpager)
    MyViewPager mViewPager;
    @BindView(R.id.indicator)
    IndicatorView mIndicator;
    @BindView(R.id.rv)
    RecyclerView mRecycler;

    private MarketHelper mHelper;
    private HandlerHelper mHandler;

    public MarketModule(Context context, HandlerHelper helper) {
        super(context);
        mHandler = helper;
        mHelper = new MarketHelper(mContext);
        mHelper.bindView(mViewPager, mIndicator);
    }

    @Override
    protected void onResume() {
        onRefresh(false);
        mHelper.setContext(mContext);
    }

    @Override
    public void onVisible(boolean visible) {
        super.onVisible(visible);
        updateHotProduct(visible);
    }

    @Override
    public void setAuditing(boolean auditing) {
        setVisibility(auditing ? GONE : VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_market;
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        updateHotProduct(true);
        if (isEmpty()) execute(true);
    }

    @Override
    public void bindZJMarket(List<MarketHQBean> list) {
        mHelper.updateData(list);
        mHandler.execute(this);
    }

    @Override
    public void bindZJMarketEmpty() {
        mHandler.cancelExecute(this);
    }

    @Override
    public void bindHotProduct(HotProBean bean) {
        mHelper.updateHotProduct(bean.id);
    }

    private void updateHotProduct(boolean visible) {
        if (visible) mPresenter.getHotProduct();
    }

    @Override
    public void execute(boolean args) {
        mPresenter.getZJMarketList();
    }

    @Override
    protected boolean isEmpty() {
        return mHelper.isEmpty();
    }
}
