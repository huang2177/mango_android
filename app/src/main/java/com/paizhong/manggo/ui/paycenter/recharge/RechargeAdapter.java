package com.paizhong.manggo.ui.paycenter.recharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;

import java.util.List;

/**
 * Created by zab on 2018/4/8 0008.
 */

public class RechargeAdapter extends BaseAdapter {
    private Context mContext;
    private List<PictureZJBean> mList;

    private int mSelectPosition = -1;

    private int width;
    private int height;
    public RechargeAdapter(Context context,int widthPixels) {
        this.mContext = context;
        int dimensionPixelSize = mContext.getResources().getDimensionPixelSize(R.dimen.app_margin);
        width = (widthPixels - dimensionPixelSize) / 4;
        height = (int) (width * 0.609);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void notifyDataChanged(List<PictureZJBean> list){
        this.mSelectPosition = list !=null && list.size() > 3 ? 3 : 0;
        this.mList = list;
        notifyDataSetChanged();
    }


    public void changeMoney(int selectPosition){
        if (mList !=null && mList.size() > 0){
            if (selectPosition < getCount()){
                this.mSelectPosition = selectPosition;
            }else {
                this.mSelectPosition = 0;
            }
            notifyDataSetChanged();
        }
    }


    private int mAmount = -1;
    public int getAmount(){
        return mAmount == -1 ? mAmount : mAmount;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_present_price_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        PictureZJBean zjBean = mList.get(position);
        if (mSelectPosition == position){
            this.mAmount = zjBean.chargeAmount;
            ImageUtils.display(zjBean.selectPicture,holder.ivSelect,R.mipmap.other_empty);
        }else {
            ImageUtils.display(zjBean.chargePicture,holder.ivSelect,R.mipmap.other_empty);
        }
        return view;
    }

     class ViewHolder {
        ImageView ivSelect;
        ViewHolder(View view) {
            ivSelect = (ImageView) view.findViewById(R.id.iv_select);
            ViewGroup.LayoutParams layoutParams = ivSelect.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            ivSelect.setLayoutParams(layoutParams);
        }
    }
}
