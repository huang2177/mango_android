package com.paizhong.manggo.widget.market;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.widget.RefreshView;

/**
 * Des: 首页行情View
 * Created by huang on 2018/8/21 0021 10:12
 */
public class MarketView extends FrameLayout {
    private ImageView ivHotFlag;
    private int mRedColor, mGreenColor, mGrayColor;
    private RefreshView tvProName, tvRateValue, tvRateRange, tvCurPoint;
    /**
     * 上一次的实时点位
     */
    private double mLastPoint = -1;
    private MarketHQBean marketBean;

    public MarketView(Context context, int position) {
        super(context);
        init(context, position);
    }

    private void init(Context context, int position) {
        setTag(position);
        setBackground(ContextCompat.getDrawable(context, R.drawable.bg_home_market_shape));
        LayoutInflater.from(context).inflate(R.layout.view_market_layout, this, true);
        ivHotFlag = findViewById(R.id.iv_hot);
        tvProName = findViewById(R.id.tv_pro_name);
        tvRateValue = findViewById(R.id.tv_rate_value);
        tvRateRange = findViewById(R.id.tv_rate_range);
        tvCurPoint = findViewById(R.id.tv_current_point);

        mRedColor = ContextCompat.getColor(context, R.color.color_F74F54);
        mGrayColor = ContextCompat.getColor(context, R.color.color_363030);
        mGreenColor = ContextCompat.getColor(context, R.color.color_1AC47A);
    }

    /**
     * 改变数据
     *
     * @param marketBean
     */
    public void updateData(MarketHQBean marketBean) {
        double point = Double.parseDouble(marketBean.point);
        this.marketBean = marketBean;
        tvProName.addText(marketBean.name);
        tvCurPoint.addText(marketBean.point);
        tvRateValue.addText(marketBean.change);
        tvRateRange.addText(marketBean.changeRate);

        double change = Double.parseDouble(marketBean.change);
        if (change == 0) {
            setTextColor(mGrayColor);
            tvProName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (change > 0) {
            setTextColor(mRedColor);
            tvProName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_up_red, 0);
        } else {
            setTextColor(mGreenColor);
            tvProName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_down_green, 0);
        }

        startAnim(point);
        mLastPoint = point;
    }

    public void updateHotProduct(boolean visible) {
        ivHotFlag.setVisibility(visible ? VISIBLE : GONE);
    }

    private void setTextColor(int textColor) {
        tvCurPoint.setTextColor(textColor);
        tvRateValue.setTextColor(textColor);
        tvRateRange.setTextColor(textColor);
    }


    /**
     * 开启背景闪烁的动画
     *
     * @param currentPoint 当前点位
     */
    private void startAnim(double currentPoint) {
        if (mLastPoint == currentPoint && mLastPoint != 1) {
            return;
        }
        try {
            if (currentPoint > mLastPoint) {
                setBackgroundResource(R.drawable.anim_market_red);
            } else if (currentPoint < mLastPoint) {
                setBackgroundResource(R.drawable.anim_market_green);
            } else {
                setBackgroundResource(R.drawable.bg_home_market_shape);
            }
            Drawable drawable = getBackground();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable anim = (AnimationDrawable) drawable;
                anim.stop();
                anim.start();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 设置背景颜色
     *
     * @param drawable
     */
    @Override
    public void setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(drawable);
        } else {
            super.setBackgroundDrawable(drawable);
        }
    }

    public MarketHQBean getData() {
        return marketBean;
    }
}
