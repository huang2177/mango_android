package com.paizhong.manggo.ui.home.module.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Des: ViewPage Adapter
 * Created by huang on 2018/8/21 0021 11:25
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    private SparseArray<Boolean> mArray;


    public ViewPagerAdapter() {
        mArray = new SparseArray<>();
    }

    public ViewPagerAdapter(List<View> views) {
        mViews = views;
        mArray = new SparseArray<>();
    }

    public void setViews(List<View> views) {
        mViews = views;
        notifyDataSetChanged();
    }

    public List<View> getViews() {
        return mViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//         container.removeView((View) object);//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {    //这个方法用来实例化页卡
        if (mArray.get(position) != null && mArray.get(position)) {
            mArray.put(position, false);
            container.removeView(mViews.get(position));
        }
        mArray.put(position, true);
        container.addView(mViews.get(position));//添加页卡
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mViews == null ? 0 : mViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }
}
