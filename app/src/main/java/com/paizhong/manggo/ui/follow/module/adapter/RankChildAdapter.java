package com.paizhong.manggo.ui.follow.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 牛人排行榜(一级页面) Adapter
 * Created by hs on 2018/5/28 0028 16:57
 */
public class RankChildAdapter extends BaseRecyclerAdapter<RankChildAdapter.ViewHolder, RankListBean> {

    public RankChildAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_follow_rank_child, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, RankListBean data) {
        holder.mTvName.setText(data.nickName);
        holder.mTvEarnings.setText(data.mProfitRate);
        ImageUtils.display(data.userPic, holder.mIvHead, R.mipmap.ic_user_head);
    }

    @Override
    public int getItemCount() {
        return data.size() >= 8 ? 8 : data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mIvHead;

        TextView mTvName, mTvEarnings;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.rank_iv);
            mTvName = itemView.findViewById(R.id.rank_tv_name);
            mTvEarnings =  itemView.findViewById(R.id.rank_tv_earnings);
        }

    }
}
