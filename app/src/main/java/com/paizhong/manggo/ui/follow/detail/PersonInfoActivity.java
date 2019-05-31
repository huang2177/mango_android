package com.paizhong.manggo.ui.follow.detail;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.follow.PersonInfoBean;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.events.CancelFollowEvent;
import com.paizhong.manggo.ui.follow.detail.newbuy.NewBuyFragment;
import com.paizhong.manggo.ui.follow.detail.weekcount.WeekCountFragment;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.appbar.AppBarChangeListener;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.tab.SimpleOnTabSelectListener;
import com.paizhong.manggo.widget.tab.SlidingTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;

/**
 * Des: 牛人详情页面
 * Created by hs on 2018/5/30 0030 16:58
 */
public class PersonInfoActivity extends BaseActivity<PersonPresenter> implements View.OnClickListener
        , PersonContract.View
        , OnRefreshListener {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsing;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.smart_refresh)
    FixRefreshLayout mRefresh;
    @BindView(R.id.commonTab)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.person_iv)
    CircleImageView mIvHead;
    @BindView(R.id.person_tv_name)
    TextView mTvName;
    @BindView(R.id.person_tv_earnings)
    TextView mTvEarnings;
    @BindView(R.id.person_care_num)
    TextView mTvCareNum;
    @BindView(R.id.tv_care)
    TextView mTvCare;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.divider)
    View mDivider;

    private String phone;
    private int isConcern = 1;

    private Fragment mCurrent;
    private NewBuyFragment mNewBuyFragment;
    private WeekCountFragment mCountFragment;
    private FragmentManager mFragmentManager;
    private int orderCount = -1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
        updateTab(0);
        setListener();
        initFragment();
        mPresenter.getPersonInfo(phone);
    }

    private void initView() {
        phone = getIntent().getStringExtra("phone");
        if (!TextUtils.equals(phone, AppApplication.getConfig().getMobilePhone())) {
            mTvCare.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置监听
     */
    private void setListener() {
        tvLeft.setOnClickListener(this);
        mTvCare.setOnClickListener(this);
        mRefresh.setOnRefreshListener(this);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    mDivider.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_left_arrow, 0, 0, 0);
                    mCollapsing.setContentScrimColor(ContextCompat.getColor(PersonInfoActivity.this, R.color.color_ffffff));
                } else if (state == State.EXPANDED) {
                    mDivider.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_left_arrow_white, 0, 0, 0);
                }
            }
        });

        mTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position, View tabLayout) {
                if (position == 0) {
                    mRefresh.setEnableRefresh(true);
                    switchFragment(mCountFragment);
                } else {
                    switchFragment(mNewBuyFragment);
                    mRefresh.setEnableRefresh(mNewBuyFragment.isEmpty());
                }
            }
        });
    }

    private void initFragment() {
        mCurrent = new Fragment();
        mFragmentManager = getSupportFragmentManager();
        mNewBuyFragment = NewBuyFragment.getInstance(phone);
        mCountFragment = WeekCountFragment.getInstance(phone);
        mFragmentManager.beginTransaction()
                .add(R.id.fl_container, mCountFragment)
                .add(R.id.fl_container, mNewBuyFragment)
                .hide(mCountFragment)
                .hide(mNewBuyFragment)
                .commitNowAllowingStateLoss();
        switchFragment(mCountFragment);
    }

    /**
     * 切换Fragment
     *
     * @param to
     */
    public void switchFragment(Fragment to) {
        if (mCurrent == to) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (to.isAdded()) {
            transaction.hide(mCurrent).show(to).commitNowAllowingStateLoss();
        }
        mCurrent = to;
    }

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_care:
                //是否取消关注dialog
                if (isConcern == 0) {
                    new AppHintDialog(this).showAppDialog(3, -1);
                } else {
                    mPresenter.addConcern(phone, isConcern);
                }
                break;
        }
    }


    @Override
    public void bindPersonInfo(PersonInfoBean person) {
        isConcern = person.isConcern;
        mTvName.setText(person.nickName);
        mTvEarnings.setText(person.profit);
        mTvCare.setSelected(isConcern == 0);
        mTvCare.setText(isConcern == 0 ? "已关注" : "关注");
        mTvCareNum.setText(person.concernCount);
        ImageUtils.display(person.userPic, mIvHead, R.mipmap.ic_user_head);
    }

    @Override
    public void bindConcern() {
        isConcern = isConcern == 0 ? 1 : 0;
        int i = Integer.parseInt(mTvCareNum.getText().toString());
        if (i > 0 || i == 0 && isConcern == 0) {
            mTvCare.setSelected(isConcern == 0);
            mTvCare.setText(isConcern == 0 ? "已关注" : "关注");
            mTvCareNum.setText(String.valueOf(isConcern == 0 ? (i + 1) : (i - 1)));
            showErrorMsg(isConcern == 0 ? "关注成功" : "取消关注成功", null);
        }
    }

    /**
     * 取消关注
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CancelFollowEvent event) {
        if (event.code == 0 && event.position == -1) {
            mPresenter.addConcern(phone, isConcern);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        setResult(isConcern);
        super.finish();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPresenter.getPersonInfo(phone);
        mCountFragment.refreshView();
        refreshlayout.finishRefresh();
    }

    public void updateTab(int size) {
        if (orderCount != size) {
            mTabLayout.addNewTab(Arrays.asList("跟买统计", "最近跟买(" + size + ")"));
        }
        this.orderCount = size;
    }
}
