package com.paizhong.manggo.ui.follow.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.events.CancelFollowEvent;
import com.paizhong.manggo.ui.follow.FollowFragment;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.follow.contract.ItemFollowPresenter;
import com.paizhong.manggo.ui.follow.module.adapter.NewBuyAdapter;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Des: 跟买广场 最新跟买 Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class NewBuyModule extends BaseFollowModule<ItemFollowPresenter> implements HandlerHelper.OnExecuteListener {

    private String mProName = "";
    private HandlerHelper mHelper;

    private NewBuyAdapter mAdapter;


    public NewBuyModule(FollowFragment fragment) {
        super(fragment.mActivity);
        mHelper = fragment.mHelper;

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    public void bindRecycle(RecyclerView recycler) {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(manager);

        mAdapter = new NewBuyAdapter(mActivity);
        recycler.setAdapter(mAdapter);

        new BestOrderModule(mActivity, mAdapter,recycler);
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        if (mAdapter.isEmpty()) execute(isRefresh);
    }

    @Override
    public void execute(boolean args) {
        mPresenter.getOrderList(mAdapter.isEmpty() && !args, mProName);
    }

    @Override
    public void bindOrderEmpty(String proName) {
        mAdapter.clearData();
        mHelper.cancelExecute(this);
    }

    @Override
    public void bindOrderList(String proName, List<FollowListBean> list) {
        mHelper.execute(this);
        mAdapter.notifyDataChanged(true, list);
    }


    public void setProName(String proName) {
        mHelper.cancelExecute(this);
        mProName = proName;
        execute(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CancelFollowEvent event) {
        int position = event.position;
        if (position == -1 || position > 100) {
            return;
        }
        FollowListBean bean = mAdapter.getData().get(position);
        if (event.code == 1 && bean.isConcern == 0) { //是否取消关注
            new AppHintDialog(mActivity).showAppDialog(3, position);
        } else {
            mPresenter.addConcern(bean.phone, bean.isConcern, position);
        }
    }

    @Override
    public void bindConcern(int position, int isConcern) {
        showErrorMsg(isConcern == 0 ? "关注成功" : "取消关注成功", null);
    }

    @Override
    public void refreshDialog(String maxId, String stockIndex, String changeValue, String time) {
        PlaceOrderDialog dialog = placeOrder();
        if (dialog != null && dialog.isShowing() && TextUtils.equals(dialog.getProductID(), maxId)) {
            dialog.refreshPlaceOrderDialog(stockIndex, changeValue, time);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private PlaceOrderDialog placeOrder() {
        if (!mActivity.isPlaceOrderEmpty()) {
            return mActivity.placeOrder();
        }
        return null;
    }
}
