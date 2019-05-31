package com.paizhong.manggo.ui.follow.detail.newbuy;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.follow.PersonOrderBean;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Des: 牛人详情（最新跟买） Adapter
 * Created by hs on 2018/5/29 0029 17:38
 */
public class NewBuyOrderAdapter extends BaseRecyclerAdapter<NewBuyOrderAdapter.ViewHolder, PersonOrderBean> {

    private BaseActivity mContext;
    private PlaceOrderDialog mOrderDialog;
    private int mRedColor, mGreenColor, mGrayColor;

    public NewBuyOrderAdapter(Activity activity) {
        super(activity);
        mContext = (BaseActivity) activity;
        mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        mGrayColor = ContextCompat.getColor(mContext, R.color.color_878787);
        mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_layout, parent, false), position);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, PersonOrderBean data) {
        holder.tvUnitPrice.setText(data.mProductMsg); //手数
        holder.tvGoodsName.setText(data.mProductName);  //产品名称
        holder.tvTime.setText(data.openTime); //建仓时间
        holder.tvServiceCharge.setText(data.fee); //手续费
        holder.tvCostPrice.setText(data.mCostPrice); //成本
        holder.tvCreatePrice.setText(data.openPrice); //建仓点位
        holder.tvFollowNum.setText(getText(data));

        handleFloatPoint(holder, data);
    }

    /**
     * 显示浮动点位
     *
     * @param holder
     * @param data
     */
    private void handleFloatPoint(ViewHolder holder, PersonOrderBean data) {
        holder.tvFloatPoint.setText(data.floatPrice);
        if (data.mFloatPrice > 0) {
            holder.tvFloatPoint.setTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvFloatPoint.setTextColor(mGreenColor);
        } else {
            holder.tvFloatPoint.setTextColor(mGrayColor);
        }

        //0 涨  1跌
        holder.tvType.setSelected(data.flag == 0);
        holder.tvType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);
    }


    private Spanned getText(PersonOrderBean data) {
        return Html.fromHtml("已有<font color = '#008EFF'>"
                + data.mFellowCount + "</font>人跟买，单笔返还<font color = '#008EFF'>"
                + data.score + "</font>积分");
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;

        TextView tvGoodsName, tvType, tvUnitPrice;
        TextView tvTime, tvFollowNum, tvFollowOrder;
        TextView tvCostPrice, tvServiceCharge, tvFloatPoint, tvCreatePrice;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            this.position = position;

            tvType = (TextView) itemView.findViewById(R.id.tv_buy_type);
            tvFloatPoint = (TextView) itemView.findViewById(R.id.tv_point);
            tvUnitPrice = (TextView) itemView.findViewById(R.id.tv_unit_price);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvServiceCharge = (TextView) itemView.findViewById(R.id.tv_service_charge);

            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvCostPrice = (TextView) itemView.findViewById(R.id.tv_cost_price);
            tvCreatePrice = (TextView) itemView.findViewById(R.id.tv_create_price);

            tvFollowNum = (TextView) itemView.findViewById(R.id.tv_follow_num);
            tvFollowOrder = (TextView) itemView.findViewById(R.id.tv_follow_order);

            tvFollowOrder.setOnClickListener(this);
        }

        /**
         * 下单
         */
        @Override
        public void onClick(View v) {
            PersonOrderBean orderBean = data.get(position);
            if (TextUtils.equals(AppApplication.getConfig().getMobilePhone(), orderBean.phone)) {
                AppToast.show("无法跟买自己的单子！");
                return;
            }
            if (!mContext.login()) {
                return;
            }
            if (mOrderDialog == null) {
                mOrderDialog = new PlaceOrderDialog(mContext);
            }

            mOrderDialog.placeOrderFollow(3
                    , String.valueOf(orderBean.typeId)
                    , orderBean.flag == 0
                    , TradeUtils.getNum(orderBean.amount, orderBean.quantity)
                    , orderBean.stockIndex
                    , orderBean.changeValue
                    , orderBean.closingPrice
                    , Integer.parseInt(orderBean.profitLimit)
                    , Integer.parseInt(orderBean.lossLimit)
                    , orderBean.phone
                    , orderBean.orderNo);
        }
    }

    public PlaceOrderDialog getOrderDialog() {
        return mOrderDialog;
    }

}
