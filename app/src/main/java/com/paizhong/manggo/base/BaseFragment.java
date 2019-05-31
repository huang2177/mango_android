package com.paizhong.manggo.base;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paizhong.manggo.utils.TUtil;
import com.paizhong.manggo.utils.TitleUtil;
import com.paizhong.manggo.utils.toast.AppToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * fragment基类
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    protected View mView;
    public T mPresenter;
    public BaseActivity mActivity;
    protected TitleUtil mTitle;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, mView);
        mActivity = (BaseActivity) getActivity();
        mPresenter = TUtil.getT(this, 0);
        mTitle = new TitleUtil(mActivity, mView);
        initPresenter();
        loadData();
        return mView;
    }


    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //加载View、设置数据
    public abstract void loadData();

    @Override
    public void showLoading(String content) {
        if (isMainThread()) {
            if (mActivity != null && !mActivity.isFinishing()) {
                mActivity.showLoading(content);
            }
        }
    }

    @Override
    public void stopLoading() {
        if (isMainThread()) {
            if (mActivity != null && !mActivity.isFinishing()) {
                mActivity.stopLoading();
            }
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)) {
            AppToast.show(msg);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
