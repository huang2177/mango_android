package com.paizhong.manggo.ui.paycenter.recharge;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.bean.zj.ChannelsZJBean;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.events.BankListEvent;
import com.paizhong.manggo.utils.CheckUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.BankCardTextWatcher;
import com.paizhong.manggo.widget.expand.ExpandableLayout;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zab on 2018/4/29 0029.
 */

public class PayModeAdapter extends BaseRecyclerAdapter<PayModeAdapter.ViewHolder, ChannelsZJBean> {

    private String mModeId;
    private String mChannel;
    private boolean mExpand;
    private int mBankPosition;
    private boolean isInitExpandLayout;

    private List<BankListBean> mList;

    public PayModeAdapter(Context context) {
        super(context);
        mBankPosition = -1;
        mList = new ArrayList<>();
    }

    //点击金额获取支付方式
    public void updateData(PictureZJBean pictureZJBean, List<ChannelsZJBean> channelsZJList){
        isInitExpandLayout = false;
        List<ChannelsZJBean> zjListTemp = new ArrayList<>();
        Iterator<ChannelsZJBean> iterator = channelsZJList.iterator();
        while (iterator.hasNext()){
            ChannelsZJBean channelsZJBean = iterator.next();
            if(pictureZJBean.type!=null){
                List<String> resultTemp = Arrays.asList(pictureZJBean.type.split(","));
                for (int i = 0; i < resultTemp.size(); i++) {
                    String temp = "" + resultTemp.get(i);
                    if(TextUtils.equals("quick_h5", channelsZJBean.channel)&&TextUtils.equals("0", temp)){
                        zjListTemp.add(channelsZJBean);
                    }
                    if(TextUtils.equals("alipay_qr", channelsZJBean.channel)&&TextUtils.equals("1", temp)){
                        zjListTemp.add(channelsZJBean);
                    }
                    if(TextUtils.equals("qq_qr", channelsZJBean.channel)&&TextUtils.equals("2", temp)){
                        zjListTemp.add(channelsZJBean);
                    }
                    if(TextUtils.equals("wx_h5", channelsZJBean.channel)&&TextUtils.equals("3", temp)){
                        zjListTemp.add(channelsZJBean);
                    }
                }
            }
        }
        if(zjListTemp.size()>0){
            this.mModeId = zjListTemp.get(0).id;
            this.mChannel = zjListTemp.get(0).channel;
        }else {
            clearPayData();
        }
        notifyDataChanged(true,zjListTemp);
    }

//    private boolean mAlPay = false;
//    public void changePay(boolean alPay){
//        if (mAlPay !=alPay && data !=null && data.size() > 0){
//            ChannelsZJBean channelsZJBean = data.get(0);
//            if (TextUtils.equals("alipay_qr",channelsZJBean.channel)){
//                this.mAlPay = alPay;
//                this.mModeId = data.get(data.size()-1).id;
//                this.mChannel = data.get(data.size()-1).channel;
//                notifyDataSetChanged();
//            }
//        }
//    }

//    public void setChangePay(boolean alPay){
//        this.mAlPay = alPay;
//    }

    public void clearPayData(){
        this.mModeId = "";
        this.mChannel = "";
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pay_mode_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, ChannelsZJBean channelsBean) {
        if (TextUtils.isEmpty(mModeId)) {
            if (channelsBean.is_disable == 0) {
                this.mModeId = channelsBean.id;
                this.mChannel = channelsBean.channel;
                holder.iv_u_pay.setSelected(true);
            } else {
                holder.iv_u_pay.setSelected(false);
            }
        } else if (TextUtils.equals(channelsBean.id, mModeId)) {
            this.mModeId = channelsBean.id;
            this.mChannel = channelsBean.channel;
            holder.iv_u_pay.setSelected(true);
        } else {
            holder.iv_u_pay.setSelected(false);
        }

        if (TextUtils.equals("alipay_qr", channelsBean.channel)) {
            //holder.mRlPay.setVisibility(mAlPay ? View.INVISIBLE : View.VISIBLE);
            holder.iv_img.setImageResource(R.mipmap.ic_ali_pay);
            holder.mExpandableLayout.collapse();
        } else if (TextUtils.equals("wx_h5", channelsBean.channel)) {
            //holder.mRlPay.setVisibility(View.VISIBLE);
            holder.iv_img.setImageResource(R.mipmap.ic_wx_pay);
            holder.mExpandableLayout.collapse();
        } else if (TextUtils.equals("qq_qr", channelsBean.channel)) {
            //holder.mRlPay.setVisibility(View.VISIBLE);
            holder.iv_img.setImageResource(R.mipmap.ic_qq_pay);
            holder.mExpandableLayout.collapse();
        } else {
            //holder.mRlPay.setVisibility(View.VISIBLE);
            mBankPosition = position;
            holder.iv_img.setImageResource(R.mipmap.ic_u_pay);
            if (!isInitExpandLayout) {
                doExpandState(false);
            }
        }
        ViewHelper.safelySetText(holder.tv_name, channelsBean.name);
    }


    /**
     * 改变充值方式
     *
     * @param modeId
     */
    public void changeID(String modeId,String channel) {
        if (data.size() > 0) {
            this.mModeId = modeId;
            this.mChannel = channel;
            notifyDataSetChanged();
        }
    }

    public String getModeId() {
        return mModeId;
    }

    public String getChannel(){
        return mChannel;
    }

    public boolean isExpand() {
        return mExpand;
    }

    /**
     * 设置银行卡列表数据
     *
     * @param list
     */
    public void setBankList(List<BankListBean> list) {
        this.mList = list;
    }

    public void bindAddBank(String account) {
        if (mList.size() >= 3) {
            return;
        }
        BankListBean bankListBean = new BankListBean(account);
        ViewHolder holder = mHolders.get(mBankPosition);
        mList.add(bankListBean);

        mExpand = true;
        doExpandState(mExpand);
        holder.changeFlag(mExpand);
        holder.mEditText.setText("");
        DeviceUtils.hintKeyboard((Activity) mContext);
    }

    public void bindDeleteBank(int position) {
        if (mList.size() > position) {
            mList.remove(position);
            mHolders.get(mBankPosition).mBankAdapter.bindDeleteBank(position);
            doExpandState(!isInitExpandLayout || isExpand());
        }
    }

    /**
     * 改变银行卡列表展开 收起状态
     *
     * @param isExpand 是否需要展开全部银行卡
     */
    public void doExpandState(boolean isExpand) {
        ViewHolder holder = mHolders.get(mBankPosition);
        if (mBankPosition == -1 || mList == null) {
            return;
        }
        holder.mExpandableLayout.expand();
        holder.tvFlag.setClickable(!mList.isEmpty());
        if (mList.isEmpty()) {
            mExpand = false;
            holder.changeFlag(mExpand);
            holder.mBankAdapter.clearData();
            holder.mInputLayout.setVisibility(View.VISIBLE);
            return;
        } else if (mList.size() < 3 && isExpand) {
            holder.mBankAdapter.addData(mList);
            holder.mInputLayout.setVisibility(View.VISIBLE);
        } else if (mList.size() == 3 && isExpand) {
            holder.mBankAdapter.addData(mList);
            holder.mInputLayout.setVisibility(View.GONE);
        } else {
            holder.mInputLayout.setVisibility(View.GONE);
            holder.mBankAdapter.addData(Collections.singletonList(mList.get(0)));
        }
        isInitExpandLayout = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements BankListAdapter.SimpleOnSelectListener {

        //View mRlPay;
        ImageView iv_img;
        EditText mEditText;
        ImageView iv_u_pay;
        TextView tv_name, tvFlag, tvInput;

        LinearLayout mInputLayout;
        RecyclerView mRecyclerView;
        ExpandableLayout mExpandableLayout;

        BankListAdapter mBankAdapter;

        public ViewHolder(final View itemView) {
            super(itemView);
            //mRlPay = itemView.findViewById(R.id.rl_pay);
            tvInput = itemView.findViewById(R.id.tv_input);
            mInputLayout = itemView.findViewById(R.id.input_root);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tvFlag = (TextView) itemView.findViewById(R.id.tv_flag);
            mEditText = itemView.findViewById(R.id.edit_bank_input);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_u_pay = (ImageView) itemView.findViewById(R.id.iv_u_pay);
            mRecyclerView = itemView.findViewById(R.id.bank_card_recycle);
            mExpandableLayout = itemView.findViewById(R.id.expandable_layout);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(manager);
            mBankAdapter = new BankListAdapter(mContext);
            mBankAdapter.setListener(this);
            mRecyclerView.setAdapter(mBankAdapter);

            BankCardTextWatcher.bind(mEditText);

            tvInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    String cardNum = mEditText.getText().toString().replace(String.valueOf(' '), "");
                    if (CheckUtils.checkBankCard(cardNum)) {
                        EventBus.getDefault().post(new BankListEvent(1, cardNum));
                    } else {
                        AppToast.show("请输入正确的银行卡号码！");
                    }
                }
            });

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String cardNum = mEditText.getText().toString().replace(String.valueOf(' '), "");
                    if (CheckUtils.checkBankCard(cardNum)) {
                        tvInput.setTextColor(ContextCompat.getColor(mContext, R.color.color_008EFF));
                        tvInput.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_bank_card_orange));
                    } else {
                        tvInput.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
                        tvInput.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_bank_card_gray1));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tvFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    mExpand = !mExpand;
                    onItemClick();
                    changeFlag(mExpand);
                    doExpandState(mExpand);
                }

            });
        }

        private void changeFlag(boolean expand) {
            tvFlag.setText(expand ? "收起" : "展开");
            tvFlag.setCompoundDrawablesWithIntrinsicBounds(0, 0
                    , expand ? R.mipmap.ic_arrow_up : R.mipmap.ic_arrow_down, 0);
        }

        /**
         * 点击Item其他控件时，也将该Item选中
         */
        @Override
        public void onItemClick() {
            if (onItemClick != null) {
                onItemClick.onItemClick(itemView, getAdapterPosition());
            }
        }
    }
}
