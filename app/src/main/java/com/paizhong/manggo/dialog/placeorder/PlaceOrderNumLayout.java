package com.paizhong.manggo.dialog.placeorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;

/**
 * Created by zab on 2018/7/6 0006.
 */
public class PlaceOrderNumLayout extends LinearLayout implements View.OnClickListener {

    private View mShouRootLayout;
    private View mShouLayout1;
    private TextView mTvShou1;
    private TextView mTvShouHint1;

    private View mShouLayout5;
    private TextView mTvShou5;
    private TextView mTvShouHint5;

    private View mShouLayout10;
    private TextView mTvShou10;
    private TextView mTvShouHint10;

    private View mShouLayoutQt;

    private View mExShouTopRootLayout;
    private View mExShouLayout1;
    private TextView mExShou1;
    private TextView mExShouHint1;

    private View mExShouLayout2;
    private TextView mExShou2;
    private TextView mExShouHint2;

    private View mExShouLayout3;
    private TextView mExShou3;
    private TextView mExShouHint3;

    private View mExShouLayout4;
    private TextView mExShou4;
    private TextView mExShouHint4;

    private View mExShouLayout5;
    private TextView mExShou5;
    private TextView mExShouHint5;

    private View mExShouBottomRootLayout;
    private View mExShouLayout6;
    private TextView mExShou6;
    private TextView mExShouHint6;

    private View mExShouLayout7;
    private TextView mExShou7;
    private TextView mExShouHint7;

    private View mExShouLayout8;
    private TextView mExShou8;
    private TextView mExShouHint8;

    private View mExShouLayout9;
    private TextView mExShou9;
    private TextView mExShouHint9;

    private View mExShouLayout10;
    private TextView mExShou10;
    private TextView mExShouHint10;

    public PlaceOrderNumLayout(Context context) {
        super(context);
    }

    public PlaceOrderNumLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceOrderNumLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mShouRootLayout = findViewById(R.id.ll_shou_layout);
        mShouLayout1 = findViewById(R.id.ll_shou_1);
        mTvShou1 = (TextView) findViewById(R.id.tv_shou_1);
        mTvShouHint1 = (TextView) findViewById(R.id.tv_shou_hint_1);

        mShouLayout5 = findViewById(R.id.ll_shou_5);
        mTvShou5 = (TextView) findViewById(R.id.tv_shou_5);
        mTvShouHint5 = (TextView) findViewById(R.id.tv_shou_hint_5);

        mShouLayout10 = findViewById(R.id.ll_shou_10);
        mTvShou10 = (TextView) findViewById(R.id.tv_shou_10);
        mTvShouHint10 = (TextView) findViewById(R.id.tv_shou_hint_10);

        mShouLayoutQt = findViewById(R.id.ll_shou_qita);
        TextView mTvShouQt = (TextView) findViewById(R.id.tv_shou_qita);

        mExShouTopRootLayout = findViewById(R.id.ll_ex_shou_top);

        mExShouLayout1 = findViewById(R.id.ll_ex_shou_1);
        mExShou1 = (TextView) findViewById(R.id.tv_ex_shou_1);
        mExShouHint1 = (TextView) findViewById(R.id.tv_ex_shou_hint_1);

        mExShouLayout2 = findViewById(R.id.ll_ex_shou_2);
        mExShou2 = (TextView) findViewById(R.id.tv_ex_shou_2);
        mExShouHint2 = (TextView) findViewById(R.id.tv_ex_shou_hint_2);

        mExShouLayout3 = findViewById(R.id.ll_ex_shou_3);
        mExShou3 = (TextView) findViewById(R.id.tv_ex_shou_3);
        mExShouHint3 = (TextView) findViewById(R.id.tv_ex_shou_hint_3);

        mExShouLayout4 = findViewById(R.id.ll_ex_shou_4);
        mExShou4 = (TextView) findViewById(R.id.tv_ex_shou_4);
        mExShouHint4 = (TextView) findViewById(R.id.tv_ex_shou_hint_4);

        mExShouLayout5 = findViewById(R.id.ll_ex_shou_5);
        mExShou5 = (TextView) findViewById(R.id.tv_ex_shou_5);
        mExShouHint5 = (TextView) findViewById(R.id.tv_ex_shou_hint_5);

        mExShouBottomRootLayout = findViewById(R.id.ll_ex_shou_bottom);

        mExShouLayout6 = findViewById(R.id.ll_ex_shou_6);
        mExShou6 = (TextView) findViewById(R.id.tv_ex_shou_6);
        mExShouHint6 = (TextView) findViewById(R.id.tv_ex_shou_hint_6);

        mExShouLayout7 = findViewById(R.id.ll_ex_shou_7);
        mExShou7 = (TextView) findViewById(R.id.tv_ex_shou_7);
        mExShouHint7 = (TextView) findViewById(R.id.tv_ex_shou_hint_7);

        mExShouLayout8 = findViewById(R.id.ll_ex_shou_8);
        mExShou8 = (TextView) findViewById(R.id.tv_ex_shou_8);
        mExShouHint8 = (TextView) findViewById(R.id.tv_ex_shou_hint_8);

        mExShouLayout9 = findViewById(R.id.ll_ex_shou_9);
        mExShou9 = (TextView) findViewById(R.id.tv_ex_shou_9);
        mExShouHint9 = (TextView) findViewById(R.id.tv_ex_shou_hint_9);

        mExShouLayout10 = findViewById(R.id.ll_ex_shou_10);
        mExShou10 = (TextView) findViewById(R.id.tv_ex_shou_10);
        mExShouHint10 = (TextView) findViewById(R.id.tv_ex_shou_hint_10);

        mShouLayout1.setOnClickListener(this);
        mShouLayout5.setOnClickListener(this);
        mShouLayout10.setOnClickListener(this);
        mShouLayoutQt.setOnClickListener(this);
        mExShouLayout1.setOnClickListener(this);
        mExShouLayout2.setOnClickListener(this);
        mExShouLayout3.setOnClickListener(this);
        mExShouLayout4.setOnClickListener(this);
        mExShouLayout5.setOnClickListener(this);
        mExShouLayout6.setOnClickListener(this);
        mExShouLayout7.setOnClickListener(this);
        mExShouLayout8.setOnClickListener(this);
        mExShouLayout9.setOnClickListener(this);
        mExShouLayout10.setOnClickListener(this);
    }

    private OnNumChangeListener changeListener;
    public void setOnNumChangeListener(OnNumChangeListener changeListener) {
        this.changeListener = changeListener;
    }
    public interface OnNumChangeListener{
        void onNumChange(int num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_shou_1:
                if (!mShouLayout1.isSelected()){
                    selectMoreNumChange(1);
                    if (changeListener !=null){
                        changeListener.onNumChange(1);
                    }
                }
                break;
            case R.id.ll_shou_5:
                if (!mShouLayout5.isSelected()){
                    selectMoreNumChange(5);
                    if (changeListener !=null){
                        changeListener.onNumChange(5);
                    }
                }
                break;
            case R.id.ll_shou_10:
                if (!mShouLayout10.isSelected()){
                    selectMoreNumChange(10);
                    if (changeListener !=null){
                        changeListener.onNumChange(10);
                    }
                }
                break;
            case R.id.ll_shou_qita:
                mShouRootLayout.setVisibility(GONE);
                mExShouTopRootLayout.setVisibility(VISIBLE);
                mExShouBottomRootLayout.setVisibility(VISIBLE);
                int smNum = mShouLayout1.isSelected() ? 1 : (mShouLayout5.isSelected() ? 5 : 10);
                selectNumChange(smNum);
                break;
            case R.id.ll_ex_shou_1:
                if (!mExShouLayout1.isSelected()){
                    selectNumChange(1);
                    if (changeListener !=null){
                        changeListener.onNumChange(1);
                    }
                }
                break;
            case R.id.ll_ex_shou_2:
                if (!mExShouLayout2.isSelected()){
                    selectNumChange(2);
                    if (changeListener !=null){
                        changeListener.onNumChange(2);
                    }
                }
                break;
            case R.id.ll_ex_shou_3:
                if (!mExShouLayout3.isSelected()){
                    selectNumChange(3);
                    if (changeListener !=null){
                        changeListener.onNumChange(3);
                    }
                }
                break;
            case R.id.ll_ex_shou_4:
                if (!mExShouLayout4.isSelected()){
                    selectNumChange(4);
                    if (changeListener !=null){
                        changeListener.onNumChange(4);
                    }
                }
                break;
            case R.id.ll_ex_shou_5:
                if (!mExShouLayout5.isSelected()){
                    selectNumChange(5);
                    if (changeListener !=null){
                        changeListener.onNumChange(5);
                    }
                }
                break;
            case R.id.ll_ex_shou_6:
                if (!mExShouLayout6.isSelected()){
                    selectNumChange(6);
                    if (changeListener !=null){
                        changeListener.onNumChange(6);
                    }
                }
                break;
            case R.id.ll_ex_shou_7:
                if (!mExShouLayout7.isSelected()){
                    selectNumChange(7);
                    if (changeListener !=null){
                        changeListener.onNumChange(7);
                    }
                }
                break;
            case R.id.ll_ex_shou_8:
                if (!mExShouLayout8.isSelected()){
                    selectNumChange(8);
                    if (changeListener !=null){
                        changeListener.onNumChange(8);
                    }
                }
                break;
            case R.id.ll_ex_shou_9:
                if (!mExShouLayout9.isSelected()){
                    selectNumChange(9);
                    if (changeListener !=null){
                        changeListener.onNumChange(9);
                    }
                }
                break;
            case R.id.ll_ex_shou_10:
                if (!mExShouLayout10.isSelected()){
                    selectNumChange(10);
                    if (changeListener !=null){
                        changeListener.onNumChange(10);
                    }
                }
                break;
        }
    }


    public void setPNumInitView(){
        mShouRootLayout.setVisibility(VISIBLE);
        mExShouTopRootLayout.setVisibility(GONE);
        mExShouBottomRootLayout.setVisibility(GONE);
        selectMoreNumChange(1);
    }

    public void setPNumInitView(int num){
        mShouRootLayout.setVisibility(GONE);
        mExShouTopRootLayout.setVisibility(VISIBLE);
        mExShouBottomRootLayout.setVisibility(VISIBLE);
        selectNumChange(num);
    }

    public void setBuyViewState(boolean mBuyUp) {
        mShouLayout1.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mShouLayout5.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mShouLayout10.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mShouLayoutQt.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));

        mExShouLayout1.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout2.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout3.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout4.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout5.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout6.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout7.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout8.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout9.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        mExShouLayout10.setBackground(mBuyUp ?  ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
    }

    //默认 布局手数选择
    public void selectMoreNumChange(int num) {
        if (mShouLayout1.isSelected()) {
            mShouLayout1.setSelected(false);
            mTvShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mTvShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        if (mShouLayout5.isSelected()) {
            mShouLayout5.setSelected(false);
            mTvShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mTvShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        if (mShouLayout10.isSelected()) {
            mShouLayout10.setSelected(false);
            mTvShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mTvShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        if (num == 1){
            mShouLayout1.setSelected(true);
            mTvShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
            mTvShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
        }else if (num == 5){
            mShouLayout5.setSelected(true);
            mTvShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
            mTvShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
        }else {
            mShouLayout10.setSelected(true);
            mTvShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
            mTvShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
        }
    }


    //默认 布局手数选择
    public void selectNumChange(int num) {
        if (mExShouLayout1.isSelected()) {
            mExShouLayout1.setSelected(false);
            mExShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout2.isSelected()) {
            mExShouLayout2.setSelected(false);
            mExShou2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout3.isSelected()) {
            mExShouLayout3.setSelected(false);
            mExShou3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout4.isSelected()) {
            mExShouLayout4.setSelected(false);
            mExShou4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout5.isSelected()) {
            mExShouLayout5.setSelected(false);
            mExShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout6.isSelected()) {
            mExShouLayout6.setSelected(false);
            mExShou6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        if (mExShouLayout7.isSelected()) {
            mExShouLayout7.setSelected(false);
            mExShou7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout8.isSelected()) {
            mExShouLayout8.setSelected(false);
            mExShou8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout9.isSelected()) {
            mExShouLayout9.setSelected(false);
            mExShou9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (mExShouLayout10.isSelected()) {
            mExShouLayout10.setSelected(false);
            mExShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            mExShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        switch (num){
            case 1:
                mExShouLayout1.setSelected(true);
                mExShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 2:
                mExShouLayout2.setSelected(true);
                mExShou2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 3:
                mExShouLayout3.setSelected(true);
                mExShou3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 4:
                mExShouLayout4.setSelected(true);
                mExShou4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 5:
                mExShouLayout5.setSelected(true);
                mExShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 6:
                mExShouLayout6.setSelected(true);
                mExShou6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 7:
                mExShouLayout7.setSelected(true);
                mExShou7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 8:
                mExShouLayout8.setSelected(true);
                mExShou8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 9:
                mExShouLayout9.setSelected(true);
                mExShou9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 10:
                mExShouLayout10.setSelected(true);
                mExShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                mExShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
        }
    }
}
