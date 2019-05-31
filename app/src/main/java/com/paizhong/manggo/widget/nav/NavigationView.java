package com.paizhong.manggo.widget.nav;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Des: 自定义底部导航栏
 * Created by huang on 2018/8/28 0028 17:17
 */
public class NavigationView extends LinearLayout implements View.OnClickListener {
    private List<String> mTexts;
    private List<Integer> mDrawables;
    private List<MenuView> mMenuViews;

    private boolean mIsAuditing;
    private OnNavSelectedListener mNavSelectedListener;

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initNav();
    }

    /**
     * 初始化Navigation
     */
    private void initNav() {
        mMenuViews = new ArrayList<>();
        mIsAuditing = AppApplication.getConfig().mIsAuditing;
        mTexts = mIsAuditing
                ? (AppApplication.getConfig().mIsUserAuditing ? Arrays.asList("首页", "行情", "快览")
                : Arrays.asList("首页", "行情", "快览","我的"))
                : Arrays.asList("首页", "行情", "交易", "跟买", "我的");

        mDrawables = mIsAuditing
                ? (AppApplication.getConfig().mIsUserAuditing ? Arrays.asList(R.mipmap.ic_home, R.mipmap.ic_market, R.mipmap.ic_trade)
                : Arrays.asList(R.mipmap.ic_home, R.mipmap.ic_market, R.mipmap.ic_trade,R.mipmap.ic_mine))
                : Arrays.asList(R.mipmap.ic_home, R.mipmap.ic_market, R.mipmap.ic_trade, R.mipmap.ic_follow, R.mipmap.ic_mine);

        setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        addMenuView();
    }



    /**
     * 添加Menu到容器中
     */
    private void addMenuView() {
        if (getChildCount() != 0) {
            removeAllViews();
            mMenuViews.clear();
        }
        for (int i = 0; i < mDrawables.size(); i++) {
            MenuView menuView = new MenuView(getContext(), mDrawables.get(i), mTexts.get(i), i);
            menuView.setOnClickListener(this);
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            addView(menuView, params);
            mMenuViews.add(menuView);
        }
    }

    @Override
    public void onClick(View v) {
        setCurrentNav((int) v.getTag());
    }


    public boolean isCurrentNav(int position) {
        return mMenuViews.get(position).isChecked();
    }

    /**
     * 设置指定的Nav
     *
     * @param position
     */
    public void setCurrentNav(int position) {
        if (mMenuViews.get(position).isChecked()) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            MenuView menuView = (MenuView) getChildAt(i);
            menuView.setChecked(position == i);
        }
        if (mNavSelectedListener != null) {
            mNavSelectedListener.onNavSelected(mMenuViews.get(position), position);
        }
    }


    public void setNavSelectedListener(OnNavSelectedListener navSelectedListener) {
        mNavSelectedListener = navSelectedListener;
    }

    /**
     * 设置是否需要显示红点
     *
     * @param position
     * @param needRedDot
     */
    public void setNeedRedDot(int position, boolean needRedDot) {
        if (position < 0 || position >= mMenuViews.size()) {
            return;
        }
        MenuView menuView = mMenuViews.get(position);
        if (menuView.isChecked() && needRedDot) {
            return;
        }
        menuView.setNeedRedDot(needRedDot);
    }

    /**
     * navigation item选中
     */
    public interface OnNavSelectedListener {
        void onNavSelected(MenuView menuView, int position);
    }
}
