package com.paizhong.manggo.ui.paycenter.present;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ViewHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zab on 2018/4/8 0008.
 */

public class PresentAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankCardZjListBean.DataBean> mList;


    public PresentAdapter(Context context) {
        this.mContext = context;
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

    public List<BankCardZjListBean.DataBean> getList(){
        return mList;
    }

    public void notifyDataChanged(BankCardZjListBean listBean){
        this.mBankMobile = listBean.phone;
        this.mIdCard = listBean.idCard;
        this.mList = listBean.data;
        notifyDataSetChanged();
    }

    public String mBankID;   //卡id
    public String mBankNum;  //银行卡号
    public String mRealName; //用户名
    public String mBankMobile; //手机号
    public String mIdCard; //身份证
    public void setBankCard(String bankID,String mBankNum,String mRealName){
        this.mBankID = bankID;
        this.mBankNum = mBankNum;
        this.mRealName = mRealName;
    }

    public void clear(){
        this.mBankID = "";
        this.mBankNum = "";
        this.mRealName = "";
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_present_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        final BankCardZjListBean.DataBean dataBean = mList.get(position);
        if (!TextUtils.isEmpty(dataBean.account) && dataBean.account.length() > 5){
            holder.tvBankNameNum .setText(dataBean.account.substring(0,3)+" **** "+dataBean.account.substring(dataBean.account.length()-4,dataBean.account.length()));
        }else {
            ViewHelper.safelySetText(holder.tvBankNameNum,dataBean.account);
        }

        if (position == 0 && TextUtils.isEmpty(mBankID)){
            holder.cbCheckbox.setChecked(true);
            setBankCard(dataBean.id,dataBean.account,dataBean.real_name);
        }else if (TextUtils.equals(dataBean.id,mBankID)){
            holder.cbCheckbox.setChecked(true);
        }else {
            holder.cbCheckbox.setChecked(false);
        }

        holder.cbCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    setBankCard(dataBean.id,dataBean.account,dataBean.real_name);
                    notifyDataSetChanged();
                }
            }
        });

        //删除
        holder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!DeviceUtils.isFastDoubleClick()){
                   if (listener !=null){
                       listener.onDelBankCard(dataBean.id);
                   }
               }
            }
        });
        return view;
    }

    static class ViewHolder{
        @BindView(R.id.tv_bank_name_num)
        TextView tvBankNameNum;
        @BindView(R.id.tv_del)
        TextView tvDel;
        @BindView(R.id.cb_checkbox)
        CheckBox cbCheckbox;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


    public void setOnDelBankCardListener(OnDelBankCardListener listener){
        this.listener = listener;
    }
    private OnDelBankCardListener listener;
    public interface OnDelBankCardListener{
        void onDelBankCard(String id);
    }
}
