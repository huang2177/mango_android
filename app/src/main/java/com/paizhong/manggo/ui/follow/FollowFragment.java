package com.paizhong.manggo.ui.follow;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.ModuleManager;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.ui.follow.module.BannerModule;
import com.paizhong.manggo.ui.follow.module.NewBuyModule;
import com.paizhong.manggo.ui.follow.module.RankModule;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

import static com.paizhong.manggo.app.ModuleManager.TAG_FOLLOW;

/**
 * Created by huang on 2018/5/28 0028 15:13
 * Des: 跟单-广场
 */
public class FollowFragment extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.follow_refresh)
    FixRefreshLayout mRefresh;
    @BindView(R.id.follow_recycle)
    RecyclerView mRecycler;
    @BindView(R.id.follow_container)
    LinearLayout mContainer;

    private static FollowFragment mFragment;

    public HandlerHelper mHelper;

    public static FollowFragment getInstance() {
        if (mFragment == null) {
            synchronized (FollowFragment.class) {
                if (mFragment == null) {
                    mFragment = new FollowFragment();
                }
            }
        }
        return mFragment;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    public void loadData() {
        initView();
        setListener();
    }

    /**
     * 初始化RecycleView相关
     */
    private void initView() {
        mContainer.removeAllViews();
        mContainer.addView(new BannerModule(mActivity));
        mContainer.addView(new RankModule(mActivity));

        mHelper = new HandlerHelper(this);
        getLifecycle().addObserver(mHelper);
        new NewBuyModule(mFragment).bindRecycle(mRecycler);
        ModuleManager.getInstance().visibleModules(TAG_FOLLOW, true);
    }


    private void setListener() {
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ModuleManager.getInstance().refreshModules(TAG_FOLLOW);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mHelper != null) {
            mHelper.setHidden(hidden);
            ModuleManager.getInstance().visibleModules(TAG_FOLLOW, !hidden);
        }
    }
}
