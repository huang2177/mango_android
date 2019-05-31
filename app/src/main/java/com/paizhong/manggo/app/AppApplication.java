package com.paizhong.manggo.app;

import android.support.multidex.MultiDexApplication;
import com.paizhong.manggo.config.ConfigUtil;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.splash.Splash;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.SobotSdkUtils;
import com.paizhong.manggo.utils.toast.AppToast;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zab on 2018/8/14 0014.
 */
public class AppApplication extends MultiDexApplication {
    private static AppApplication mInstance;
    private static ConfigUtil configUtil = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppToast.register(this);
        SobotSdkUtils.initSobotSDK(getInstance());
        QbSdk.initX5Environment(getApplicationContext(), null);
        initUM();

        JPushInterface.setDebugMode(AppApplication.getConfig().isDebug()); // 设置开启 JPush 日志,发布时请关闭日志
        JPushInterface.init(this);//初始化JPush

        new Splash().getSplash();
    }

    //App 全局上下文
    public static AppApplication getInstance() {
        return mInstance;
    }

    //保存一些常用的配置
    public static ConfigUtil getConfig() {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }


    //初始化友盟数据
    private void initUM() {
        /*注册UMENG*/
        String appMarket = AndroidUtil.getMarketId(getInstance());
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getInstance(),
                Constant.UM_KEY, appMarket));
        AppApplication.getConfig().setAppMarket(appMarket);
        //关闭默认统计
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(getConfig().isDebug());

        /*集成友盟推广链接*/
        //new UMSpread().sendMessage(this, Constant.UM_KEY);
    }
}
