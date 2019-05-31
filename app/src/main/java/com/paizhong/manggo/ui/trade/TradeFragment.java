package com.paizhong.manggo.ui.trade;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.trade.capital.CapitalFragment;
import com.paizhong.manggo.ui.trade.hangup.HangUpFragment;
import com.paizhong.manggo.ui.trade.openposition.OpenPositionFragment;
import com.paizhong.manggo.ui.trade.position.PositionFragment;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.widget.MyViewPager;
import com.paizhong.manggo.widget.tab.MySlidingTabLayout;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Des:
 * Created by hs on 2018/8/16 0016 17:04
 */
public class TradeFragment extends BaseFragment {
    @BindView(R.id.commonTab)
    MySlidingTabLayout mCommonTab;
    @BindView(R.id.vp_view_pager)
    MyViewPager mViewPager;
    @BindView(R.id.iv_hint)
    ImageView ivHint;

    private boolean mIsInitView = false;
    private static TradeFragment mTradeFragment;

    public static TradeFragment getInstance() {
        if (mTradeFragment == null) {
            synchronized (TradeFragment.class) {
                if (mTradeFragment == null) {
                    mTradeFragment = new TradeFragment();
                }
            }
        }
        return mTradeFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_trade;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        initFragment();
        //mViewPager.setCanScroll(false);
        setPageChangeListener();
        mIsInitView = true;
    }


    /**
     * 初始化 fragment
     */
    private void initFragment() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OpenPositionFragment.getInstance());
        if (AppApplication.getConfig().mIsAuditing) {
            ivHint.setVisibility(View.INVISIBLE);
            //mViewPager.setOffscreenPageLimit(3);
            String[] titles = new String[]{"快览"};
            mCommonTab.setViewPager(mViewPager, titles, getFragmentManager(), fragmentList);
        } else {
            fragmentList.add(PositionFragment.getInstance());
            fragmentList.add(HangUpFragment.getInstance());
            fragmentList.add(CapitalFragment.getInstance());
            String[] titles = new String[]{"建仓", "持仓", "挂单", "资金"};
            mViewPager.setOffscreenPageLimit(3);
            mCommonTab.setViewPager(mViewPager, titles, getFragmentManager(), fragmentList);
        }
    }


    @OnClick(R.id.iv_hint)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", Constant.H5_AITAO_RULE);
        intent.putExtra("title", "爱淘商城规则");
        intent.putExtra("noTitle", true);
        intent.putExtra("builderPar", false);
        startActivity(intent);
    }

    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {
                if (AppApplication.getConfig().mIsAuditing) {
                    return;
                }
                if (position == 1 || position == 2 || position == 3) {
                    if (mActivity.login()) {
                        mViewPager.setCanScroll(true);
                    } else {
                        mCommonTab.setCurrentTab(0);
                    }
                } else {
                    // mViewPager.setCanScroll(false);
                }
            }
        });
    }


    public void setCanScroll(boolean canScroll) {
        if (mViewPager != null) {
            mViewPager.setCanScroll(canScroll);
        }
    }


    public void setSmPageCurrentTab(int smPage) {
        setSmPageCurrentTab(smPage, true);
    }

    public void setSmPageCurrentTab(int smPage, boolean postDelayed) {
        setSmPageCurrentTab(smPage, "", postDelayed);
    }

    public void setSmPageCurrentTab(final int smPage, final String productId, boolean postDelayed) {
        if (AppApplication.getConfig().mIsAuditing) {
            return;
        }
        if (mCommonTab == null) {
            return;
        }
        if (mCommonTab.getCurrentTab() == smPage) {
            changeProduct(smPage, productId);
            return;
        }
        if (postDelayed) {
            mCommonTab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCommonTab.setCurrentTab(smPage > 0 && smPage < 4 && !TextUtils.isEmpty(AppApplication.getConfig().getAt_token()) ? smPage : 0);
                    changeProduct(smPage, productId);
                }
            }, 20);
        } else {
            mCommonTab.setCurrentTab(smPage > 0 && smPage < 4 && !TextUtils.isEmpty(AppApplication.getConfig().getAt_token()) ? smPage : 0);
            changeProduct(smPage, productId);
        }
    }

    private void changeProduct(int smPage, final String productId) {
        if (smPage == 0 && !TextUtils.isEmpty(productId) && ivHint != null) {
            ivHint.postDelayed(new Runnable() {
                @Override
                public void run() {
                    OpenPositionFragment.getInstance().changeProduct(productId);
                }
            }, 20);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopRunHttp();
        } else {
            startRunHttp(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHidden()) {
            stopRunHttp();
        } else {
            startRunHttp(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRunHttp();
    }

    private void stopRunHttp() {
        if (mIsInitView && mCommonTab != null) {
            if (mCommonTab.getCurrentTab() == 0) {
                OpenPositionFragment.getInstance().stopRun();
            } else if (mCommonTab.getCurrentTab() == 1) {
                PositionFragment.getInstance().stopRun();
            } else if (mCommonTab.getCurrentTab() == 2) {
                HangUpFragment.getInstance().stopRun();
            } else if (mCommonTab.getCurrentTab() == 3) {
                CapitalFragment.getInstance().stopRun();
            }
        }
    }


    private void startRunHttp(boolean mHiddenChanged) {
        if (mIsInitView && mCommonTab != null) {
            if (mCommonTab.getCurrentTab() == 0) {
                if (OpenPositionFragment.getInstance().isInitView()) {
                    OpenPositionFragment.getInstance().startRun();
                }
            } else if (mCommonTab.getCurrentTab() == 1) {
                if (PositionFragment.getInstance().isInitView()) {
                    PositionFragment.getInstance().startRun();
                }
            } else if (mCommonTab.getCurrentTab() == 2) {
                if (HangUpFragment.getInstance().isInitView()) {
                    HangUpFragment.getInstance().startRun(false);
                }
            } else if (mCommonTab.getCurrentTab() == 3) {
                if (CapitalFragment.getInstance().isInitView()) {
                    CapitalFragment.getInstance().startRun();
                }
            }
        }
    }
}
