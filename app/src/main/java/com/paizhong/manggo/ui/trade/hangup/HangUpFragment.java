package com.paizhong.manggo.ui.trade.hangup;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.HangUpListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.trade.TradeFragment;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.RecyclerView;
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
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * 挂单
 * Created by zab on 2018/8/17 0017.
 */
public class HangUpFragment extends BaseFragment<HangUpPresenter>implements HangUpContract.View{
    @BindView(R.id.smart_refresh)
    FixRefreshLayout mSmartRefresh;
    @BindView(R.id.commonTab)
    CommonNoViewTabLayout commonTab;
    @BindView(R.id.rv_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_jump)
    TextView tvJump;

    private boolean  mIsInitView = false;
    private HangUpAdapter hangUpAdapter;
    private int mPageIndex = 1;


    private static HangUpFragment mHangUpFragment;
    public static  HangUpFragment getInstance() {
        if (mHangUpFragment == null){
            synchronized (HangUpFragment.class){
                if (mHangUpFragment == null){
                    mHangUpFragment = new HangUpFragment();
                }
            }
        }
        return mHangUpFragment;
    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_hangup;
    }

    @Override
    public void initPresenter() {
       mPresenter.init(HangUpFragment.this);
    }

    public boolean isInitView() {
        return mIsInitView;
    }

    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        ivEmpty.setImageResource(R.mipmap.ic_empty_no_open_pisition);
        mSmartRefresh.setEnableLoadMore(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        hangUpAdapter = new HangUpAdapter(mActivity);
        recyclerView.setAdapter(hangUpAdapter);
        recyclerView.setEmptyView(llEmpty);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeFragment.getInstance().setSmPageCurrentTab(0,false);
            }
        });
        final ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("正在挂单", ViewConstant.SIMP_TAB_ITEM_ZERO));
        tabEntities.add(new TabEntity("挂单历史", ViewConstant.SIMP_TAB_ITEM_ONE));
        commonTab.setOnTabSelectListener(new SimpleOnTabSelectListener(){
            @Override
            public void onTabSelect(int position , View view) {
                super.onTabSelect(position ,view);
                if (isVisible()){
                    if (position == 0){
                        mSmartRefresh.setEnableLoadMore(false);
                        hangUpAdapter.setViewType(ViewConstant.SIMP_TAB_ITEM_ZERO);
                        hangUpAdapter.notifyDataChanged(true,null);
                        startRun(true);
                    }else {
                        stopRun();
                        mPageIndex = 1;
                        hangUpAdapter.setViewType(ViewConstant.SIMP_TAB_ITEM_ONE);
                        hangUpAdapter.notifyDataChanged(true,null);
                        getHangUpHistoryList(true,mPageIndex);
                    }
                }
            }
        });


        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (hangUpAdapter.getItemViewType(0)  == ViewConstant.SIMP_TAB_ITEM_ZERO){
                    startRun(false);
                }else {
                    getHangUpHistoryList(false,1);
                }
                 mSmartRefresh.finishRefresh();
            }
        });

        mSmartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getHangUpHistoryList(false,mPageIndex);
                mSmartRefresh.finishLoadMore();
            }
        });

        commonTab.setTabData(tabEntities);
        mIsInitView = true;
    }


    //撤单
    public void getCancelHangUp(String hangUpID){
        mPresenter.getCancelHangUp(hangUpID);
    }


    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getMarketList();
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void stopRun(){
        mHandler.removeCallbacks(mRunnable);
    }


    public void startHttpRun(){
         mHandler.removeCallbacks(mRunnable);
         mHandler.post(mRunnable);
    }

    public void startRun(boolean showLoad){
        if (isInitView() && hangUpAdapter.getItemViewType(0)  == ViewConstant.SIMP_TAB_ITEM_ZERO){
            stopRun();
            mPresenter.getHangUpOrderList(showLoad);
        }
    }

    private void getHangUpHistoryList(boolean showLoad,int pageIndex){
        mPresenter.getHangUpHistoryList(showLoad ,10,pageIndex);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInitView()){
            if (isVisibleToUser) {
                startRun(false);
            } else {
                stopRun();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    /*******
     * 修改挂单  type 1
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseUIRefreshEvent event) {
        if (event.mType == 1){
            startRun(false);
        }
    }


    @Override
    public void bindHangUpOrderList(List<HangUpListBean> hangUpOrderList) {
        if (hangUpAdapter.getItemViewType(0) == ViewConstant.SIMP_TAB_ITEM_ZERO){
             if (hangUpOrderList !=null && hangUpOrderList.size() > 0){
                 hangUpAdapter.notifyDataChanged(true,hangUpOrderList);
                 if (!TradeFragment.getInstance().isHidden() && getUserVisibleHint()){
                     startHttpRun();
                 }else {
                     mPresenter.getMarketList();
                 }
             }else {
                 hangUpAdapter.notifyDataChanged(true,hangUpOrderList);
             }
         }
    }


    @Override
    public void bindMarketList(List<MarketHQBean> marketList) {
        if (hangUpAdapter.getItemViewType(0) == ViewConstant.SIMP_TAB_ITEM_ZERO){
            if (marketList != null && marketList.size() > 0 && hangUpAdapter.getItemCount() > 0){

                for (MarketHQBean marketBean : marketList){

                    for (HangUpListBean hangUpBean : hangUpAdapter.getHangUpList()){

                        if (TextUtils.equals(hangUpBean.typeId,marketBean.remark)){
                            hangUpBean.stockIndex = marketBean.point;
                            hangUpBean.changValue = Double.parseDouble(marketBean.change);
                        }
                    }
                }

                hangUpAdapter.notifyDataSetChanged();
            }
        }
    }


    //挂单历史
    @Override
    public void bindHangUpHistoryList(int pageIndex,List<HangUpListBean> hangUpOrderList) {
        if (hangUpAdapter.getItemViewType(0) == ViewConstant.SIMP_TAB_ITEM_ONE){
            this.mPageIndex = pageIndex;
            if (hangUpOrderList !=null && hangUpOrderList.size() > 0 ){
                 boolean loadMore = hangUpOrderList.size()>=10;
                 mSmartRefresh.setEnableLoadMore(loadMore);
                 hangUpAdapter.notifyDataChanged(mPageIndex == 1 ,hangUpOrderList);
                 if (loadMore){
                     mPageIndex ++;
                 }
            }else {
                mSmartRefresh.setEnableLoadMore(false);
            }
        }
    }


    //撤单
    @Override
    public void bindCancelHangUp(String id) {
        showErrorMsg("撤单成功",null);
        if (hangUpAdapter.getItemViewType(0) == ViewConstant.SIMP_TAB_ITEM_ZERO){
            if (hangUpAdapter !=null && hangUpAdapter.getHangUpList() !=null && hangUpAdapter.getHangUpList().size() > 0){
                Iterator<HangUpListBean> iterator = hangUpAdapter.getHangUpList().iterator();
                while (iterator.hasNext()) {
                    HangUpListBean bean = iterator.next();
                    if (TextUtils.equals(bean.id,id)) {
                        iterator.remove();
                        break;
                    }
                }
                if (hangUpAdapter.getHangUpList().size() == 0){
                    stopRun();
                }
                hangUpAdapter.notifyDataSetChanged();
            }
        }
    }
}
