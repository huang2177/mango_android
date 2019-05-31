package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.CalendarBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 财经日历 --- 财经新闻列表
 * Created by hs on 2018/6/28 0028 15:40
 */
public class NewsAdapter extends BaseRecyclerAdapter<NewsAdapter.ViewHolder, CalendarBean> {

    private static final String IMAGE_BASE_URL = "https://cdn.jin10.com/assets/img/commons/flag/";

    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_news, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, CalendarBean data) {
        holder.tvTitle.setText(data.title);
        holder.tvTime.setText(data.time_show);
        holder.mRatingBar.setRating(data.star);
        ImageUtils.display(IMAGE_BASE_URL + data.country + ".png", holder.ivCountry, R.mipmap.other_empty);

        if (TextUtils.isEmpty(data.previous)) {
            holder.tvBeforeValue.setText("前值： 未公布");
        } else {
            holder.tvBeforeValue.setText(Html.fromHtml("前值：<font color = '#333333'>" + data.previous + "</font>"));
        }

        if (TextUtils.isEmpty(data.consensus)) {
            holder.tvGuessValue.setText("预测值： 未公布");
        } else {
            holder.tvGuessValue.setText(Html.fromHtml("预测值：<font color = '#333333'>" + data.consensus + "</font>"));
        }

        if (TextUtils.isEmpty(data.actual)) {
            holder.tvResultValue.setText("公布值： 未公布");
        } else {
            holder.tvResultValue.setText(Html.fromHtml("公布值：<font color = '#333333'>" + data.actual + "</font>"));
        }

        if (TextUtils.equals(data.status_name, "利多")) {
            holder.tvEffectValue.setText(Html.fromHtml("影响：<strong><font color = '#ff5500'>" + data.status_name + "</font></strong>"));
        } else if (TextUtils.equals(data.status_name, "利空")) {
            holder.tvEffectValue.setText(Html.fromHtml("影响：<strong><font color = '#00A969'>" + data.status_name + "</font></strong>"));
        } else {
            holder.tvEffectValue.setText("影响：" + data.status_name);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCountry;
        private RatingBar mRatingBar;
        private TextView tvTime, tvTitle, tvBeforeValue, tvResultValue, tvEffectValue, tvGuessValue;

        public ViewHolder(View view, int viewType) {
            super(view);
            tvTime = view.findViewById(R.id.tv_time);
            tvTitle = view.findViewById(R.id.tv_title);
            ivCountry = view.findViewById(R.id.iv_country);
            mRatingBar = view.findViewById(R.id.rating_bar);
            tvGuessValue = view.findViewById(R.id.tv_guess_value);
            tvBeforeValue = view.findViewById(R.id.tv_before_value);
            tvEffectValue = view.findViewById(R.id.tv_effect_value);
            tvResultValue = view.findViewById(R.id.tv_result_value);
        }
    }
}
