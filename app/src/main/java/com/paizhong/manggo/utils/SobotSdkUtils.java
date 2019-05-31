package com.paizhong.manggo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.config.Constant;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;

import java.util.UUID;

/**
 * Created by zab on 2018/5/7 0007.
 */

public class SobotSdkUtils {

    public static void startKeFu(Context context){
        SobotApi.setNotificationFlag(context,true, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        initKFInfo(context);
        SobotApi.startSobotChat(context, info);
    }


    public static void initSobotSDK(Context context){
        SobotApi.initSobotSDK(context, "897104dce1f24c3486fe04b15b9710ac", "");
    }

    public static void setInfoNull(){
        info = null;
    }

    public static Information info;//客服系统的初始化信息
    public static Information initKFInfo(Context context){
        if (info==null){
            info = new Information();
            //f0fe520a1bac40cc96e6421ca051bfc6   EtdR6n0USSaW
            info.setAppkey("897104dce1f24c3486fe04b15b9710ac");
            //用户编号
            //注意：uid为用户唯一标识，不能传入一样的值

            //用户昵称，选填
            if (AppApplication.getConfig().isLoginStatus()){
                info.setUname(SpUtil.getString(Constant.USER_KEY_NICKNAME));
                info.setUid(AppApplication.getConfig().getUserId());
            }else {
                info.setUname("芒果匿名用户");
                String deviceId = AppApplication.getConfig().getDeviceId();
                if (!TextUtils.isEmpty(deviceId)){
                    info.setUid(deviceId);
                }else {
                    info.setUid(UUID.randomUUID().toString());
                }
            }
            //用户姓名，选填
            info.setRealname("");
            //用户电话，选填
            info.setTel(AppApplication.getConfig().getMobilePhone());
            //用户邮箱，选填
            info.setEmail("");
            //自定义头像，选填
            info.setFace(SpUtil.getString(Constant.USER_KEY_USERPIC));
            //用户QQ，选填
            info.setQq("");
            //用户备注，选填
            info.setRemark("");
            //对话页标题，选填
            //info.setVisitTitle("");
            //对话页路径，选填
            //info.setVisitUrl("");

            //默认false：显示转人工按钮。true：智能转人工

            // info.setArtificialIntelligence(false);

            //当未知问题或者向导问题显示超过(X)次时，显示转人工按钮。

            //注意：只有ArtificialIntelligence参数为true时起作用

            //  info.setArtificialIntelligenceNum(X);

            //是否使用语音功能 true使用 false不使用   默认为true

            //   info.setUseVoice(true);
            //转接类型(0-可转入其他客服，1-必须转入指定客服)
            info.setTranReceptionistFlag(0);
            //指定客服id
            info.setReceptionistId("");
            //1仅机器人 2仅人工 3机器人优先 4人工优先 客服模式控制 -1不控制 按照服务器后台设置的模式运行
            info.setInitModeType(-1);
            info.setShowSatisfaction(true);//返回时弹出是否满意
            info.setColor("#008EFF");
            SobotApi.setCustomAdminHelloWord(context,"");
            SobotApi.setCustomAdminNonelineTitle(context,"");
        }
        return info;
    }

}
