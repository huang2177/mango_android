package com.paizhong.manggo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.paizhong.manggo.bean.other.JPushBean;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.utils.NumberUtil;

import org.greenrobot.eventbus.EventBus;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zab on 2018/5/30 0030.
 */

public class JPushReceiver extends BroadcastReceiver{
    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logs.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " );

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logs.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logs.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JPushBean jpushBean =  new Gson().fromJson(json, JPushBean.class);
                if (jpushBean !=null){
                    if (jpushBean.type == 1){
                        EventBus.getDefault().post(new BaseUIRefreshEvent(3));
                    }else if (jpushBean.type == 2){
                        EventBus.getDefault().post(new BaseUIRefreshEvent(4,jpushBean.phone,jpushBean.nickName,jpushBean.product));
                    }
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 用户点击打开了通知");
                //打开自定义的Activity
                //ppmgtaojin://mgtaojin/mgtaojin/main?page=0&smPage=0
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (!TextUtils.isEmpty(json) && MainActivity.mIsMainStart){
                    JPushBean jpushBean =  new Gson().fromJson(json, JPushBean.class);
                    if (jpushBean !=null && !TextUtils.isEmpty(jpushBean.jump)){
                        Uri uri = Uri.parse(jpushBean.jump);
                        String urlPath = uri.getLastPathSegment();
                        if (TextUtils.equals("main",urlPath)){
                            Intent mainIntent = new Intent(context, MainActivity.class);
                            mainIntent.putExtra("page", NumberUtil.getInt(uri.getQueryParameter("page"),0));
                            mainIntent.putExtra("smPage", NumberUtil.getInt(uri.getQueryParameter("smPage"),0));
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(mainIntent);
                        }
                    }
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logs.d(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Logs.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }
    }
}
