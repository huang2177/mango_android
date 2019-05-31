package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.ProfitBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

/**
 * Des:
 * Created by huang on 2019/1/14 0014 17:29
 */
public class ProfitRankAdapter extends BaseRecyclerAdapter<ProfitRankAdapter.ViewHolder, ProfitBean> {
    public ProfitRankAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_profit_layout, parent, (false)));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, ProfitBean data) {
        ImageUtils.display(data.userPic, holder.ivHead, R.mipmap.user_head_default);

        ViewHelper.safelySetText(holder.tvName,data.nickName);
        if (data.currentTime !=null && data.closeTime !=null){
            holder.tvTime.setText(TimeUtil.getFriendTimeOffer((data.currentTime - data.closeTime)));
        }else {
            holder.tvTime.setText("");
        }
        String type = data.flag == 1 ? "买跌" : "买涨";
        String color = data.flag == 1 ? "#00CE64" : "#FF5376";

        ViewHelper.safelySetText(holder.tvProName,data.productName);
        holder.tvAmount.setText(data.amount + "元");
        holder.tvType.setText(Html.fromHtml(("<font color='" + color + "'>" + type + "</font>")));
        holder.tvCloseProfit.setText(Html.fromHtml("<font color='#FF5376'>" + data.closeProfit + "</font>元"));
        holder.tvDes.setText((data.amount / data.getQuantity()) + "元/手x" + data.getQuantity() + "手  "
                + data.openPrice + "建仓--" + data.closePrice + "平仓");
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivHead;
        private TextView tvName, tvDes, tvTime;
        private TextView tvAmount, tvType, tvProName, tvCloseProfit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDes = itemView.findViewById(R.id.tv_des);
            ivHead = itemView.findViewById(R.id.iv_head);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvType = itemView.findViewById(R.id.tv_type);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvProName = itemView.findViewById(R.id.tv_product_name);
            tvCloseProfit = itemView.findViewById(R.id.tv_close_profit);
        }
    }
}
