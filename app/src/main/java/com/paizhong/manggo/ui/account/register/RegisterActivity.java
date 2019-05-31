package com.paizhong.manggo.ui.account.register;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.ui.account.login.LoginActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.bar.StatusBarUtil;
import com.paizhong.manggo.widget.CountDownTextView;
import com.paizhong.manggo.widget.keyboard.KeyboardChangeListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 注册- app
 * Created by zab on 2018/4/9 0009.
 */

public class RegisterActivity extends BaseActivity<RegistPresenter> implements RegistContract.View {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.close_layout)
    RelativeLayout closeLayout;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_mobile)
    EditText tvMobile;
    @BindView(R.id.tv_sms_code)
    EditText tvSmsCode;
    @BindView(R.id.btn_sendCode)
    CountDownTextView btnSendCode;
    @BindView(R.id.tv_pwd)
    EditText tvPwd;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.tv_protocol)
    TextView tvProtocol;
    @BindView(R.id.register_content)
    LinearLayout registerContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    private KeyboardChangeListener mKeyboardChangeListener;
    private int mScreenHeight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mScreenHeight = DeviceUtils.getScreenHeight(RegisterActivity.this);
        tvContent.setText(getString(R.string.register));
        tvLogin.setText(getString(R.string.login));

        StatusBarUtil.setStatusBarColor(this,0);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    String phone = tvMobile.getText().toString();
                    String smsCode = tvSmsCode.getText().toString();
                    String app_name = getString(R.string.app_name) + phone.substring(phone.length() - 3, phone.length());
                    mPresenter.registApp(phone, smsCode, app_name);
                }
            }
        });

        tvMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tvMobile.getText().toString().length() == 11) {
                    btnSendCode.setEnable(true);
                }else {
                    btnSendCode.setEnable(false);
                }
                if (tvMobile.getText().toString().length() >= 11 && tvSmsCode.getText().toString().length() >= 4 && tvPwd.getText().toString().length() >= 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });


        tvSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tvMobile.getText().toString().length() >= 11 && tvSmsCode.getText().toString().length() >= 4 && tvPwd.getText().toString().length() >= 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        tvPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tvMobile.getText().toString().length() >= 11 && tvSmsCode.getText().toString().length() >= 4  && tvPwd.getText().toString().length() >= 6) {
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
                String phone = tvMobile.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    if (!DeviceUtils.isFastDoubleClick()) {
                        btnSendCode.start();
                        mPresenter.sendSms(phone);
                    }
                }
            }
        });

        mKeyboardChangeListener = new KeyboardChangeListener(RegisterActivity.this);
        mKeyboardChangeListener.setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow){
                    int[] location = new int[2];
                    btnLogin.getLocationInWindow(location);
                    final int srollHeight = keyboardHeight - (mScreenHeight -  location[1])+btnLogin.getHeight();
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0,srollHeight);
                        }
                    }, 50);

                }else {
                    scrollView.scrollTo(0,0);
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyboardChangeListener != null) {
            mKeyboardChangeListener.destroy();
        }
        if (btnSendCode != null) {
            btnSendCode.removeMessages();
        }
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
}
