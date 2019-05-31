package com.paizhong.manggo.ui.trade.position;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.ui.trade.TradeFragment;
import com.paizhong.manggo.widget.recycle.RecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * 持仓
 * Created by zab on 2018/8/17 0017.
 */
public class PositionFragment extends BaseFragment<PositionPresenter> implements PositionContract.View{
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

    private PositionAdapter mAdapter;
    private boolean  mIsInitView = false;

    private static PositionFragment mPositionFragment;
    public static  PositionFragment getInstance() {
        if (mPositionFragment == null){
            synchronized (PositionFragment.class){
                if (mPositionFragment == null){
                    mPositionFragment = new PositionFragment();
                }
            }
        }
        return mPositionFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_position;
    }

    @Override
    public void initPresenter() {
       mPresenter.init(PositionFragment.this);
    }

    public boolean isInitView() {
        return mIsInitView;
    }

    @Override
    public void loadData() {
        ivEmpty.setImageResource(R.mipmap.ic_empty_no_open_pisition);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PositionAdapter(mActivity);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setEmptyView(llEmpty);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TradeFragment.getInstance().setSmPageCurrentTab(0,false);
            }
        });
        mAdapter.setOnOrderHoldNightListener(new PositionAdapter.OnOrderHoldNightListener() {
            @Override
            public void onOrderHoldNight(String orderId,String orderNo ,boolean holdNight) {
                mPresenter.getOrderHoldNight(AppApplication.getConfig().getAt_token(),AppApplication.getConfig().getAt_secret_access_key(),orderId,orderNo,holdNight);
            }
        });
        mIsInitView = true;
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getPositionList(AppApplication.getConfig().getAt_token());
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    public void stopRun(){
        mHandler.removeCallbacks(mRunnable);
    }

    public void startRun(){
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInitView()){
            if (isVisibleToUser) {
                startRun();
            } else {
                stopRun();
            }
        }
    }


    @Override
    public boolean getCheck(String key) {
        return mAdapter.getCheck(key);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter.getCloseDialog() !=null){
            mAdapter.getCloseDialog().onDestroy();
        }
    }

    //刷新 dialog
    @Override
    public void refreshDialog(String productID, String orderId, String stockIndex, double changValue, double floatingPrice) {
       if (getUserVisibleHint()){
           if (mAdapter !=null
                   && mAdapter.getCloseDialog() !=null
                   && mAdapter.getCloseDialog().isShowing()
                   && !TextUtils.isEmpty(mAdapter.getCloseDialog().getOrderId())
                   && TextUtils.equals(mAdapter.getCloseDialog().getOrderId(),orderId)){
               mAdapter.getCloseDialog().refreshClosePositionDialog(stockIndex,floatingPrice);
           }

           if (!((MainActivity)mActivity).isPlaceOrderEmpty()){
               PlaceOrderDialog placeOrderDialog = ((MainActivity) mActivity).placeOrder();
               if (placeOrderDialog.isShowing() && TextUtils.equals(productID,placeOrderDialog.getProductID())){
                   placeOrderDialog.refreshPlaceOrderDialog(stockIndex,String.valueOf(changValue));
               }
           }
       }
    }

    @Override
    public void bindPositionList(List<PositionListBean> positionList) {
        if (positionList == null || positionList.size() == 0){
            mAdapter.clearCheckExMap();
            stopRun();
        }
        mAdapter.notifyDataChanged(positionList);
    }
}
