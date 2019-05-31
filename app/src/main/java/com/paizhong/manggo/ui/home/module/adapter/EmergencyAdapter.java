package com.paizhong.manggo.ui.home.module.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.HttpUrl;

/**
 * Des: 突发事件 adapter
 * Created by huang on 2018/10/24 0024 14:42
 */
public class EmergencyAdapter extends BaseRecyclerAdapter<EmergencyAdapter.ViewHolder, EmergencyBean> {
    private int leftMargin, topMargin;

    public EmergencyAdapter(Context context) {
        super(context);
        topMargin = DeviceUtils.dip2px(mContext, 10);
        leftMargin = DeviceUtils.dip2px(mContext, 15);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_emergency_layout, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, EmergencyBean data) {
        holder.tvTitle.setText(data.title);
        holder.tvTime.setText(data.time_show);
        ImageUtils.display(data.thumb, holder.image, R.mipmap.other_empty);

        saveOtherHosts(data);
    }

    /**
     * 将 突发行情、大事件、财经日历的url保存起来，方便请求时解析判断
     * {@link com.paizhong.manggo.http.HttpManager}
     */
    private void saveOtherHosts(EmergencyBean data) {
        try {
            String host = new URL(data.detail_json_url).getHost();
            if (!Constant.OTHER_HOSTS.contains(host)) {
                Constant.OTHER_HOSTS.add(host);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView image;
        TextView tvTitle, tvTime;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            tvTime = itemView.findViewById(R.id.time);
            tvTitle = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.card_view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int descent = (int) tvTitle.getPaint().getFontMetrics().descent;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.topMargin = descent;
                params.bottomMargin = descent;
                card.setLayoutParams(params);
            }
            if (position == 0) {
                itemView.setPadding(leftMargin, topMargin, leftMargin, 0);
            }
        }
    }
}
