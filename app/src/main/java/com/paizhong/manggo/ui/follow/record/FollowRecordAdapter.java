package com.paizhong.manggo.ui.follow.record;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.FollowRecordBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;


/**
 * Created by zab on 2018/7/2 0002.
 */
public class FollowRecordAdapter extends BaseRecyclerAdapter<FollowRecordAdapter.ViewHolder, FollowRecordBean> {
    public static final int VIEW_TYPE_ONE = 1;
    public static final int VIEW_TYPE_TWO = 2;

    private int mViewType;
    private int mRecordType;
    private boolean mIsNiu;
    private int mRedColor;
    private int mGreenColor;
    private int mBlackColor;
    private int mGrayColor;

    public FollowRecordAdapter(boolean mIsNiu, Activity context) {
        super(context);
        this.mIsNiu = mIsNiu;
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
        this.mBlackColor = ContextCompat.getColor(mContext, R.color.color_333333);
        this.mGrayColor = ContextCompat.getColor(mContext, R.color.color_878787);
    }

    public void setRecordType(int recordType) {
        this.mRecordType = recordType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_record_layout, parent, false), viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, FollowRecordBean data) {
        if (mIsNiu) {
            bindNiuData(holder, data);
        } else {
            bindNoNiuData(holder, data);
        }
    }


    //跟买 大牛
    private void bindNiuData(ViewHolder holder, FollowRecordBean data) {
        //发起跟买
        if (ViewConstant.SIMP_TAB_ITEM_ONE == mRecordType) {
            if (VIEW_TYPE_ONE == mViewType) {
                bindOrderSendPost(holder, data);
            } else {
                bindOrderSendPc(holder, data);
            }
        } else {
            //我的跟买 持仓
            if (VIEW_TYPE_ONE == mViewType) {
                bindOrderPost(holder, data);
            } else {
                bindPcData(holder, data);
            }
        }
    }

    //跟买 非大牛
    private void bindNoNiuData(ViewHolder holder, FollowRecordBean data) {
        if (VIEW_TYPE_ONE == mViewType) {
            bindOrderPost(holder, data);
        } else {
            bindPcData(holder, data);
        }
    }


    //发起跟买 持仓
    private void bindOrderSendPost(ViewHolder holder, FollowRecordBean data) {
        holder.tvGmMoneyHint1.setVisibility(View.GONE);
        holder.tvGmMoneyHint2.setVisibility(View.GONE);
        holder.tvGmMoney.setVisibility(View.GONE);
        holder.llBottomLayout1.setVisibility(View.GONE);
        holder.llBottomLayout2.setVisibility(View.VISIBLE);
        holder.vLine.setVisibility(View.VISIBLE);
        holder.llLayout5.setVisibility(View.VISIBLE);
        holder.llLayout7.setVisibility(View.VISIBLE);
        holder.tvText7.setVisibility(View.GONE);


        holder.tvText1.setText(R.string.text1);//建仓成本
        holder.tvText2.setText(R.string.text2);//建仓点位
        holder.tvText3.setText(R.string.text3);//浮动点位
        holder.tvText4.setText(R.string.text4);//建仓手续费
        holder.tvText5.setText(R.string.text12);//盈余金额
        holder.tvText6.setText(R.string.text5);//建仓时间

        holder.tvProductName.setText(data.productName);
        holder.tvProductMsg.setText(data.mProductMsg);

        holder.tvBuyType.setSelected(data.flag == 0);
        holder.tvBuyType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvValue1.setText(String.valueOf(data.amount) + "元");
        holder.tvValue2.setText(data.openPrice);
        holder.tvValue3.setText(data.floatPrice);
        if (data.mFloatPrice > 0) {
            holder.tvValue3.setTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvValue3.setTextColor(mGreenColor);
        } else {
            holder.tvValue3.setTextColor(mBlackColor);
        }
        holder.tvValue4.setText(data.fee + "元");
        holder.tvValue5.setText(data.closeProfit);
        if (data.mCloseProfit > 0) {
            holder.tvValue5.setTextColor(mRedColor);
        } else if (data.mCloseProfit < 0) {
            holder.tvValue5.setTextColor(mGreenColor);
        } else {
            holder.tvValue5.setTextColor(mBlackColor);
        }
        holder.tvValue6.setText(data.openTime);

        holder.tvIntegral.setText(data.score);

        holder.tvRenSize.setText(String.valueOf(data.fellowCount));

        setTextColor(holder.tvValue7, data.profitLimit, data.lossLimit);
    }


    //发起跟买 平仓
    private void bindOrderSendPc(ViewHolder holder, FollowRecordBean data) {
        holder.tvGmMoneyHint1.setVisibility(View.GONE);
        holder.tvGmMoneyHint2.setVisibility(View.GONE);
        holder.tvGmMoney.setVisibility(View.GONE);
        holder.llBottomLayout1.setVisibility(View.GONE);
        holder.llBottomLayout2.setVisibility(View.VISIBLE);
        holder.vLine.setVisibility(View.VISIBLE);
        holder.llLayout7.setVisibility(View.VISIBLE);
        holder.tvText7.setVisibility(View.VISIBLE);

        holder.tvText1.setText(R.string.text1);//建仓成本
        holder.tvText2.setText(R.string.text2);//建仓点位
        holder.tvText3.setText(R.string.text3);//浮动点位
        holder.tvText4.setText(R.string.text4);//建仓手续费

        holder.tvText5.setText(R.string.text12);//盈余金额
        holder.tvText6.setText(R.string.text5);//建仓时间
        holder.tvText7.setText(R.string.text8);//平仓时间

        holder.tvProductName.setText(data.productName);
        holder.tvProductMsg.setText(data.mProductMsg);

        holder.tvBuyType.setSelected(data.flag == 0);
        holder.tvBuyType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvValue1.setText(String.valueOf(data.amount) + "元");
        holder.tvValue2.setText(data.openPrice);
        holder.tvValue3.setText(data.floatPrice);
        if (data.mFloatPrice > 0) {
            holder.tvValue3.setTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvValue3.setTextColor(mGreenColor);
        } else {
            holder.tvValue3.setTextColor(mBlackColor);
        }
        holder.tvValue4.setText(data.fee + "元");

        double closeProfit = NumberUtil.getDouble(data.closeProfit);
        holder.tvValue5.setText(data.closeProfit);
        if (closeProfit > 0) {
            holder.tvValue5.setTextColor(mRedColor);
        } else if (closeProfit < 0) {
            holder.tvValue5.setTextColor(mGreenColor);
        } else {
            holder.tvValue5.setTextColor(mBlackColor);
        }
        holder.tvValue6.setText(data.openTime);
        holder.tvValue7.setText(data.closeTime);

        holder.tvIntegral.setText(data.score);

        holder.tvRenSize.setText(String.valueOf(data.fellowCount));
    }

    //我的跟买 持仓
    private void bindOrderPost(ViewHolder holder, final FollowRecordBean data) {
        holder.tvGmMoneyHint1.setVisibility(View.VISIBLE);
        holder.tvGmMoney.setVisibility(View.VISIBLE);
        holder.tvGmMoneyHint2.setVisibility(View.VISIBLE);
        holder.llLayout5.setVisibility(View.GONE);
        holder.llLayout7.setVisibility(View.GONE);
        holder.llBottomLayout1.setVisibility(View.VISIBLE);
        holder.llBottomLayout2.setVisibility(View.GONE);
        holder.vLine.setVisibility(View.GONE);

        holder.tvText1.setText(R.string.text2);//建仓点位
        holder.tvText2.setText(R.string.text4);//建仓手续费
        holder.tvText3.setText(R.string.text3);//浮动点位
        holder.tvText4.setText(R.string.text5);//建仓时间

        holder.tvProductName.setText(data.productName);
        holder.tvProductMsg.setText(data.mProductMsg);

        holder.tvBuyType.setSelected(data.flag == 0);
        holder.tvBuyType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvGmMoney.setText(String.valueOf(data.amount));
        holder.tvValue1.setText(data.openPrice);
        holder.tvValue2.setText(data.fee + "元");

        holder.tvValue3.setText(data.floatPrice);
        if (data.mFloatPrice > 0) {
            holder.tvValue3.setTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvValue3.setTextColor(mGreenColor);
        } else {
            holder.tvValue3.setTextColor(mBlackColor);
        }
        holder.tvValue4.setText(data.openTime);

        holder.tvYkPrice.setText(data.closeProfit);
        if (data.mCloseProfit > 0) {
            holder.tvYkPrice.setTextColor(mRedColor);
        } else if (data.mCloseProfit < 0) {
            holder.tvYkPrice.setTextColor(mGreenColor);
        } else {
            holder.tvYkPrice.setTextColor(mBlackColor);
        }
        holder.tvName.setText(data.nickName);
        ImageUtils.display(data.userPic, holder.civHead, R.mipmap.ic_user_head);

        holder.civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isRanking == 1 && !DeviceUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("phone", data.fellowerPhone);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    //我的跟买 平仓
    private void bindPcData(ViewHolder holder, final FollowRecordBean data) {
        holder.tvGmMoneyHint1.setVisibility(View.VISIBLE);
        holder.tvGmMoney.setVisibility(View.VISIBLE);
        holder.tvGmMoneyHint2.setVisibility(View.VISIBLE);
        holder.llLayout5.setVisibility(View.GONE);
        holder.llLayout7.setVisibility(View.VISIBLE);
        holder.tvText7.setVisibility(View.VISIBLE);
        holder.llBottomLayout1.setVisibility(View.VISIBLE);
        holder.llBottomLayout2.setVisibility(View.GONE);
        holder.vLine.setVisibility(View.GONE);

        holder.tvText1.setText(R.string.text2);//建仓点位
        holder.tvText2.setText(R.string.text4);//建仓手续费
        holder.tvText3.setText(R.string.text3);//浮动点位
        holder.tvText4.setText(R.string.text5);//建仓时间
        holder.tvText7.setText(R.string.text8);//平仓时间

        holder.tvProductName.setText(data.productName);
        holder.tvProductMsg.setText(data.mProductMsg);

        holder.tvBuyType.setSelected(data.flag == 0);
        holder.tvBuyType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvGmMoney.setText(String.valueOf(data.amount));
        holder.tvValue1.setText(data.openPrice);

        holder.tvValue3.setText(data.floatPrice);
        if (data.mFloatPrice > 0) {
            holder.tvValue3.setTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            holder.tvValue3.setTextColor(mGreenColor);
        } else {
            holder.tvValue3.setTextColor(mBlackColor);
        }
        holder.tvValue2.setText(data.fee + "元");
        holder.tvValue4.setText(data.openTime);
        holder.tvValue7.setText(data.closeTime);

        double closeProfit = NumberUtil.getDouble(data.closeProfit);
        holder.tvYkPrice.setText(data.closeProfit);
        if (closeProfit > 0) {
            holder.tvYkPrice.setTextColor(mRedColor);
        } else if (closeProfit < 0) {
            holder.tvYkPrice.setTextColor(mGreenColor);
        } else {
            holder.tvYkPrice.setTextColor(mBlackColor);
        }
        holder.tvName.setText(data.nickName);
        ImageUtils.display(data.userPic, holder.civHead, R.mipmap.ic_user_head);

        holder.civHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isRanking == 1 && !DeviceUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(mContext, PersonInfoActivity.class);
                    intent.putExtra("phone", data.fellowerPhone);
                    mContext.startActivity(intent);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvProductName;
        TextView tvBuyType;
        TextView tvProductMsg;
        View tvGmMoneyHint1;
        TextView tvGmMoney;
        View tvGmMoneyHint2;

        TextView tvText1;
        TextView tvValue1;
        TextView tvText2;
        TextView tvValue2;
        TextView tvText3;
        TextView tvValue3;
        TextView tvText4;
        TextView tvValue4;
        TextView tvText5;
        TextView tvValue5;
        TextView tvText6;
        TextView tvValue6;
        TextView tvText7;
        TextView tvValue7;

        View llLayout5;
        View llLayout7;

        View llBottomLayout1;
        DinTextView tvYkPrice;
        CircleImageView civHead;
        TextView tvName;
        View vLine;

        View llBottomLayout2;
        TextView tvIntegral;
        TextView tvRenSize;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvBuyType = (TextView) itemView.findViewById(R.id.tv_buy_type);
            tvProductMsg = (TextView) itemView.findViewById(R.id.tv_product_msg);
            tvGmMoneyHint1 = itemView.findViewById(R.id.tv_gm_money_hint1);
            tvGmMoney = (TextView) itemView.findViewById(R.id.tv_gm_money);
            tvGmMoneyHint2 = itemView.findViewById(R.id.tv_gm_money_hint2);

            tvText1 = (TextView) itemView.findViewById(R.id.tv_text1);
            tvValue1 = (TextView) itemView.findViewById(R.id.tv_value1);
            tvText2 = (TextView) itemView.findViewById(R.id.tv_text2);
            tvValue2 = (TextView) itemView.findViewById(R.id.tv_value2);
            tvText3 = (TextView) itemView.findViewById(R.id.tv_text3);
            tvValue3 = (TextView) itemView.findViewById(R.id.tv_value3);
            tvText4 = (TextView) itemView.findViewById(R.id.tv_text4);
            tvValue4 = (TextView) itemView.findViewById(R.id.tv_value4);
            tvText5 = (TextView) itemView.findViewById(R.id.tv_text5);
            tvValue5 = (TextView) itemView.findViewById(R.id.tv_value5);
            tvText6 = (TextView) itemView.findViewById(R.id.tv_text6);
            tvValue6 = (TextView) itemView.findViewById(R.id.tv_value6);
            tvText7 = (TextView) itemView.findViewById(R.id.tv_text7);
            tvValue7 = (TextView) itemView.findViewById(R.id.tv_value7);

            llLayout5 = itemView.findViewById(R.id.ll_layout5);

            llLayout7 = itemView.findViewById(R.id.ll_layout7);
            llBottomLayout1 = itemView.findViewById(R.id.ll_bottom_layout1);
            tvYkPrice = (DinTextView) itemView.findViewById(R.id.tv_yk_price);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            civHead = (CircleImageView) itemView.findViewById(R.id.civ_head);
            vLine = itemView.findViewById(R.id.v_line);

            llBottomLayout2 = itemView.findViewById(R.id.ll_bottom_layout2);
            tvIntegral = (TextView) itemView.findViewById(R.id.tv_integral);
            tvRenSize = (TextView) itemView.findViewById(R.id.tv_ren_size);

            tvName.setOnClickListener(this);
            civHead.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void setTextColor(TextView textView, String profitLimit, String lossLimit) {
        textView.setText(Html.fromHtml("<font color = '#F74F54'>止盈 " + profitLimit + "%</font><font color = '#1AC47A'> 止损 " + lossLimit + "%</font>"));
    }
}
