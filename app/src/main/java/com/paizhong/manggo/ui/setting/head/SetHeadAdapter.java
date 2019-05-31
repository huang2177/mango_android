package com.paizhong.manggo.ui.setting.head;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.other.HeadImageBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.CircleImageView;

import java.util.List;

/**
 * Created by zab on 2018/7/5 0005.
 */
public class SetHeadAdapter extends RecyclerView.Adapter<SetHeadAdapter.ViewHolder>{

    private List<HeadImageBean> images;
    private Context mContext;

    public SetHeadAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return images !=null ? images.size() : 0;
    }


    public void notifyDataChanged(List<HeadImageBean> images){
        this.images = images;
        notifyDataSetChanged();
    }


    public int mPosition = -1;
    public String mImageUrl;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_set_head_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         final HeadImageBean imageBean = images.get(position);
         ImageUtils.display(imageBean.images,holder.civHead,R.mipmap.other_empty);
         if (mPosition == position){
             holder.vCheckbox.setSelected(true);
         }else {
             holder.vCheckbox.setSelected(false);
         }
         holder.llRootView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()){
                    if (holder.vCheckbox.isSelected()){
                        holder.vCheckbox.setSelected(false);
                        int post = mPosition;
                        mPosition = -1;
                        mImageUrl = "";
                        if (post !=-1 && post < getItemCount()){
                            notifyItemChanged(post);
                        }
                    }else {
                        holder.vCheckbox.setSelected(true);
                        int post = mPosition;
                        mPosition = position;
                        mImageUrl = imageBean.images;
                        if (post !=-1 && post < getItemCount()){
                            notifyItemChanged(post);
                        }
                    }
                }
             }
         });
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView civHead;
        View vCheckbox;
        View llRootView;
        public ViewHolder(View itemView) {
            super(itemView);
            civHead = (CircleImageView)itemView.findViewById(R.id.civ_head);
            vCheckbox = itemView.findViewById(R.id.v_checkbox);
            llRootView = itemView.findViewById(R.id.ll_root_view);
        }
    }
}
