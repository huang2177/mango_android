package com.paizhong.manggo.ui.trade.openposition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.other.ChatMsgBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zab on 2018/11/26 0026.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    private BaseActivity mContext;
    private List<ChatMsgBean> list;
    private Long mDateTime;
    private boolean mGM = false;

    public ChatAdapter(BaseActivity context){
        this.mContext = context;
        this.list = new ArrayList<>();
    }

    public ChatAdapter(BaseActivity context ,Long mDateTime ,List<ChatMsgBean> chatMsgBeans){
        this.mContext = context;
        this.mDateTime = mDateTime;
        this.list = chatMsgBeans;
    }

    public void setGm(boolean gm){
        this.mGM = gm;
    }

    public void notifyDataChanged(ChatMsgBean chatMsgBean){
        if (list !=null){
            this.mDateTime = chatMsgBean.dateTime;
            if (list.size()< 10){
                list.add(chatMsgBean);
            }else {
                list.remove(0);
                list.add(list.size(),chatMsgBean);
            }
            notifyDataSetChanged();
        }
    }

    public List<ChatMsgBean> getList() {
        return list;
    }

    public ChatMsgBean getChatMsgBean(){
        return getItemCount() > 0 ? list.get(list.size()-1) : null;
    }

    public Long getmDateTime() {
        return mDateTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_layout, parent, false));
    }


    @Override
    public int getItemCount() {
        return list !=null ? list.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatMsgBean chatMsgBean = list.get(position);
        if (!TextUtils.isEmpty(chatMsgBean.content)){
            holder.tvContent.setText(chatMsgBean.content);
        }
        if (mDateTime !=null && chatMsgBean.dateTime !=null){
            holder.tvTime.setText(TimeUtil.getFriendTimeOffer(mDateTime - chatMsgBean.dateTime));
        }else {
            holder.tvTime.setText("");
        }
        if (!TextUtils.isEmpty(chatMsgBean.phone) && TextUtils.equals(chatMsgBean.phone, AppApplication.getConfig().getMobilePhone())){
            holder.tvVipUser.setVisibility(View.GONE);
            holder.tvContent.setSelected(true);
            holder.tvName.setText("我");
        }else {
            holder.tvContent.setSelected(false);
            if (!TextUtils.isEmpty(chatMsgBean.userName)){
                holder.tvName.setText(chatMsgBean.userName);
            }
            if (!mGM){
                holder.tvVipUser.setVisibility(View.GONE);
            }else {
                holder.tvVipUser.setVisibility(View.VISIBLE);
                holder.tvVipUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!DeviceUtils.isFastDoubleClick()){
                            OpenPositionFragment.getInstance().forbiddenUser(chatMsgBean.userName,chatMsgBean.phone);
                        }
                    }
                });

                holder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AndroidUtil.clipboard(mContext,chatMsgBean.userName+":"+chatMsgBean.phone);
                        return false;
                    }
                });
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        TextView tvVipUser;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvVipUser = itemView.findViewById(R.id.tv_vipUser);
        }
    }
}
