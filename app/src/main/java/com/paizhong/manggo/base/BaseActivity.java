package com.paizhong.manggo.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.dialog.loginfirst.LoginFirstDialog;
import com.paizhong.manggo.dialog.regist.RegisterDialog;
import com.paizhong.manggo.utils.TUtil;
import com.paizhong.manggo.utils.TitleUtil;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.NetAlertDialog;
import com.umeng.analytics.MobclickAgent;
import ezy.boost.update.AppManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    public T mPresenter;
    private Unbinder mUnbinder;
    protected TitleUtil mTitle;
    private NetAlertDialog mNetAlertDialog;
    public BaseActivity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getInstance().addActivity(this);
        mActivity = this;
        mUnbinder = ButterKnife.bind(this);
        mPresenter = TUtil.getT(this, 0);
        mTitle = new TitleUtil(this, getWindow().getDecorView());
        initStatusBar();
        initPresenter();
        loadData();
    }

    public void initStatusBar() {
        //StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    //获取布局文件

    public abstract int getLayoutId();
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通

    public abstract void initPresenter();
    //加载、设置数据

    public abstract void loadData();

    @Override
    public void showLoading(String content) {
        if (isMainThread()) {
            if (mNetAlertDialog == null) {
                mNetAlertDialog = new NetAlertDialog(BaseActivity.this);
            }
            mNetAlertDialog.dismiss();
            mNetAlertDialog.showLoading(content);
        }
    }

    @Override
    public void stopLoading() {
        if (isMainThread()) {
            if (mNetAlertDialog != null && mNetAlertDialog.isShowing()) {
                mNetAlertDialog.dismissLoading();
            }
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)) {
            AppToast.show(msg);
        }
    }


    /**
     * 重写getResources  保证改变系统字体大小不引起适配问题
     *
     * @return
     */
    private Resources res;
    @Override
    public Resources getResources() {
        if (res == null) {
            res = super.getResources();
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return res;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetAlertDialog != null) {
            stopLoading();
            mNetAlertDialog = null;
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        AppManager.getInstance().finishActivity(this);
    }


    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    /**
     * 登录全局接口
     * @return
     */
    public boolean login(){
        if (TextUtils.isEmpty(AppApplication.getConfig().getAppToken())|| TextUtils.isEmpty(AppApplication.getConfig().getAt_token())){
            if (TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())) {
                new RegisterDialog(BaseActivity.this).showMyDialog();
            }else if(!TextUtils.isEmpty(AppApplication.getConfig().getMobilePhone())&&
                    !AppApplication.getConfig().mIsAuditing){
                new LoginFirstDialog(BaseActivity.this).showAccountDialog();
            }
            return false;
        }else {
            return true;
        }
    }
}
