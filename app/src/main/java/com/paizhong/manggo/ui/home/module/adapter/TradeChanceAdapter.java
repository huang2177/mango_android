package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.TradeChanceBean;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.widget.FlickerProgressBar;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.util.Date;
import java.util.List;

/**
 * Des:
 * Created by huang on 2018/9/5 0005 16:12
 */
public class TradeChanceAdapter extends BaseRecyclerAdapter<TradeChanceAdapter.TradeChanceHolder, TradeChanceBean> {
    private Context mContext;

    public TradeChanceAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public TradeChanceHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TradeChanceHolder(mInflater.inflate(R.layout.item_home_trade_chace, parent, false));
    }

    @Override
    public void mOnBindViewHolder(TradeChanceHolder holder, int position, TradeChanceBean data) {
        if (data.type == 1) {
            showNotice(holder, position);
        } else {
            showTradeChance(holder, position);
        }
    }


    /**
     * 公告
     *
     * @param position
     */
    private void showNotice(TradeChanceHolder holder, int position) {
        holder.root.setVisibility(View.GONE);
        holder.tvContent.setVisibility(View.GONE);

        TradeChanceBean bean = data.get(position);

        holder.tvTitle.setText(bean.title);
        holder.tvTime3.setText(bean.pubTime1);
        holder.tvTime2.setText(TimeUtil.getFriendTimeOffer((new Date().getTime() - bean.pubTime)));

        holder.tvProName.setText("公告");
        holder.tvProName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        holder.tvProName.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        holder.tvProName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //setTime(holder.tvTime1, position, bean);
    }

    /**
     * 交易机会
     *
     * @param position
     */
    private void showTradeChance(TradeChanceHolder holder, int position) {
        TradeChanceBean bean = data.get(position);
        holder.tvTitle.setText(bean.title);
        holder.tvContent.setText(bean.tips);
        holder.tvAuthor.setText(bean.createMan);
        holder.tvTime3.setText(bean.pubTime1);
        holder.tvUp.setText(bean.morePercent + "%用户看涨");
        holder.mProgressBar.setProgress(bean.morePercent);
        holder.tvDown.setText(bean.sellEmptyPercent + "%用户看空");
        holder.tvTime2.setText(TimeUtil.getFriendTimeOffer((new Date().getTime() - bean.pubTime)));

        String text = bean.productName + (TextUtils.equals(bean.direction, "1")
                ? " 看空" : " 看多");
        holder.tvProName.setText(text);
        holder.tvProName.setTextColor(TextUtils.equals(bean.direction, "1")
                ? ContextCompat.getColor(mContext, R.color.color_1AC47A)
                : ContextCompat.getColor(mContext, R.color.color_F74F54));
        holder.tvProName.setCompoundDrawablesWithIntrinsicBounds(0
                , 0
                , TextUtils.equals(bean.direction, "1") ? R.mipmap.ic_down : R.mipmap.ic_up
                , 0);
        //setTime(holder.tvTime1, position, bean);
    }

    private void setTime(TextView tv, int position, TradeChanceBean bean) {
        String time = TimeUtil.getStringByFormat(bean.pubTime, "MM-dd");
        if (position == 0) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(time);
        } else {
            String prePosTime = TimeUtil.getStringByFormat(data.get(position - 1).pubTime, "MM-dd");
            if (!TextUtils.equals(time, prePosTime)) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(time);
            } else {
                tv.setVisibility(View.GONE);
            }
        }
    }

    public List<TradeChanceBean> getData() {
        return data;
    }

    class TradeChanceHolder extends RecyclerView.ViewHolder {
        private LinearLayout root;
        private TextView tvAuthor, tvUp, tvDown;
        private FlickerProgressBar mProgressBar;
        private TextView tvTime1, tvTime2, tvTime3;
        private TextView tvProName, tvTitle, tvContent;

        TradeChanceHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tvUp = itemView.findViewById(R.id.tv_up);
            tvDown = itemView.findViewById(R.id.tv_down);
            tvTime1 = itemView.findViewById(R.id.tv_time1);
            tvTime2 = itemView.findViewById(R.id.tv_time2);
            tvTime3 = itemView.findViewById(R.id.tv_time3);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvProName = itemView.findViewById(R.id.tv_pro_name);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
