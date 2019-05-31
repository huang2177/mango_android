package com.paizhong.manggo.ui.follow.record;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.follow.FollowRecordBean;
import com.paizhong.manggo.bean.follow.RecordRankBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.widget.appbar.AppBarChangeListener;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.DrawableCenterTextView;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.expand.ExpandableLayout;
import com.paizhong.manggo.widget.tab.OnTabSelectListener;
import com.paizhong.manggo.widget.tab.SlidingTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 跟买记录
 * Created by zab on 2018/7/2 0002.
 */
public class FollowRecordActivity extends BaseActivity<FollowRecordPresenter> implements FollowRecordContract.View
        , OnTabSelectListener
        , View.OnClickListener
        , OnRefreshLoadMoreListener {
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsing;
    @BindView(R.id.ab_barLayout)
    AppBarLayout abBarLayout;
    @BindView(R.id.tv_left)
    DrawableCenterTextView mLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_price)
    DinTextView tvPrice;
    @BindView(R.id.tv_integral)
    DinTextView tvIntegral;
    @BindView(R.id.common_tab)
    SlidingTabLayout mCommonTab;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.sliding_tab2)
    SlidingTabLayout mSlidingTab2;
    @BindView(R.id.smart_refresh_head)
    FixRefreshLayout refreshHead;
    @BindView(R.id.smart_refresh_footer)
    FixRefreshLayout refreshFooter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.expanded_layout)
    ExpandableLayout mExpandLayout;
    @BindView(R.id.divider)
    View mDivider;

    private boolean mIsNiu;
    private int mFollowTab; //tab type
    private int mPositionTab = 1; //是否为持仓tab 1持仓  2平仓

    private int mPageIndex = 1;//页码
    private final int mPageSize = 10; //每页数量

    private FollowRecordAdapter mRecordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
        initRecycler();
        setListener();
        mPresenter.getWeekProfit(AppApplication.getConfig().getMobilePhone());
    }

    private void initView() {
        //true 是牛人 false 不是牛人
        List<String> titles = Arrays.asList("持仓", "平仓");
        mIsNiu = getIntent().getBooleanExtra("niu", false);

        if (mIsNiu) {
            mCommonTab.addNewTab(Arrays.asList("发起跟买", "我的跟买"));
        } else {
            mSlidingTab2.addNewTab(titles);
            mDivider.setVisibility(View.GONE);
        }
        mSlidingTab.addNewTab(titles);
        mExpandLayout.expand();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(FollowRecordActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mFollowTab = mIsNiu ? ViewConstant.SIMP_TAB_ITEM_ONE : ViewConstant.SIMP_TAB_ITEM_TWO;

        mRecordAdapter = new FollowRecordAdapter(mIsNiu, FollowRecordActivity.this);
        mRecordAdapter.setRecordType(mFollowTab);
        mRecordAdapter.setViewType(mPositionTab);
        mRecyclerView.setAdapter(mRecordAdapter);
    }

    private void setListener() {
        mLeft.setOnClickListener(this);
        refreshHead.setOnRefreshListener(this);
        mCommonTab.setOnTabSelectListener(this);
        mSlidingTab.setOnTabSelectListener(this);
        refreshFooter.setOnLoadMoreListener(this);
        mSlidingTab2.setOnTabSelectListener(this);
        abBarLayout.addOnOffsetChangedListener(new AppBarChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    if (!mIsNiu) {
                        mExpandLayout.collapse(true);
                    }
                    mSlidingTab2.setCurrentTab(mSlidingTab.getCurrentTab());
                    mSlidingTab2.setVisibility(View.VISIBLE);
                    mLeft.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_left_arrow, 0, 0, 0);
                    tvTitle.setTextColor(ContextCompat.getColor(FollowRecordActivity.this, R.color.color_333333));
                    mCollapsing.setContentScrimColor(ContextCompat.getColor(FollowRecordActivity.this, R.color.color_ffffff));
                } else if (state == State.EXPANDED) {
                    if (!mIsNiu) {
                        mExpandLayout.expand(true);
                    }
                    mSlidingTab2.setVisibility(View.GONE);
                    mLeft.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_left_arrow_white, 0, 0, 0);
                    tvTitle.setTextColor(ContextCompat.getColor(FollowRecordActivity.this, R.color.color_ffffff));
                }
            }
        });
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsNiu) {
                if (ViewConstant.SIMP_TAB_ITEM_ONE == mFollowTab) {
                    mPresenter.getOrderPosition();
                } else {
                    mPresenter.getMyFellowOrder();
                }
            } else {
                mPresenter.getMyFellowOrder();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void startRun() {
        if (mPositionTab == 1) {
            stopRun();
            mHandler.post(mRunnable);
        } else {
            stopRun();
            //表示上拉加载状态
            if (mPageIndex > 1) {
                return;
            }
            if (mIsNiu) {
                if (ViewConstant.SIMP_TAB_ITEM_ONE == mFollowTab) {
                    mPresenter.getOrderPositionUp(AppApplication.getConfig().getMobilePhone(), mPageSize, mPageIndex);
                } else {
                    mPresenter.getMyFellowOrderUp(mPageSize, mPageIndex);
                }
            } else {
                mPresenter.getMyFellowOrderUp(mPageSize, mPageIndex);
            }
        }
    }

    private void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void bindStopShowLoad() {
        stopLoading();
    }

    //跟买盈利
    @Override
    public void bindWeekProfit(RecordRankBean rankBean) {
        if (rankBean != null) {
            ViewHelper.safelySetText(tvPrice, rankBean.totalProfit);
            ViewHelper.safelySetText(tvIntegral, rankBean.score);
        }
    }

    /**
     * @param pageIndex  接口行下标
     * @param recordType 接口 "1" 发起  2跟买
     * @param postType   1 持仓  2 平仓
     * @param recordList
     */
    @Override
    public void bindOrderPosition(int pageIndex, int recordType, int postType, List<FollowRecordBean> recordList) {
        if (mFollowTab == recordType) {
            mRecordAdapter.clearFooterView();
            if (postType == 1) {
                mRecordAdapter.notifyDataChanged(true, recordList);
            } else {
                this.mPageIndex = pageIndex;
                boolean mLoadMore = recordList.size() >= mPageSize;
                refreshFooter.setEnableLoadMore(mLoadMore);
                mRecordAdapter.notifyDataChanged(mPageIndex <= 1, recordList);
                if (mLoadMore) {
                    mPageIndex++;
                }
            }
        }
    }

    @Override
    public void bindOrderEmpty() {
        if (mRecordAdapter.getFootersCount() == 0) {
            stopRun();
            mRecordAdapter.clearData();
            View mEmptyView = View.inflate(this, R.layout.view_empty_layout, null);
            mRecordAdapter.addFooterView(mEmptyView);
        }
    }

    @Override
    public void onTabSelect(int position, View tabLayout) {
        switch (tabLayout.getId()) {
            case R.id.common_tab: {
                showLoading("");
                if (position == 0) { //发起的跟买
                    mFollowTab = ViewConstant.SIMP_TAB_ITEM_ONE;
                    mRecordAdapter.setRecordType(mFollowTab);
                    mPageIndex = 1;
                    startRun();
                } else {    //我的跟买
                    mFollowTab = ViewConstant.SIMP_TAB_ITEM_TWO;
                    mRecordAdapter.setRecordType(mFollowTab);
                    mPageIndex = 1;
                    startRun();
                }
            }
            break;
            case R.id.sliding_tab:
            case R.id.sliding_tab2: {
                switch (position) {
                    case 0: //持仓
                        refreshHead.setEnableRefresh(false);
                        refreshFooter.setEnableLoadMore(false);
                        mPositionTab = FollowRecordAdapter.VIEW_TYPE_ONE;
                        break;
                    case 1://平仓
                        refreshHead.setEnableRefresh(true);
                        refreshFooter.setEnableLoadMore(false);
                        mPositionTab = FollowRecordAdapter.VIEW_TYPE_TWO;
                        break;
                }
                mRecordAdapter.setViewType(mPositionTab);
                mPageIndex = 1;
                startRun();
            }
            break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPositionTab == 2) {
            if (ViewConstant.SIMP_TAB_ITEM_ONE == mFollowTab) {
                mPresenter.getOrderPositionUp(AppApplication.getConfig().getMobilePhone(), mPageSize, mPageIndex);
            } else {
                mPresenter.getMyFellowOrderUp(mPageSize, mPageIndex);
            }
        }
        refreshFooter.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getWeekProfit(AppApplication.getConfig().getMobilePhone());
        if (mPositionTab == 2) {
            if (ViewConstant.SIMP_TAB_ITEM_ONE == mFollowTab) {
                mPresenter.getOrderPositionUp(AppApplication.getConfig().getMobilePhone(), mPageSize, 1);
            } else {
                mPresenter.getMyFellowOrderUp(mPageSize, 1);
            }
        }
        refreshHead.finishRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRun();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRun();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRun();
    }
}
