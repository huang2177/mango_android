package com.paizhong.manggo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zab on 2018/7/17 0017.
 */
public class PushTextView extends TextView{

    public PushTextView(Context context) {
        super(context);
    }

    public PushTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PushTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

   private Runnable runnable = new Runnable() {
        @Override
        public void run() {
          if (getVisibility() == VISIBLE){
              setGone();
          }
        }
    };

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setGone(){
        removeCallbacks(runnable);
        setVisibility(GONE);
    }

    public void pushMsg(String phone, String name, String productName){
        if (getVisibility() == GONE){
            setVisibility(VISIBLE);
            this.phone = phone;
            setText(Html.fromHtml("您关注的 <font color='#ffffff'>"+name+"</font>发起了 <font color='#ffffff'>"+productName+"</font>的跟买"));
            postDelayed(runnable,3000);
        }
    }
}
