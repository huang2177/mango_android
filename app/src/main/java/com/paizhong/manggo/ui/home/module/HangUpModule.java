package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.placeordermarket.PlaceOrderMarketDialog;
import com.paizhong.manggo.ui.home.HomeFragment;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.bar.SystemBarConfig;
import com.paizhong.manggo.widget.appbar.FixAppBarBehavior;
import com.paizhong.manggo.widget.expand.ExpandableLayout;

import butterknife.BindView;

/**
 * Des: 首页底部挂单 Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class HangUpModule extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener
        , FixAppBarBehavior.OnScrollListener
        , AppBarLayout.OnOffsetChangedListener {
    @BindView(R.id.tv_hangup_order)
    TextView tvHangUpOrder;
    @BindView(R.id.expandable_layout)
    ExpandableLayout mExpandLayout;

    private ViewGroup container;
    private AppBarLayout mAppBar;

    private int position;
    private int lastOffset;
    private int totalRange;


    public HangUpModule(Context context, ViewGroup parent) {
        super(context, null);
        this.container = parent;
        tvHangUpOrder.setOnClickListener(this);
    }

    public void showLayout() {
        container.addView(this);
    }

    @Override
    public void onVisible(boolean visible) {
        super.onVisible(visible);
        initAppBar();
        if (visible && lastOffset == totalRange && totalRange != 0) {
            onScrollDown();
        } else {
            onScrollUp();
        }
    }

    @Override
    protected void onResume() {
        initAppBar();
    }

    public void onTabSelect(int position) {
        this.position = position;
        if (position == 1 && lastOffset == totalRange) {
            onScrollDown();
        } else {
            onScrollUp();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mAppBar == null) {
            return;
        }
        int offset = Math.abs(verticalOffset);
        totalRange = mAppBar.getTotalScrollRange();
        if (lastOffset == offset) {
            return;
        } else if (appBarLayout.getTotalScrollRange() == offset) {
            onScrollDown();
        } else {
            onScrollUp();
        }
        lastOffset = offset;
    }

    private void initAppBar() {
        if (mAppBar == null) {
            mAppBar = findView(HomeFragment.class, R.id.app_bar);
            if (mAppBar != null) {
                mAppBar.addOnOffsetChangedListener(this);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_home_hang_up;
    }

    @Override
    public void onClick(View v) {
        if (mActivity.login() && !DeviceUtils.isFastDoubleClick()) {
            PlaceOrderMarketDialog marketDialog = new PlaceOrderMarketDialog(mActivity);
            marketDialog.placeOrderHangUp(ViewConstant.PRODUCT_ID_AG, true, 1);
        }
    }

    @Override
    public void onScrollUp() {
        mExpandLayout.collapse(true);
    }

    @Override
    public void onScrollDown() {
        if (position == 1) {
            mExpandLayout.expand(true);
        }
    }
}
