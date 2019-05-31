package com.paizhong.manggo.widget.switcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ViewSwitcher;

import com.paizhong.manggo.R;


/**
 * 向上滚动的text
 */
public class AutoViewSwitcher extends ViewSwitcher implements ViewSwitcher.ViewFactory {
    private Context mContext;
    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Animation mInUp;
    private Animation mOutUp;

    public AutoViewSwitcher(Context context) {
        this(context, null);
    }

    public AutoViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @Override
    public View getNextView() {
        return super.getNextView();
    }

    private void init() {
        setFactory(this);
        mInUp = createInAnim();
        mOutUp = createOutAnim();
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }


    private Animation createInAnim() {
        Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_viewswitch_in);
        inAnimation.setDuration(650);
        inAnimation.setFillAfter(true);
        inAnimation.setInterpolator(new LinearInterpolator());
        return inAnimation;
    }

    private Animation createOutAnim() {
        Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_viewswitcher_out);
        inAnimation.setDuration(650);
        inAnimation.setFillAfter(true);
        inAnimation.setInterpolator(new LinearInterpolator());
        return inAnimation;
    }

    //这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
        return LayoutInflater.from(mContext).inflate(R.layout.autoview_switcher_layout, null, false);
    }

    //定义动作，向上滚动翻页
    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
            mInUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setOutAnimation(mOutUp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
            mOutUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setInAnimation(mInUp);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

}
