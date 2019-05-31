package com.paizhong.manggo.utils;

import android.text.TextUtils;
import com.paizhong.manggo.app.AppApplication;
import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.WrongTypeException;


public class SpUtil {

    private static AppPreferences appPreferences;

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void putString(String key, String value) {
        getSharedPreferences().put(key, value);
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }
    public static boolean getBoolean(String key,boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }
    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().put(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int value) {
        if (TextUtils.isEmpty(key))
            return value;
        try{
            return getSharedPreferences().getInt(key, value);
        }catch (WrongTypeException e){
            return  0;
        }
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().put(key, value);
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().put(key, value);
    }

    public static void remove(String key) {
        getSharedPreferences().remove(key);
    }

    public static AppPreferences getSharedPreferences() {
        if (appPreferences ==null){
            synchronized (AppPreferences.class){
                if (appPreferences == null){
                    appPreferences=new AppPreferences(AppApplication.getInstance());
                }
            }
        }
        return appPreferences;
    }

}
