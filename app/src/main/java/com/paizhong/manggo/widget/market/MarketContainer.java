package com.paizhong.manggo.widget.market;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.paizhong.manggo.utils.DeviceUtils;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/8/21 0021 11:13
 */
public class MarketContainer extends LinearLayout implements View.OnClickListener {

    private OnItemClickListener mListener;

    public MarketContainer(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    /**
     * 添加行情View
     *
     * @param pageSize
     * @param views
     */
    public void addMarketView(int pageSize, int pageIndex, List<MarketView> views) {
        for (int i = 0; i < pageSize; i++) {
            MarketView marketView = new MarketView(getContext(),pageIndex * pageSize + i);
            marketView.setOnClickListener(this);
            views.add(marketView);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            lp.leftMargin = DeviceUtils.dip2px(getContext(), 5);
            lp.rightMargin = DeviceUtils.dip2px(getContext(), 5);
            addView(marketView, lp);
        }
    }


    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick((int) v.getTag(), (MarketView) v);
        }
    }

    /**
     * Item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position, MarketView view);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
