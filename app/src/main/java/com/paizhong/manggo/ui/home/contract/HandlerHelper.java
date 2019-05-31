package com.paizhong.manggo.ui.home.contract;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseFragment;

import java.util.HashMap;

/**
 * Des: 线程辅助类，用于秒刷接口
 * Created by huang on 2018/8/31 0031 11:28
 */
public class HandlerHelper extends Handler implements Runnable, LifecycleObserver {

    private static final int DELAY_MILLIS = 1000;
    private static HandlerHelper sMHandlerHelper;

    private boolean mExecute; //是否正在执行
    private boolean mHidden;
    private Fragment mFragment;
    private HashMap<String, OnExecuteListener> mListeners;

    public HandlerHelper(BaseFragment fragment) {
        this(fragment.mActivity);
        mFragment = fragment;
    }

    public HandlerHelper(BaseActivity activity) {
        mListeners = new HashMap<>();
//        activity.getLifecycle().addObserver(this);
    }

    @Override
    public void run() {
        for (OnExecuteListener l: mListeners.values()) {
            l.execute(true);
        }
        postDelayed(this, DELAY_MILLIS);
    }


    /**
     * 是否停止线程
     *
     * @param hidden
     */
    public void setHidden(boolean hidden) {
        mHidden = hidden;
        if (hidden) {
            stopRun();
        } else {
            startRun();
        }
    }

    /**
     * 开始请求数据
     */
    private void startRun() {
        if (!mListeners.isEmpty() && !mHidden) {
            removeCallbacks(this);
            post(this);
            mExecute = true;
        }
    }

    /**
     * 停止请求
     */
    private void stopRun() {
        removeCallbacks(this);
        mExecute = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        setHidden(mFragment == null || mFragment.isHidden());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        setHidden(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        setHidden(true);
        if (mListeners != null && !mListeners.isEmpty()) {
            mListeners.clear();
        }
        if (sMHandlerHelper != null) {
            sMHandlerHelper = null;
        }
    }

    /**
     * 执行
     *
     * @param tag
     */
    public void execute(OnExecuteListener tag) {
        String key = tag.getClass().getSimpleName();
        if (!mListeners.containsKey(key)) {
            mListeners.put(key, tag);
        }
        if (!mExecute) {
            startRun();
        }
    }

    /**
     * 取消执行 （一个页面可能存在多个任务执行）
     *
     * @param tag
     */
    public void cancelExecute(OnExecuteListener tag) {
        String key = tag.getClass().getSimpleName();
        if (!mListeners.isEmpty() && mListeners.containsKey(key)) {
            mListeners.remove(key);
        }
        if (mListeners.isEmpty() && mExecute) {
            stopRun();
        }
    }

    public boolean isExecute() {
        return mExecute;
    }

    /**
     * 线程执行，用于实现类执行自己的业务逻辑
     */
    public interface OnExecuteListener {
        void execute(boolean args);
    }
}
