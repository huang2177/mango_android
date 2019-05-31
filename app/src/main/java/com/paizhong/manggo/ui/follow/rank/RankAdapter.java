package com.paizhong.manggo.ui.follow.rank;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 牛人堂 Adapter
 * Created by hs on 2018/5/29 0029 17:38
 */
public class RankAdapter extends BaseRecyclerAdapter<RankAdapter.ViewHolder, RankListBean> {

    private Activity mContext;

    public RankAdapter(Activity activity) {
        super(activity);
        this.mContext = activity;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rank_layout, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, RankListBean data) {
        holder.tvName.setText(data.nickName);
        holder.tvBalance.setText(data.totalScore);
        holder.tvEarnings.setText(data.mProfitRate);
        ImageUtils.display(data.userPic, holder.ivHead, R.mipmap.ic_user_head);

        if (position == 0) {
            holder.tvRank.setBackgroundResource(R.mipmap.bg_rank1);
        } else if (position == 1) {
            holder.tvRank.setBackgroundResource(R.mipmap.bg_rank2);
        } else if (position == 2) {
            holder.tvRank.setBackgroundResource(R.mipmap.bg_rank3);
        } else {
            holder.tvRank.setBackgroundResource(0);
            holder.tvRank.setText((position + 1) + "");
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum;
        TextView tvName;
        TextView tvRank;
        TextView tvBalance;
        TextView tvEarnings;
        CircleImageView ivHead;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            tvRank = (TextView) itemView.findViewById(R.id.tv_rank);
            tvName = (TextView) itemView.findViewById(R.id.item_rank_name);
            ivHead = (CircleImageView) itemView.findViewById(R.id.item_rank_iv);
            tvNum = (TextView) itemView.findViewById(R.id.item_rank_follow_num);
            tvBalance = (TextView) itemView.findViewById(R.id.item_rank_balance);
            tvEarnings = (TextView) itemView.findViewById(R.id.item_rank_earnings1);
        }
    }
}
