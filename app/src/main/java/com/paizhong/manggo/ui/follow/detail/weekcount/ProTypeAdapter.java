package com.paizhong.manggo.ui.follow.detail.weekcount;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.ProTypeBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: (牛人详情) 本周跟买交易品 Adapter
 * Created by hs on 2018/5/28 0028 16:57
 */
public class ProTypeAdapter extends BaseRecyclerAdapter<ProTypeAdapter.ViewHolder, ProTypeBean> {

    public ProTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pro_type, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, ProTypeBean data) {
        holder.mTvProName.setText(data.proName);
        holder.mTvCount.setText(data.count + "单");
        holder.mIvPoint.setImageResource(data.colorRes);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mIvPoint;
        TextView mTvProName, mTvCount;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            mIvPoint = itemView.findViewById(R.id.iv_point);
            mTvCount = itemView.findViewById(R.id.tv_pro_count);
            mTvProName = itemView.findViewById(R.id.tv_pro_name);
            if (position == 1 || position == 3 || position == 5) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvPoint.getLayoutParams();
                params.leftMargin = DeviceUtils.dip2px(mContext, 20);
                mIvPoint.setLayoutParams(params);
            }
        }
    }
}
