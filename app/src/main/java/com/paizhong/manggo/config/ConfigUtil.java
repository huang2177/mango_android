package com.paizhong.manggo.config;

import android.content.Context;
import android.text.TextUtils;

import com.paizhong.manggo.BuildConfig;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.CheckUtils;
import com.paizhong.manggo.utils.SpUtil;

/**
 * Created by zab on 2018/8/14 0014.
 */
public class ConfigUtil {
    //--------控制整个app Debug 状态-----
    private boolean isDebug = BuildConfig.DEBUG;//是否调试模式,上线必须改为false
    public boolean isDebug() {
        return isDebug;
    }

    public boolean mIsAuditing = false;//true 审核中  false 放开
    public boolean mIsUserAuditing = false;//user页面审核中是否放开
    //-----------公共参数----------------
    private String appMarket = "official";//默认渠道号
    private String appVersion;//app 版本号
    private String deviceName;//设备名称
    private String osVersion;//系统版本
    private String deviceId; //设备号
    private boolean mIsYeStyle = false; //k线主题

    //--------------app账户相关------------------------
    private boolean isLoginStatus = false;//用户的登录状态
    private String userId; //用户id
    private String mobilePhone; //手机号
    private String appToken;


    //-----------爱淘-交易所相关---------------------------------
    private String at_token;
    private String at_secret_access_key;
    private String at_userId;
    private boolean at_register;//标记当前用户是否在中金注册过


    public boolean splash = false;
    public void initParam(Context context){
        if (context !=null){
            String marketId = AndroidUtil.getMarketId(context);
            if (!TextUtils.isEmpty(marketId)){
                this.appMarket = marketId;
            }
            String appVersion = AndroidUtil.getAppVersion(context);
            if (!TextUtils.isEmpty(appVersion)){
                this.appVersion = appVersion;
            }
            String deviceName = AndroidUtil.getDeviceName();
            if (!TextUtils.isEmpty(deviceName)){
                this.deviceName = deviceName.trim();
            }
            String osVersion = AndroidUtil.getOsVersion();
            if (!TextUtils.isEmpty(osVersion)){
                this.osVersion = osVersion;
            }
            String deviceId = AndroidUtil.getDeviceId(context);
            if (!TextUtils.isEmpty(deviceId)){
                this.deviceId = deviceId;
            }
        }
        setYeStyle(SpUtil.getBoolean(Constant.CACHE_IS_YE_STYLE,false));
    }

    public void initUserParam(){
        String token = SpUtil.getString(Constant.USER_KEY_TOKEN);
        if (!TextUtils.isEmpty(token)){
            setLoginStatus(true);
            setAppToken(token);
            setMobilePhone(SpUtil.getString(Constant.USER_KEY_PHONE));
            setUserId(SpUtil.getString(Constant.USER_KEY_USERID));
            setAt_register(SpUtil.getBoolean(Constant.USER_KEY_AT_REGISTER));
            if(CheckUtils.atCheckTime()){
                setAt_secret_access_key("");
                setAt_token("");
                setAt_userId("");
                SpUtil.remove(Constant.USER_KEY_AT_TOKEN);
                SpUtil.remove(Constant.USER_KEY_AT_ACCESS_KEY);
                SpUtil.remove(Constant.USER_KEY_AT_USERID);
                SpUtil.remove(Constant.USER_KEY_LOGIN_TIME);
            }else {
                String aToken = SpUtil.getString(Constant.USER_KEY_AT_TOKEN);
                if (!TextUtils.isEmpty(aToken)){
                    setAt_token(aToken);
                    setAt_secret_access_key(SpUtil.getString(Constant.USER_KEY_AT_ACCESS_KEY));
                    setAt_userId(SpUtil.getString(Constant.USER_KEY_AT_USERID));
                }
            }
        }
    }


    //渠道号
    public String getAppMarket(){
        return appMarket;
    }

    //渠道号
    public void setAppMarket(String appMarket){
        this.appMarket = appMarket;
    }

    //app 版本号
    public String getAppVersion(){
        return appVersion;
    }

    public void setAppVersion(String appVersion){
        this.appVersion = appVersion;
    }

    //设备名称
    public String getDeviceName(){
        return deviceName;
    }

    //系统版本
    public String getOsVersion(){
        return osVersion;
    }

    //设备号
    public String getDeviceId(){
        if (TextUtils.isEmpty(deviceId)){
            deviceId = AndroidUtil.getDeviceId(AppApplication.getInstance());
        }
        return deviceId;
    }
    public void setYeStyle(boolean mIsYeStyle){
        this.mIsYeStyle = mIsYeStyle;
    }
    public boolean getYeStyle(){
        return mIsYeStyle;
    }




    public boolean isLoginStatus() {
        return isLoginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        isLoginStatus = loginStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }


    public boolean isAt_register() {
        return at_register;
    }

    public void setAt_register(boolean at_register) {
        this.at_register = at_register;
    }

    public String getAt_token() {
        return at_token;
    }

    public void setAt_token(String at_token) {
        this.at_token = at_token;
    }

    public String getAt_secret_access_key() {
        return at_secret_access_key;
    }

    public void setAt_secret_access_key(String at_secret_access_key) {
        this.at_secret_access_key = at_secret_access_key;
    }

    public String getAt_userId() {
        return at_userId;
    }

    public void setAt_userId(String at_userId) {
        this.at_userId = at_userId;
    }

}
