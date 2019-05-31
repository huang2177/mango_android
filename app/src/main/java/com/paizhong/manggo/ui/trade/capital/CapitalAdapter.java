package com.paizhong.manggo.ui.trade.capital;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.trade.CapitalListBean;
import com.paizhong.manggo.config.ViewConstant;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by zab on 2018/4/27 0027.
 */

public class CapitalAdapter extends BaseAdapter{

    private Context mContext;
    private List<CapitalListBean> mList;

    public CapitalAdapter(Context context){
        this.mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList !=null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        int tabSelect = mList.get(position).tabSelect;
        if (ViewConstant.SIMP_TAB_ITEM_ZERO == tabSelect){
            return ViewConstant.SIMP_TAB_ITEM_ZERO;

        }else if (ViewConstant.SIMP_TAB_ITEM_ONE == tabSelect){
            return ViewConstant.SIMP_TAB_ITEM_ONE;
        }else {
            return ViewConstant.SIMP_TAB_ITEM_TWO;
        }
    }


    public void notifyDataChanged(boolean mRefresh,List<CapitalListBean> listBeans){
        if (mRefresh){
            mList.clear();
            if (listBeans !=null && listBeans.size() > 0){
                mList.addAll(listBeans);
            }
            notifyDataSetChanged();
        }else {
            if (listBeans !=null && listBeans.size() >0){
                mList.addAll(listBeans);
                notifyDataSetChanged();
            }
        }
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View ret = null;
        int itemViewType = getItemViewType(position);
        TradeRecordViewHolder recordViewHolder;
        OhterViewHolder txViewHolder;
        OhterViewHolder czViewHolder;


        if (view !=null){
            ret = view;
        }else {
            switch (itemViewType){
                case ViewConstant.SIMP_TAB_ITEM_ZERO:
                    ret = LayoutInflater.from(mContext).inflate(R.layout.item_trade_record_layout, null);
                    break;
                case ViewConstant.SIMP_TAB_ITEM_ONE:
                    ret = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_record_layout, null);
                    break;
                case ViewConstant.SIMP_TAB_ITEM_TWO:
                    ret = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_record_layout, null);
                    break;
            }
        }

        switch (itemViewType){
            case ViewConstant.SIMP_TAB_ITEM_ZERO: //交易记录
                recordViewHolder = (TradeRecordViewHolder) ret.getTag();
                if (recordViewHolder == null){
                    recordViewHolder = new TradeRecordViewHolder(ret);
                    ret.setTag(recordViewHolder);
                }

                CapitalListBean record = mList.get(position);
                bindTradeRecord(recordViewHolder,record);
                break;
            case ViewConstant.SIMP_TAB_ITEM_ONE: //提现记录
                txViewHolder = (OhterViewHolder) ret.getTag();
                if (txViewHolder == null){
                    txViewHolder = new OhterViewHolder(ret);
                    ret.setTag(txViewHolder);
                }

                CapitalListBean txBean = mList.get(position);
                txOhter(txViewHolder,txBean);
                break;
            case ViewConstant.SIMP_TAB_ITEM_TWO: //充值记录
                 czViewHolder = (OhterViewHolder) ret.getTag();
                if (czViewHolder == null){
                    czViewHolder = new OhterViewHolder(ret);
                    ret.setTag(czViewHolder);
                }
                CapitalListBean czBean = mList.get(position);
                czOhter(czViewHolder,czBean);
                break;
        }
        return ret;
    }



    //交易记录
    public void bindTradeRecord(final TradeRecordViewHolder holder, final CapitalListBean listBean){

        holder.tvUpDown.setSelected(listBean.flag == 0);
        holder.tvUpDown.setText(listBean.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvName.setText(listBean.product_name);
        holder.tvPrice.setText(listBean.amount);
        holder.tvTime.setText(listBean.close_time);
        if (TextUtils.isEmpty(listBean.profit) || TextUtils.isEmpty(listBean.lossLimit)){
            holder.llProfitLimit.setVisibility(View.GONE);
            holder.lLossLimit.setVisibility(View.GONE);
        }else {
            holder.llProfitLimit.setVisibility(View.VISIBLE);
            holder.lLossLimit.setVisibility(View.VISIBLE);
            holder.tvProfitLimit.setText(String.format("%s%%", listBean.profit));
            holder.tvLossLimit.setText(String.format("%s%%", listBean.lossLimit));
        }
        if (!TextUtils.isEmpty(listBean.coupon_id)){
            holder.ivVoucher.setVisibility(View.VISIBLE);
        }else {
            holder.ivVoucher.setVisibility(View.GONE);
        }

        double close_profit = Double.parseDouble(listBean.close_profit);
        holder.tvDianwei.setText(listBean.close_profit);
        if (close_profit > 0){
            holder.tvDianwei.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
        }else if (close_profit < 0){
            holder.tvDianwei.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
        }else {
            holder.tvDianwei.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        }


        holder.tvJcprice.setText(listBean.open_price);
        holder.tvJcchengben.setText(String.format("%s元", listBean.amount));
        holder.tvGysxf.setText(String.format("%s元", listBean.hold_fee));
        holder.tvJctiem.setText(listBean.open_time);

        holder.tvPcprice.setText(listBean.close_price);
        holder.tvJcsxf.setText(String.format("%s元", listBean.fee));
        holder.tvPctype.setText(listBean.typeName);
        holder.tvPctime.setText(listBean.close_time);



        if (listBean.iOpen){
            holder.llBottomRoot.setVisibility(View.VISIBLE);
            holder.vLine.setVisibility(View.VISIBLE);
            holder.ivArrow.setSelected(true);
        }else {
            holder.llBottomRoot.setVisibility(View.GONE);
            holder.ivArrow.setSelected(false);
            holder.vLine.setVisibility(View.GONE);
        }

        holder.llOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ivArrow.isSelected()){
                    listBean.iOpen = false;
                    holder.ivArrow.setSelected(false);
                    holder.llBottomRoot.setVisibility(View.GONE);
                    holder.vLine.setVisibility(View.GONE);
                }else {
                    listBean.iOpen = true;
                    holder.ivArrow.setSelected(true);
                    holder.llBottomRoot.setVisibility(View.VISIBLE);
                    holder.vLine.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    //提现记录
    public void txOhter(OhterViewHolder holder,CapitalListBean listBean){
       if (listBean.status == 0){
            holder.ivTradeState.setImageResource(R.mipmap.ic_c_g);
       }else if (listBean.status == 1 || listBean.status == 5){
            holder.ivTradeState.setImageResource(R.mipmap.ic_c_r);
       }else {
            holder.ivTradeState.setImageResource(R.mipmap.ic_c_y);
       }
        if (!TextUtils.isEmpty(listBean.typeName)){
            holder.tvTradeState.setText(listBean.typeName);
        }
        holder.tvTime.setText(listBean.create_time);
        holder.tvMoney.setText(listBean.amount);
    }

     //充值记录
    public void czOhter(OhterViewHolder holder,CapitalListBean listBean){
        if (listBean.status == 0){
            holder.ivTradeState.setImageResource(R.mipmap.ic_c_g);
        }else {
            holder.ivTradeState.setImageResource(R.mipmap.ic_c_y);
        }
        if (!TextUtils.isEmpty(listBean.typeName)){
            holder.tvTradeState.setText(listBean.typeName);
        }
        holder.tvTime.setText(listBean.create_time);
        holder.tvMoney.setText(listBean.amount);
    }



    static class TradeRecordViewHolder{
        TextView tvUpDown;
        TextView tvName;
        TextView tvPrice;
        View ivVoucher;
        TextView tvDianwei;
        ImageView ivArrow;
        TextView tvTime;
        TextView tvJcprice;
        TextView tvJcchengben;
        TextView tvGysxf;
        TextView tvJctiem;
        TextView tvPcprice;
        TextView tvJcsxf;
        TextView tvPctype;
        TextView tvPctime;
        TextView tvProfitLimit;
        TextView tvLossLimit;
        View llProfitLimit;
        View lLossLimit;
        View vLine;
        View llBottomRoot;
        View llOpen;

          public TradeRecordViewHolder(View view){
              llOpen = view.findViewById(R.id.ll_open);
              tvUpDown = (TextView)view.findViewById(R.id.tv_up_down);
              tvName = (TextView)view.findViewById(R.id.tv_name);
              tvPrice = (TextView)view.findViewById(R.id.tv_price);
              ivVoucher = view.findViewById(R.id.iv_voucher);
              tvDianwei = (TextView)view.findViewById(R.id.tv_dianwei);
              ivArrow = (ImageView)view.findViewById(R.id.iv_arrow);
              tvTime = (TextView)view.findViewById(R.id.tv_time);
              tvJcprice = (TextView)view.findViewById(R.id.tv_jcprice);
              tvJcchengben = (TextView)view.findViewById(R.id.tv_jcchengben);
              tvGysxf = (TextView)view.findViewById(R.id.tv_gysxf);
              tvJctiem = (TextView)view.findViewById(R.id.tv_jctiem);
              tvPcprice = (TextView)view.findViewById(R.id.tv_pcprice);
              tvJcsxf = (TextView)view.findViewById(R.id.tv_jcsxf);
              tvPctype = (TextView)view.findViewById(R.id.tv_pctype);
              tvPctime = (TextView)view.findViewById(R.id.tv_pctime);
              tvProfitLimit = view.findViewById(R.id.tv_profitLimit);
              tvLossLimit = view.findViewById(R.id.tv_lossLimit);
              llProfitLimit = view.findViewById(R.id.ll_profitLimit);
              lLossLimit = view.findViewById(R.id.ll_lossLimit);
              vLine = view.findViewById(R.id.v_lien);
              llBottomRoot = view.findViewById(R.id.ll_bottom_root);
          }
    }


    static class OhterViewHolder{
        ImageView ivTradeState;
        TextView tvTradeState;
        TextView tvTime;
        TextView tvMoney;

        public OhterViewHolder(View view){
            ivTradeState = (ImageView)view.findViewById(R.id.iv_trade_state);
            tvTradeState = (TextView)view.findViewById(R.id.tv_trade_state);
            tvTime = (TextView)view.findViewById(R.id.tv_time);
            tvMoney = (TextView)view.findViewById(R.id.tv_money);
        }
    }
}
