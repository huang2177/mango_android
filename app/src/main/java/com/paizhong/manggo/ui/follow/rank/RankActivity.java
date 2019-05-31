package com.paizhong.manggo.ui.follow.rank;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 牛人堂
 * Created by hs on 2018/5/30 0030 16:58
 */
public class RankActivity extends BaseActivity<RankPresenter> implements View.OnClickListener
        , RankContract.View
        , BaseRecyclerAdapter.OnItemClick
        , OnRefreshListener {

    @BindView(R.id.follow_refresh)
    FixRefreshLayout mRefreshLayout;
    @BindView(R.id.follow_recycle)
    RecyclerView mRecyclerView;

    private RankAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_rank;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initTitle();
        initRecycleView();
        getRankList(false);
    }

    private void initTitle() {
        mTitle.setTitle(true, "牛人堂");
        mTitle.setRightTitle("说明", R.color.color_333333, this);
    }

    private void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new RankAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 获取数据
     *
     * @param isRefresh 下拉加载时 不显示loading
     */
    private void getRankList(boolean isRefresh) {
        boolean showLoading = !isRefresh && mAdapter.getData().isEmpty();
        mPresenter.getRankList(showLoading);
    }

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        Intent intentAbout = new Intent(this, WebViewActivity.class);
        intentAbout.putExtra("url", Constant.H5_RANK_DESC);
        intentAbout.putExtra("title", "排行说明");
        intentAbout.putExtra("noTitle", true);
        startActivity(intentAbout);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        if (!login()) {
            return;
        }
        if (mAdapter.getData() == null || mAdapter.getData().get(position) == null) {
            return;
        }
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra("phone", mAdapter.getData().get(position).phone);
        startActivity(intent);
    }

    @Override
    public void bindRankList(List<RankListBean> list) {
        mAdapter.notifyDataChanged(true, list);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getRankList(true);
        refreshlayout.finishRefresh();
    }
}
