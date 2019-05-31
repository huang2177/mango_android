package com.paizhong.manggo.ui.voucher;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.zj.VoucherKyZJBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.widget.recycle.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/6/28 0028.
 */
public class VoucherZjAdapter extends RecyclerView.Adapter<VoucherZjAdapter.ViewHolder>{

    private Context mContext;
    private List<VoucherKyZJBean> mVoucherList;
    private String mVoucherType;

    public VoucherZjAdapter(Context context){
        this.mContext = context;
        this.mVoucherList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_aipei_ky_voucher_layout, parent,false));
    }

    @Override
    public int getItemCount() {
        return mVoucherList !=null ? mVoucherList.size() : 0;
    }

    public void clearData(){
        this.mVoucherList.clear();
        notifyDataSetChanged();
    }

    public void notifyDataChanged(String voucherType,boolean  isRefresh,List<VoucherKyZJBean> voucherList){
        this.mVoucherType = voucherType;
        if (isRefresh){
          this.mVoucherList.clear();
          if (voucherList !=null && voucherList.size() >0){
              this.mVoucherList.addAll(voucherList);
          }
        }else {
            if (voucherList !=null && voucherList.size() >0){
                this.mVoucherList.addAll(voucherList);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (TextUtils.equals(ViewConstant.VOUCHER_TAB_GQ,mVoucherType)){
            VoucherKyZJBean bean = mVoucherList.get(position);
            holder.llRootView.setBackgroundResource(R.mipmap.bg_gq_voucher);
            holder.flGqImg.setVisibility(View.VISIBLE);
            if (TextUtils.equals("已使用",bean.status)){
                holder.ivImg.setImageResource(R.mipmap.ic_sy_voucher);
            }else {
                holder.ivImg.setImageResource(R.mipmap.ic_gq_voucher);
            }
            holder.tvBtn.setVisibility(View.GONE);
            holder.tvTimeTitle.setText(R.string.voucher_gq);
            holder.tvMoney.setText(String.valueOf(bean.amount));
            holder.tvTime.setText(bean.end_time);
        }else {
            VoucherKyZJBean bean = mVoucherList.get(position);
            holder.llRootView.setBackgroundResource(R.mipmap.bg_ky_voucher);
            holder.flGqImg.setVisibility(View.GONE);
            holder.tvBtn.setVisibility(View.VISIBLE);
            holder.tvTimeTitle.setText(R.string.voucher_yx);
            holder.tvMoney.setText(String.valueOf(bean.amount));
            holder.tvTime.setText(bean.end_time);
            holder.tvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("page",MainActivity.TRADE_PAGE);
                    intent.putExtra("smPage",0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMoney;
        TextView tvTime;
        TextView tvTimeTitle;
        TextView tvBtn;
        View flGqImg;
        View llRootView;
        ImageView ivImg;
        public ViewHolder(View view) {
            super(view);
            tvMoney = (TextView) view.findViewById(R.id.tv_money);
            tvTimeTitle = view.findViewById(R.id.tv_time_title);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvBtn = (TextView) view.findViewById(R.id.tv_btn);
            flGqImg = view.findViewById(R.id.fl_gq_img);
            ivImg = view.findViewById(R.id.iv_img);
            llRootView = view.findViewById(R.id.ll_root_view);
        }
    }

    private String getTitme(String time){
        try {
            time =  time.substring(0,time.lastIndexOf("-")+3);
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }
}
