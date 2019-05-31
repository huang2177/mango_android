package com.paizhong.manggo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.paizhong.manggo.bean.home.RedPacketBean;

/**
 * Des: 倒计时TextView
 * Created by huang on 2018/9/17 0017 17:18
 */
public class CountDownTimeView extends AppCompatTextView {
    private static final long ONE_HOUR = 1000 * 60 * 60;

    private int progress;
    private long targetData;
    private StringBuffer mTime;
    private OnCountDownListener listener;

    private RedPacketBean data;


    public CountDownTimeView(Context context) {
        this(context, null);
    }

    public CountDownTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTime = new StringBuffer();
    }

    public void setRemainTime(RedPacketBean data) {
        this.data = data;
        this.targetData = data.remainTime + System.currentTimeMillis();
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        this.listener = listener;
    }

    /**
     * 倒计时
     */
    public void countDown() {
        mTime.delete(0, mTime.length());
        long curDate = System.currentTimeMillis();
        long diffTime = targetData - curDate;
        try {
            long days = diffTime / (ONE_HOUR * 24);
            long hours = (diffTime - days * (ONE_HOUR * 24)) / ONE_HOUR;
            long minute = (diffTime - days * (ONE_HOUR * 24) - hours * ONE_HOUR) / (1000 * 60);
            long second = Math.round((float) diffTime % 60000 / 1000);
            if (hours >= 10) {
                mTime.append(hours).append(":");
            } else if (hours > 0) {
                mTime.append("0").append(hours).append(":");
            }

            if (minute >= 10) {
                mTime.append(minute).append(":");
            } else if (minute >= 0) {
                mTime.append("0").append(minute).append(":");
            }

            if (second >= 10) {
                mTime.append(second);
            } else if (second >= 0) {
                mTime.append("0").append(second);
            }
            if (hours == 0 && minute == 0 && second == 0) {
                listener.onRefreshTime();
                targetData = curDate + getNextRemainTime();
            }
            progress = (int) (100 - ((float) diffTime / data.preRemainTime * 100));
        } catch (Exception ignored) {
            progress = 100;
            mTime.append("00:00:00");
            //listener.onCountDownError();
        }
        setText(mTime.toString());
        //listener.onCountDown(progress);
    }

    /**
     * 获取下一个时间段的时间戳
     */
    private long getNextRemainTime() {
        if (data.time1 > data.time) {
            return (data.time1 - data.time) * ONE_HOUR;
        } else {
            return (24 - data.time + data.time1) * ONE_HOUR;
        }
    }


    public interface OnCountDownListener {
        void onRefreshTime();

        void onCountDownError();

        void onCountDown(int progress);
    }
}
