package com.paizhong.manggo.dialog.signin;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.other.SignBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.widget.DinTextView;

/**
 * Created by zab on 2018/12/18 0018.
 */
public class SigninNewDialog extends BaseDialog<SigninNewPresenter> implements SigninNewContract.View,View.OnClickListener{
    //没签到
    private View mLlNoSign;
    private TextView tvNoSignMsg;
    private DinTextView tvNoSign;
    //已经签到
    private View llSign;
    private TextView tvSignMsg1,tvSignMsg2;
    private TextView tvItem1,tvItem2,tvItem3,tvItem4,tvItem5,tvItem6,tvItem7,tvItem8;
    private ImageView ivItemBtn;

    private Handler mHandler;
    private int mStopNum = -1;//转盘停止index
    private int mScore;


    public SigninNewDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void dismiss() {
        onDestroy();
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        stopRun();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signin_new_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        mHandler = new Handler();

        mLlNoSign = findViewById(R.id.ll_no_sign);
        View llScore = findViewById(R.id.ll_score);
        tvNoSignMsg = findViewById(R.id.tv_no_sign_msg);
        tvNoSign = findViewById(R.id.tv_no_sign);

        llSign = findViewById(R.id.ll_sign);
        tvSignMsg1 = findViewById(R.id.tv_sign_msg1);
        tvSignMsg2 = findViewById(R.id.tv_sign_msg2);

        tvItem1 = findViewById(R.id.tv_item1);
        tvItem2 = findViewById(R.id.tv_item2);
        tvItem3 = findViewById(R.id.tv_item3);
        tvItem4 = findViewById(R.id.tv_item4);
        tvItem5 = findViewById(R.id.tv_item5);
        tvItem6 = findViewById(R.id.tv_item6);
        tvItem7 = findViewById(R.id.tv_item7);
        tvItem8 = findViewById(R.id.tv_item8);
        ivItemBtn = findViewById(R.id.iv_item_btn);

        findViewById(R.id.ll_closed).setOnClickListener(this);
        llScore.setOnClickListener(this);
        ivItemBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ll_closed://关闭
               if (!DeviceUtils.isFastDoubleClick()){
                   dismiss();
               }
               break;
           case R.id.iv_item_btn://抽奖
               if (!DeviceUtils.isFastDoubleClick()){
                   if (!NetUtil.isConnected()){
                       showErrorMsg("网络不可用，请检查网络设置",null);
                   }else {
                       ivItemBtn.setEnabled(false);
                       stopRun();
                       mHandler.post(runnable);
                       ivItemBtn.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               mPresenter.drawPrize(AppApplication.getConfig().getUserId());
                           }
                       }, 800);
                   }
               }
               break;
           case R.id.ll_score://点击签到
               if (!DeviceUtils.isFastDoubleClick()){
                   if (!NetUtil.isConnected()){
                       showErrorMsg("网络不可用，请检查网络设置",null);
                   }else {
                       mPresenter.saveSign();
                   }
               }
               break;
       }
    }


    private void stopRun(){
        if (mHandler !=null){
            mHandler.removeCallbacks(runnable);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (changeViewStyle() ==-1){
                mHandler.postDelayed(this, 100);
            }else {
                stopRun();
                SigninNewSucDialog signinNewSucDialog = new SigninNewSucDialog(mContext);
                signinNewSucDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                          dismiss();
                    }
                });
                signinNewSucDialog.showSucDialog(mScore);
            }
        }
    };


    public void httpShowSigninDialog(){
        if (!isShowing()){ show();}
        tvItem1.setText(getTextSpan(String.valueOf(100)));
        tvItem2.setText(getTextSpan(String.valueOf(3200)));
        tvItem3.setText(getTextSpan(String.valueOf(200)));
        tvItem4.setText(getTextSpan(String.valueOf(1600)));
        tvItem5.setText(getTextSpan(String.valueOf(800)));
        tvItem6.setText(getTextSpan(String.valueOf(50)));
        tvItem7.setText(getTextSpan(String.valueOf(500)));
        tvItem8.setText(getTextSpan(String.valueOf(6000)));
        //没签到
        mLlNoSign.setVisibility(View.VISIBLE);
        llSign.setVisibility(View.GONE);
        mPresenter.saveSign2();
    }

    //签到成功
    @Override
    public void bindSign(SignBean signBean) {
        mLlNoSign.setVisibility(View.GONE);
        llSign.setVisibility(View.VISIBLE);
        tvSignMsg1.setText(getTextSpan("今日签到成功 奖励 ",signBean.todayScore," 积分",
                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_24sp),
                    mContext.getResources().getColor(R.color.color_F5BE03)));

        tvSignMsg2.setText(getTextSpan("已连续签到 ",signBean.dayNum," 天 | 明日签到奖励 ",signBean.tomorrowScore," 积分",
                mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15sp),
                mContext.getResources().getColor(R.color.color_F5BE03)));
    }


    //幸运抽奖
    @Override
    public void bindDrawPrize(Integer score) {
       if (score == -1){
           stopRun();
           initViewStyle();
           ivItemBtn.setEnabled(true);
       }else {
           this.mStopNum = getDrawNum(score);
       }
    }



    private int getDrawNum(Integer score){
        this.mScore = score;
        if (score == 100){
            return 1;
        }else if (score == 3200){
            return 2;
        }else if (score == 200){
            return 3;
        }else if (score == 1600){
            return 4;
        }else if (score == 800){
            return 5;
        }else if (score == 500){
            return 7;
        }else if (score == 6000){
            return 8;
        }else {
            //score = 50
            return 6;
        }
    }

    /**
     * 更改view的样式
     */
    private int changeViewStyle(){
        if (tvItem1.isSelected()){
            tvItem1.setSelected(false);
            tvItem2.setSelected(true);
            if (mStopNum == 2){return 2;}

        }else if (tvItem2.isSelected()){
            tvItem2.setSelected(false);
            tvItem3.setSelected(true);
            if (mStopNum == 3){return 3;}

        }else if (tvItem3.isSelected()){
            tvItem3.setSelected(false);
            tvItem4.setSelected(true);
            if (mStopNum == 4){return 4;}

        }else if (tvItem4.isSelected()){
            tvItem4.setSelected(false);
            tvItem5.setSelected(true);
            if (mStopNum == 5){return 5;}

        }else if (tvItem5.isSelected()){
            tvItem5.setSelected(false);
            tvItem6.setSelected(true);
            if (mStopNum == 6){return 6;}

        }else if (tvItem6.isSelected()){
            tvItem6.setSelected(false);
            tvItem7.setSelected(true);
            if (mStopNum == 7){return 7;}

        }else if (tvItem7.isSelected()){
            tvItem7.setSelected(false);
            tvItem8.setSelected(true);
            if (mStopNum == 8){return 8;}

        }else if (tvItem8.isSelected()){
            tvItem8.setSelected(false);
            tvItem1.setSelected(true);
            if (mStopNum == 1){return 1;}

        }else {
            tvItem1.setSelected(true);
            if (mStopNum == 1){return 1;}
        }
        return -1;
    }

    /**
     * 初始化 view 样式
     */
    private void initViewStyle(){
        if (tvItem1.isSelected()){
            tvItem1.setSelected(false);
        }else if (tvItem2.isSelected()){
            tvItem2.setSelected(false);
        }else if (tvItem3.isSelected()){
            tvItem3.setSelected(false);
        }else if (tvItem4.isSelected()){
            tvItem4.setSelected(false);
        }else if (tvItem5.isSelected()){
            tvItem5.setSelected(false);
        }else if (tvItem6.isSelected()){
            tvItem6.setSelected(false);
        }else if (tvItem7.isSelected()){
            tvItem7.setSelected(false);
        }else if (tvItem8.isSelected()){
            tvItem8.setSelected(false);
        }
    }

    private SpannableStringBuilder getTextSpan(String score){
        SpannableStringBuilder style = new SpannableStringBuilder(score+"\n积分");
        try {
            Typeface mCustomFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/DIN-Medium.ttf");
            style.setSpan(mCustomFont,0,score.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new AbsoluteSizeSpan(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30sp)) , 0, score.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return style;
    }


    private SpannableStringBuilder getTextSpan(String text1,String text2,String text3,int size ,int color){
        SpannableStringBuilder style = new SpannableStringBuilder(text1+text2+text3);
        try {
            Typeface mCustomFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/DIN-Medium.ttf");
            style.setSpan(mCustomFont,text1.length(),text2.length()+text1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (size !=0){
                style.setSpan(new AbsoluteSizeSpan(size) , text1.length(), text2.length()+text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (color !=0){
                style.setSpan(new ForegroundColorSpan(color), text1.length(), text2.length()+text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return style;
    }

    private SpannableStringBuilder getTextSpan(String text1,String text2,String text3,String text4,String text5, int size ,int color){
        SpannableStringBuilder style = new SpannableStringBuilder(text1+text2+text3+text4+text5);
        try {
            Typeface mCustomFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/DIN-Medium.ttf");
            style.setSpan(mCustomFont,text1.length(),text1.length() +text2.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            style.setSpan(mCustomFont,text1.length()+text2.length()+text3.length(),text1.length()+text2.length()+text3.length()+text4.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (size !=0){
                style.setSpan(new AbsoluteSizeSpan(size) , text1.length(), text1.length()+text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new AbsoluteSizeSpan(size) , text1.length()+text2.length()+text3.length(), text1.length()+text2.length()+text3.length()+text4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (color !=0){
                style.setSpan(new ForegroundColorSpan(color), text1.length(), text2.length()+text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(color), text1.length()+text2.length()+text3.length(), text1.length()+text2.length()+text3.length()+text4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return style;
    }
}
