package com.paizhong.manggo.ui.follow.detail.newbuy;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.follow.PersonOrderBean;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Des:
 * Created by huang on 2018/9/3 0003 14:18
 */
public class NewBuyFragment extends BaseFragment<NewBuyPresenter> implements HandlerHelper.OnExecuteListener,
        NewBuyContract.View {

    @BindView(R.id.new_buy_adapter)
    RecyclerView mRecycler;

    private static String mPhone;
    public static NewBuyFragment mFragment;

    private NewBuyOrderAdapter mAdapter;
    private HandlerHelper mHelper;

    public static NewBuyFragment getInstance(String phone) {
        if (mFragment == null) {
            mFragment = new NewBuyFragment();
        }
        mPhone = phone;
        return mFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_buy;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initRecycle();
        execute(true);
    }

    private void initRecycle() {
        mHelper = new HandlerHelper(mFragment);
        getLifecycle().addObserver(mHelper);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecycler.setLayoutManager(manager);

        mAdapter = new NewBuyOrderAdapter(mActivity);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (mHelper != null) {
            mHelper.setHidden(hidden);
        }
    }

    @Override
    public void execute(boolean args) {
        if (args) {
            mPresenter.getOrderList(mPhone, mAdapter.isEmpty());
        }
    }

    @Override
    public void bindOrderEmpty() {
        mHelper.cancelExecute(this);
        if (mAdapter.getFootersCount() == 0 && mAdapter.isEmpty()) {
            mAdapter.addFooterView(LayoutInflater.from(mActivity).inflate(R.layout.view_empty_layout, null));
        }
    }

    @Override
    public void bindOrderList(List<PersonOrderBean> list) {
        mHelper.execute(this);
        mAdapter.clearFooterView();
        mAdapter.notifyDataChanged(true, list);
        ((PersonInfoActivity) mActivity).updateTab(list.size());
    }

    @Override
    public void refreshDialog(String maxId, String stockIndex, String changeValue, String time) {
        PlaceOrderDialog dialog = mAdapter.getOrderDialog();
        if (dialog != null) {
            if (dialog.isShowing() && TextUtils.equals(dialog.getProductID(), maxId)) {
                dialog.refreshPlaceOrderDialog(stockIndex, changeValue, time);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getOrderDialog() != null) {
            mAdapter.getOrderDialog().startRunHttp();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter.getOrderDialog() != null) {
            mAdapter.getOrderDialog().stopRun();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter.getOrderDialog() != null) {
            mAdapter.getOrderDialog().stopRun();
            mAdapter.getOrderDialog().onDestroy();
        }
    }

    public boolean isEmpty() {
        return mAdapter != null && mAdapter.isEmpty();
    }
}
