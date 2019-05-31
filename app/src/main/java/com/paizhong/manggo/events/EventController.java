package com.paizhong.manggo.events;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * 处理 app 退出与登录相关
 * Created by zab on 2018/4/11 0011.
 */

public class EventController {
    private static volatile EventController instance = null;
    private EventController() {

    }
    public static EventController getInstance() {
        if (instance == null) {
            synchronized (EventController.class) {
                if (instance == null) {
                    instance = new EventController();
                }
            }
        }
        return instance;
    }

    public void handleMessage(AppAccountEvent event){
         if (event.code == 1){
             login(event);
         }else {
             logOut();
         }
    }


    /**
     * 登录
     */
    private void login(AppAccountEvent event){

        LoginUserBean loginUserBean = event.loginUserBean;
        //改变内存中数据
        AppApplication.getConfig().setLoginStatus(true);
        AppApplication.getConfig().setMobilePhone(loginUserBean.phone);
        AppApplication.getConfig().setAppToken(loginUserBean.appToken);
        AppApplication.getConfig().setUserId(loginUserBean.userId);

        //改变sp 数据
        SpUtil.putString(Constant.USER_KEY_TOKEN,loginUserBean.appToken);
        SpUtil.putString(Constant.USER_KEY_PHONE,loginUserBean.phone);
        SpUtil.putString(Constant.USER_KEY_USERID,loginUserBean.userId);
        SpUtil.putString(Constant.USER_KEY_NICKNAME,loginUserBean.nickName);
        SpUtil.putString(Constant.USER_KEY_USERPIC,loginUserBean.userPic);

        JPushInterface.setAlias(AppApplication.getInstance(),2,loginUserBean.phone);
        //通知用户页面
        EventBus.getDefault().post(new AppUserAccountEvent(1,loginUserBean.flag));
    }

    /**
     * 退出
     */
    private void logOut(){
        AppApplication.getConfig().setLoginStatus(false);
        AppApplication.getConfig().setMobilePhone("");
        AppApplication.getConfig().setAppToken("");
        AppApplication.getConfig().setUserId("");
        AppApplication.getConfig().setAt_register(false);

        //交易所相关
        AppApplication.getConfig().setAt_secret_access_key("");
        AppApplication.getConfig().setAt_token("");
        AppApplication.getConfig().setAt_userId("");

        //改变sp 数据
        SpUtil.remove(Constant.USER_KEY_TOKEN);
        SpUtil.remove(Constant.USER_KEY_PHONE);
        SpUtil.remove(Constant.USER_KEY_USERID);
        SpUtil.remove(Constant.USER_KEY_NICKNAME);
        SpUtil.remove(Constant.USER_KEY_USERPIC);
        SpUtil.remove(Constant.USER_KEY_AT_REGISTER);

        //交易所数据
        SpUtil.remove(Constant.USER_KEY_LOGIN_TIME);
        SpUtil.remove(Constant.USER_KEY_AT_TOKEN);
        SpUtil.remove(Constant.USER_KEY_AT_ACCESS_KEY);
        SpUtil.remove(Constant.USER_KEY_AT_USERID);

        JPushInterface.setAlias(AppApplication.getInstance(),3,null);
        //通知用户页面
        EventBus.getDefault().post(new AppUserAccountEvent(0));

        CookieSyncManager.createInstance(AppApplication.getInstance());
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
}