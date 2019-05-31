package com.paizhong.manggo.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zab on 2018/3/29 0029.
 */

public class DinTextView extends TextView {
    public DinTextView(Context context) {
        this(context,null);
    }

    public DinTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public DinTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextFonts();
    }

    //设置字体样式
    private void setTextFonts() {
        //得到AssetManager
        AssetManager mgr = getContext().getAssets();
        if (mgr !=null){
            //根据路径得到Typeface
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
            if (tf !=null){
                //设置字体
                this.setTypeface(tf);
            }
        }
    }
}
