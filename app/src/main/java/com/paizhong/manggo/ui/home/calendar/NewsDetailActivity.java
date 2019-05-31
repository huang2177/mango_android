package com.paizhong.manggo.ui.home.calendar;

import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.home.CalendarDetailBean;
import com.paizhong.manggo.utils.ViewHelper;

import butterknife.BindView;

/**
 * Des: 财经日历 详情
 * Created by hs on 2018/6/28 0028 15:19
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailContract.View {
    @BindView(R.id.iv_calendar_bg)
    ImageView ivCalendarBg;
    @BindView(R.id.tv_detail_title)
    TextView tvTitle;
    @BindView(R.id.tv_detail_time)
    TextView tvTime;
    @BindView(R.id.tv_detail_company)
    TextView tvCompany;
    @BindView(R.id.tv_detail_rate)
    TextView tvRate;
    @BindView(R.id.tv_detail_way)
    TextView tvWay;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;


    @Override

    public int getLayoutId() {
        return R.layout.activity_calendar_news_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void onDestroy() {
        ViewHelper.recycleImageView(ivCalendarBg);
        super.onDestroy();
    }

    @Override
    public void loadData() {
        ViewHelper.setImageView(ivCalendarBg, getResources(), R.mipmap.bg_calendar2);
        String newsId = getIntent().getStringExtra("id");
        mTitle.setTitle(true, "财经日历");

        mPresenter.getNewsDetail(newsId);
    }


    @Override
    public void bindNewsDetail(CalendarDetailBean data) {
        tvTitle.setText(data.title);
        tvContent.setText(data.impact);
        tvContent1.setText(data.paraphrase);
        tvContent2.setText(data.focus);

        tvWay.setText(Html.fromHtml("统计方法：<font color='#333333'>" + data.method + "</font>"));
        tvRate.setText(Html.fromHtml("发布频率：<font color='#333333'>" + data.frequency + "</font>"));
        tvTime.setText(Html.fromHtml("下次公布时间：<font color='#333333'>" + data.publictime + "</font>"));
        tvCompany.setText(Html.fromHtml("数据公布机构：<font color='#333333'>" + data.institutions + "</font>"));
    }
}
