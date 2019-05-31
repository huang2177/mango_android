package com.paizhong.manggo.dialog.regist;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.bean.zj.UserZJCheckBean;
import com.paizhong.manggo.bean.zj.UserZJRegisterBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.events.JysAccountEvent;
import com.paizhong.manggo.events.NewGuideEvent;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.AnimationUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.widget.CountDownTextView;

import org.greenrobot.eventbus.EventBus;


public class RegisterDialog extends BaseDialog<RegisterDialogPresenter> implements RegisterDialogContract.View,
        View.OnClickListener{

    private TextView tvProtocol;
    private TextView tvFenXian;
    private FrameLayout flRegisterPic;
    private EditText etRegisterMobile;
    private TextView tvRegisterCommit;
    private LinearLayout llRegisterOne;
    private LinearLayout llRegisterTwo;
    private LinearLayout llRegisterThree;
    private LinearLayout llRegisterFour;
    private LinearLayout llTip;

    private TextView tvRegisterTwoFront;
    private TextView tvRegisterThreeFront;
    private CountDownTextView btnSendCode;
    private TextView tvRegisterTwoNum;
    private EditText etRegisterThreePwd;
    private TextView tvRegisterTwoTip;
    private TextView tvRegisterThreeTip;
    private LinearLayout llRegisterMainTop;
    private TextView tvRegisterThreeTitle;
    private TextView tvThreeTitleTip;
    private LinearLayout llRegisterView;
    private EditText etRegisterTwoCode;
    private TextView tvRegisterForget;

    private EditText etResetPwd;
    private EditText tvResetPhone;
    private EditText etResetMsnCode;
    private EditText etResetImgCode;
    private CountDownTextView cdResetSendCode;
    private ImageView ivResetImgCode;

    private String mImgResultToken;
    private BaseActivity mContext;
    private int status = 0 ;  // 1手机号页  2验证码页  3资金密码页 4忘记密码

    public RegisterDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_register_main);

        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        setFindAndListener();
        protocol();
        setWatcher();
        showNewPic();
        status = 1;
        DeviceUtils.showSoftInPut(mContext,etRegisterMobile,false);

    }

    private void setFindAndListener() {

        flRegisterPic = findViewById(R.id.fl_register_pic);
        etRegisterMobile = findViewById(R.id.et_register_mobile);
        tvRegisterCommit = findViewById(R.id.tv_register_commit);
        llRegisterOne = findViewById(R.id.ll_register_one);
        llRegisterTwo = findViewById(R.id.ll_register_two);
        llRegisterThree = findViewById(R.id.ll_register_three);
        llRegisterFour = findViewById(R.id.ll_register_four);
        tvRegisterTwoFront = findViewById(R.id.tv_register_two_front);
        tvRegisterThreeFront = findViewById(R.id.tv_register_three_front);
        btnSendCode = findViewById(R.id.btn_sendCode);
        tvRegisterTwoNum = findViewById(R.id.tv_register_two_num);
        etRegisterThreePwd = findViewById(R.id.et_register_three_pwd);
        tvRegisterTwoTip = findViewById(R.id.tv_register_two_tip);
        tvRegisterThreeTip = findViewById(R.id.tv_register_three_tip);
        llRegisterMainTop = findViewById(R.id.ll_register_main_top);
        tvRegisterThreeTitle = findViewById(R.id.tv_register_three_title);
        tvThreeTitleTip = findViewById(R.id.tv_three_title_tip);
        llRegisterView = findViewById(R.id.ll_register_view);
        etRegisterTwoCode = findViewById(R.id.et_register_two_code);
        tvRegisterForget = findViewById(R.id.tv_register_forget);

        tvResetPhone = (EditText) findViewById(R.id.tv_reset_phone);
        etResetMsnCode = (EditText) findViewById(R.id.et_reset_msn_code);
        etResetImgCode = (EditText) findViewById(R.id.et_reset_img_code);
        etResetPwd = (EditText) findViewById(R.id.et_reset_pwd);
        cdResetSendCode = (CountDownTextView) findViewById(R.id.cd_reset_send_code);
        ivResetImgCode = (ImageView) findViewById(R.id.iv_reset_img_code);
        llTip = (LinearLayout) findViewById(R.id.ll_tip);

        tvRegisterCommit.setOnClickListener(this);
        tvRegisterTwoFront.setOnClickListener(this);
        tvRegisterThreeFront.setOnClickListener(this);
        llRegisterView.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        tvRegisterForget.setOnClickListener(this);
        cdResetSendCode.setOnClickListener(this);
        ivResetImgCode.setOnClickListener(this);

    }

    private void protocol() {

        tvProtocol = findViewById(R.id.tv_protocol);
        tvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()) {
                    Intent intentAbout = new Intent(mContext, WebViewActivity.class);
                    intentAbout.putExtra("url", Constant.H5_USER_PROTOCOL);
                    intentAbout.putExtra("title", "用户协议");
                    mContext.startActivity(intentAbout);
                }
            }
        });

        if (AppApplication.getConfig().mIsAuditing){
            llTip.setVisibility(View.GONE);
        }

        tvFenXian = findViewById(R.id.tv_talk);
        tvFenXian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()) {
                    Intent intentAbout = new Intent(mContext, WebViewActivity.class);
                    intentAbout.putExtra("url", Constant.H5_RISK_WARNING);
                    intentAbout.putExtra("title", "风险告知");
                    mContext.startActivity(intentAbout);
                }
            }
        });

    }

    private void setWatcher() {
        etRegisterMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count == 1) {
                        String content = s.toString().replaceAll(" ", "");
                        if (content.length() > 2 && content.length() < 8) {
                            etRegisterMobile.setText(content.substring(0, 3) + " " + content.substring(3, content.length()));
                            etRegisterMobile.setSelection(etRegisterMobile.getText().toString().length());
                        } else if (content.length() > 7) {
                            etRegisterMobile.setText(content.substring(0, 3) + " " + content.substring(3, 7) + " " + content.substring(7, content.length()));
                            etRegisterMobile.setSelection(etRegisterMobile.getText().toString().length());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etRegisterMobile.getText().toString().replaceAll(" ", "").length() == 11) {
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
            }
        });

        etRegisterThreePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(etRegisterThreePwd.getText().toString().length() >= 6){
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
                tvRegisterThreeTip.setVisibility(View.GONE);
                tvRegisterThreeTip.setText("");
            }
        });

        etRegisterTwoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tvRegisterTwoTip.setVisibility(View.GONE);
                tvRegisterTwoTip.setText("");
                if(etRegisterTwoCode.getText().toString().length() == 4){
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
            }
        });


        //图片验证码
        etResetImgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String imgCode = etResetImgCode.getText().toString();
                if (imgCode.length() == 4) {
                    cdResetSendCode.setEnable(true);
                } else {
                    cdResetSendCode.setEnable(false);
                }
                if (etResetMsnCode.getText().toString().length() == 6
                        && etResetPwd.getText().toString().length() == 6
                        && imgCode.length() == 4) {
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
            }
        });

        etResetMsnCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etResetMsnCode.getText().toString().length() == 6 && etResetPwd.getText().toString().length() == 6) {
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
            }
        });

        etResetPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etResetMsnCode.getText().toString().length() == 6 && etResetPwd.getText().toString().length() == 6) {
                    tvRegisterCommit.setEnabled(true);
                } else {
                    tvRegisterCommit.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_reset_img_code: //重置密码 获取图片验证码
                if (!DeviceUtils.isFastDoubleClick()) {
                    mPresenter.getUserZjSmsCaptcha();
                }
                break;
            case R.id.cd_reset_send_code://重置密码,获取验证码
                if (!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())&&TextUtils.isEmpty(mImgResultToken)) {
                    showErrorMsg("请重新获取图片验证码", null);
                    return;
                }
                if(etResetImgCode.getText().toString().trim().equals("")){
                    showErrorMsg("请输入图片验证码", null);
                    return;
                }
                mPresenter.getForgetSmsCode(AppApplication.getConfig().getMobilePhone(), etResetImgCode.getText().toString(), mImgResultToken);
                cdResetSendCode.start();
                etResetMsnCode.requestFocus();
                etResetMsnCode.setFocusableInTouchMode(true);
                etResetMsnCode.setFocusable(true);
                break;
            case R.id.tv_register_forget:
                status = 4;
                llRegisterOne.setVisibility(View.GONE);
                llRegisterTwo.setVisibility(View.GONE);
                llRegisterThree.setVisibility(View.GONE);
                llRegisterFour.setVisibility(View.VISIBLE);
                llTip.setVisibility(View.GONE);
                tvResetPhone.setText("");
                etResetMsnCode.setText("");
                etResetImgCode.setText("");
                etResetPwd.setText("");
                tvRegisterCommit.setText("重置");
                if (AppApplication.getConfig().isLoginStatus() && AppApplication.getConfig().getMobilePhone().length()>10) {
                    tvResetPhone.setText(AppApplication.getConfig().getMobilePhone().substring(0, 3) + "****"
                            + AppApplication.getConfig().getMobilePhone().substring(7, 11));
                    tvResetPhone.setEnabled(false);
                    etResetImgCode.requestFocus();
                    etResetImgCode.setFocusableInTouchMode(true);
                    etResetImgCode.setFocusable(true);
                    DeviceUtils.showSoftInPut(mContext,etResetImgCode,false);
                }
                mPresenter.getUserZjSmsCaptcha();   //获取图片验证码
                break;
            case R.id.ll_register_view:
                dismiss();
                break;
            case R.id.btn_sendCode:
                if (!NetUtil.isConnected()) {
                    showErrorMsg("网络不可用", null);
                    return;
                }
                tvRegisterTwoTip.setText("");
                tvRegisterTwoTip.setVisibility(View.GONE);
                String phone = etRegisterMobile.getText().toString().trim().replaceAll(" ", "");
                if (!TextUtils.isEmpty(phone) && !DeviceUtils.isFastDoubleClick()){
                    mPresenter.sendSms(phone);
                    btnSendCode.start();
                }
                break;
            case R.id.tv_register_commit:
                commit();
                break;
            case R.id.tv_register_two_front:
                if(status == 2){
                    llRegisterOne.setVisibility(View.VISIBLE);
                    llRegisterTwo.setVisibility(View.GONE);
                    llRegisterThree.setVisibility(View.GONE);
                    llRegisterFour.setVisibility(View.GONE);
                    tvRegisterTwoTip.setVisibility(View.GONE);
                    tvRegisterTwoTip.setText("");
                    tvRegisterCommit.setEnabled(true);
                    AnimationUtils.startLeftIn(mContext,llRegisterMainTop);
                    status = 1;
                }
                break;
            case R.id.tv_register_three_front:
                if(status == 3){
                    llRegisterOne.setVisibility(View.GONE);
                    llRegisterTwo.setVisibility(View.VISIBLE);
                    llRegisterThree.setVisibility(View.GONE);
                    llRegisterFour.setVisibility(View.GONE);
                    tvRegisterThreeTip.setVisibility(View.GONE);
                    tvRegisterThreeTip.setText("");
                    tvRegisterCommit.setEnabled(true);
                    tvRegisterCommit.setText("下一步");
                    AnimationUtils.startLeftIn(mContext,llRegisterMainTop);
                    status = 2;
                }
                break;
        }
    }

    public void commit(){

        switch (status){
            case 1:
                toSmsCode();
                break;
            case 2:
                appLogin();
                break;
            case 3:
                if (!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone()) && SpUtil.getBoolean(Constant.USER_KEY_AT_REGISTER)) {
                    mPresenter.getUserZjLogin(AppApplication.getConfig().getMobilePhone(), etRegisterThreePwd.getText().toString());
                } else if (!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())){
                    mPresenter.getUserZjRegister(AppApplication.getConfig().getMobilePhone(), etRegisterThreePwd.getText().toString());
                }
                break;
            case 4:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if(!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())){
                    mPresenter.getUserZjForgot(AppApplication.getConfig().getMobilePhone(), etResetPwd.getText().toString(), etResetMsnCode.getText().toString(), mImgResultToken);
                }
                break;
        }

    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    public void showMyDialog() {
        show();
    }

    public void showNewPic() {
        if (SpUtil.getBoolean(Constant.CACHE_IS_NEW_USER, true) && !AppApplication.getConfig().mIsAuditing) {
            ImageView imageNew = new ImageView(mContext);
            imageNew.setImageResource(R.mipmap.login_new);
            imageNew.setAdjustViewBounds(true);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.LEFT;
            flRegisterPic.addView(imageNew, layoutParams);
        } else {
            flRegisterPic.removeAllViews();
        }
    }

    //登录成功返回
    @Override
    public void loginAppSucc(LoginUserBean loginUserBean) {
        if (loginUserBean != null) {
            //0 退出 1登录
            EventBus.getDefault().post(new AppAccountEvent(1, loginUserBean));
            AppApplication.getConfig().setMobilePhone(loginUserBean.phone);
            AppApplication.getConfig().setLoginStatus(true);
            getUserZjCheck();
        } else {
            showErrorMsg("异常错误,请重新请求", null);
            stopLoading();
        }
    }

    //请求，检查是否注册过交易所
    private void getUserZjCheck() {
        if (!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())) {
            mPresenter.getUserZjCheck(AppApplication.getConfig().getMobilePhone(), "register");
        }
    }

    //返回，检查是否注册过交易所
    @Override
    public void bindUserZjCheck(UserZJCheckBean userZJCheckBean) {
        if (userZJCheckBean != null && userZJCheckBean.isSuccess() && userZJCheckBean.data != null) {
            if (userZJCheckBean.data.isregister) {
                AppApplication.getConfig().setAt_register(true);
                SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, true);
                tvRegisterCommit.setText("登录");
                if(!AppApplication.getConfig().mIsAuditing){
                    tvRegisterThreeTitle.setText("请输入资金密码");
                    tvThreeTitleTip.setText("资金密码为6位数字");
                }else {
                    tvRegisterThreeTitle.setText("请输入账号密码");
                    tvThreeTitleTip.setText("账号密码为6位数字");
                }
                tvRegisterForget.setVisibility(View.VISIBLE);
            } else {
                AppApplication.getConfig().setAt_register(false);
                SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, false);
                tvRegisterCommit.setText("注册");
                if(!AppApplication.getConfig().mIsAuditing){
                    tvRegisterThreeTitle.setText("请设置资金密码");
                    tvThreeTitleTip.setText("资金密码为6位数字");
                }else {
                    tvRegisterThreeTitle.setText("请设置账号密码");
                    tvThreeTitleTip.setText("账号密码为6位数字");
                }
                tvRegisterForget.setVisibility(View.GONE);
            }
            llRegisterOne.setVisibility(View.GONE);
            llRegisterTwo.setVisibility(View.GONE);
            llRegisterThree.setVisibility(View.VISIBLE);
            llRegisterFour.setVisibility(View.GONE);
            AnimationUtils.startRightIn(mContext,llRegisterMainTop);
            DeviceUtils.showSoftInPut(mContext,etRegisterThreePwd,false);
            if(etRegisterThreePwd.getText().toString().trim().length() == 6){
                tvRegisterCommit.setEnabled(true);
            }else {
                tvRegisterCommit.setEnabled(false);
            }
            status = 3;
        } else {
            showErrorMsg("异常错误,请重新请求", null);
        }
    }

    //登录或注册交易所，返回
    @Override
    public void getUserZjRegister(UserZJRegisterBean userAiPeiRegisterBean) {
        if (userAiPeiRegisterBean != null && userAiPeiRegisterBean.isSuccess()) {
            if (userAiPeiRegisterBean.data != null) {
                AppApplication.getConfig().setAt_secret_access_key(userAiPeiRegisterBean.data.secret_access_key);
                AppApplication.getConfig().setAt_token(userAiPeiRegisterBean.data.token);
                AppApplication.getConfig().setAt_userId(userAiPeiRegisterBean.data.userId);
                AppApplication.getConfig().setAt_register(true);

                SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, true);
                SpUtil.putString(Constant.USER_KEY_AT_TOKEN, userAiPeiRegisterBean.data.token);
                SpUtil.putString(Constant.USER_KEY_AT_ACCESS_KEY, userAiPeiRegisterBean.data.secret_access_key);
                SpUtil.putString(Constant.USER_KEY_AT_USERID, userAiPeiRegisterBean.data.userId);
                SpUtil.putLong(Constant.USER_KEY_LOGIN_TIME, System.currentTimeMillis());

                if(status == 3 && tvRegisterCommit.getText().equals("登录")){
                    showErrorMsg("登录成功", "1");
                }else if(status == 3 && tvRegisterCommit.getText().equals("注册")){
                    showErrorMsg("注册成功", "1");
                }

                EventBus.getDefault().post(new JysAccountEvent(1, 1));
                dismiss();
            }
        } else {
            if (userAiPeiRegisterBean != null) {
                showErrorMsg(userAiPeiRegisterBean.desc, null);
            }
        }
        stopLoading();
    }

    //短信验证码发送成功
    @Override
    public void smsSuccess(boolean isSuc) {
        showErrorMsg("手机验证码已发送 \n请查看手机", "1");
    }

    public void toSmsCode(){
        String phone = etRegisterMobile.getText().toString().replaceAll(" ", "");
        String smsCode = etRegisterTwoCode.getText().toString().trim();
        if(phone.length() > 10)tvRegisterTwoNum.setText(Html.fromHtml(""+ "<font color='#727272' size='16'>手机号：</font>"
                        + phone.substring(0, 3) + "****"+ phone.substring(7, 11)));
        llRegisterOne.setVisibility(View.GONE);
        llRegisterTwo.setVisibility(View.VISIBLE);
        llRegisterThree.setVisibility(View.GONE);
        llRegisterFour.setVisibility(View.GONE);
        btnSendCode.setEnable(true);
        AnimationUtils.startRightIn(mContext,llRegisterMainTop);
        etRegisterTwoCode.requestFocus();
        etRegisterTwoCode.setFocusableInTouchMode(true);
        etRegisterTwoCode.setFocusable(true);
        if(smsCode.length() != 4)tvRegisterCommit.setEnabled(false);
        status = 2;
        DeviceUtils.showSoftInPut(mContext,etRegisterTwoCode,false);
    }

    public void appLogin() {
        if (!NetUtil.isConnected()) {
            showErrorMsg("网络不可用", null);
            return;
        }
        if (!DeviceUtils.isFastDoubleClick()) {
            String phone = etRegisterMobile.getText().toString().trim().replaceAll(" ", "");
            String smsCode = etRegisterTwoCode.getText().toString().trim();
            String app_name = mContext.getString(R.string.app_name) + phone.substring(phone.length() - 3, phone.length());
            if (TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())) {
                mPresenter.loginApp(phone, smsCode, app_name);
            }else {
                getUserZjCheck();
            }
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if(status == 2 && type == null){
            tvRegisterTwoTip.setText(msg);
            tvRegisterTwoTip.setVisibility(View.VISIBLE);
        }else if(status == 3 && type == null){
            tvRegisterThreeTip.setText(msg);
            tvRegisterThreeTip.setVisibility(View.VISIBLE);
        }else {
            super.showErrorMsg(msg, type);
        }
    }

    @Override
    public void bindUserZjSmsCaptcha(String imgResultToken, String base64Img) {
        this.mImgResultToken = imgResultToken;
        ImageUtils.display(ImageUtils.base64ToByte(base64Img), ivResetImgCode, R.mipmap.ic_sms_new_code);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onDestroy();
        if (btnSendCode !=null){
            btnSendCode.removeMessages();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        EventBus.getDefault().post(new NewGuideEvent(2));
    }
}
