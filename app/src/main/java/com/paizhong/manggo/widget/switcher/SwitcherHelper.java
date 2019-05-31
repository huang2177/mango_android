package com.paizhong.manggo.widget.switcher;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.home.ReportBean;
import com.paizhong.manggo.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Des: 滚动公告 辅助类
 * Created by hs on 2018/6/1 0001 17:12
 */
public class SwitcherHelper {

    private List<NoticeListBean> mNotices;
    private List mList;
    private AutoViewSwitcher mSwitcher;
    private static final int DEFAULT_TIME = 3200;
    private static final int SCROLL_AD_CHANG = 1;

    private int curPosition;
    private int type; // 1 首页  2 跟买
    private List<ReportBean> mReportBeans;


    public SwitcherHelper(AutoViewSwitcher switcher) {
        mSwitcher = switcher;
    }

    public void setNotices(int type, List<NoticeListBean> notices) {
        this.type = type;
        mNotices = notices;
    }

    public void setReportBeans(int type, List<ReportBean> reportBeans) {
        this.type = type;
        mReportBeans = reportBeans;
    }

    public void start() {
        if (mHandler != null) {
            mHandler.removeMessages(SCROLL_AD_CHANG);
            mHandler.sendEmptyMessage(SCROLL_AD_CHANG);
            mList = type == 1 ? mReportBeans : mNotices;
        }
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.removeMessages(SCROLL_AD_CHANG);
            curPosition = 0;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what != SCROLL_AD_CHANG) {
                    return;
                }
                if (mList == null || mList.size() == 0) {
                    return;
                }
                mSwitcher.next();
                View nextView = mSwitcher.getNextView();
                setText(nextView, curPosition);
                mSwitcher.showNext();

                if (curPosition >= (mList.size() - 1)) {
                    curPosition = 0;
                } else {
                    curPosition++;
                }

                mHandler.sendEmptyMessageDelayed(SCROLL_AD_CHANG, DEFAULT_TIME);

            } catch (Exception e) {
                start();
            }
        }
    };

    /**
     * 设置滚动的文字
     *
     * @param v
     * @param position
     */
    private void setText(View v, int position) {
        TextView tvTime = v.findViewById(R.id.tv_time);
        TextView tvContent = v.findViewById(R.id.tv_content);
        if (type == 1) {
            ReportBean bean = mReportBeans.get(position);
            String flag = bean.flag == 0 ? "买涨" : "买跌";
            String text1 = "<font color = '#F74F54'>" + flag + bean.name + "</font>";
            String text2 = "<font color = '#1AC47A'>" + flag + bean.name + "</font>";
            tvContent.setText(Html.fromHtml(bean.nickName
                    + (bean.flag == 0 ? text1 : text2)
                    + "盈利<font color = '#F74F54'>" + bean.closeProfit + "</font>元"));
            tvTime.setText(TimeUtil.getFriendTimeOffer(new Date().getTime() - mReportBeans.get(position).closeTime));
        } else {
            tvContent.setText(Html.fromHtml("<font color = '#333333'>"
                    + mNotices.get(position).fellowNickName + "</font>" +
                    "<font color = '#878787'>参与了</font><font color = '#333333'>"
                    + mNotices.get(position).fellowerNickName + "</font>" +
                    "<font color = '#878787'>的跟买</font>"));
        }
    }
}
