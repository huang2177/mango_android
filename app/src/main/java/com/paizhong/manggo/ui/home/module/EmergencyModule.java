package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.home.emergency.EmergencyDetailActivity;
import com.paizhong.manggo.ui.home.module.adapter.EmergencyAdapter;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 突发事件 module
 * Created by huang on 2018/10/24 0024 14:38
 */
public class EmergencyModule extends BaseHomeModule<ItemHomePresenter> implements BaseRecyclerAdapter.OnItemClick {
    private static final String EMERGENCY_METHOD = "getSuddenMarket";

    @BindView(R.id.emergency_recycler)
    RecyclerView mRecycler;

    private EmergencyAdapter mAdapter;

    public EmergencyModule(Context context) {
        super(context);
        mAdapter = new EmergencyAdapter(context);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        mRecycler.setLayoutManager(manager);

        onRefresh(false);
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.getEmergencyDetail(EMERGENCY_METHOD, (isEmpty() && !isRefresh));
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_emergency_alyout;
    }

    @Override
    public void bindEmergencyDetail(List<EmergencyBean> data) {
        if (data.size() == 0) {
            mAdapter.clearData();
            if (mAdapter.getFootersCount() == 0) {
                TextView emptyView = (TextView) mInflater.inflate(R.layout.view_empty_layout, null);
                emptyView.setText("暂无数据!");
                mAdapter.addFooterView(emptyView);
            }
        } else {
            mAdapter.addData(data);
            mRecycler.scrollToPosition(0);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        EmergencyBean bean = mAdapter.getData().get(position);
        if (bean != null) {
            Intent intent = new Intent(mActivity, EmergencyDetailActivity.class);
            intent.putExtra("url", bean.detail_json_url);
            intent.putExtra("title", "突发行情");
            mContext.startActivity(intent);
        }
    }
}
