package com.paizhong.manggo.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.paizhong.manggo.R;

public class AnimationUtils {

    //旋转动画
    public static void rotateOne(View v){
        //创建旋转动画 对象   fromDegrees:旋转开始的角度  toDegrees:结束的角度
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画的显示时间
        rotateAnimation.setDuration(200);
        //设置动画重复播放几次
        rotateAnimation.setRepeatCount(1);
        //设置动画插值器
        rotateAnimation.setInterpolator(new LinearInterpolator());
        //设置动画重复播放的方式,翻转播放
        rotateAnimation.setRepeatMode(Animation.RESTART);
        //拿着imageview对象来运行动画效果
        v.setAnimation(rotateAnimation);
    }

    public static void startRightIn(Context context,View view){
        Animation mAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.register_right_in);
        mAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(mAnimation);
    }

    public static void startLeftIn(Context context,View view){
        Animation mAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.register_left_in);
        mAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(mAnimation);
    }

}
