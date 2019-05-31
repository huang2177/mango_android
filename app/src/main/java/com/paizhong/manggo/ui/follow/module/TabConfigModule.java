package com.paizhong.manggo.ui.follow.module;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.ModuleManager;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.widget.tab.OnTabSelectListener;
import com.paizhong.manggo.widget.tab.SlidingTabLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Des:
 * Created by huang on 2018/9/17 0017 11:52
 */
public class TabConfigModule extends BaseFollowModule implements OnTabSelectListener {
    @BindView(R.id.commonTab)
    SlidingTabLayout mTabLayout;

    private List<String> mTitles;

    public TabConfigModule(Context context) {
        this(context, null);
    }

    public TabConfigModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitles = Arrays.asList("全部", "GL银", "GL铜", "GL镍", "GL大豆", "GL玉米", "GL小麦");
        mTabLayout.addNewTab(mTitles);

        mTabLayout.setOnTabSelectListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_follow_config;
    }

    @Override
    public void onTabSelect(int position, View tabLayout) {
        NewBuyModule module = ModuleManager.getInstance().getModule(NewBuyModule.class);
        BestOrderModule bestModule = ModuleManager.getInstance().getModule(BestOrderModule.class);
        if (module != null) {
            module.setProName(position == 0 ? "" : mTitles.get(position));
        }
        if (bestModule != null) {
            bestModule.setProName(position == 0 ? "" : mTitles.get(position));
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }
}
