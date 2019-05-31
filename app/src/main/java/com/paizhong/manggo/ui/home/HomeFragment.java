package com.paizhong.manggo.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.app.ModuleManager;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.http.ApiException;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;
import com.paizhong.manggo.ui.home.module.ActivityModule1;
import com.paizhong.manggo.ui.home.module.ActivityModule2;
import com.paizhong.manggo.ui.home.module.BannerModule;
import com.paizhong.manggo.ui.home.module.BottomControlHelper;
import com.paizhong.manggo.ui.home.module.MarketModule;
import com.paizhong.manggo.ui.home.module.NewBuyModule;
import com.paizhong.manggo.ui.home.module.TabConfigModule;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

import static com.paizhong.manggo.app.ModuleManager.TAG_HOME;

/**
 * Des：首页
 * Created by huang on 2018/8/16 0016 17:04
 */
public class HomeFragment extends BaseFragment implements OnRefreshListener {
    private static HomeFragment mFragment;

    @BindView(R.id.app_bar)
    AppBarLayout mBarLayout;
    @BindView(R.id.home_refresh)
    FixRefreshLayout mRefresh;
    @BindView(R.id.module_config)
    TabConfigModule mConfigModule;

    private HandlerHelper mHelper;
    private LinearLayout mContainer;

    public static HomeFragment getInstance() {
        if (mFragment == null) {
            synchronized (HomeFragment.class) {
                if (mFragment == null) {
                    mFragment = new HomeFragment();
                }
            }
        }
        return mFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {
    }

    /***
     * 预加载数据
     */
    public void preLoadData(Context context) {
        if (mContainer != null) return;

        mHelper = new HandlerHelper(mFragment);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);

        mContainer.addView(new BannerModule(context));
        mContainer.addView(new MarketModule(context, mHelper));
        mContainer.addView(new ActivityModule2(context));
        mContainer.addView(new NewBuyModule(context, mHelper));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        preLoadData(context);
        ModuleManager.getInstance().bindContext(context);
    }

    @Override
    public void loadData() {
        preLoadData(mActivity);
        getLifecycle().addObserver(mHelper);

        mBarLayout.removeView(mContainer);
        if (mContainer.getParent() == null) {
            mBarLayout.addView(mContainer, (0));
        }
        BottomControlHelper.init(mActivity,AppApplication.getConfig().mIsAuditing);
        ModuleManager.getInstance().setAuditing(AppApplication.getConfig().mIsAuditing);

        mRefresh.setOnRefreshListener(this);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mHelper != null) {
            mHelper.setHidden(hidden);
            ModuleManager.getInstance().visibleModules(TAG_HOME, !hidden);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ModuleManager.getInstance().refreshModules(TAG_HOME);
        refreshLayout.finishRefresh();
    }

    /***
     * 跳转到指定tab
     * @param position 0盈利广场 1交易机会 2财经日历 3突发行情 4大事件
     */
    public void setCurrentTab(int position) {
        mBarLayout.setExpanded(false);
        mConfigModule.setCurrentTab(position);
    }
}
