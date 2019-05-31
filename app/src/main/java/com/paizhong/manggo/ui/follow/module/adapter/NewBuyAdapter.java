package com.paizhong.manggo.ui.follow.module.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 牛人详情（最新跟买） Adapter
 * Created by hs on 2018/5/29 0029 17:38
 */
public class NewBuyAdapter extends BaseRecyclerAdapter<ViewHolder, FollowListBean> {

    private int mRedColor, mGreenColor, mGrayColor;

    public NewBuyAdapter(Activity activity) {
        super(activity);
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGrayColor = ContextCompat.getColor(mContext, R.color.color_878787);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_follow_new_buy, parent, false));
    }

    public void mOnBindViewHolder(ViewHolder holder, int position, FollowListBean data) {
        holder.setFollowBean(data, position);

        handleBaseInfo(data, holder);
        handleVariableWidget(holder, data);
    }

    private void handleBaseInfo(FollowListBean data, ViewHolder holder) {
        holder.tvName.addText(data.nickName);  //昵称
        ImageUtils.display(data.userPic, holder.ivHead, R.mipmap.ic_user_head); //头像
        holder.tvEarnings.addText(Html.fromHtml("周盈利率 <font color = '#008EFF'>" + data.mProfitRate + "%</font>")); //盈利率

        holder.tvTime.addText(data.openTime); //建仓时间
        holder.tvServiceCharge.addText(data.mFee); //手续费
        holder.tvCostPrice.addText(data.mCostPrice); //成本
        holder.tvUnitPrice.addText(data.mProductMsg); //手数
        holder.tvCreatePrice.addText(data.openPrice); //建仓点位
        holder.tvGoodsName.addText(data.mProductName);  //产品名称

        //买涨买跌（0 涨  1跌）
        holder.tvType.setSelected(data.flag == 0);
        holder.tvType.addText(data.flag == 0 ? R.string.text9 : R.string.text10);
    }

    /**
     * 处理可变的控件的显示
     *
     * @param holder
     * @param data
     */
    private void handleVariableWidget(ViewHolder holder, FollowListBean data) {
        //止盈 止损
        holder.tvStopUp.addText(data.profitLimit);
        holder.tvStopDown.addText(data.lossLimit);

        holder.tvIntegral.addText(data.mScore); // 积分
        holder.tvTime2.addText(data.mCurrentTime); //时间
        holder.tvFloatPoint.addText(data.floatPrice); //浮动点位
        holder.tvFollowNum.addText(data.mFellowCount); //几人跟买
        holder.tvFollow1.setSelected(data.isConcern == 0); //是否关注
        holder.tvFollow1.addText(String.valueOf(data.concernCount)); //关注数

        if (data.mFloatPrice > 0) {
            holder.tvFloatPoint.addTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvFloatPoint.addTextColor(mGreenColor);
        } else {
            holder.tvFloatPoint.addTextColor(mGrayColor);
        }
    }
}
