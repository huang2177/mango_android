package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.utils.DeviceUtils;


public class LightSpotMarkerView extends RelativeLayout {
    private RippleBackground rippleBackground;
    private RelativeLayout mParentLayout;
    private TextView mMessageTv;

    public LightSpotMarkerView(Context context, String tipsMsg) {
        super(context);
        initViews(tipsMsg);
    }

    private void initViews(String tipsMsg) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.trade_record_marker, this);
        mMessageTv = (TextView) view.findViewById(R.id.k_buy_user_tv);
        mMessageTv.setPadding(DeviceUtils.dip2px(getContext(), 8), 0, DeviceUtils.dip2px(getContext(), 8), 0);
        mMessageTv.setBackgroundDrawable(buildGradientDrawable(0xE6000000));
        mMessageTv.setText(tipsMsg);
        mParentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        rippleBackground = (RippleBackground) findViewById(R.id.ripple_view);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }


    public void setMessage(String messageStr, String direction) {
        mMessageTv.setText(messageStr);
        if (!TextUtils.isEmpty(direction)) {
            if (TextUtils.equals("1", direction)) {
                mMessageTv.setBackgroundDrawable(buildGradientDrawable(0xE6FF5376));
                rippleBackground.init(Color.parseColor("#FFFF5376"));
            } else {
                mMessageTv.setBackgroundDrawable(buildGradientDrawable(0xE600CE64));
                rippleBackground.init(Color.parseColor("#FF00CE64"));
            }
        }
    }

    public void setScaleType(int type) {
        switch (type) {
            case 1:
                mParentLayout.setScaleX(-1);
                mParentLayout.setScaleY(-1);
                mMessageTv.setScaleX(-1);
                mMessageTv.setScaleY(-1);
                break;
            case 2:
                mParentLayout.setScaleY(-1);
                mMessageTv.setScaleY(-1);
                break;
            case 3:
                break;
            case 4:
                mParentLayout.setScaleX(-1);
                mMessageTv.setScaleX(-1);
                break;
        }
    }

    private GradientDrawable buildGradientDrawable(int color) {
        int strokeWidth = 1; // 1dp 边框宽度
        int roundRadius = 100; // 2dp 圆角半径
        int strokeColor = color;//边框颜色0xE6000000
        int fillColor = color;//内部填充颜色
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

}
