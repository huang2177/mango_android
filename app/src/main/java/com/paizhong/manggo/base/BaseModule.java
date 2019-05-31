package com.paizhong.manggo.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.TUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseModule
 */
public abstract class BaseModule<P extends BasePresenter> extends FrameLayout implements LifecycleObserver {
    protected static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected P mPresenter;
    protected Context mContext;
    protected Unbinder mUnbinder;
    protected MainActivity mActivity;
    protected LayoutInflater mInflater;

    private boolean mVisible;
    protected boolean mIsAuditing;

    public BaseModule(Context context) {
        this(context, null);
    }

    public BaseModule(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBase(context);
    }

    private void initBase(Context context) {
        bindContext(context);
        mInflater = LayoutInflater.from(context);

        mPresenter = TUtil.getT(this, 0);

        int id = getLayoutId();
        if (id > 0) {
            View view = mInflater.inflate(id, this, true);
            mUnbinder = ButterKnife.bind(this, view);
        }
    }

    public void bindContext(Context context) {
        mContext = context;
        if (context instanceof MainActivity) {
            this.mActivity = (MainActivity) context;
            mActivity.getLifecycle().addObserver(this);
        }
    }

    /**
     * 获取指定的Fragment
     */
    public <A extends BaseFragment> A getFragment(Class<A> clazz) {
        List<Fragment> list = mActivity.getSupportFragmentManager().getFragments();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClass().equals(clazz)) {
                if (list.get(i) instanceof BaseFragment) {
                    return (A) list.get(i);
                }
            }
        }
        return null;
    }


    /**
     * 获取对应fragment下面的View
     */
    public <V extends View> V findView(Class parentClazz, int resId) {
        if (getFragment(parentClazz) == null) {
            return null;
        }
        if (getFragment(parentClazz).mView == null) {
            return null;
        }
        return getFragment(parentClazz).mView.findViewById(resId);
    }

    public void setAuditing(boolean auditing) {
        mIsAuditing = auditing;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
        }
    }

    protected abstract int getLayoutId();

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {

    }

    /**
     * 当页面对用户可见时(对应的Fragment被调用OnHiddenChange时)
     */
    public void onVisible(boolean visible) {
        this.mVisible = visible;
        if (visible && isEmpty()) {
            onRefresh(false);
        }
    }

    protected abstract boolean isEmpty();

    public boolean isVisible() {
        return mVisible;
    }

    /**
     * 当页面进行刷新时
     *
     * @param isRefresh
     */
    public void onRefresh(boolean isRefresh) {

    }

    /**
     * 当页面销毁时
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        mActivity.getLifecycle().removeObserver(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (mUnbinder != null && mActivity != null) {
            mUnbinder.unbind();
        }
    }
}

