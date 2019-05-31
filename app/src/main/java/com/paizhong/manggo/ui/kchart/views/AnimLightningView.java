package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.utils.OnGetLocationListener;

import java.util.List;


public class AnimLightningView extends RelativeLayout implements OnGetLocationListener {
    private LightningView mLightningView;
    private FlickerView mFlickerView;

    public void updateStyle(boolean mYeSyle){
        if (mLightningView !=null){
            mLightningView.updateStyle(mYeSyle);
        }
    }

    public AnimLightningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLightningView = new LightningView(context);
        mLightningView.setOnGetLocationListener(this);
        addView(mLightningView, params);
        mFlickerView = new FlickerView(context);
    }

    public void setDataAndInvalidate(List<Float> prices, String typeCode, /*List<String> times,*/ double yc) {
        final float scale = getResources().getDimension(R.dimen.dimen_11dp);
        LayoutParams rippleParams = new LayoutParams((int) scale, (int) scale);
        if (mFlickerView.getParent() == null)
            addView(mFlickerView, rippleParams);
        mLightningView.setDataAndInvalidate(prices, typeCode, /*times,*/ yc);
    }

    public void insertPrice(String price/*, String time*/) {
        mLightningView.insertPrice(price/*, time*/);
    }

    @Override
    public void onGetLocation(float x, float y) {
        final float scale = getResources().getDimension(R.dimen.dimen_11dp);
        if (mFlickerView.getX() + scale / 2 == x && mFlickerView.getY() + scale / 2 == y) return;
        mFlickerView.setX(x - scale / 2);
        mFlickerView.setY(y - scale / 2);
    }

    public void setLightningViewXNum(int num) {
        mLightningView.setXNum(num);
    }

    public void setLightningViewYNum(int num) {
        mLightningView.setYNum(num);
    }
}
