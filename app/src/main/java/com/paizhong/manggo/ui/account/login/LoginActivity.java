package com.paizhong.manggo.ui.account.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.ui.account.register.RegisterActivity;
import com.paizhong.manggo.ui.account.resetpwd.ResetPwdActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.bar.StatusBarUtil;
import com.paizhong.manggo.widget.CountDownTextView;
import com.paizhong.manggo.widget.keyboard.KeyboardChangeListener;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 登录-app
 * Created by zab on 2018/4/9 0009.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.scrollView)
    ScrollView svScrollView;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_smsed_code)
    EditText etSmsCode;
    @BindView(R.id.btn_sendCode)
    CountDownTextView btnSendCode;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.tv_protocol)
    TextView tvProtocol;
    @BindView(R.id.tv_fenxian)
    TextView tvFenXian;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_forpwd)
    TextView tvForpwd;

    private int mScreenHeight;
    private KeyboardChangeListener mKeyboardChangeListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mScreenHeight = DeviceUtils.getScreenHeight(LoginActivity.this);

        StatusBarUtil.setStatusBarColor(this,0);

        tvLogin.setVisibility(View.GONE);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        tvForpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPwdActivity.class));
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1){
                    int length = s.toString().length();
                    if (length == 3 || length == 8){
                        etMobile.setText(s + " ");
                        etMobile.setSelection(etMobile.getText().toString().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etMobile.getText().toString().length() == 13) {
                    btnSendCode.setEnable(true);
                    if (etSmsCode.getText().toString().length() == 4) {
                        btnLogin.setEnabled(true);
                    } else {
                        btnLogin.setEnabled(false);
                    }
                } else {
                    btnLogin.setEnabled(false);
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
                if (etMobile.getText().toString().length() == 13 && etSmsCode.getText().toString().length() == 4) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
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
                String phone = etMobile.getText().toString().trim().replaceAll(" ","");
                if (!TextUtils.isEmpty(phone)) {
                    if (!DeviceUtils.isFastDoubleClick()) {
                        btnSendCode.start();
                        mPresenter.sendSms(phone);
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetUtil.isConnected()) {
                    showErrorMsg("网络不可用", null);
                    return;
                }
                if (!DeviceUtils.isFastDoubleClick()) {
                    String phone = etMobile.getText().toString().trim().replaceAll(" ","");
                    String smsCode = etSmsCode.getText().toString();
                    String app_name = getString(R.string.app_name) + phone.substring(phone.length() - 3, phone.length());
                    mPresenter.loginApp(phone, smsCode, app_name);
                }
            }
        });

        mKeyboardChangeListener = new KeyboardChangeListener(LoginActivity.this);
        mKeyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    int[] location = new int[2];
                    btnLogin.getLocationInWindow(location);
                    final int srollHeight = keyboardHeight - (mScreenHeight - location[1]) + btnLogin.getHeight();
                    svScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svScrollView.scrollTo(0, srollHeight);
                        }
                    }, 50);

                } else {
                    svScrollView.scrollTo(0, 0);
                }
            }
        });

        tvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()) {
//                    Intent intentAbout = new Intent(mContext, WebViewActivity.class);
//                    intentAbout.putExtra("url", Constant.H5_USER_PROTOCOL);
//                    intentAbout.putExtra("title", "用户协议");
//                    startActivity(intentAbout);
                }
            }
        });

        tvFenXian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()) {
//                    Intent intentAbout = new Intent(mContext, WebViewActivity.class);
//                    intentAbout.putExtra("url", Constant.H5_RISK_WARNING);
//                    intentAbout.putExtra("title", "风险告知");
//                    startActivity(intentAbout);
                }
            }
        });
    }


    //登录成功返回
    @Override
    public void loginAppSucc(LoginUserBean loginUserBean) {
        if (loginUserBean != null) {
            //0 退出 1登录
            EventBus.getDefault().post(new AppAccountEvent(1, loginUserBean));
            finish();
        }else {
            showErrorMsg("异常错误,请重新请求",null);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyboardChangeListener != null) {
            mKeyboardChangeListener.destroy();
        }
    }
}
