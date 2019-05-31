package com.paizhong.manggo.ui.trade.hangup;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.trade.HangUpListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.recycle.RecyclerView;

import java.util.List;

/**
 * Created by zab on 2018/8/22 0022.
 */
public class HangUpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private BaseActivity mContext;
    private int mViewType = ViewConstant.SIMP_TAB_ITEM_ZERO;
    private LayoutInflater layoutInflater;
    private List<HangUpListBean> mHangUpList;

    private int mRedColor;
    private int mGreenColor;
    private int mBlackColor;

    public HangUpAdapter(BaseActivity context) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
        this.mBlackColor = ContextCompat.getColor(mContext, R.color.color_727272);
    }


    public void notifyDataChanged(boolean isRefresh,List<HangUpListBean> hangUpList){
        if (isRefresh){
            this.mHangUpList = hangUpList;
            notifyDataSetChanged();
        }else {
            if (mHangUpList !=null){
                mHangUpList.addAll(hangUpList);
            }else {
                this.mHangUpList = hangUpList;
            }
            notifyDataSetChanged();
        }
    }



    public void setViewType(int viewType){
        this.mViewType = viewType;
    }

    @Override
    public int getItemCount() {
        return mHangUpList != null ? mHangUpList.size() : 0;
    }


    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }


    public List<HangUpListBean> getHangUpList(){
        return mHangUpList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewConstant.SIMP_TAB_ITEM_ZERO) {
            return new ViewHolderItem(layoutInflater.inflate(R.layout.item_hangup_layout, parent, false));
        } else {
            return new ViewHolderHistory(layoutInflater.inflate(R.layout.item_hangup_history_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HangUpListBean hangUpListBean = mHangUpList.get(position);
        if (holder instanceof ViewHolderItem){
             hangUpList((ViewHolderItem)holder,hangUpListBean);
          }else if (holder instanceof ViewHolderHistory){
             hangUpHistoryList((ViewHolderHistory)holder,hangUpListBean);
          }
    }


    private void hangUpHistoryList(ViewHolderHistory holder , HangUpListBean hangUpBean){
           holder.tvProductName.setText(hangUpBean.productName+" ");
           holder.tvProductAmount.setText(hangUpBean.amount+"元");
           holder.tvHangupPrice.setText(hangUpBean.registerAmount+" ± "+hangUpBean.floatNum);
           holder.tvHangupTime.setText(hangUpBean.createTime);
           if (hangUpBean.flag == 0){
               holder.tvBuyText.setSelected(true);
               holder.tvBuyText.setText("买涨");
           }else {
               holder.tvBuyText.setSelected(false);
               holder.tvBuyText.setText("买跌");
           }
           if (hangUpBean.status == 2||hangUpBean.status == 3){
               holder.tvHangupState.setText("挂单成功");
               holder.tvHangupState.setTextColor(ContextCompat.getColor(mContext,R.color.color_008EFF));
               holder.tvTimeErrorHint.setText("建仓时间");
               holder.tvTimeError.setText(hangUpBean.registerFinishTime);
               holder.tvOpenPriceHint.setVisibility(View.VISIBLE);
               holder.tvOpenPrice.setVisibility(View.VISIBLE);
               holder.tvOpenPrice.setText(hangUpBean.openPrice);
               if (hangUpBean.flag == 0){
                   holder.tvOpenPrice.setTextColor(ContextCompat.getColor(mContext,R.color.color_F74F54));
               }else {
                   holder.tvOpenPrice.setTextColor(ContextCompat.getColor(mContext,R.color.color_1AC47A));
               }
           }else {
               holder.tvHangupState.setText("挂单失败");
               holder.tvHangupState.setTextColor(ContextCompat.getColor(mContext,R.color.color_F74F54));
               holder.tvTimeErrorHint.setText("失败原因");
               holder.tvTimeError.setText(hangUpBean.remark);
               holder.tvOpenPriceHint.setVisibility(View.INVISIBLE);
               holder.tvOpenPrice.setVisibility(View.INVISIBLE);
           }
    }

    private void hangUpList(ViewHolderItem holder, final HangUpListBean hangUpBean){
        if (hangUpBean.typeTag){
            holder.llProductLayout.setVisibility(View.VISIBLE);
            holder.tvProductName.setText(hangUpBean.productName);
            holder.tvMarketData.setText(hangUpBean.stockIndex);
            if (hangUpBean.changValue > 0){
                holder.tvMarketData.setTextColor(mRedColor);
                holder.ivBuyImg.setVisibility(View.VISIBLE);
                holder.ivBuyImg.setSelected(true);
            }else if (hangUpBean.changValue < 0){
                holder.tvMarketData.setTextColor(mGreenColor);
                holder.ivBuyImg.setVisibility(View.VISIBLE);
                holder.ivBuyImg.setSelected(false);
            }else {
                holder.tvMarketData.setTextColor(mBlackColor);
                holder.ivBuyImg.setVisibility(View.INVISIBLE);
            }
        }else {
            holder.llProductLayout.setVisibility(View.GONE);
        }

         //0 涨  1跌
         if (hangUpBean.flag == 0){
             holder.tvBuyText.setSelected(true);
             holder.tvBuyText.setText("买涨");
         }else {
             holder.tvBuyText.setSelected(false);
             holder.tvBuyText.setText("买跌");
         }

         holder.tvKgS.setText(hangUpBean.kgNum);

         holder.tvHangupPrice.setText(hangUpBean.registerAmount+" ± "+hangUpBean.floatNum);

         holder.tvOpenPrice.setText(hangUpBean.amount+"元");

         holder.tvTargetProfit.setText("止盈"+hangUpBean.profitLimit+"%");
         holder.tvTargetStop.setText("止损"+hangUpBean.lossLimit+"%");
         holder.tvOpenFee.setText(hangUpBean.fee +"元");
         holder.tvHangupTime.setText(hangUpBean.createTime);

         holder.tvRepealHangup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!DeviceUtils.isFastDoubleClick()){
                     final AppHintDialog mAppHintDialog  =  new AppHintDialog(mContext);
                     mAppHintDialog.showAppDialog(8, "", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HangUpFragment.getInstance().getCancelHangUp(hangUpBean.id);
                            mAppHintDialog.dismiss();
                        }
                    });
                 }
             }
         });

         holder.tvUpdateHangup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!DeviceUtils.isFastDoubleClick()){
                     ((MainActivity)mContext).placeOrder().placeOrderUpdateHangUp(
                             hangUpBean.id,
                             hangUpBean.typeId,
                             hangUpBean.stockIndex,
                             String.valueOf(hangUpBean.changValue),
                             hangUpBean.flag == 0 ,
                             TradeUtils.getNum(Integer.parseInt(hangUpBean.amount),hangUpBean.quantity),
                             hangUpBean.quantity,
                             hangUpBean.holdNight,
                             hangUpBean.profitLimit,
                             hangUpBean.lossLimit,
                             hangUpBean.registerAmount,
                             hangUpBean.floatNum);
                 }
             }
         });
    }

    //挂单 viewHolder
    public class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView tvProductName;
        DinTextView tvMarketData;
        ImageView ivBuyImg;
        LinearLayout llProductLayout;

        TextView tvBuyText;
        TextView tvKgS;
        TextView tvHangupPrice;

        TextView tvOpenPrice;
        TextView tvTargetProfit;
        TextView tvTargetStop;

        TextView tvOpenFee;
        TextView tvHangupTime;

        TextView tvRepealHangup;
        TextView tvUpdateHangup;
        View vMaxLine;

        public ViewHolderItem(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMarketData = itemView.findViewById(R.id.tv_market_data);
            ivBuyImg = itemView.findViewById(R.id.iv_buy_img);
            llProductLayout = itemView.findViewById(R.id.ll_product_layout);
            tvBuyText = itemView.findViewById(R.id.tv_buy_text);
            tvKgS = itemView.findViewById(R.id.tv_kg_s);
            tvHangupPrice = itemView.findViewById(R.id.tv_hangup_price);
            tvOpenPrice = itemView.findViewById(R.id.tv_open_price);
            tvTargetProfit = itemView.findViewById(R.id.tv_target_profit);
            tvTargetStop = itemView.findViewById(R.id.tv_target_stop);
            tvOpenFee = itemView.findViewById(R.id.tv_open_fee);
            tvHangupTime = itemView.findViewById(R.id.tv_hangup_time);
            tvRepealHangup = itemView.findViewById(R.id.tv_repeal_hangup);
            tvUpdateHangup = itemView.findViewById(R.id.tv_update_hangup);
            vMaxLine = itemView.findViewById(R.id.v_max_line);
        }
    }


    //挂单历史
    public class ViewHolderHistory extends RecyclerView.ViewHolder {
        TextView tvBuyText;
        TextView tvProductName;
        TextView tvProductAmount;
        TextView tvHangupPrice;

        TextView tvHangupTime;
        TextView tvHangupState;

        TextView tvTimeErrorHint;
        TextView tvTimeError;
        TextView tvOpenPriceHint;
        TextView tvOpenPrice;

        public ViewHolderHistory(View itemView) {
            super(itemView);
            tvBuyText = itemView.findViewById(R.id.tv_buy_text);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductAmount = itemView.findViewById(R.id.tv_product_amount);
            tvHangupPrice = itemView.findViewById(R.id.tv_hangup_price);
            tvHangupTime = itemView.findViewById(R.id.tv_hangup_time);
            tvHangupState = itemView.findViewById(R.id.tv_hangup_state);
            tvTimeErrorHint = itemView.findViewById(R.id.tv_time_error_hint);
            tvTimeError = itemView.findViewById(R.id.tv_time_error);
            tvOpenPriceHint = itemView.findViewById(R.id.tv_open_price_hint);
            tvOpenPrice = itemView.findViewById(R.id.tv_open_price);
        }
    }
}
