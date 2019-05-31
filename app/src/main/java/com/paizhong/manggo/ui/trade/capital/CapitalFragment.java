package com.paizhong.manggo.ui.trade.capital;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.trade.CapitalListBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.other.DateDialog;
import com.paizhong.manggo.events.AppUserAccountEvent;
import com.paizhong.manggo.ui.paycenter.present.PresentActivity;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeActivity;
import com.paizhong.manggo.ui.voucher.VoucherZjActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.tab.CommonNoViewTabLayout;
import com.paizhong.manggo.widget.tab.CustomTabEntity;
import com.paizhong.manggo.widget.tab.SimpleOnTabSelectListener;
import com.paizhong.manggo.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易 - 资金
 * Created by zab on 2018/4/2 0002.
 */

public class CapitalFragment extends BaseFragment<CapitalPresenter> implements CapitalContract.View {
    @BindView(R.id.smart_refresh)
    FixRefreshLayout mSmartRefresh;
    @BindView(R.id.lv_listView)
    ListView lvListView;

    private DinTextView tvPrice;
    private DinTextView tvZProfitLoss;
    private DinTextView tvAvailableBalance;
    private TextView tvVoucherNum;

    private CommonNoViewTabLayout mCommonTab;
    private View mTimeLayout;
    private TextView mStartTime;
    private TextView mEndTime;
    private View mEmptyView;
    private TextView mTvEmpty;
    private View llVoucher;

    private boolean mIsInitView = false;
    private DateDialog mDateDialog;
    private CapitalAdapter mCapitalAdapter;

    private final int mPageSize = 7;//每次加载条数
    private int mPage = 0;

    //当前选择的tab
    private int mTabSelect = ViewConstant.SIMP_TAB_ITEM_ZERO;
    private TextView tvWithdrawals;
    private TextView tvRecharge;
    private double mAmount;
    private String mYMDTime;

    private static CapitalFragment mCapitalFragment;
    public static  CapitalFragment getInstance() {
        if (mCapitalFragment == null){
            synchronized (CapitalFragment.class){
                if (mCapitalFragment == null){
                    mCapitalFragment = new CapitalFragment();
                }
            }
        }
        return mCapitalFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_capital;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    //app 登录成功、退出
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AppUserAccountEvent event) {
        if (event.code == 0) {
            clearCapital();
        }
    }


    public void clearCapital() {
        if (mIsInitView) {
            tvAvailableBalance.setText("---");
            tvVoucherNum.setText("---");
            tvZProfitLoss.setText("---");
            tvPrice.setText("---");
            mCapitalAdapter.notifyDataChanged(true, null);
        }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        mSmartRefresh.setEnableLoadMore(false);
        mSmartRefresh.setEnableRefresh(false);
        mDateDialog = new DateDialog(getActivity());
        View headView = View.inflate(mActivity, R.layout.include_capital_layout, null);
        tvPrice =  headView.findViewById(R.id.tv_price);
        tvVoucherNum = headView.findViewById(R.id.tv_voucher_num);
        tvZProfitLoss = headView.findViewById(R.id.tv_z_profit_loss);
        tvAvailableBalance = headView.findViewById(R.id.tv_available_balance);
        mCommonTab = headView.findViewById(R.id.commonTab);
        mTimeLayout = headView.findViewById(R.id.ll_time_layout);
        mStartTime = headView.findViewById(R.id.tv_start_time);
        mEndTime = headView.findViewById(R.id.tv_end_time);
        tvWithdrawals = headView.findViewById(R.id.tv_withdrawals);
        tvRecharge = headView.findViewById(R.id.tv_recharge);
        llVoucher = headView.findViewById(R.id.ll_voucher);


        mEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_layout, null);
        ImageView iv_empty = mEmptyView.findViewById(R.id.iv_empty);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_empty.getLayoutParams();
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.dimen_50dp);
        iv_empty.setLayoutParams(layoutParams);
        iv_empty.setImageResource(R.mipmap.ic_empty_no_trad);
        mTvEmpty = mEmptyView.findViewById(R.id.tv_empty);
        mTvEmpty.setText("暂无记录");

        int year = mDateDialog.getYear();
        int month = mDateDialog.getMonth();
        int day = mDateDialog.getDay();
        mYMDTime = year + "-" + (month >= 10 ? month : "0" + month) + "-" + (day >= 10 ? day : "0" + day);

        mStartTime.setText(mYMDTime);
        mEndTime.setText(mYMDTime);

        mCapitalAdapter = new CapitalAdapter(getActivity());
        lvListView.addHeaderView(headView);
        lvListView.setAdapter(mCapitalAdapter);

        final ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("交易记录", ViewConstant.SIMP_TAB_ITEM_ZERO));
        tabEntities.add(new TabEntity("提现记录", ViewConstant.SIMP_TAB_ITEM_ONE));
        tabEntities.add(new TabEntity("充值记录", ViewConstant.SIMP_TAB_ITEM_TWO));

        mCommonTab.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position,View view) {
                super.onTabSelect(position,view);
                if (isVisible()) {
                    mCapitalAdapter.clear();
                    if (position == 0){
                        mTimeLayout.setVisibility(View.VISIBLE);
                        mStartTime.setText(mYMDTime);
                        mEndTime.setText(mYMDTime);
                    }else {
                        mTimeLayout.setVisibility(View.GONE);
                    }
                    mTabSelect = tabEntities.get(position).getIntType();
                    setmEmptyView(false);
                    mPage = 0;
                    mRefreshData(true, mPage);
                }
            }
        });
        mCommonTab.setTabData(tabEntities);
        setListener();
        this.mIsInitView = true;
    }

    private void setListener() {
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateDialog.show();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateDialog.show();
            }
        });


        //日期筛选
        mDateDialog.setBirthdayListener(new DateDialog.OnBirthListener() {
            @Override
            public void onClick(String start, String end) {
                mStartTime.setText(start);
                mEndTime.setText(end);
                mPage = 0;
                mRefreshData(false, mPage);
            }
        });

        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 0;
                mRefreshData(false, mPage);
                mSmartRefresh.finishRefresh();
            }
        });

        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mRefreshData(false, mPage);
                mSmartRefresh.finishLoadMore();
            }
        });

        //提现
        tvWithdrawals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    startActivity(new Intent(getActivity(), PresentActivity.class));
                }
            }
        });

        //充值
        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                }
            }
        });

        //代金券
        llVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    startActivity(new Intent(getActivity(), VoucherZjActivity.class));
                }
            }
        });
    }


    private void mRefreshData(boolean showLoad, int mPage) {
        if (ViewConstant.SIMP_TAB_ITEM_ZERO == mTabSelect) {
            //交易记录
            mPresenter.getTodayOrderBill(mPage > 0 ? false : showLoad, AppApplication.getConfig().getAt_token(), mPage, mPageSize,
                    TimeUtil.getDateByFormatYMD(mStartTime.getText().toString()), TimeUtil.getDateByFormatYMDate(mEndTime.getText().toString()));
        } else if (ViewConstant.SIMP_TAB_ITEM_ONE == mTabSelect) {
            //提现记录
            mPresenter.getWithdrawalList(mPage > 0 ? false : showLoad, AppApplication.getConfig().getAt_token(), mPage, mPageSize);
        } else if (ViewConstant.SIMP_TAB_ITEM_TWO == mTabSelect) {
            //充值记录
            mPresenter.getRechargeList(mPage > 0 ? false : showLoad, AppApplication.getConfig().getAt_token(), mPage, mPageSize);
        }
    }


    private void setmEmptyView(boolean isEmpty) {
        if (isEmpty) {
            if (lvListView.getFooterViewsCount() <= 0 && mEmptyView != null) {
                lvListView.addFooterView(mEmptyView);
            }
        } else {
            if (lvListView.getFooterViewsCount() > 0 && mEmptyView != null) {
                lvListView.removeFooterView(mEmptyView);
            }
        }
    }


    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (AppApplication.getConfig().isLoginStatus()) {
                mPresenter.getPositionUserMoney(mAmount, AppApplication.getConfig().getAt_token());
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                stopRun();
            }
        }
    };


    public boolean isInitView() {
        return mIsInitView;
    }

    public void startRun() {
        lvListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppApplication.getConfig().isLoginStatus() && !TextUtils.isEmpty(AppApplication.getConfig().getAt_token())) {
                    mPage = 0;
                    mRefreshData(false, mPage);
                    mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
                }
            }
        }, 20);
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    public void startRunUserMoney() {
        if (isHidden()) {
            mPresenter.getPositionUserMoney(mAmount, AppApplication.getConfig().getAt_token());
        } else {
            mHandler.removeCallbacks(mRunnable);
            mHandler.post(mRunnable);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInitView()) {
            if (isVisibleToUser) {
                startRun();
            } else {
                stopRun();
            }
        }
    }



    @Override
    public int getTabSelect() {
        return mTabSelect;
    }


    @Override
    public void bindTodayOrderBill(int tabType, List<CapitalListBean> listBeans) {
        if (tabType == mTabSelect) {
            if (listBeans == null || listBeans.size() == 0) {
                if (mSmartRefresh.isEnableLoadMore()){
                    mSmartRefresh.setEnableLoadMore(false);
                }
                if (mPage <= 0) {
                    setmEmptyView(true);
                    mCapitalAdapter.notifyDataChanged(true, null);
                }
            } else {
                setmEmptyView(false);
                boolean mLoadMore = listBeans.size() >= mPageSize ? true : false;
                if (mLoadMore != mSmartRefresh.isEnableLoadMore()){
                    mSmartRefresh.setEnableLoadMore(mLoadMore);
                }
                mCapitalAdapter.notifyDataChanged(mPage > 0 ? false : true, listBeans);
                if (mLoadMore) {
                    mPage++;
                }
            }
        }
    }


    //获取余额
    @Override
    public void bindUserInfo(UserZJBean userZJBean) {
        if (userZJBean != null && userZJBean.isSuccess() && userZJBean.data != null) {
            mAmount = NumberUtil.divide(userZJBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
            tvAvailableBalance.setText(String.valueOf(mAmount));
            int voucherNum = userZJBean.data.coupons == null ? 0 : userZJBean.data.coupons.size();
            tvVoucherNum.setText(voucherNum + "");
            startRunUserMoney();
        }
    }

    @Override
    public void bindPositionUserMoney(Double countAmount, Double countPostAmount ,Double countAmountBj) {
        if (countAmount == null || countPostAmount == null) {
            tvPrice.setText(mAmount + "");
            tvZProfitLoss.setText("0");
            tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_333333));
            stopRun();
        } else {
            tvPrice.setText(countAmount + "");
            tvZProfitLoss.setText(countPostAmount + "");
            if (countPostAmount > countAmountBj){
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_F74F54));
            }else if (countPostAmount < countAmountBj){
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_1AC47A));
            }else {
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_333333));
            }
        }
    }
}
