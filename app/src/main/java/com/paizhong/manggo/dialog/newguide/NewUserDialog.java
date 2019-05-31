package com.paizhong.manggo.dialog.newguide;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.loginfirst.LoginFirstDialog;
import com.paizhong.manggo.events.NewGuideEvent;
import com.paizhong.manggo.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Des:新人专享 dialog
 * Created by huang on 2018/5/10 0010.
 */

public class NewUserDialog extends Dialog implements View.OnClickListener
        , DialogInterface.OnDismissListener {
    private BaseActivity mActivity;

    public NewUserDialog(@NonNull Activity context) {
        super(context, R.style.center_dialog);
        this.mActivity = (BaseActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_user);
        setCanceledOnTouchOutside(false);

        setOnDismissListener(this);
        findViewById(R.id.iv_guide).setOnClickListener(this);
        findViewById(R.id.iv_closed).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_guide: //立即领取
                mActivity.login();
                break;
            case R.id.iv_closed:
                EventBus.getDefault().post(new NewGuideEvent(0));
                break;
        }
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        SpUtil.putBoolean(Constant.CACHE_IS_FIRST_GUIDED, true);
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new NewGuideEvent(0));
        super.onBackPressed();
    }
}
