package com.paizhong.manggo.widget.market;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.ui.home.module.MarketModule;
import com.paizhong.manggo.ui.home.module.adapter.ViewPagerAdapter;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketActivity;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.widget.MyViewPager;
import com.paizhong.manggo.widget.banner.IndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Des: 首页行情辅助类
 * Created by huang on 2018/8/21 0021 11:05
 */
public class MarketHelper implements ViewPager.OnPageChangeListener, MarketContainer.OnItemClickListener {
    private static final int DEF_COUNT = 6;
    private static final int PAGE_COUNT = 2;
    private static final float PAGE_SIZE = 3f;

    private Context mContext;

    private List<View> mContainers;
    private List<MarketView> mViews;

    private int mCurrentPosition;
    private ViewPager mViewPager;
    private IndicatorView mIndicator;


    public MarketHelper(Context context) {
        mContext = context;
        mViews = new ArrayList<>();
        mContainers = new ArrayList<>();
    }


    /**
     * 初始化MarketView，并添加到布居中
     */
    private void initMarketView() {
        for (int i = 0; i < PAGE_COUNT; i++) {
            MarketContainer container = new MarketContainer(mContext);
            container.addMarketView((int) PAGE_SIZE, i, mViews);
            container.setListener(this);
            mContainers.add(container);
        }
        mIndicator.init(PAGE_COUNT);
        mIndicator.currentTab(mCurrentPosition);
        mViewPager.setAdapter(new ViewPagerAdapter(mContainers));
    }

    /***
     * 刷新数据
     * @param list
     */
    public void updateData(List<MarketHQBean> list) {
        for (int i = 0; i < mViews.size(); i++) {
            MarketView marketView = mViews.get(i);
            marketView.updateData(list.get(i));
        }
    }

    public void updateHotProduct(String id) {
        for (int i = 0; i < mViews.size(); i++) {
            mViews.get(i).updateHotProduct(false);
            MarketHQBean data = mViews.get(i).getData();
            if (data != null && TextUtils.equals(data.remark, id)) {
                mViews.get(i).updateHotProduct(true);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.currentTab(position);
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int position, MarketView view) {
        MarketHQBean data = view.getData();
        if (!DeviceUtils.isFastDoubleClick() && data != null) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("productID", data.remark);
            intent.putExtra("page", MainActivity.TRADE_PAGE);
            intent.putExtra("smPage", 0);
            mContext.startActivity(intent);
        }
    }

    public void bindView(MyViewPager viewPager, IndicatorView indicator) {
        mIndicator = indicator;
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        indicator.setIndicatorColor(R.drawable.bg_indicator_blue, R.drawable.bg_indicator_gray);

        initMarketView();
    }

    public boolean isEmpty() {
        return mViewPager.getChildCount() == 0;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
