package com.paizhong.manggo.dialog.position;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.utils.DeviceUtils;

import java.util.List;


/**
 * Created by zab on 2018/6/19 0019.
 */
public class PositionDgAdapter extends RecyclerView.Adapter<PositionDgAdapter.ViewHolder>{


    private ArrayMap<String,Boolean> checkMap = new ArrayMap<>();
    private Context mContext;
    private List<PositionListBean> orderZjList;
    public PositionDgAdapter(Context context){
        this.mContext = context;
    }

    public void notifyDataChanged(List<PositionListBean> orderZjList){
        this.orderZjList = orderZjList;
        notifyDataSetChanged();
    }


    public boolean getCheck(String key){
        if (checkMap.containsKey(key)){
            return checkMap.get(key);
        }else {
            return false;
        }
    }


    public ArrayMap<String,Boolean> getCheckMap(){
        return checkMap;
    }

    public List<PositionListBean> getList(){
        return orderZjList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_position_dg_layout, parent,false));
    }

    @Override
    public int getItemCount() {
        return orderZjList !=null ? orderZjList.size() : 0;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
       final PositionListBean zjBean = orderZjList.get(position);
       //0 涨  1跌
        holder.tvZd.setSelected(zjBean.flag == 0);
        holder.tvZd.setText(zjBean.flag == 0 ? R.string.text9 : R.string.text10);

        holder.tvKgS.setText(zjBean.name);

        if (!TextUtils.isEmpty(zjBean.coupon_id)){
            holder.ivVoucher.setVisibility(View.VISIBLE);
        }else {
            holder.ivVoucher.setVisibility(View.GONE);
        }

        holder.tvJcPrice.setText(zjBean.open_price);

        holder.tvSelect.setSelected(zjBean.check);

        holder.tvPrice.setText(zjBean.floatingPrice+"");
        if (zjBean.floatingPrice > 0){
            holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
        }else if (zjBean.floatingPrice < 0){
            holder.tvPrice.setTextColor( ContextCompat.getColor(mContext, R.color.color_1AC47A));
        }else {
            holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
        }

        holder.tvJcTime.setText(zjBean.create_time);

        holder.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (holder.tvSelect.isSelected()){
                    holder.tvSelect.setSelected(false);
                    checkMap.put(zjBean.id,false);
                    holder.tvSelect.setSelected(false);
                    if (checkChangeListener !=null){
                        checkChangeListener.onCheckChange();
                    }
                }else {
                    holder.tvSelect.setSelected(true);
                    checkMap.put(zjBean.id,true);
                    holder.tvSelect.setSelected(true);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvZd;
        TextView tvKgS;
        ImageView ivVoucher;
        TextView tvJcPrice;
        TextView tvSelect;
        TextView tvPrice;
        TextView tvJcTime;
        public ViewHolder(View view) {
            super(view);
            tvZd = view.findViewById(R.id.tv_zd);
            tvKgS =  view.findViewById(R.id.tv_kg_s);
            ivVoucher = view.findViewById(R.id.iv_voucher);
            tvJcPrice =  view.findViewById(R.id.tv_jc_price);
            tvSelect = view.findViewById(R.id.tv_select);
            tvPrice = view.findViewById(R.id.tv_price);
            tvJcTime = view.findViewById(R.id.tv_jc_time);
        }
    }

    public OnCheckChangeListener checkChangeListener;
    public void setCheckChangeListener(OnCheckChangeListener listener){
        this.checkChangeListener = listener;
    }
    public interface OnCheckChangeListener{
        void onCheckChange();
    }
}
