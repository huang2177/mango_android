package com.paizhong.manggo.ui.account.resetpwd;

import android.support.v4.content.ContextCompat;
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
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.bar.StatusBarUtil;
import com.paizhong.manggo.widget.CountDownTextView;
import com.paizhong.manggo.widget.keyboard.KeyboardChangeListener;

import butterknife.BindView;

/**
 * 重置密码 - app
 * Created by zab on 2018/4/9 0009.
 */

public class ResetPwdActivity extends BaseActivity<ResetPwdPresenter> implements ResetPwdContract.View {

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
    @BindView(R.id.register_content)
    LinearLayout registerContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private KeyboardChangeListener mKeyboardChangeListener;
    private int mScreenHeight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {

        StatusBarUtil.setStatusBarColor(this,0);

        mScreenHeight = DeviceUtils.getScreenHeight(ResetPwdActivity.this);
        tvLogin.setVisibility(View.INVISIBLE);
        tvContent.setText(getString(R.string.reset_pwd));
        ivClose.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_left_arrow));
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
//                        mPresenter.sendSms(phone);
                    }
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
                if (tvMobile.getText().toString().length() >= 11 && tvSmsCode.getText().toString().length() >= 4 && tvPwd.getText().toString().length() >= 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        mKeyboardChangeListener = new KeyboardChangeListener(ResetPwdActivity.this);
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
}
