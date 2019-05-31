package com.paizhong.manggo.ui.follow.module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.follow.contract.ItemFollowPresenter;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.ui.follow.module.adapter.RankChildAdapter;
import com.paizhong.manggo.ui.follow.rank.RankActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import com.paizhong.manggo.widget.switcher.AutoViewSwitcher;
import com.paizhong.manggo.widget.switcher.SwitcherHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 跟买广场 牛人堂 Adapter
 * Created by huang on 2018/8/17 0017 11:26
 */
public class RankModule extends BaseFollowModule<ItemFollowPresenter> implements View.OnClickListener
        , BaseRecyclerAdapter.OnItemClick {
    @BindView(R.id.rank_tv_more)
    TextView tvMore;
    @BindView(R.id.rank_recycle)
    RecyclerView mRecycler;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.home_switcher)
    AutoViewSwitcher switcher;
    private RankChildAdapter mAdapter;

    private SwitcherHelper mHelper;

    public RankModule(Context context) {
        super(context);
        initView();
        setListener();
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.getRankList();
        mPresenter.getRecordList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_follow_rank;
    }

    public void setListener() {
        tvMore.setOnClickListener(this);
    }

    @Override
    public void bindRankList(List<RankListBean> list) {
        mAdapter.addData(list);
    }

    @Override
    public void bindRecordList(List<NoticeListBean> list) {
        mHelper.setNotices(2, list);
        mHelper.start();
    }

    @Override
    public void onClick(View v) {
        if (!DeviceUtils.isFastDoubleClick()) {
            mContext.startActivity(new Intent(mContext, RankActivity.class));
        }
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(manager);

        mAdapter = new RankChildAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);

        mImage.setImageResource(R.mipmap.ic_notify1);
        mHelper = new SwitcherHelper(switcher);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (DeviceUtils.isFastDoubleClick() || !(mActivity).login()) {
            return;
        }
        Intent intent = new Intent(mContext, PersonInfoActivity.class);
        intent.putExtra("phone", mAdapter.getData().get(position).phone);
        mContext.startActivity(intent);
    }
}
