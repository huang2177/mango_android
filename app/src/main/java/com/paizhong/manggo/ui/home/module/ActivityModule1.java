package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页广告活动 Module (改版)
 * Created by huang on 2018/8/17 0017 11:26
 */
public class ActivityModule1 extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener {
    @BindView(R.id.iv_aitao)
    ImageView ivAiTao;
    @BindView(R.id.iv_score)
    ImageView ivScore;
    @BindView(R.id.iv_new_user)
    ImageView ivNewUser;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.fl_score_root)
    FrameLayout scoreRoot;

    private BannerBean mBean;
    private List<BannerBean> mList;

    public ActivityModule1(Context context) {
        super(context);
        initView();
        onRefresh(false);
    }

    @Override
    protected void onResume() {
        queryUserScore(isVisible() && tvScore.getVisibility() == VISIBLE);
    }

    @Override
    public void onVisible(boolean visible) {
        super.onVisible(visible);
        queryUserScore(visible && tvScore.getVisibility() == VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_home_activity1;
    }

    @Override
    protected boolean isEmpty() {
        return mBean == null || mList == null;
    }

    private void initView() {
        int width = DeviceUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = ivAiTao.getLayoutParams();
        params.height = (int) (width * 0.16);
        ivAiTao.setLayoutParams(params);

        final int imgWidth = width / 2 - DeviceUtils.dip2px(mContext, 15);
        final ViewGroup.LayoutParams params1 = ivNewUser.getLayoutParams();
        params1.height = (int) (imgWidth * 0.382);
        ivNewUser.setLayoutParams(params1);

        ViewGroup.LayoutParams params2 = scoreRoot.getLayoutParams();
        params2.height = params1.height;
        scoreRoot.setLayoutParams(params2);

        Paint.FontMetrics fontMetrics = tvScore.getPaint().getFontMetrics();
        tvScore.setPadding( Math.round(imgWidth * 0.3851f),Math.round(params1.height * 0.563f - fontMetrics.descent),0,0);

        ivAiTao.setOnClickListener(this);
        scoreRoot.setOnClickListener(this);
        ivNewUser.setOnClickListener(this);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.selectBanner(ViewConstant.HOME_ACTIVITY);
        mPresenter.selectBanner(ViewConstant.HOME_BANNER_BOTTOM);
        queryUserScore(mList == null || tvScore.getVisibility() == VISIBLE);
    }

    private void queryUserScore(boolean isVisible) {
        if (isVisible) mPresenter.queryUserScore();
    }

    @Override
    public void bindActivity1(BannerBean bean, boolean isGif) {
        mBean = bean;
        ImageUtils.display(bean.activityImageUrl, ivAiTao);
    }

    @Override
    public void bindActivity2(List<BannerBean> list) {
        mList = list;
        ImageUtils.display(list.get(1).activityImageUrl, ivScore);
        ImageUtils.display(list.get(0).activityImageUrl, ivNewUser);
        tvScore.setVisibility(mList.get(1).activityName.contains("积分商城") ? VISIBLE : GONE);
    }

    @Override
    public void bindUserScore(IntegralBean bean) {
        tvScore.setText(bean.myscore);
    }

    @Override
    public void onClick(View v) {
        String url = null;
        String title = null;
        int isNeedHead = -1;
        boolean isNeedLogin = false;
        switch (v.getId()) {
            case R.id.iv_aitao:
                if (mBean != null) {
                    title = mBean.activityName;
                    url = mBean.activityHtml5Url;
                    isNeedHead = mBean.isNeedHead;
                    isNeedLogin = mBean.isLogin == 1;
                }
                break;
            case R.id.iv_new_user:
                if (mList != null && mList.size() >= 2) {
                    title = mList.get(0).activityName;
                    url = mList.get(0).activityHtml5Url;
                    isNeedHead = mList.get(0).isNeedHead;
                    isNeedLogin = mList.get(0).isLogin == 1;
                }
                break;
            case R.id.fl_score_root:
                if (mList != null && mList.size() >= 2) {
                    title = mList.get(1).activityName;
                    url = mList.get(1).activityHtml5Url;
                    isNeedHead = mList.get(1).isNeedHead;
                    isNeedLogin = mList.get(1).isLogin == 1;
                }
                break;
        }
        if ((!isNeedLogin || mActivity.login()) && isNeedHead != -1) {
            Intent intentAbout = new Intent(mContext, WebViewActivity.class);
            intentAbout.putExtra("url", url);
            intentAbout.putExtra("title", title);
            intentAbout.putExtra("noTitle", isNeedHead == 0);
            mContext.startActivity(intentAbout);
        }
    }
}
