package com.paizhong.manggo.ui.follow.follower;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.follow.MyFollowerBean;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Des: 我的关注
 * Created by hs on 2018/5/30 0030 16:58
 */
public class MyFollowerActivity extends BaseActivity<MyFollowerPresenter> implements MyFollowerContract.View
        , BaseRecyclerAdapter.OnItemClick
        , OnRefreshLoadMoreListener {

    @BindView(R.id.follow_refresh)
    FixRefreshLayout mRefreshLayout;
    @BindView(R.id.follow_recycle)
    RecyclerView mRecyclerView;

    private int mPageIndex = 1;
    private String concernPhone;
    private static final int PAGE_SIZE = 20;
    private static final int REQUEST_CODE = 100;

    private MyFollowerAdapter mAdapter;
    private TextView mEmptyView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_follower;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initTitle();
        initRecycleView();
        getFollowerList(1, false);
    }

    private void initTitle() {
        mTitle.setTitle(true, "我的关注");
    }

    private void initRecycleView() {
        mEmptyView = (TextView) LayoutInflater.from(this).inflate(R.layout.view_empty_layout, null);
        mEmptyView.setText("暂无数据");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new MyFollowerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        if (mAdapter.getData() == null || mAdapter.getData().get(position) == null) {
            return;
        }
        concernPhone = mAdapter.getData().get(position).concernPhone;
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra("phone", concernPhone);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void bindFollowerList(int pageIndex, List<MyFollowerBean> list) {
        mPageIndex = pageIndex;
        if (list.size() >= 20) {
            mPageIndex++;
        }
        mAdapter.clearFooterView();
        mAdapter.notifyDataChanged(pageIndex == 1, list);

        mRefreshLayout.setEnableLoadMore(list.size() >= 20);
    }

    @Override
    public void bindFollowerEmpty() {
        mRefreshLayout.setEnableLoadMore(false);
        if (mPageIndex == 1 && mAdapter.getFootersCount() == 0) {
            mAdapter.clearData();
            mAdapter.addFooterView(mEmptyView);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getFollowerList(1, true);
        refreshlayout.finishRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        getFollowerList(mPageIndex, false);
        refreshlayout.finishLoadMore();
    }

    /**
     * 获取数据
     *
     * @param isRefresh 下拉加载时 不显示loading
     */
    private void getFollowerList(int pageIndex, boolean isRefresh) {
        boolean showLoading = !isRefresh && mAdapter.getData().isEmpty();
        mPresenter.getFollowerList(pageIndex, PAGE_SIZE, showLoading);
    }

    /**
     * @param requestCode
     * @param resultCode  为1时说明在牛人详情页面取消关注了
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1) {
            Iterator<MyFollowerBean> iterator = mAdapter.getData().iterator();
            while (iterator.hasNext()) {
                MyFollowerBean bean = iterator.next();
                if (TextUtils.equals(concernPhone, bean.concernPhone)) {
                    iterator.remove();
                    break;
                }
            }
            if (!iterator.hasNext()) {
                bindFollowerEmpty();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
