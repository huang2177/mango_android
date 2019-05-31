package com.paizhong.manggo.ui.follow.follower;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.MyFollowerBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 我的关注 Adapter
 * Created by hs on 2018/5/29 0029 17:38
 */
public class MyFollowerAdapter extends BaseRecyclerAdapter<MyFollowerAdapter.ViewHolder, MyFollowerBean> {

    private Activity mContext;

    public MyFollowerAdapter(Activity activity) {
        super(activity);
        this.mContext = activity;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_follower, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, MyFollowerBean data) {
        holder.tvName.setText(data.nickName);
        holder.tvFollowCount.setText(data.count);
        holder.tvBalance.setText(data.totalScore);
        holder.tvEarnings.setText(String.valueOf((int) data.profit));
        ImageUtils.display(data.userPic, holder.ivHead, R.mipmap.ic_user_head);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvBalance;
        TextView tvEarnings;
        CircleImageView ivHead;
        TextView tvFollowCount;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_follower_name);
            ivHead = (CircleImageView) itemView.findViewById(R.id.item_follower_iv);
            tvFollowCount = (TextView) itemView.findViewById(R.id.item_follower_count);
            tvBalance = (TextView) itemView.findViewById(R.id.item_follower_balance);
            tvEarnings = (TextView) itemView.findViewById(R.id.item_follower_earnings1);
        }
    }
}
