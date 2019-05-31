package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.ModuleManager;
import com.paizhong.manggo.base.BaseModule;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.home.HomeFragment;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.home.module.adapter.CalendarHelper;
import com.paizhong.manggo.ui.home.module.adapter.ViewPagerAdapter;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.tab.OnTabSelectListener;
import com.paizhong.manggo.widget.tab.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页tab Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class TabConfigModule extends BaseHomeModule<ItemHomePresenter> implements OnTabSelectListener
        , CalendarHelper.OnItemClickListener
        , Runnable {

    @BindView(R.id.calendar_vp)
    ViewPager mCalendarPager;
    @BindView(R.id.commonTab)
    SlidingTabLayout mTabLayout;

    private HangUpModule mHangUp;
    private CalendarModule mCalendar;

    private List<View> modules;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    private SparseArray<Integer> indexMap;

    public TabConfigModule(Context context, @Nullable AttributeSet attr) {
        super(context, attr);
        modules = new ArrayList<>();
        indexMap = new SparseArray<>();
        mAdapter = new ViewPagerAdapter();
    }

    @Override
    public void setAuditing(boolean auditing) {
        super.setAuditing(auditing);
        mTabLayout.addNewTab(auditing
                ? Arrays.asList("突发行情", "大事件", "财经日历")
                : Arrays.asList("盈利广场", "交易机会", "财经日历", "突发行情", "大事件"));


        mViewPager = findView(HomeFragment.class, R.id.view_pager);
        if (mViewPager != null) {
            mViewPager.setAdapter(mAdapter);
            addModule(0);
        }
        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void run() {
        int width = 0;
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            width += mTabLayout.getTitleView(i).getWidth();
        }
        float totalWidth = DeviceUtils.getScreenWidth(mContext) - DeviceUtils.dp2px(mContext, 30) - width;
        mTabLayout.setSecTabPadding(totalWidth / (mTabLayout.getTabCount() - 1));
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_home_config;
    }

    @Override
    public void onItemClick(View v, int year, int month, int day) {
        if (mCalendar != null) {
            mCalendar.setDate(year, month, day);
            mCalendar.onRefresh(false);
        }
    }

    @Override
    public void onTabSelect(int i, View tabLayout) {
        addModule(i);
        mViewPager.setCurrentItem(indexMap.get(i));
        mCalendarPager.setVisibility(TextUtils.equals(mTabLayout.getTitle(i)
                , "财经日历") ? VISIBLE : GONE);

        if (mHangUp == null) {
            mHangUp = ModuleManager.getInstance().getModule(HangUpModule.class);
        }
        if (mHangUp != null) {
            mHangUp.onTabSelect(i);
        }
    }

    @Override
    public void onTabReselect(int position) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        int index = 0;
        if (indexMap.size() != 0) {
            index = indexMap.get(position);
        }
        BaseModule module = null;
        if (index < modules.size()) {
            module = (BaseModule) modules.get(index);
        }
        if (module != null) {
            module.onRefresh(true);
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    /***
     * 添加module到vp
     * @param i
     */
    private void addModule(int i) {
        if (indexMap.get(i) != null) {
            return;
        }
        BaseModule module = null;
        switch (mTabLayout.getTitle(i)) {
            case "盈利广场": //盈利广场
                module = new ProfitRankModule(mContext);
                break;
            case "交易机会": //交易机会
                module = new ChanceModule(mContext);
                break;
            case "财经日历": //财经日历
                module = getCalendarModule();
                break;
            case "突发行情": //突发行情
                module = new EmergencyModule(mContext);
                break;
            case "大事件": //大事件
                module = new BigEventsModule(mContext);
                break;
        }
        if (module != null) {
            modules.add(module);
            mAdapter.setViews(modules);
            indexMap.put(i, modules.size() - 1);
        }
    }

    private BaseModule getCalendarModule() {
        BaseModule module;
        Constant.OTHER_HOSTS.add("rili.jin10.com");
        mCalendar = new CalendarModule(mContext);
        CalendarHelper helper = new CalendarHelper(mContext);
        helper.setItemClickListener(this);

        ViewPagerAdapter mAdapter = new ViewPagerAdapter(helper.getViews());
        mCalendarPager.setAdapter(mAdapter);
        module = mCalendar;
        return module;
    }

    public void setCurrentTab(int position) {
        if (position < 0 || position >= mTabLayout.getTabCount()) {
            return;
        }
        mTabLayout.setCurrentTab(position);
        onTabSelect(position, null);
    }
}
