package com.paizhong.manggo.widget.tab;

import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    String getTabType();

    int getIntType();

    String setTabTitle(String title);

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}