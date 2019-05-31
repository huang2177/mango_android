package com.paizhong.manggo.ui.home.module;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.GlideSimpleTarget;
import com.paizhong.manggo.bean.home.RedPacketBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.home.HomeFragment;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.CountDownTimeView;
import com.paizhong.manggo.widget.RefreshView;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页广告活动 Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class ActivityModule extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener
        , HandlerHelper.OnExecuteListener
        , CountDownTimeView.OnCountDownListener
        , GlideSimpleTarget.OnGifReadyListener {
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.home_activity_iv1)
    ImageView mImage1;
    @BindView(R.id.home_activity_iv2)
    ImageView mImage2;
    @BindView(R.id.home_activity_iv3)
    ImageView mImage3;
    @BindView(R.id.tv_all_point)
    RefreshView tvAllPoint;
    @BindView(R.id.count_down_time)
    CountDownTimeView tvDownTime;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.iv_degree_scale)
    ImageView ivDegree;
    @BindView(R.id.container)
    RelativeLayout mContainer;

    private BannerBean mBean;
    private List<BannerBean> mList;

    private HandlerHelper mHelper;
    private GlideSimpleTarget target;

    private int excuteNum;

    public ActivityModule(HomeFragment fragment, HandlerHelper helper) {
        super(fragment.mActivity);
        this.mHelper = helper;
        setHeight();
        setListener();
    }

    private void setHeight() {
        target = new GlideSimpleTarget(this);

        int with = DeviceUtils.getScreenWidth(mContext) - DeviceUtils.dip2px(mContext, 20);
        LayoutParams params = (LayoutParams) root.getLayoutParams();
        params.height = (int) (with * 0.437);
        root.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mProgressBar.getLayoutParams();
        params1.width = (int) (with * 0.61);
        mProgressBar.setLayoutParams(params1);

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivDegree.getLayoutParams();
        params2.width = (int) (with * 0.61);
        ivDegree.setLayoutParams(params2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_home_activity;
    }

    public void setListener() {
        mImage1.setOnClickListener(this);
        mImage2.setOnClickListener(this);
        mImage3.setOnClickListener(this);
        tvDownTime.setOnCountDownListener(this);
    }

    @Override
    protected void onResume() {
        if (target != null && isVisible()) {
            target.start();
        }
    }

    @Override
    protected void onPause() {
        if (target != null) {
            target.stop();
            excuteNum = 0;
        }
    }

    @Override
    public void onVisible(boolean visible) {
        super.onVisible(visible);
        if (visible && target != null) {
            target.start();
        } else if (target != null) {
            target.stop();
            excuteNum = 0;
        }
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        if (!isRefresh || excuteNum == 0) {
            queryRedPacketInfo();
        }
        mPresenter.selectBanner(ViewConstant.HOME_ACTIVITY);
        mPresenter.selectBanner(ViewConstant.HOME_BANNER_BOTTOM);
    }

    /**
     * 查询红包相关信息
     */
    private void queryRedPacketInfo() {
        if (excuteNum == 0) { //每隔10秒执行一次
            mPresenter.queryRedPacketInfo();
            excuteNum = 10;
        } else {
            excuteNum--;
        }
    }


    @Override
    public void onGifReady(GifDrawable gif) {
        mImage1.setImageDrawable(gif);
        if (isVisible() && target != null) {
            target.start();
        }
    }


    @Override
    public void execute(boolean args) {
        tvDownTime.countDown();
        queryRedPacketInfo();
    }

    @Override
    public void bindRedPacketInfo(RedPacketBean bean) {
        if (bean != null) {
            mHelper.execute(this);
            tvDownTime.setRemainTime(bean);
            tvAllPoint.addText(bean.totalScore);
            onCountDown((int) ((Integer.parseInt(bean.totalScore) / 100000f) * 100));
        } else {
            mHelper.cancelExecute(this);
        }
    }

    @Override
    public void bindActivity1(BannerBean bean, boolean isGif) {
        mBean = bean;
        setVisibility(VISIBLE);
        if (isGif) {
            mContainer.setVisibility(VISIBLE);
            ImageUtils.displayGif(mContext, bean.activityImageUrl, target);
        } else {
            mContainer.setVisibility(GONE);
            ImageUtils.display(bean.activityImageUrl, mImage1, R.mipmap.other_empty);

            mHelper.cancelExecute(this);
        }
    }

    @Override
    public void bindActivity2(List<BannerBean> list) {
        mList = list;
        setVisibility(VISIBLE);
        ImageUtils.display(list.get(0).activityImageUrl, mImage2, R.mipmap.other_empty);
        ImageUtils.display(list.get(1).activityImageUrl, mImage3, R.mipmap.other_empty);
    }

    @Override
    public void onCountDown(int progress) {
        if (mProgressBar.getProgress() != progress) {
            mProgressBar.setProgress(progress);
        }
    }

    @Override
    public void onRefreshTime() {
        excuteNum = 0;
        queryRedPacketInfo();
    }

    @Override
    public void onCountDownError() {
        mHelper.cancelExecute(this);
    }

    @Override
    public void onClick(View v) {
        String url = null;
        String title = null;
        int isNeedHead = -1;
        switch (v.getId()) {
            case R.id.home_activity_iv1:
                title = mBean.activityName;
                url = mBean.activityHtml5Url;
                isNeedHead = mBean.isNeedHead;
                break;
            case R.id.home_activity_iv2:
                title = mList.get(0).activityName;
                url = mList.get(0).activityHtml5Url;
                isNeedHead = mList.get(0).isNeedHead;
                break;
            case R.id.home_activity_iv3:
                title = mList.get(1).activityName;
                url = mList.get(1).activityHtml5Url;
                isNeedHead = mList.get(1).isNeedHead;
                break;
        }
        if (v.getId() == R.id.home_activity_iv1 && !mActivity.login()) {
            return;
        }
        Intent intentAbout = new Intent(mContext, WebViewActivity.class);
        intentAbout.putExtra("url", url);
        intentAbout.putExtra("title", title);
        intentAbout.putExtra("noTitle", isNeedHead == 0);
        mContext.startActivity(intentAbout);
    }
}
