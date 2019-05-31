package com.paizhong.manggo.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.paizhong.manggo.R;

/**
 * Des: 用于秒刷的TextView 如果内容为改变不进行刷新
 * Created by huang on 2018/9/26 0026 11:04
 */
public class RefreshView extends AppCompatTextView {

    // 加粗字体
    private boolean enableMediumStyle;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getParams(attrs);
        setTextFonts();
    }

    private void getParams(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RefreshView, 0, 0);
        enableMediumStyle = array.getBoolean(R.styleable.RefreshView_enableMedium, false);
        array.recycle();
    }

    /**
     * 设置文字显示
     */
    public void addText(@StringRes int resId) {
        addText(getContext().getResources().getText(resId));
    }

    /**
     * 设置文字显示
     */
    public void addText(CharSequence text) {
        if (!TextUtils.isEmpty(getText()) && TextUtils.equals(getText().toString(), text)) {
            return;
        }
        setText(text);
    }


    /**
     * 设置字体颜色
     */
    public void addTextColor(@ColorInt int color) {
        if (getCurrentTextColor() == color) {
            return;
        }
        setTextColor(color);
    }

    /**
     * 设置字体样式
     */
    private void setTextFonts() {
        if (!enableMediumStyle) {
            return;
        }
        AssetManager mgr = getContext().getAssets();
        if (mgr == null) {
            return;
        }
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
        if (tf != null) setTypeface(tf);
    }
}
