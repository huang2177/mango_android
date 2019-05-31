package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.paizhong.manggo.R;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.Arrays;

import butterknife.ButterKnife;

/**
 * Des:
 * Created by huang on 2018/9/10 0010 19:26
 */
public class MarketAdapter extends BaseRecyclerAdapter {
    public MarketAdapter(Context context) {
        super(context);
        addData(Arrays.asList("","","","","",""));
    }

    @Override
    public RecyclerView.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.view_market_layout, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(RecyclerView.ViewHolder holder, int position, Object data) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView, int position) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
