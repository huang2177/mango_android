package com.paizhong.manggo.dialog.market;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import java.util.List;


/**
 * Created by zab on 2018/9/5 0005.
 */
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder>{


    private int mPosition;
    private String mProductID;
    private Activity context;
    private List<MarketHQBean> marketList;

    public MarketAdapter(Activity context){
        this.context = context;
    }


    public void setProductID(String productID){
        this.mProductID = productID;
    }


    public void notifyDataChanged(List<MarketHQBean> marketList){
        this.marketList = marketList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return marketList !=null ? marketList.size() : 0;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_market_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MarketHQBean marketHQBean = marketList.get(position);
        holder.tvProductName.setText(marketHQBean.name);
        holder.tvFloatPoint.setText(marketHQBean.point);
        holder.tvProductTag.setText(marketHQBean.code);
        double v = NumberUtil.getDouble(marketHQBean.change);

        if (v > 0){
            holder.tvPriceHighest.setText("+" + marketHQBean.change +" +"+marketHQBean.changeRate);
            holder.tvProductName.setTextColor(AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context,R.color.color_ffffff) : ContextCompat.getColor(context,R.color.color_333333));
            int color = ContextCompat.getColor(context, R.color.color_F74F54);
            holder.tvFloatPoint.setTextColor(color);
            holder.tvPriceHighest.setTextColor(color);
        }else if (v < 0){
            holder.tvPriceHighest.setText(marketHQBean.change +" "+marketHQBean.changeRate);
            holder.tvProductName.setTextColor(AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context,R.color.color_ffffff) : ContextCompat.getColor(context,R.color.color_333333));
            int color = ContextCompat.getColor(context, R.color.color_1AC47A);
            holder.tvFloatPoint.setTextColor(color);
            holder.tvPriceHighest.setTextColor(color);
        }else {
            holder.tvPriceHighest.setText(marketHQBean.change +" "+marketHQBean.changeRate);
            int color = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context,R.color.color_ffffff) : ContextCompat.getColor(context,R.color.color_333333);
            holder.tvProductName.setTextColor(color);
            holder.tvFloatPoint.setTextColor(color);
            holder.tvPriceHighest.setTextColor(color);
        }

        if (TextUtils.equals(marketHQBean.remark,mProductID)){
            this.mPosition = position;
            int color = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context, R.color.color_1F1C28) : ContextCompat.getColor(context, R.color.color_f0f0f0);
            holder.llRootView.setBackgroundColor(color);
            holder.vLine.setBackgroundColor(color);
        }else {
            holder.llRootView.setBackgroundColor(0);
            holder.vLine.setBackgroundColor(AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context, R.color.color_414C5E) : ContextCompat.getColor(context, R.color.color_f0f0f0));
        }

        holder.llRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick() && position !=mPosition){
                    int mLPosition = mPosition;
                    mProductID = marketHQBean.remark;
                    mPosition = position;
                    holder.llRootView.setBackgroundColor(AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(context, R.color.color_1F1C28) : ContextCompat.getColor(context, R.color.color_f0f0f0));
                    notifyItemChanged(mLPosition);
                    ((KLineMarketActivity)context).setQHProduct(marketHQBean.remark,marketHQBean.close,marketHQBean.name);
                    if (onItemListener !=null){
                        onItemListener.onOnItem();
                    }
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         View llRootView;
         View vLine;
         TextView tvProductName;
         TextView tvFloatPoint;
         TextView tvProductTag;
         TextView tvPriceHighest;

        public ViewHolder(View itemView) {
            super(itemView);
            llRootView = itemView.findViewById(R.id.ll_root_view);
            vLine = itemView.findViewById(R.id.v_line);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvFloatPoint = itemView.findViewById(R.id.tv_float_point);
            tvProductTag = itemView.findViewById(R.id.tv_product_tag);
            tvPriceHighest = itemView.findViewById(R.id.tv_price_highest);
        }
    }


    private OnItemListener onItemListener;
    public void setOnItemListener(OnItemListener itemListener){
        this.onItemListener = itemListener;
    }
    public interface OnItemListener{
        void onOnItem();
    }
}
