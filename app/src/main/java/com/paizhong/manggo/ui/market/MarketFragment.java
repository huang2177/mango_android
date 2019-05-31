package com.paizhong.manggo.ui.market;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeActivity;
import com.paizhong.manggo.utils.AnimationUtils;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.tab.CommonTabLayoutHor;
import com.paizhong.manggo.widget.tab.CustomTabEntity;
import com.paizhong.manggo.widget.tab.OnMyIntTabSelectListener;
import com.paizhong.manggo.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Des:
 * Created by hs on 2018/8/16 0016 17:04
 */
public class MarketFragment extends BaseFragment<MarketPresenter> implements MarketContract.View {
    @BindView(R.id.ll_change_range)
    LinearLayout llChangeRange;
    @BindView(R.id.iv_change_range)
    ImageView ivChangeRange;
    @BindView(R.id.tv_change_range_value)
    TextView mChangeRangeValue;
    @BindView(R.id.tv_recharge)
    TextView mRecharge;
    @BindView(R.id.rv_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh)
    FixRefreshLayout mSmartRefresh;
    @BindView(R.id.commonTabLayoutHor)
    CommonTabLayoutHor commonTabLayoutHor;

    private MarketAdapter mMarketAdapter;

    private static MarketFragment mMarketFragment;

    public static MarketFragment getInstance() {
        if (mMarketFragment == null) {
            synchronized (MarketFragment.class) {
                if (mMarketFragment == null) {
                    mMarketFragment = new MarketFragment();
                }
            }
        }
        return mMarketFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mMarketAdapter = new MarketAdapter(getActivity());
        mRecyclerView.setAdapter(mMarketAdapter);

        setTab();

        llChangeRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.rotateOne(ivChangeRange);
                if (mMarketAdapter.isChangeRange()) {
                    mChangeRangeValue.setText("涨跌值");
                    mMarketAdapter.setIsChangeRange(false);
                } else {
                    mChangeRangeValue.setText("涨跌幅");
                    mMarketAdapter.setIsChangeRange(true);
                }
            }
        });

        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
                mSmartRefresh.finishRefresh();
            }
        });
        mRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity.login()){
                    startActivity(new Intent(mActivity, RechargeActivity.class));
                }
            }
        });
        if (AppApplication.getConfig().mIsAuditing){
            mRecharge.setVisibility(View.GONE);
        }
    }

    public void setTab() {

        final ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("爱淘商城", ViewConstant.SIMP_TAB_ITEM_ZERO));
        tabEntities.add(new TabEntity("国际行情", ViewConstant.SIMP_TAB_ITEM_ONE));
        tabEntities.add(new TabEntity("CBOT农产品", ViewConstant.SIMP_TAB_ITEM_TWO));
        tabEntities.add(new TabEntity("LME贵金属", ViewConstant.SIMP_TAB_ITEM_THR));
        tabEntities.add(new TabEntity("国际外汇", ViewConstant.SIMP_TAB_ITEM_FOU));

        commonTabLayoutHor.setOnMyTabSelectListener(new OnMyIntTabSelectListener() {
            @Override
            public void onTabSelect(int position, int type) {
                if (!isHidden()) {
                    showLoading("");
                    mMarketAdapter.clear(tabEntities.get(position).getIntType());
                    getData();
                }
            }
        });
        commonTabLayoutHor.addTab(tabEntities);

    }

    public void getData() {
        if (ViewConstant.SIMP_TAB_ITEM_ZERO == mMarketAdapter.getTabType()) {
            mPresenter.getZJMarketList();
        } else {
            mPresenter.getZJCKSJMarketList(mMarketAdapter.getTabType());
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isHidden() && mMarketAdapter != null) {
                getData();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void startRun() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    private void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            startRun();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopRun();
        } else {
            startRun();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRun();
    }

    @Override
    public void bindStopShowLoad() {
        stopLoading();
    }

    @Override
    public List<MarketHQBean> getFrontData(int frontType) {
        return mMarketAdapter != null ? mMarketAdapter.getList(frontType) : null;
    }

    @Override
    public void bindZJMarketList(int tabType, List<MarketHQBean> marketBeanList) {
        if (mMarketAdapter.getTabType() == tabType) {
            mMarketAdapter.notifyDataChanged(marketBeanList);
        }
    }
}
