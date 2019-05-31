package com.paizhong.manggo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.utils.bar.SystemBarConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeviceUtils {

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        // 屏幕宽度（像素）
        return metric.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getWholeHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param spVal
     * @return 根据设备的分辨率从 ps 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, int spVal) {
        return Math.round(spVal * context.getResources().getDisplayMetrics().density);
    }

    /**
     * @param pxVal
     * @return 根据设备的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, int pxVal) {
        return Math.round(pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static boolean isNum(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formartTimeToSec(String intMs) {
        if (intMs.equals("")) {
            intMs = "0";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String date = sdf.format(new Date(
                (int) Double.parseDouble(intMs) * 1000L));
        return date;
    }

    public static String formartTimeToMis(String intMs) {
        if (intMs.equals("")) {
            intMs = "0";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(
                (int) Double.parseDouble(intMs) * 1000L));

        return date;
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 弹出软键盘
     *
     * @param editText
     * @param context
     * @param softInPutIsOpen 软键盘是否打开
     */
    public static void showSoftInPut(final Context context, final EditText editText, boolean softInPutIsOpen) {
        if (softInPutIsOpen) {
            return;
        }
        editText.requestFocus();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(editText, 0);
                }
            }
        }, 100);
    }

    public static void hintKeyboard(Activity activity) {
        try {
            if (activity != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive() && activity.getCurrentFocus() != null) {
                    if (activity.getCurrentFocus().getWindowToken() != null) {
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    private static long lastOperateTime;

    public static boolean isFastOperate() {
        long time = System.currentTimeMillis();
        long timeD = time - lastOperateTime;
        if (0 < timeD && timeD < 100) {
            return true;
        }
        lastOperateTime = time;
        return false;
    }


    private static long recordDelayTime;

    public static boolean isDelayInFo() {
        long time = System.currentTimeMillis();
        long timeD = time - recordDelayTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        recordDelayTime = time;
        return false;
    }

    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                    isAppRunning = true;
                    // find it, break
                    break;
                }
            }
        } catch (Exception e) {
        }
        return isAppRunning;
    }

    public static String getScreenDensity() {
        DisplayMetrics mDisplayMetrics = AppApplication.getInstance().getResources().getDisplayMetrics();
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.density;
        int densityDpi = mDisplayMetrics.densityDpi;
        return height + "*" + width;
    }

    public static int[] getLocationInWindow(Activity context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    public static int[] getLocationInContent(Activity context) {
        int[] location = getLocationInWindow(context);
        int height = location[1] - new SystemBarConfig(context).getStatusBarHeight();
        return new int[]{location[0], height};
    }

    public static int getNavigationBarHeight(Activity context) {
        return new SystemBarConfig(context).getNavigationBarHeight();
    }
}
