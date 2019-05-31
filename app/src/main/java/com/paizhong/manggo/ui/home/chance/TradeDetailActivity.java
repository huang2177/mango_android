package com.paizhong.manggo.ui.home.chance;

import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * 交易机会详情
 * Created by zab on 2018/5/3 0003.
 */

public class TradeDetailActivity extends BaseActivity implements OnRefreshListener {
    @BindView(R.id.scroll_view)
    LinearLayout mScrollView;
    @BindView(R.id.smart_refresh)
    FixRefreshLayout mSmartRefresh;

    public int type;//1 公告  2表示分析师
    public String tradeId;


    private TradeDetailModule mTradeDetailView;
    private TradeNoticeModule mTradeNoticeView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_trade_detail;
    }

    @Override
    public void initPresenter() {
        //mPresenter.init(TradeChanceDetailActivity.this);
    }

    @Override
    public void loadData() {
        tradeId = getIntent().getStringExtra("tradeId");
        type = getIntent().getIntExtra("type", 0);

        showLoading("");
        switch (type) {
            case 1:
                mTitle.setTitle(true, "公告");
                mTradeNoticeView = new TradeNoticeModule(TradeDetailActivity.this, tradeId);
                mScrollView.addView(mTradeNoticeView.mView);
                break;
            case 2:
                mTitle.setTitle(true, "专家解读");
                mTradeDetailView = new TradeDetailModule(TradeDetailActivity.this, tradeId);
                mScrollView.addView(mTradeDetailView.mView);
                break;
        }

        mSmartRefresh.setEnableLoadMore(false);
        mSmartRefresh.setOnRefreshListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTradeNoticeView != null) {
            mTradeNoticeView.onDestory();
        }
        if (mTradeDetailView != null) {
            mTradeDetailView.onDestory();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (type == 1 && mTradeNoticeView != null) {
            mTradeNoticeView.refreshData();
        } else if (type == 2 && mTradeDetailView != null) {
            mTradeDetailView.refreshData();
        }
        mSmartRefresh.finishRefresh();
    }
}
