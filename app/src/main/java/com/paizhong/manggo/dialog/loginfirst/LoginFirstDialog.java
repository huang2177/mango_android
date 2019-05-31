package com.paizhong.manggo.dialog.loginfirst;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.widget.CountDownTextView;

import org.greenrobot.eventbus.EventBus;

public class LoginFirstDialog extends BaseDialog<LoginFirstDialogPresenter> implements LoginFirstDialogContract.View, View.OnClickListener {

    private EditText etMobile;
    private EditText etSmsCode;
    private EditText etPwd;
    private CountDownTextView btnSendCode;
    private TextView btnLoginFirst;
    private TextView btnLoginOther;
    private LinearLayout llPwd;

    private TextView tvTitle;
    private EditText etLoginPwd;
    private TextView tvLoginForgetPwd;
    private TextView tvLoginForgetPwd1;
    private LinearLayout llLoginLayout;
    private LinearLayout llTip;

    private EditText etNewPwd;
    private EditText etNewPwdQren;
    private LinearLayout llNewPwdLayout;
    private TextView tvJumpLogin;
    private TextView atmall_tv_phone;

    private TextView tvProtocol;
    private TextView tvFenXian;
    private TextView loginSafetip;
    private TextView loginBottomTip;

    private EditText tvResetPhone;
    private EditText etResetMsnCode;
    private EditText etResetImgCode;
    private ImageView ivResetImgCode;
    private CountDownTextView cdResetSendCode;
    private EditText etResetPwd;
    private LinearLayout llResetPwdLayout;
    private LinearLayout llPhone;
    private String mImgResultToken;

    private LinearLayout llOther;
    private LinearLayout llFirst;
    private FrameLayout loginNew;
    private RelativeLayout otherCommit;

    private ImageView loginLoad;
    private Animation mAnimation;
    private int from = 0;    // 1未登录,点忘记密码

    public LoginFirstDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_main);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("您好，请登录/注册");
        loginSafetip = findViewById(R.id.login_tv_safetip);
        loginSafetip.setText(Html.fromHtml("爱淘商城资金账户保护中，密码将被保存" + "<font color='#008EFF'>3</font>" + "小时"));
        loginBottomTip = findViewById(R.id.login_bottom_tip);
        loginNew = findViewById(R.id.login_fl);

        otherCommit = findViewById(R.id.rl_commit);
        otherCommit.setOnClickListener(this);
        loginLoad = findViewById(R.id.login_load);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_net_load_rotate);
        mAnimation.setInterpolator(new LinearInterpolator());

        first();
        other();
        protocol();

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

    public void first() {

        etMobile = findViewById(R.id.et_mobile);
        etSmsCode = findViewById(R.id.et_smsed_code);
        btnSendCode = findViewById(R.id.btn_sendCode);
        etPwd = findViewById(R.id.et_reset_pwd_first);
        btnLoginFirst = findViewById(R.id.tv_commit_first);
        llPwd = findViewById(R.id.ll_pwd);
        llOther = findViewById(R.id.ll_other);
        llFirst = findViewById(R.id.ll_first);

        btnLoginFirst.setOnClickListener(this);

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count == 1) {
                        String content = s.toString().replaceAll(" ", "");
                        if (content.length() > 2 && content.length() < 8) {
                            etMobile.setText(content.substring(0, 3) + " " + content.substring(3, content.length()));
                            etMobile.setSelection(etMobile.getText().toString().length());
                        } else if (content.length() > 7) {
                            etMobile.setText(content.substring(0, 3) + " " + content.substring(3, 7) + " " + content.substring(7, content.length()));
                            etMobile.setSelection(etMobile.getText().toString().length());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etMobile.getText().toString().replaceAll(" ", "").length() == 11) {
                    btnSendCode.setEnable(true);
                    checkFirst();
                } else {
                    btnSendCode.setEnable(false);
                    btnLoginFirst.setEnabled(false);
                }
            }
        });

        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFirst();
            }
        });

        //设置交易所密码
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFirst();
            }
        });

        //获取验证码
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetUtil.isConnected()) {
                    showErrorMsg("网络不可用", null);
                    return;
                }
                String phone = etMobile.getText().toString().trim().replaceAll(" ", "");
                if (!TextUtils.isEmpty(phone)) {
                    if (!DeviceUtils.isFastDoubleClick()) {
                        etSmsCode.setFocusable(true);
                        etSmsCode.setFocusableInTouchMode(true);
                        etSmsCode.requestFocus();
                        btnSendCode.start();
                        mPresenter.sendSms(phone);
                    }
                }
            }
        });
    }

    public void other() {

        etLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
        tvLoginForgetPwd = (TextView) findViewById(R.id.tv_login_forget_pwd);
        tvLoginForgetPwd1 = (TextView) findViewById(R.id.tv_login_forget_pwd1);
        llLoginLayout = (LinearLayout) findViewById(R.id.ll_login_layout);     //已平台登录，中金超时
        llTip = (LinearLayout) findViewById(R.id.ll_tip);

        btnLoginOther = findViewById(R.id.tv_commit_other);

        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        etNewPwdQren = (EditText) findViewById(R.id.et_new_pwd_qren);
        llNewPwdLayout = (LinearLayout) findViewById(R.id.ll_new_pwd_layout);
        tvJumpLogin = (TextView) findViewById(R.id.tv_jump_login);

        llPhone = findViewById(R.id.ll_phone);
        atmall_tv_phone = findViewById(R.id.atmall_tv_phone);

        tvResetPhone = (EditText) findViewById(R.id.tv_reset_phone);
        etResetMsnCode = (EditText) findViewById(R.id.et_reset_msn_code);
        etResetImgCode = (EditText) findViewById(R.id.et_reset_img_code);
        ivResetImgCode = (ImageView) findViewById(R.id.iv_reset_img_code);
        cdResetSendCode = (CountDownTextView) findViewById(R.id.cd_reset_send_code);
        etResetPwd = (EditText) findViewById(R.id.et_reset_pwd);
        llResetPwdLayout = (LinearLayout) findViewById(R.id.ll_reset_pwd_layout);

        ivResetImgCode.setOnClickListener(this);
        tvJumpLogin.setOnClickListener(this);
        tvLoginForgetPwd.setOnClickListener(this);
        tvLoginForgetPwd1.setOnClickListener(this);
        cdResetSendCode.setOnClickListener(this);
        btnLoginOther.setOnClickListener(this);

        tvResetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count == 1) {
                        String content = s.toString().replaceAll(" ", "");
                        if (content.length() > 2 && content.length() < 8) {
                            tvResetPhone.setText(content.substring(0, 3) + " " + content.substring(3, content.length()));
                            tvResetPhone.setSelection(tvResetPhone.getText().toString().length());
                        } else if (content.length() > 7) {
                            tvResetPhone.setText(content.substring(0, 3) + " " + content.substring(3, 7) + " " + content.substring(7, content.length()));
                            tvResetPhone.setSelection(tvResetPhone.getText().toString().length());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(from==1){
                    if (tvResetPhone.getText().toString().replaceAll(" ", "").length() == 11) {
                        cdResetSendCode.setEnable(true);
                        checkFirst();
                    } else {
                        cdResetSendCode.setEnable(false);
                    }
                }
            }
        });

        //登录
        etLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etLoginPwd.getText().toString().length() == 6) {
                    otherCommit.setEnabled(true);
                    btnLoginOther.setEnabled(true);
                } else {
                    otherCommit.setEnabled(false);
                    btnLoginOther.setEnabled(false);
                }
            }
        });

        //设置交易所密码
        etNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String qurenPwd = etNewPwdQren.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                if (newPwd.length() >= 6 && qurenPwd.length() >= 6 && TextUtils.equals(qurenPwd, newPwd)) {
                    otherCommit.setEnabled(true);
                    btnLoginOther.setEnabled(true);
                } else {
                    otherCommit.setEnabled(false);
                    btnLoginOther.setEnabled(false);
                }
            }
        });

        etNewPwdQren.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String qurenPwd = etNewPwdQren.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                if (newPwd.length() >= 6 && qurenPwd.length() >= 6 && TextUtils.equals(qurenPwd, newPwd)) {
                    otherCommit.setEnabled(true);
                    btnLoginOther.setEnabled(true);
                } else {
                    otherCommit.setEnabled(false);
                    btnLoginOther.setEnabled(false);
                }
            }
        });

        cdResetSendCode.setOnCountDownStopListeners(new CountDownTextView.OnCountDownStopListeners() {
            @Override
            public void countDownStop() {
                if (etResetImgCode.getText().toString().length() == 4) {
                    cdResetSendCode.setEnable(true);
                } else {
                    cdResetSendCode.setEnable(false);
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
                if(from != 1){
                    String imgCode = etResetImgCode.getText().toString();
                    if (imgCode.length() == 4) {
                        cdResetSendCode.setEnable(true);
                    } else {
                        cdResetSendCode.setEnable(false);
                    }
                    if (etResetMsnCode.getText().toString().length() == 6
                            && etResetPwd.getText().toString().length() == 6
                            && imgCode.length() == 4) {
                        otherCommit.setEnabled(true);
                        btnLoginOther.setEnabled(true);
                    } else {
                        otherCommit.setEnabled(false);
                        btnLoginOther.setEnabled(false);
                    }
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
                    otherCommit.setEnabled(true);
                    btnLoginOther.setEnabled(true);
                } else {
                    otherCommit.setEnabled(false);
                    btnLoginOther.setEnabled(false);
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
                    otherCommit.setEnabled(true);
                    btnLoginOther.setEnabled(true);
                } else {
                    otherCommit.setEnabled(false);
                    btnLoginOther.setEnabled(false);
                }
            }
        });

        if (!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())) {
            atmall_tv_phone.setText(AppApplication.getConfig().getMobilePhone().substring(0, 3) + "****"
                    + AppApplication.getConfig().getMobilePhone().substring(7, 11));

            tvResetPhone.setText(AppApplication.getConfig().getMobilePhone().substring(0, 3) + "****"
                    + AppApplication.getConfig().getMobilePhone().substring(7, 11));

        }

    }

    public void showLoginLoading(){
        if (loginLoad !=null){
            loginLoad.setVisibility(View.VISIBLE);
            loginLoad.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.login_load));
            loginLoad.startAnimation(mAnimation);
            btnLoginOther.setText("登录中");
        }
    }

    // 1 忘记密码登录
    public void stopLoginLoading(int from){
        if (loginLoad !=null){
            try {
                loginLoad.setVisibility(View.VISIBLE);
                loginLoad.clearAnimation();
                loginLoad.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.login_suc));
            }catch (Exception e){e.printStackTrace();}
            btnLoginOther.setText("登录成功");
            if(from!=1){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                },1000);
            }
        }
    }

    public void checkFirst() {
        if (etMobile.getText().toString().replaceAll(" ", "").length() == 11 && etSmsCode.getText().toString().length() == 4 && etPwd.getText().toString().length() >= 6) {
            btnLoginFirst.setEnabled(true);
        } else {
            btnLoginFirst.setEnabled(false);
        }
    }

    public void showPic() {
        if (SpUtil.getBoolean(Constant.CACHE_IS_NEW_USER, true)) {
            ImageView imageNew = new ImageView(mContext);
            imageNew.setImageResource(R.mipmap.login_new);
            imageNew.setAdjustViewBounds(true);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.LEFT;
            loginNew.addView(imageNew, layoutParams);
        } else {
            loginNew.removeAllViews();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit_first:
                goLogin();
                break;
            case R.id.tv_jump_login: //已注册去登录
                llLoginLayout.setVisibility(View.VISIBLE);
                llNewPwdLayout.setVisibility(View.GONE);
                llResetPwdLayout.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);
                llTip.setVisibility(View.VISIBLE);
                etLoginPwd.setText("");
                btnLoginOther.setText("确定");
                tvTitle.setText("您好，请登录/注册");
                loginSafetip.setVisibility(View.GONE);
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
                if(from == 1){
                    getUserZjCheck();
                }else {
                    mPresenter.getForgetSmsCode(AppApplication.getConfig().getMobilePhone(), etResetImgCode.getText().toString(), mImgResultToken);
                    cdResetSendCode.start();
                }
                break;
            case R.id.iv_reset_img_code: //重置密码 获取图片验证码
                if (!DeviceUtils.isFastDoubleClick()) {
                    mPresenter.getUserZjSmsCaptcha();
                }
                break;
            case R.id.tv_login_forget_pwd: //已登录，忘记密码
                //cdResetSendCode.setEnable(true);
                llLoginLayout.setVisibility(View.GONE);
                llNewPwdLayout.setVisibility(View.GONE);
                llResetPwdLayout.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                llTip.setVisibility(View.GONE);
                btnLoginOther.setText("登录");
                loginSafetip.setVisibility(View.GONE);
                etNewPwd.setText("");
                tvTitle.setText(mContext.getString(R.string.user_jys_czpwd));
                if (AppApplication.getConfig().isLoginStatus()) {
                    tvResetPhone.setEnabled(false);
                    tvResetPhone.setText(AppApplication.getConfig().getMobilePhone().substring(0, 3) + "****"
                            + AppApplication.getConfig().getMobilePhone().substring(7, 11));
                }
                mPresenter.getUserZjSmsCaptcha(); //获取图片验证码
                break;
            case R.id.tv_login_forget_pwd1:  //未登录，忘记密码
                from = 1;
                tvResetPhone.setText("");
                etResetMsnCode.setText("");
                etResetImgCode.setText("");
                etResetPwd.setText("");
                loginLoad.setVisibility(View.GONE);
                llFirst.setVisibility(View.GONE);
                llOther.setVisibility(View.VISIBLE);
                llLoginLayout.setVisibility(View.GONE);
                llNewPwdLayout.setVisibility(View.GONE);
                llResetPwdLayout.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                llTip.setVisibility(View.GONE);
                loginSafetip.setVisibility(View.VISIBLE);
                loginNew.removeAllViews();
                btnLoginOther.setText("重置");
                loginSafetip.setVisibility(View.GONE);
                etNewPwd.setText("");
                tvTitle.setText(mContext.getString(R.string.user_jys_czpwd));
                AppApplication.getConfig().setMobilePhone("");
                if (AppApplication.getConfig().isLoginStatus() && AppApplication.getConfig().getMobilePhone().length()>10) {
                    tvResetPhone.setText(AppApplication.getConfig().getMobilePhone().substring(0, 3) + "****"
                            + AppApplication.getConfig().getMobilePhone().substring(7, 11));
                }
                mPresenter.getUserZjSmsCaptcha();   //获取图片验证码
                break;
            case R.id.tv_commit_other: //确认
            case R.id.rl_commit:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (llLoginLayout.getVisibility() == View.VISIBLE) {
                    showLoginLoading();
                    if(TextUtils.isEmpty(AppApplication.getConfig().getAt_token()) && AppApplication.getConfig().isAt_register()){
                        mPresenter.getUserZjLogin(AppApplication.getConfig().getMobilePhone(), etLoginPwd.getText().toString());
                    }else if(TextUtils.isEmpty(AppApplication.getConfig().getAt_token()) && !AppApplication.getConfig().isAt_register()){
                        mPresenter.getUserZjRegister(AppApplication.getConfig().getMobilePhone(), etLoginPwd.getText().toString());
                    }
                } else if(!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())){
                    mPresenter.getUserZjForgot(AppApplication.getConfig().getMobilePhone(), etResetPwd.getText().toString(), etResetMsnCode.getText().toString(), mImgResultToken);
                } else if(from == 1){
                    showErrorMsg("短信验证码错误，请重新获取",null);
                }
                break;
        }
    }

    public void goLogin() {
        if (!NetUtil.isConnected()) {
            showErrorMsg("网络不可用", null);
            return;
        }
        if (!DeviceUtils.isFastDoubleClick()) {
            String phone = etMobile.getText().toString().trim().replaceAll(" ", "");
            String smsCode = etSmsCode.getText().toString();
            String app_name = mContext.getString(R.string.app_name) + phone.substring(phone.length() - 3, phone.length());
            if (TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())) {
                mPresenter.loginApp(phone, smsCode, app_name);
            } else {
                showLoading("");
                getUserZjCheck();
            }
        }
    }

    //还原到登录
    public void showFirst() {
        showPic();
        llFirst.setVisibility(View.VISIBLE);
        llOther.setVisibility(View.GONE);
        tvTitle.setText("您好，请登录/注册");
        loginSafetip.setVisibility(View.GONE);
        loginBottomTip.setText("注册即代表同意");

        llLoginLayout.setVisibility(View.VISIBLE);
        llNewPwdLayout.setVisibility(View.VISIBLE);
        llResetPwdLayout.setVisibility(View.GONE);
        llPhone.setVisibility(View.VISIBLE);
        llTip.setVisibility(View.VISIBLE);

        AppApplication.getConfig().setMobilePhone("");
    }

    public void showAccountDialog() {
        from = 2;  //超时登录标识
        getUserZjCheck();
    }

    public void setShow(String title){

        from = 0 ;
        show();
        llFirst.setVisibility(View.GONE);
        llOther.setVisibility(View.VISIBLE);
        llLoginLayout.setVisibility(View.VISIBLE);
        llNewPwdLayout.setVisibility(View.GONE);
        llResetPwdLayout.setVisibility(View.GONE);
        llPhone.setVisibility(View.VISIBLE);
        llTip.setVisibility(View.VISIBLE);
        etLoginPwd.setText("");
        btnLoginOther.setText(title);
        otherCommit.setEnabled(false);
        loginSafetip.setVisibility(View.VISIBLE);
        loginNew.removeAllViews();
        loginBottomTip.setText(title + "即代表同意");
        if(title.equals("注册")){
            tvTitle.setText("设置资金密码");
            tvLoginForgetPwd.setVisibility(View.GONE);
        }else {
            tvTitle.setText("输入资金密码");
            tvLoginForgetPwd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        super.showErrorMsg(msg, type);
        try {
            if (loginLoad !=null) {
                loginLoad.setVisibility(View.GONE);
                loginLoad.clearAnimation();
            }
        }catch (Exception e){e.printStackTrace();}

        if(loginSafetip !=null && loginSafetip.getVisibility() == View.VISIBLE){
            btnLoginOther.setText("登录");
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
            //检测交易所是否已经注册
            mPresenter.getUserZjCheck(AppApplication.getConfig().getMobilePhone(), "register");
        }else if (from == 1 && TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())){
            String phone = tvResetPhone.getText().toString().trim().replaceAll(" ", "");
            AppApplication.getConfig().setMobilePhone(phone);
            mPresenter.getUserZjCheck(phone, "register");
        }

    }

    //返回，检查是否注册过交易所
    @Override
    public void bindUserZjCheck(UserZJCheckBean userZJCheckBean) {
        if (userZJCheckBean != null && userZJCheckBean.isSuccess() && userZJCheckBean.data != null) {
            if (userZJCheckBean.data.isregister) {
                if(from==1){
                    mPresenter.getForgetSmsCode(AppApplication.getConfig().getMobilePhone(), etResetImgCode.getText().toString(), mImgResultToken);
                    cdResetSendCode.start();
                }else if(from == 2){
                    AppApplication.getConfig().setAt_register(true);
                    SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, true);
                    setShow("登录");
                }else {
                    AppApplication.getConfig().setAt_register(true);
                    SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, true);
                    mPresenter.getUserZjLogin(AppApplication.getConfig().getMobilePhone(), etPwd.getText().toString());
                }
            } else {
                if(from==1){
                    showErrorMsg("您还未注册，请去注册登录", null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            etMobile.setText(""+tvResetPhone.getText().toString());
                            etMobile.setSelection(tvResetPhone.getText().toString().length());
                            showFirst();
                            from = 0;
                        }
                    },2000);
                }else if(from == 2){
                    AppApplication.getConfig().setAt_register(false);
                    SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, false);
                    setShow("注册");
                }else {
                    AppApplication.getConfig().setAt_register(false);
                    SpUtil.putBoolean(Constant.USER_KEY_AT_REGISTER, false);
                    mPresenter.getUserZjRegister(AppApplication.getConfig().getMobilePhone(), etPwd.getText().toString());
                }
            }
        } else {
            showErrorMsg("异常错误,请重新请求", null);
            stopLoading();
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

                if (from ==1){
                    from = 0;
                    etMobile.setText(""+tvResetPhone.getText().toString());
                    etMobile.setSelection(tvResetPhone.getText().toString().length());
                    showFirst();
                    showErrorMsg("重置密码成功", null);
                } else if (loginSafetip.getVisibility() == View.VISIBLE){
                    EventBus.getDefault().post(new JysAccountEvent(2, 1));
                    stopLoginLoading(0);
                } else if(llResetPwdLayout.getVisibility() == View.VISIBLE){
                    EventBus.getDefault().post(new JysAccountEvent(2, 1));
                    dismiss();
                } else {
                    EventBus.getDefault().post(new JysAccountEvent(1, 1));
                    dismiss();
                }
            }
        } else {
            if (userAiPeiRegisterBean != null) {
                showErrorMsg(userAiPeiRegisterBean.desc, null);
            }
        }
        stopLoading();
    }

    @Override
    public void bindUserZjSmsCaptcha(String imgResultToken, String base64Img) {
        this.mImgResultToken = imgResultToken;
        ImageUtils.display(ImageUtils.base64ToByte(base64Img), ivResetImgCode, R.mipmap.ic_sms_new_code);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (cdResetSendCode !=null){
            cdResetSendCode.removeMessages();
        }
        onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        EventBus.getDefault().post(new NewGuideEvent(2));
    }

}
