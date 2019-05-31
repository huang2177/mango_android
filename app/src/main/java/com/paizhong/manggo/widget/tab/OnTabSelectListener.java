package com.paizhong.manggo.widget.tab;

import android.view.View;

public interface OnTabSelectListener {
    void onTabSelect(int position, View tabLayout);

    void onTabReselect(int position);

    void onTabUnselected(int position);
}