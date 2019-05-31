package com.paizhong.manggo.dialog.newguide;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;

/**
 * Des:新用户领券成功提示 dialog
 * Created by huang on 2018/5/10 0010.
 */

public class GuideOrderDialog extends Dialog implements View.OnClickListener {
    private BaseActivity mActivity;

    public GuideOrderDialog(@NonNull Activity context) {
        super(context, R.style.translucent_theme);
        this.mActivity = (BaseActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guide_order);
        setCanceledOnTouchOutside(true);

        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        findViewById(R.id.tv_order).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        new NewOrderDialog(mActivity).showDialog();
        dismiss();
    }
}
