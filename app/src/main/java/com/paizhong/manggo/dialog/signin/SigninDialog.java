package com.paizhong.manggo.dialog.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
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
import com.paizhong.manggo.bean.other.SigninListBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.WeekUtil;
import java.util.Date;

/**
 * Created by zab on 2018/6/20 0020.
 */
public class SigninDialog extends BaseDialog<SigninPresenter> implements SigninContract.View {

    private ImageView ivSignH7;
    private View llSign1,llSign2,llSign3,llSign4,llSign5,llSign6,llSign7;
    private TextView tvSignH1,tvSignH2,tvSignH3,tvSignH4,tvSignH5,tvSignH6,tvSignH7;
    private TextView tvSign1,tvSign2,tvSign3,tvSign4,tvSign5,tvSign6,tvSign7;

    private TextView tvSignIntegral,tvSignCount,tvSignBtn;


    public SigninDialog(@NonNull BaseActivity context) {
        super(context, R.style.center_dialog);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (mPresenter !=null){
            mPresenter.onDestroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signin_layout);
        Window mWindow = this.getWindow();
        final WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        mWindow.setAttributes(params);

        initView();
        setListener();
    }


    private void initView(){
        llSign1 = findViewById(R.id.ll_sign1);
        tvSignH1 = findViewById(R.id.tv_sign_h1);
        tvSign1 = findViewById(R.id.tv_sign1);

        llSign2 = findViewById(R.id.ll_sign2);
        tvSignH2 = findViewById(R.id.tv_sign_h2);
        tvSign2 = findViewById(R.id.tv_sign2);

        llSign3 = findViewById(R.id.ll_sign3);
        tvSignH3 = findViewById(R.id.tv_sign_h3);
        tvSign3 = findViewById(R.id.tv_sign3);

        llSign4 = findViewById(R.id.ll_sign4);
        tvSignH4 = findViewById(R.id.tv_sign_h4);
        tvSign4 = findViewById(R.id.tv_sign4);

        llSign5 = findViewById(R.id.ll_sign5);
        tvSignH5 = findViewById(R.id.tv_sign_h5);
        tvSign5 = findViewById(R.id.tv_sign5);

        llSign6 = findViewById(R.id.ll_sign6);
        tvSignH6 = findViewById(R.id.tv_sign_h6);
        tvSign6 = findViewById(R.id.tv_sign6);

        llSign7 = findViewById(R.id.ll_sign7);
        ivSignH7 = findViewById(R.id.iv_sign_h7);
        tvSignH7 = findViewById(R.id.tv_sign_h7);
        tvSign7 = findViewById(R.id.tv_sign7);

        tvSignIntegral = findViewById(R.id.tv_sign_integral);
        tvSignCount = findViewById(R.id.tv_sign_count);
        tvSignBtn = findViewById(R.id.tv_sign_btn);
    }

    private void setListener(){
        tvSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (isSign){
                    showErrorMsg("今日已经签到,明天在来吧！",null);
                }else {
                    mPresenter.saveSign();
                }
            }
        });

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvSignCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    Intent intentAbout = new Intent(mContext, WebViewActivity.class);
                    intentAbout.putExtra("noTitle",true);
                    intentAbout.putExtra("url", Constant.H5_SIGN_GZ);
                    intentAbout.putExtra("builderPar", false);
                    intentAbout.putExtra("title", "活动规则");
                    mContext.startActivity(intentAbout);
                }
            }
        });
    }


    @Override
    public void bindSign() {
        showErrorMsg("签到成功",null);
        dismiss();
    }


    @Override
    public void bindUserScore(IntegralBean integralBean) {
        this.myscore = integralBean.myscore;
        mPresenter.getSigninList();
    }

    public void httpShowSigninDialog() {
        mPresenter.getUserIntegral(AppApplication.getConfig().getAppToken(),AppApplication.getConfig().getUserId(),AppApplication.getConfig().getMobilePhone());
    }

    @Override
    public void bindSigninError() {
       if (mPresenter !=null){
           mPresenter.onDestroy();
       }
    }


    private boolean isSign;
    private String myscore;
    @Override
    public void bindSigninList(SigninListBean signinBean) {
        if (!isShowing()){
            show();
        }
        tvSignIntegral.setText(myscore);
        tvSignCount.setText(Html.fromHtml("当前积分 |  累计签到 <font color='#FEBC18'>"+signinBean.totalDay+"</font> 天"));
        if (!TextUtils.isEmpty(signinBean.score)){
            this.isSign = false;
            tvSignBtn.setText("签到领"+signinBean.score+"积分");
            tvSignBtn.setBackgroundResource(R.mipmap.ic_sign_btn);
        }else {
            this.isSign = true;
            tvSignBtn.setText("再签领"+signinBean.tomorrowScore+"积分");
            tvSignBtn.setBackgroundResource(R.drawable.bg_shape_r1_33ffffff);
        }
        for (int i = 0 ; i < signinBean.data.size() ; i++){
            SigninListBean.SigninItemBean signinItemBean = signinBean.data.get(i);
            setSignStyle(i,signinItemBean.checked,signinBean.flag,signinItemBean.time);
        }
    }


    private void setSignStyle(int i,boolean checked,int flag,long time){
        Date date = new Date(time);
        String dayOfMonth = String.valueOf(WeekUtil.getDayOfMonth(date));
        String currentMonth = String.valueOf(getMonth(WeekUtil.getCurrentMonth(date)));
        if (i == 0){
            tvSign1.setText(dayOfMonth);
            tvSignH1.setText(currentMonth);
            if (checked){
                tvSignH1.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign1.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign1.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH1.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign1.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign1.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i == 1){
            tvSign2.setText(dayOfMonth);
            tvSignH2.setText(currentMonth);
            if (checked){
                tvSignH2.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign2.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign2.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH2.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign2.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign2.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i == 2){
            tvSign3.setText(dayOfMonth);
            tvSignH3.setText(currentMonth);
            if (checked){
                tvSignH3.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign3.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign3.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH3.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign3.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign3.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i== 3){
            tvSign4.setText(dayOfMonth);
            tvSignH4.setText(currentMonth);
            if (checked){
                tvSignH4.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign4.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign4.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH4.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign4.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign4.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i == 4){
            tvSign5.setText(dayOfMonth);
            tvSignH5.setText(currentMonth);
            if (checked){
                tvSignH5.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign5.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign5.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH5.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign5.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign5.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i == 5){
            tvSign6.setText(dayOfMonth);
            tvSignH6.setText(currentMonth);
            if (checked){
                tvSignH6.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                tvSign6.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                llSign6.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
            }else {
                tvSignH6.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                tvSign6.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                llSign6.setBackgroundResource(R.drawable.bg_shape_r2_263037);
            }
        }else if (i ==6){
            if (flag == 1){
                tvSignH7.setVisibility(View.GONE);
                tvSign7.setVisibility(View.GONE);
                ivSignH7.setVisibility(View.VISIBLE);
                tvSignH7.setText(currentMonth);
                if (checked){
                    llSign7.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
                }else {
                    llSign7.setBackgroundResource(R.drawable.bg_shape_r2_263037);
                }
            }else {
                tvSignH7.setVisibility(View.VISIBLE);
                tvSign7.setVisibility(View.VISIBLE);
                ivSignH7.setVisibility(View.GONE);
                tvSign7.setText(dayOfMonth);
                tvSignH7.setText(currentMonth);
                if (checked){
                    tvSignH7.setTextColor(ContextCompat.getColor(mContext, R.color.white_60));
                    tvSign7.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
                    llSign7.setBackgroundResource(R.drawable.bg_shape_r2_6187ff);
                }else {
                    tvSignH7.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                    tvSign7.setTextColor(ContextCompat.getColor(mContext, R.color.white_50));
                    llSign7.setBackgroundResource(R.drawable.bg_shape_r2_263037);
                }
            }
        }
    }


    private String getMonth(int month){
         if (month == 1){
             return "Jan";
         }else if (month == 2){
             return "Feb";
         }else if (month == 3){
             return "Mar";
         }else if (month == 4){
             return "Apr";
         }else if (month == 5){
             return "May";
         }else if (month == 6){
             return "Jun";
         }else if (month == 7){
             return "Jul";
         }else if (month == 8){
             return "Aug";
         }else if (month == 9){
             return "Sep";
         }else if (month == 10){
             return "Oct";
         }else if (month == 11){
             return "Nov";
         }else if (month == 12){
             return "Dec";
         }
         return "";
    }
}
