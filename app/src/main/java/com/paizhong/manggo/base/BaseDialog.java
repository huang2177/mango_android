package com.paizhong.manggo.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;

import com.paizhong.manggo.utils.TUtil;
import com.paizhong.manggo.utils.toast.AppToast;

/**
 * Created by zhuaibing on 2017/11/15
 */

public abstract class BaseDialog<T extends BasePresenter> extends Dialog implements BaseView, DialogInterface.OnCancelListener {
    public T mPresenter;
    public BaseActivity mContext;

    public BaseDialog(@NonNull BaseActivity context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mPresenter = TUtil.getT(this, 0);
        initPresenter();
        setOnCancelListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void showLoading(String content) {
        if (mContext !=null && !mContext.isFinishing()){
            mContext.showLoading(content);
        }
    }

    @Override
    public void stopLoading() {
        if (mContext !=null && !mContext.isFinishing()){
            mContext.stopLoading();
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)){
            AppToast.show(msg);
        }
    }

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();


    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
