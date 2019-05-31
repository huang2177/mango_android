package com.paizhong.manggo.ui.voucher;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.zj.VoucherKyZJBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.RecyclerView;
import com.paizhong.manggo.widget.tab.CommonNoViewTabLayout;
import com.paizhong.manggo.widget.tab.CustomTabEntity;
import com.paizhong.manggo.widget.tab.SimpleOnTabSelectListener;
import com.paizhong.manggo.widget.tab.TabEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zab on 2018/6/28 0028.
 */
public class VoucherZjActivity extends BaseActivity<VoucherZjPresenter> implements VoucherZjContract.View{
    @BindView(R.id.commonTab)
    CommonNoViewTabLayout mCommonTab;
    @BindView(R.id.smart_refresh)
    FixRefreshLayout smartRefresh;
    @BindView(R.id.rv_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private final int mPageSize = 10;
    private int mPage = 0;
    private String mVoucherZJType = ViewConstant.VOUCHER_TAB_KY;
    private VoucherZjAdapter mVoucherZjAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zj_voucher;
    }

    @Override
    public void initPresenter() {
         mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, "代金券");
        LinearLayoutManager layoutManager = new LinearLayoutManager(VoucherZjActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ivEmpty.setImageResource(R.mipmap.ic_empty_no_voucher);
        tvEmpty.setText("暂无代金券");
        recyclerView.setEmptyView(llEmpty);

        mVoucherZjAdapter = new VoucherZjAdapter(VoucherZjActivity.this);
        recyclerView.setAdapter(mVoucherZjAdapter);

        ArrayList<CustomTabEntity> tabEntitys = new ArrayList<>();
        tabEntitys.add(new TabEntity("可使用", ViewConstant.VOUCHER_TAB_KY));
        tabEntitys.add(new TabEntity("历史券",ViewConstant.VOUCHER_TAB_GQ));
        mCommonTab.setOnTabSelectListener(new SimpleOnTabSelectListener(){
            @Override
            public void onTabSelect(int position, View view) {
                super.onTabSelect(position,view);
                showLoading("");
                if (position == 0){
                    smartRefresh.setEnableLoadMore(false);
                    mVoucherZJType = ViewConstant.VOUCHER_TAB_KY;
                    mVoucherZjAdapter.clearData();
                    mPresenter.getUserVoucherZjList(AppApplication.getConfig().getAt_token(),AppApplication.getConfig().getAt_userId());
                }else {
                    smartRefresh.setEnableLoadMore(false);
                    mVoucherZJType = ViewConstant.VOUCHER_TAB_GQ;
                    mVoucherZjAdapter.clearData();
                    mPage = 0;
                    mPresenter.getHistoryCouponsList(AppApplication.getConfig().getAt_token(),mPage,mPageSize);
                }
            }
        });
        mCommonTab.setTabData(tabEntitys);

        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (TextUtils.equals(ViewConstant.VOUCHER_TAB_KY,mVoucherZJType)){
                    mPresenter.getUserVoucherZjList(AppApplication.getConfig().getAt_token(),AppApplication.getConfig().getAt_userId());
                }else {
                    mPresenter.getHistoryCouponsList(AppApplication.getConfig().getAt_token(),0,mPageSize);
                }
                smartRefresh.finishRefresh();
            }
        });

        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (TextUtils.equals(ViewConstant.VOUCHER_TAB_GQ,mVoucherZJType)){
                    mPresenter.getHistoryCouponsList(AppApplication.getConfig().getAt_token(),mPage,mPageSize);
                }
                smartRefresh.finishLoadMore();
            }
        });
    }

    //可用
    @Override
    public void bindUserVoucherZjList(List<VoucherKyZJBean> voucherList) {
       if (TextUtils.equals(ViewConstant.VOUCHER_TAB_KY,mVoucherZJType)){
           mVoucherZjAdapter.notifyDataChanged(mVoucherZJType,true,voucherList);
       }
    }

    //过期
    @Override
    public void bindUserGqVoucherZjList(int page,int listSize,List<VoucherKyZJBean> voucherList) {
        if (TextUtils.equals(ViewConstant.VOUCHER_TAB_GQ,mVoucherZJType)){
            this.mPage = page;

            boolean mLoadMore = listSize >= mPageSize;
            smartRefresh.setEnableLoadMore(mLoadMore);
            mVoucherZjAdapter.notifyDataChanged(mVoucherZJType, mPage <= 0,voucherList);
            if (mLoadMore){
                this.mPage ++;
            }
        }
    }
}
