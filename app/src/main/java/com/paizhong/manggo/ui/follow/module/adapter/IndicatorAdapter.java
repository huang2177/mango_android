package com.paizhong.manggo.ui.follow.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Des: 跟单广场 右上角dialog adapter
 * Created by hs on 2018/7/9 0009 11:00
 */
public class IndicatorAdapter extends BaseRecyclerAdapter<IndicatorAdapter.ViewHolder, String> {

    public IndicatorAdapter(Context context) {
        super(context);
        List<String> list = Arrays.asList("跟买记录", "我的关注", "跟买规则");
        addData(list);
    }


    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_dialog, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, String data) {
        holder.mTextView.setText(data);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        View mDivider;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            mDivider = itemView.findViewById(R.id.divider);
            mTextView = itemView.findViewById(R.id.dialog_tv);

            if (position == data.size() - 1) {
                mDivider.setVisibility(View.GONE);
            }
        }
    }
}
