package com.paizhong.manggo.ui.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketActivity;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketOtherActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.DinTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    private int mTabType = ViewConstant.SIMP_TAB_ITEM_ZERO;
    private Context mContext;
    private List<MarketHQBean> mList;

    public MarketAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    public void clear(int mTabType) {
        this.mTabType = mTabType;
        mList.clear();
        notifyDataSetChanged();
    }

    public int getTabType() {
        return mTabType;
    }

    public List<MarketHQBean> getList(int frontTabType){
        if(mTabType == frontTabType){
            return mList;
        }
       return null;
    }

    public void notifyDataChanged(List<MarketHQBean> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_market_layout, parent, false));
    }

    private AnimationDrawable mGreenDrawable;
    private AnimationDrawable mRedDrawable;


    //true涨跌幅  false 涨跌值
    private boolean mIsChangeRange = true;

    public boolean isChangeRange() {
        return mIsChangeRange;
    }

    public void setIsChangeRange(boolean mIsChangeRange) {
        this.mIsChangeRange = mIsChangeRange;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        final MarketHQBean marketBean = mList.get(position);

        holder.mCodeName.setText(marketBean.name);
        holder.mPrice.setText(marketBean.point);
        holder.mCode.setText(marketBean.code);

        double v = Double.parseDouble(marketBean.change);
        if (v > 0) {
            int color = ContextCompat.getColor(mContext, R.color.color_F74F54);
            holder.mZdf.setTextColor(color);
            holder.mPrice.setTextColor(color);
            if (mIsChangeRange) {//涨跌幅
                holder.mZdf.setText("+" + marketBean.changeRate);
            } else {
                holder.mZdf.setText("+" + marketBean.change);
            }
        } else if (v < 0) {
            int color = ContextCompat.getColor(mContext, R.color.color_1AC47A);
            holder.mZdf.setTextColor(color);
            holder.mPrice.setTextColor(color);
            if (mIsChangeRange) {//涨跌幅
                holder.mZdf.setText(marketBean.changeRate);
            } else {
                holder.mZdf.setText(marketBean.change);
            }
        } else {
            int color = ContextCompat.getColor(mContext, R.color.color_333333);
            holder.mZdf.setTextColor(color);
            holder.mPrice.setTextColor(color);
            if (mIsChangeRange) {//涨跌幅
                holder.mZdf.setText(marketBean.changeRate);
            } else {
                holder.mZdf.setText(marketBean.change);
            }
        }


        if (marketBean.rise == 0) { //0 跌  1涨  2平
            marketBean.rise = 2;
            mGreenDrawable = (AnimationDrawable) holder.mGreen.getBackground();
            mGreenDrawable.stop();
            mGreenDrawable.start();
        } else if (marketBean.rise == 1) {
            marketBean.rise = 2;
            mRedDrawable = (AnimationDrawable) holder.mRed.getBackground();
            mRedDrawable.stop();
            mRedDrawable.start();
        }


        holder.mllRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(mContext, ViewConstant.SIMP_TAB_ITEM_ZERO == mTabType ? KLineMarketActivity.class : KLineMarketOtherActivity.class);
                    intent.putExtra("productID", ViewConstant.SIMP_TAB_ITEM_ZERO == mTabType ? marketBean.remark : marketBean.code);
                    intent.putExtra("productName", marketBean.name);
                    intent.putExtra("close", false);
                    intent.putExtra("closePrice", marketBean.close);
                    intent.putExtra("type",mTabType);
                    mContext.startActivity(intent);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View mllRootView;
        TextView mCodeName;
        DinTextView mPrice;
        DinTextView mZdf;
        TextView mCode;
        View mGreen;
        View mRed;

        ViewHolder(View view) {
            super(view);
            mllRootView = view.findViewById(R.id.ll_root_view);
            mCodeName = (TextView) view.findViewById(R.id.tv_code_name);
            mPrice = (DinTextView) view.findViewById(R.id.tv_price);
            mZdf = (DinTextView) view.findViewById(R.id.tv_zdf);
            mCode = (TextView) view.findViewById(R.id.tv_code);
            mGreen = view.findViewById(R.id.v_green);
            mRed = view.findViewById(R.id.v_red);
        }
    }
}
