package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.WeekUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.paizhong.manggo.R;

/**
 * Des: 财经日历 --- 日历Adapter
 * Created by hs on 2018/6/28 0028 15:39
 */
public class CalendarHelper {
    private Context mContext;
    private List<Date> mDateList;
    private SparseArray<ViewHolder> mHolders;

    private OnItemClickListener mItemClick;

    private static final int PAGE_SIZE = 7;
    private static final int PAGE_COUNT = 2;
    private static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    public CalendarHelper(Context context) {
        mContext = context;
        mDateList = new ArrayList<>();
        mHolders = new SparseArray<>();
        mDateList.addAll(WeekUtil.getWeekDays(0));
        mDateList.addAll(WeekUtil.getWeekDays(1));
    }

    /**
     * 获取两周的日历 View
     */
    public List<View> getViews() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            LinearLayout parent = new LinearLayout(mContext);
            for (int j = 0; j < PAGE_SIZE; j++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, null);
                new ViewHolder(view, PAGE_SIZE * i + j);
                LinearLayout.LayoutParams params = null;
                if (j == 0) {
                    params = new LinearLayout.LayoutParams(WRAP, WRAP);
                    params.leftMargin = DeviceUtils.dip2px(mContext, 15);
                    params.rightMargin = DeviceUtils.dip2px(mContext, 10);
                } else if (j == 6) {
                    params = new LinearLayout.LayoutParams(WRAP, WRAP);
                    params.leftMargin = DeviceUtils.dip2px(mContext, 10);
                    params.rightMargin = DeviceUtils.dip2px(mContext, 15);
                } else {
                    params = new LinearLayout.LayoutParams(0, WRAP, 1f);
                }
                parent.addView(view, params);
            }
            views.add(parent);
        }
        return views;
    }


    class ViewHolder implements View.OnClickListener {
        public TextView tvWeek;
        public TextView tvDay;
        public int position;

        public ViewHolder(View itemView, int position) {
            this.position = position;
            mHolders.put(position, this);
            itemView.setOnClickListener(this);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvWeek = itemView.findViewById(R.id.tv_week);
            int day = WeekUtil.getDayOfMonth(mDateList.get(position));

            tvDay.setText(String.valueOf(day));
            tvWeek.setText(getWeekText(position));
            if (day == WeekUtil.getToDay()) {
                tvDay.setText("今");
                tvDay.setBackgroundResource(R.drawable.bg_shape_r45_008eff);
                tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            }
        }

        public String getWeekText(int position) {
            if (position > 6) {
                position -= 7;
            }
            switch (position) {
                case 0:
                    return "周一";
                case 1:
                    return "周二";
                case 2:
                    return "周三";
                case 3:
                    return "周四";
                case 4:
                    return "周五";
                case 5:
                    return "周六";
                case 6:
                    return "周日";
                default:
                    return "";
            }
        }

        @Override
        public void onClick(View v) {
            changeStatus(position);
            mItemClick.onItemClick(v
                    , WeekUtil.getYear(mDateList.get(position))
                    , WeekUtil.getCurrentMonth(mDateList.get(position))
                    , WeekUtil.getDayOfMonth(mDateList.get(position)));
        }
    }

    /**
     * 改变选中后的背景
     */
    public void changeStatus(int position) {
        for (int i = 0; i < mHolders.size(); i++) {
            ViewHolder holder = mHolders.get(i);
            int day = WeekUtil.getDayOfMonth(mDateList.get(i));
            if (day == WeekUtil.getToDay()) {
                holder.tvDay.setBackgroundResource(R.mipmap.ic_calendar_circle);
                holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_008EFF));
            } else {
                holder.tvDay.setBackgroundResource(0);
                holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_2A3342));
            }
            if (position == i) {
                holder.tvDay.setBackgroundResource(R.drawable.bg_shape_r45_008eff);
                holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            }
        }
    }

    public void setItemClickListener(OnItemClickListener itemClick) {
        mItemClick = itemClick;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int year, int month, int day);
    }
}
