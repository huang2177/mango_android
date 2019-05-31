package com.paizhong.manggo.ui.kchart.utils;


import com.paizhong.manggo.ui.kchart.bean.KLineBean;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.bean.MinutesBean;

public class KLineChartController {
    private static KLineChartController sInstance = new KLineChartController();

    public static KLineChartController getInstance() {
        return sInstance;
    }

    private KLineChartController() {
    }

    private OnMinuteLineOnTouchListener mOnMinuteLineOnTouchListener;

    private onKLineOnTouchListener mOnKlineOnTouchListener;


    private OnActivityMinuteLineOnTouchListener mOnActivityMinuteLineOnTouchListener;

    private onActivityKLineOnTouchListener mOnActivityKlineOnTouchListener;


    public void setOnMinuteLineOnTouchListener(OnMinuteLineOnTouchListener mOnMinuteLineOnTouchListener) {
        this.mOnMinuteLineOnTouchListener = mOnMinuteLineOnTouchListener;
    }

    public OnMinuteLineOnTouchListener getOnMinuteLineOnTouchListener() {
        return mOnMinuteLineOnTouchListener;
    }

    public void setOnKlineOnTouchListener(onKLineOnTouchListener mOnKlineOnTouchListener) {
        this.mOnKlineOnTouchListener = mOnKlineOnTouchListener;
    }

    public onKLineOnTouchListener getOnKlineOnTouchListener() {
        return mOnKlineOnTouchListener;
    }

    public void setActivityOnMinuteLineOnTouchListener(OnActivityMinuteLineOnTouchListener mOnMinuteLineOnTouchListener) {
        this.mOnActivityMinuteLineOnTouchListener = mOnMinuteLineOnTouchListener;
    }

    public OnActivityMinuteLineOnTouchListener getOnActivityMinuteLineOnTouchListener() {
        return mOnActivityMinuteLineOnTouchListener;
    }

    public void setActivityOnKlineOnTouchListener(onActivityKLineOnTouchListener mOnKlineOnTouchListener) {
        this.mOnActivityKlineOnTouchListener = mOnKlineOnTouchListener;
    }

    public onActivityKLineOnTouchListener getOnActivityKlineOnTouchListener() {
        return mOnActivityKlineOnTouchListener;
    }

    public interface OnMinuteLineOnTouchListener {
        void onValueSelected(MinutesBean bean);

        void onNothingSelected();
    }

    public interface onKLineOnTouchListener {
        void onValueSelected(KLineBean bean);

        void onNothingSelected();
    }


    public interface OnActivityMinuteLineOnTouchListener {
        void onValueSelected(MinuteHourBean bean);

        void onNothingSelected();
    }

    public interface onActivityKLineOnTouchListener {
        void onValueSelected(KLineBean bean);

        void onNothingSelected();
    }


    private onCardNoBindListener onCardNoBindListener;

    public void setOnCardNoBindListener(KLineChartController.onCardNoBindListener onCardNoBindListener) {
        this.onCardNoBindListener = onCardNoBindListener;
    }

    public KLineChartController.onCardNoBindListener getOnCardNoBindListener() {
        return onCardNoBindListener;
    }

    public interface onCardNoBindListener {
       // void onChanged(CardBindInfo model);
    }

    //分时图行情气泡下的小圆点点击事件
    private OnPointClickListener pointClickListener;

    //获得接口对象的方法。
    public void setOnPointClickListener(OnPointClickListener listener) {
        this.pointClickListener = listener;
    }
    //定义一个接口
    public interface OnPointClickListener{
        void onPointClick(boolean isShow);
    }

    public OnPointClickListener getOnPointClickListener() {
        return pointClickListener;
    }

}
