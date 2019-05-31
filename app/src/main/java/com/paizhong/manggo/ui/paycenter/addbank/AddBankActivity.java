package com.paizhong.manggo.ui.paycenter.addbank;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.zj.BankCardZjBean;
import com.paizhong.manggo.utils.CheckUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.IDCardUtils;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.widget.FixRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加银行卡
 * Created by zab on 2018/4/8 0008.
 */

public class AddBankActivity extends BaseActivity<AddBankPresenter> implements AddBankContract.View {
    @BindView(R.id.et_bank_num)
    EditText etBankNum;
    @BindView(R.id.et_bank_phone)
    EditText etBankPhone;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_idCard)
    EditText etUserIdCard;
    @BindView(R.id.tv_save)
    TextView tvSave;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bank_layout;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        tvSave.setEnabled(false);
        setListener();
        mTitle.setTitle(true, "实名认证银行卡");
        if (getIntent().hasExtra("mBankNum")){
            ViewHelper.safelySetText(etBankNum,getIntent().getStringExtra("mBankNum"));//银行卡号
            ViewHelper.safelySetText(etBankPhone,getIntent().getStringExtra("mBankMobile"));//手机号
            ViewHelper.safelySetText(etUserName,getIntent().getStringExtra("mRealName"));//用户名
            ViewHelper.safelySetText(etUserIdCard,getIntent().getStringExtra("mIdCard"));//身份证
        }
    }


    @OnClick({R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save: //保存
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                String bankNum = etBankNum.getText().toString();
                String userName = etUserName.getText().toString();
                String userIdCard = etUserIdCard.getText().toString();
                String bankPhone = etBankPhone.getText().toString();
                mPresenter.bankCardZj(AppApplication.getConfig().getAt_token(), userName, bankNum, "ebank", userIdCard,bankPhone);
                break;
        }
    }


    private void setListener() {
        //银行卡
        etBankNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEnabled();
            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEnabled();
            }
        });

        etUserIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setEnabled();
            }
        });
    }


    private void setEnabled() {
        //ebank
        String userName = etUserName.getText().toString();
        String bankNum = etBankNum.getText().toString();
        String userIdCard = etUserIdCard.getText().toString();
        String bankPhone = etBankPhone.getText().toString();
        if (!TextUtils.isEmpty(userName)
                && userName.length() >= 2
                && CheckUtils.checkBankCard(bankNum)
                && IDCardUtils.validateCard(userIdCard)
                && CheckUtils.checkPhoneNum(bankPhone)) {
            tvSave.setEnabled(true);
        } else {
            tvSave.setEnabled(false);
        }
    }

    //绑卡
    @Override
    public void bindBankCardZj(BankCardZjBean cardZjBean) {
        if (cardZjBean == null){
            showErrorMsg("绑卡失败",null);
           return;
        }
        if (TextUtils.isEmpty(cardZjBean.realCode)){
            if (cardZjBean.isSuccess()){
                showErrorMsg("绑卡成功", null);
                finish();
            }else {
                String error = TextUtils.isEmpty(cardZjBean.desc) ? "绑卡失败" :cardZjBean.desc;
                showErrorMsg(error,null);
            }
        }else {
            String error = TextUtils.isEmpty(cardZjBean.message) ? "实名失败" :cardZjBean.message;
            showErrorMsg(error,null);
        }
    }

}
