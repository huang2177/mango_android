package com.paizhong.manggo.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.app.GlideApp;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.home.HomeFragment;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.utils.SpUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_jump)
    TextView tvJump;

    private boolean isRequesting = false;//为了避免在onResume中多次请求权限

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(SplashActivity.this);
    }

    @Override
    public void loadData() {
        //新的应用重复启动解决方法
        if (!isTaskRoot()) {
            //判断该Activity是不是任务空间的源Activity,"非"也就是说是被系统重新实例化出来的
            //如果你就放在Launcher Activity中的话，这里可以直接return了
            Intent mainIntent = getIntent();
            if (mainIntent !=null && mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(mainIntent.getAction())) {
                finish();
                return;
            }
        }

        AppApplication.getConfig().setAppMarket(AndroidUtil.getMarketId(AppApplication.getInstance()));
        AppApplication.getConfig().setAppVersion(AndroidUtil.getAppVersion(AppApplication.getInstance()));
        if (!TextUtils.isEmpty(AppApplication.getConfig().getAppMarket())
                && ViewConstant.getAuditMarket().contains(AppApplication.getConfig().getAppMarket())
                && TextUtils.equals(ViewConstant.getAuditVersion(),AppApplication.getConfig().getAppVersion())){
             mPresenter.getAuditing();
        }else {
             loadJump();
        }
    }


    /**
     * 屏蔽返回
     * @param mIsAuditing
     */
    @Override
    public void bindAuditing(boolean mIsAuditing) {
        AppApplication.getConfig().mIsAuditing = mIsAuditing;
        if (mIsAuditing){
            //屏蔽时是否屏蔽User 页面
            AppApplication.getConfig().mIsUserAuditing = ViewConstant.getAuditUserMarket().contains(AppApplication.getConfig().getAppMarket());
        }else {
            AppApplication.getConfig().mIsUserAuditing = false;
        }
        loadJump();
    }


    private void loadJump(){
        String splashUrl = SpUtil.getString(Constant.USER_KEY_SPLASH);
        String splashHtml = SpUtil.getString(Constant.USER_KEY_SPLASH_URL);
        if (!TextUtils.isEmpty(splashUrl) && !TextUtils.isEmpty(splashHtml)) {
            loadImage(splashUrl);
        } else {
            jumpActivity();
        }
    }


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            jumpActivity();
        }
    };

    /**
     * 图片加载
      * @param imageUrl
     */
    private void loadImage(String imageUrl) {
        GlideApp.with(ivImg.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        jumpActivity();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        onResourceReadySuc(resource);
                    }
                });
    }


    /**
     * 图片加载成功后逻辑
     * @param resource
     */
    private void onResourceReadySuc(Drawable resource) {
        ivImg.setImageDrawable(resource);
        handler.postDelayed(runnable, 2000);
        tvJump.setVisibility(View.VISIBLE);
        String serversion = SpUtil.getString(Constant.USER_KEY_SPLASH_SERVERSION);
        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()) {
                    handler.removeCallbacks(runnable);
                    jumpActivity();
                }
            }
        });
        if (SpUtil.getBoolean(Constant.CACHE_IS_FIRST_GUIDED)
                && !TextUtils.isEmpty(serversion)
                && TextUtils.equals(serversion, AndroidUtil.getAppVersion(SplashActivity.this))) {
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DeviceUtils.isFastDoubleClick()) {
                        ivImg.setEnabled(false);
                        handler.removeCallbacks(runnable);
                        jumpActivity();
                    }
                }
            });
        }
    }



    private Disposable mDisposable;
    private void jumpActivity() {
        if (!isRequesting) {
            try {
                isRequesting = true;
                mDisposable = new RxPermissions(SplashActivity.this).requestEach(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) {
                                AppApplication.getConfig().initParam(AppApplication.getInstance());
                                AppApplication.getConfig().initUserParam();
                                AppApplication.getConfig().splash = !ivImg.isEnabled();
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomeFragment.getInstance().preLoadData(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        try {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
                mDisposable = null;
            }
            getWindow().setBackgroundDrawable(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
