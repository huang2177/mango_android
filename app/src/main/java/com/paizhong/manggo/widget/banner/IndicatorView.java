package com.paizhong.manggo.widget.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.utils.DeviceUtils;

/**
 * Des:
 * Created by huang on 2018/8/20 0020 17:22
 */
public class IndicatorView extends LinearLayout {
    private int mCount;
    private Context mContext;

    private int selectedRes, unSelectedRes;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        this.mContext = context;
        selectedRes = R.drawable.bg_indicator_white;
        unSelectedRes = R.drawable.bg_indicator_alpha;
    }


    public IndicatorView init(int count) {
        mCount = count;
        addView(count);
        currentTab(0);
        return this;
    }

    /**
     * 添加一个每个指示器
     *
     * @param mCount
     */
    private void addView(int mCount) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (int i = 0; i < mCount; i++) {
            View view = new View(mContext);
            LayoutParams params = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_13dp), mContext.getResources().getDimensionPixelSize(R.dimen.dimen_2dp));
            params.setMargins(i == 0 ? 0 : DeviceUtils.dip2px(mContext, 5), 0, 0, 0);
            addView(view, params);
            setBackground(i, ContextCompat.getDrawable(mContext, unSelectedRes));
        }
    }

    /**
     * 当前显示那个位置
     *
     * @param position
     */
    public IndicatorView currentTab(int position) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == position) {//当前位置显示
                setBackground(i, ContextCompat.getDrawable(mContext, selectedRes));
            } else {
                setBackground(i, ContextCompat.getDrawable(mContext, unSelectedRes));
            }
        }
        return this;
    }


    /**
     * 设置背景颜色
     *
     * @param i
     * @param drawable
     */
    private void setBackground(int i, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getChildAt(i).setBackground(drawable);
        } else {
            getChildAt(i).setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置指示器颜色
     *
     * @param selectedRes
     * @param unSelectedRes
     */
    public void setIndicatorColor(int selectedRes, int unSelectedRes) {
        this.selectedRes = selectedRes;
        this.unSelectedRes = unSelectedRes;
    }
}
