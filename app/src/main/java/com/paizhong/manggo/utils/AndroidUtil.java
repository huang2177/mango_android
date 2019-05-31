package com.paizhong.manggo.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import com.paizhong.manggo.BuildConfig;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.utils.toast.AppToast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zab on 2018/3/26 0026.
 */

public class AndroidUtil {

    //获取当前app的版本号
    public static String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取当前设备ID(设备号为空时使用AndroidId)
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        return tm.getImei();
                    } else if (Build.VERSION.SDK_INT >= 23) {
                        return tm.getDeviceId(0);
                    } else {
                        return tm.getDeviceId();
                    }
                } else {
                    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }catch (Exception e1){
              e1.printStackTrace();
            }
            return "";
        }
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String imsi = mTelephonyMgr.getSubscriberId();
                if (TextUtils.isEmpty(imsi)) {
                    //是否为MTK双卡
                    List<String> list = getMTKImsi(mTelephonyMgr);
                    if (list !=null && list.size() > 0){
                        imsi = list.get(0);
                        if (TextUtils.isEmpty(imsi) && list.size() > 1){
                            imsi = list.get(1);
                        }
                    }
                }
                return !TextUtils.isEmpty(imsi) ? imsi : Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    private static List<String> getMTKImsi(TelephonyManager tm) {
        try {
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int id1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int id2 = (Integer) fields2.get(null);
            Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);
            String imsi1 = (String) m.invoke(tm, id1);
            String imsi2 = (String) m.invoke(tm, id2);
            if (TextUtils.isEmpty(imsi1) && TextUtils.isEmpty(imsi2)) return null;
            List<String> list = new ArrayList<>();
            if (!TextUtils.isEmpty(imsi1)) {
                list.add(imsi1);
            }
            if (!TextUtils.isEmpty(imsi2)) {
                list.add(imsi2);
            }
            return list;
        } catch (Exception e) {
        }
        return null;
    }


    //获取当前设备信息
    public static String getDeviceName() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    //获取当前系统的版本号
    public static String getOsVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    //获取渠道号
    public static String getMarketId(Context context) {
        String appType = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            appType = String.valueOf(appInfo.metaData.get("UMENG_CHANNEL"));
            if (TextUtils.isEmpty(appType)) {
                appType = "unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
            appType = "unknown";
        }
        return appType;
    }


    /**
     * 返回当前程序版本号
     */
    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }


    public static void setEmojiFilter(EditText et) {
        try {
            InputFilter emojiFilter = new InputFilter() {
                Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|" +
                        "[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    Matcher matcher = pattern.matcher(source);
                    if (matcher.find()) {
                        AppToast.show("暂不支持表情输入!");
                        return "";
                    }
                    return null;
                }
            };
            InputFilter[] oldFilters = et.getFilters();
            int oldFiltersLength = oldFilters.length;
            InputFilter[] newFilters = new InputFilter[oldFiltersLength + 1];
            if (oldFiltersLength > 0) {
                System.arraycopy(oldFilters, 0, newFilters, 0, oldFiltersLength);
            }
            //添加新的过滤规则
            newFilters[oldFiltersLength] = emojiFilter;
            et.setFilters(newFilters);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  //息屏 false  亮屏 true
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context){
        PowerManager powerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);

        if (powerManager !=null){
            try {
                return powerManager.isScreenOn();
            }catch (Exception e){
                return false;
            }
        }else {
            return false;
        }
    }



    public static boolean isRunBg(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        if (manager !=null){
            List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
            if (runningTasks !=null && runningTasks.size() > 0){
                ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                if (runningTaskInfo !=null){
                    ComponentName componentName = runningTaskInfo.topActivity;
                    if (componentName !=null){
                        String packageName = componentName.getPackageName();
                        if (!TextUtils.isEmpty(packageName) && TextUtils.equals(packageName,context.getPackageName())){
                            return true;
                        }else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    public static void clipboard(BaseActivity context, String msg){
        try {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(msg, msg));
            context.showErrorMsg("复制成功",null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
