package com.paizhong.manggo.ui.setting.name;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.setting.main.SettingMainActivity;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SpUtil;

import butterknife.BindView;

/**
 * 更改名称
 * Created by zab on 2018/6/11 0011.
 */

public class SettingNameActivity extends BaseActivity<SettingNamePresenter> implements SettingNameContract.View{
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_qren)
    TextView tvQren;

    private Boolean mValidRole;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_name;
    }

    @Override
    public void initPresenter() {
         mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mValidRole = getIntent().getBooleanExtra("validRole",false);
        mTitle.setTitle(true, "修改昵称");
        String nickName = SpUtil.getString(Constant.USER_KEY_NICKNAME);
        etInput.setText(nickName);
        etInput.setSelection(nickName.length());
        AndroidUtil.setEmojiFilter(etInput);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = etInput.getText().toString();
                if (!TextUtils.isEmpty(text) && text.trim().length() > 0){
                    if (text.trim().length() > 7){
                        etInput.setText(text.substring(0, 7));
                        etInput.setSelection(etInput.getText().toString().length());
                    }else {
                        tvQren.setEnabled(true);
                    }
                }else {
                    tvQren.setEnabled(false);
                }
            }
        });

        tvQren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                //1：牛人   0：不是牛人
                mPresenter.updateNickName(AppApplication.getConfig().getUserId(),etInput.getText().toString(),mValidRole ? 1 : 0,AppApplication.getConfig().getMobilePhone());
            }
        });
    }

    @Override
    public void bindNickName(String nickName) {
         if (!TextUtils.isEmpty(nickName)){
             Intent intent = new Intent();
             intent.putExtra("nickName",nickName);
             setResult(SettingMainActivity.NICKNAME_UPDATE,intent);
             finish();
         }
    }
}
