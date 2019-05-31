package com.paizhong.manggo.ui.trade.position;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.dialog.closeposition.ClosePositionDialog;
import com.paizhong.manggo.dialog.positionzyzs.PositionZyzsDialog;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketActivity;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.recycle.RecyclerView;
import java.util.List;

/**
 * Created by zab on 2018/8/22 0022.
 */
public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {

    private ArrayMap<String,Boolean> checkExMap;
    private BaseActivity mContext;
    private List<PositionListBean> mPositionList;
    private int mRedColor;
    private int mGreenColor;
    private int mBlackColor;
    private ClosePositionDialog mClosePositionDialog;

    public PositionAdapter(BaseActivity context) {
        this.checkExMap = new ArrayMap<>();
        this.mContext = context;
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
        this.mBlackColor = ContextCompat.getColor(mContext, R.color.color_727272);
    }


    public void notifyDataChanged(List<PositionListBean> positionList){
        this.mPositionList = positionList;
        notifyDataSetChanged();
    }


    public void putCheck(String key,boolean value){
        checkExMap.put(key,value);
    }

    public boolean getCheck(String key){
        if (checkExMap.containsKey(key)){
            return checkExMap.get(key);
        }else {
            return true;
        }
    }

    public void clearCheckExMap(){
        checkExMap.clear();
    }


    public ClosePositionDialog getCloseDialog(){
        return mClosePositionDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_position_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return mPositionList != null ? mPositionList.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PositionListBean listBean = mPositionList.get(position);
        if (listBean.typeTag){
            holder.vMaxLine.setVisibility(View.VISIBLE);
            holder.llProductLayout.setVisibility(View.VISIBLE);

            holder.tvProductName.setText(listBean.name);
            holder.tvMarketData.setText(listBean.stockIndex);
            if (listBean.changValue > 0){
                holder.tvMarketData.setTextColor(mRedColor);
                holder.ivBuyImg.setVisibility(View.VISIBLE);
                holder.ivBuyImg.setSelected(true);
            }else if (listBean.changValue < 0){
                holder.tvMarketData.setTextColor(mGreenColor);
                holder.ivBuyImg.setVisibility(View.VISIBLE);
                holder.ivBuyImg.setSelected(false);
            }else {
                holder.tvMarketData.setTextColor(mBlackColor);
                holder.ivBuyImg.setVisibility(View.GONE);
            }
        }else {
             holder.vMaxLine.setVisibility(View.GONE);
             holder.llProductLayout.setVisibility(View.GONE);
        }

        //0 涨  1跌
        if (listBean.flag == 0){
            holder.tvBuyText.setText(R.string.text9);
            holder.tvBuyText.setSelected(true);
        }else {
            holder.tvBuyText.setText(R.string.text10);
            holder.tvBuyText.setSelected(false);
        }

        holder.tvKgS.setText(listBean.kgNum);

        if (!TextUtils.isEmpty(listBean.coupon_id)){
            holder.ivVoucher.setVisibility(View.VISIBLE);
            holder.ivHoldFee.setSelected(false);
            holder.ivHoldFee.setEnabled(false);
        }else {
            holder.ivVoucher.setVisibility(View.GONE);
            //1是持仓过夜，0是不持仓过夜
            holder.ivHoldFee.setEnabled(true);
            holder.ivHoldFee.setSelected(listBean.hold_night == 1);
        }

        holder.tvOpenPrice.setText(String.format("%s元", listBean.amount));

        holder.tvOpenFee.setText(String.format("%s元", listBean.fee));

        holder.tvOpenDw.setText(listBean.open_price);
        holder.tvFloatPoint.setText(listBean.flu_dw);
        holder.tvFloatPrice.setText(String.valueOf(listBean.floatingPrice));
        if (listBean.floatingPrice > 0){
            holder.tvFloatPoint.setTextColor(mRedColor);
            holder.tvFloatPrice.setTextColor(mRedColor);
        }else if (listBean.floatingPrice < 0){
            holder.tvFloatPoint.setTextColor(mGreenColor);
            holder.tvFloatPrice.setTextColor(mGreenColor);
        }else {
            holder.tvFloatPoint.setTextColor(mBlackColor);
            holder.tvFloatPrice.setTextColor(mBlackColor);
        }

        holder.tvOpenTime.setText(listBean.create_time);

        holder.tvTargetProfit.setText(String.format("止盈 %d%%", listBean.profit_limit));
        holder.tvTargetStop.setText(String.format("止损 %d%%", listBean.loss_limit));

        if (listBean.check){
            holder.llContentOne.setVisibility(View.VISIBLE);
            holder.llContentTwo.setVisibility(View.VISIBLE);
            holder.llContentThr.setVisibility(View.VISIBLE);
            holder.tvDetail.setSelected(true);
            holder.tvDetail.setText(R.string.text20);
        }else {
            holder.llContentOne.setVisibility(View.GONE);
            holder.llContentTwo.setVisibility(View.GONE);
            holder.llContentThr.setVisibility(View.GONE);
            holder.tvDetail.setSelected(false);
            holder.tvDetail.setText(R.string.text21);
        }



        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (holder.tvDetail.isSelected()){
                    putCheck(listBean.order_no,false);
                    holder.tvDetail.setSelected(false);
                    holder.tvDetail.setText(R.string.text21);
                    holder.llContentOne.setVisibility(View.GONE);
                    holder.llContentTwo.setVisibility(View.GONE);
                    holder.llContentThr.setVisibility(View.GONE);
                }else {
                    putCheck(listBean.order_no,true);
                    holder.tvDetail.setSelected(true);
                    holder.tvDetail.setText(R.string.text20);
                    holder.llContentOne.setVisibility(View.VISIBLE);
                    holder.llContentTwo.setVisibility(View.VISIBLE);
                    holder.llContentThr.setVisibility(View.VISIBLE);
                }
            }
        });



        //平仓
        holder.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (DeviceUtils.isFastDoubleClick()){
                  return;
              }
              if (mClosePositionDialog == null){
                  mClosePositionDialog = new ClosePositionDialog(mContext);
              }
              mClosePositionDialog.showDialog(listBean.type,listBean.id,listBean.name,listBean.stockIndex,listBean.floatingPrice);
            }
        });


        //是否持仓过夜
        holder.ivHoldFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (DeviceUtils.isFastDoubleClick()){
                   return;
               }
               if (nightListener !=null){
                   nightListener.onOrderHoldNight(listBean.id,listBean.order_no,!holder.ivHoldFee.isSelected());
               }
            }
        });

        holder.llTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    new PositionZyzsDialog(mContext).showPositionZyzsDialog(listBean.id,listBean.coupon_id,listBean.order_no,Integer.parseInt(listBean.amount),listBean.loss_limit,listBean.profit_limit);
                }
            }
        });

        holder.tvAddPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    ((MainActivity)mContext).placeOrder().placeOrder(listBean.type,true,1);
                }
            }
        });


        holder.llProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    Intent intent = new Intent(mContext,KLineMarketActivity.class );
                    intent.putExtra("productID", listBean.type);
                    intent.putExtra("productName", listBean.name);
                    intent.putExtra("closePrice", listBean.close);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        DinTextView tvMarketData;
        ImageView ivBuyImg;
        TextView tvAddPosition;
        LinearLayout llProductLayout;

        TextView tvBuyText;
        TextView tvKgS;
        ImageView ivVoucher;
        TextView tvOpenPrice;
        TextView tvDetail;

        TextView tvOpenFee;
        TextView tvOpenDw;
        ImageView ivHoldFee;
        LinearLayout llContentOne;

        TextView tvFloatPoint;
        TextView tvOpenTime;
        LinearLayout llContentTwo;

        DinTextView tvFloatPrice;
        TextView tvTargetProfit;
        TextView tvTargetStop;
        LinearLayout llTarget;
        TextView tvClose;
        View vMaxLine;

        View llContentThr;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvMarketData = itemView.findViewById(R.id.tv_market_data);
            ivBuyImg = itemView.findViewById(R.id.iv_buy_img);
            tvAddPosition = itemView.findViewById(R.id.tv_add_position);
            llProductLayout = itemView.findViewById(R.id.ll_product_layout);
            tvBuyText = itemView.findViewById(R.id.tv_buy_text);
            tvKgS = itemView.findViewById(R.id.tv_kg_s);
            ivVoucher = itemView.findViewById(R.id.iv_voucher);
            tvOpenPrice = itemView.findViewById(R.id.tv_open_price);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            tvOpenFee = itemView.findViewById(R.id.tv_open_fee);
            tvOpenDw = itemView.findViewById(R.id.tv_open_dw);
            ivHoldFee = itemView.findViewById(R.id.iv_hold_fee);
            llContentOne = itemView.findViewById(R.id.ll_content_one);
            tvFloatPoint = itemView.findViewById(R.id.tv_float_point);
            tvOpenTime = itemView.findViewById(R.id.tv_open_time);
            llContentTwo = itemView.findViewById(R.id.ll_content_two);
            tvFloatPrice = itemView.findViewById(R.id.tv_float_price);
            tvTargetProfit = itemView.findViewById(R.id.tv_target_profit);
            tvTargetStop = itemView.findViewById(R.id.tv_target_stop);
            llTarget = itemView.findViewById(R.id.ll_target);
            tvClose = itemView.findViewById(R.id.tv_close);
            vMaxLine = itemView.findViewById(R.id.v_max_line);
            llContentThr = itemView.findViewById(R.id.ll_content_thr);
        }
    }


    private OnOrderHoldNightListener nightListener;
    public interface OnOrderHoldNightListener{
        void onOrderHoldNight(String orderId,String orderNo,boolean holdNight);
    }
    public void setOnOrderHoldNightListener(OnOrderHoldNightListener listener){
        this.nightListener = listener;
    }
}
