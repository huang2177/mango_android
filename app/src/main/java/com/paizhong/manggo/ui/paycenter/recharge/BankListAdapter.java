package com.paizhong.manggo.ui.paycenter.recharge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.events.BankListEvent;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.BankCardTextWatcher;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * Des：绑定银行卡列表 Adapter
 * Created by huang on 2018/8/29 0029 17:17
 */

public class BankListAdapter extends BaseRecyclerAdapter<BankListAdapter.ViewHolder, BankListBean> {

    private int mCheckedItem;

    public BankListAdapter(Context context) {
        super(context);
        mCheckedItem = -1;
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_list, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position, BankListBean data) {
        if (mCheckedItem == position) {
            holder.tvCopy.setText("已复制");
            holder.tvCopy.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            holder.tvCopy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_bank_card_orange1));
        } else {
            holder.tvCopy.setText("复制");
            holder.tvCopy.setTextColor(ContextCompat.getColor(mContext, R.color.color_008EFF));
            holder.tvCopy.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_bank_card_orange));
        }
        holder.mEditText.setText(hiddenText(data.account));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * 清除已复制的item标记
     *
     * @param position
     */
    public void bindDeleteBank(int position) {
        mCheckedItem = position == mCheckedItem ? -1 : mCheckedItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCopy;
        EditText mEditText;
        ImageView ivDelete;

        public ViewHolder(View itemView, final int position) {
            super(itemView);
            tvCopy = itemView.findViewById(R.id.tv_copy);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            mEditText = itemView.findViewById(R.id.edit_bank_num);

            BankCardTextWatcher.bind(mEditText);

            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    mCheckedItem = position;
                    notifyDataSetChanged();
                    putTextIntoClip(data.get(position).account);
                    if (mListener != null) {
                        mListener.onItemClick();
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick() || data.size() <= position) {
                        return;
                    }
                    EventBus.getDefault().post(new BankListEvent(2
                            , data.get(position).account
                            , position));
                    if (mListener != null) {
                        mListener.onItemClick();
                    }
                }
            });
        }
    }

    public void putTextIntoClip(String copyText) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", copyText);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
        }
    }

    /**
     * 隐藏银行卡中间数字
     */
    public String hiddenText(String source) {
        StringBuilder buffer = new StringBuilder();
        String s = source.replace(String.valueOf(' '), "");
        if (s.length() < 16) {
            for (int i = 0; i < s.length(); i++) {
                if (i >= 4 && i < 12) {
                    buffer.append("*");
                } else {
                    buffer.append(s.charAt(i));
                }
            }
        } else {
            for (int i = 0; i < s.length(); i++) {
                if (i >= 4 && i < 16) {
                    buffer.append("*");
                } else {
                    buffer.append(s.charAt(i));
                }
            }
        }
        return buffer.toString();
    }

    private SimpleOnSelectListener mListener;

    public interface SimpleOnSelectListener {
        void onItemClick();
    }

    public void setListener(SimpleOnSelectListener listener) {
        mListener = listener;
    }
}
