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
import com.paizhong.manggo.events.AppUserAccountEvent;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页广告活动 Module (改版2)
 * Created by huang on 2018/8/17 0017 11:26
 */
public class ActivityModule2 extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener {
    @BindView(R.id.iv_aitao)
    ImageView ivAiTao;
    @BindView(R.id.iv_score)
    ImageView ivScore;
    @BindView(R.id.iv_new_user)
    ImageView ivNewUser;
    @BindView(R.id.iv_my_account)
    ImageView ivMyAccount;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.fl_score_root)
    FrameLayout scoreRoot;

    private BannerBean mBean;
    private List<BannerBean> mList;

    public ActivityModule2(Context context) {
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
    public void setAuditing(boolean auditing) {
        setVisibility(auditing ? GONE : VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.module_home_activity2;
    }

    @Override
    protected boolean isEmpty() {
        return mBean == null || mList == null;
    }

    private void initView() {
        int width = DeviceUtils.getScreenWidth(mContext);
        float unitWidth = (width - DeviceUtils.dip2px(mContext, 50)) / 2.64f;

        /*爱淘*/
        ViewGroup.LayoutParams params = ivAiTao.getLayoutParams();
        params.height = (int) (width * 0.11);
        ivAiTao.setLayoutParams(params);

        /*新人活动，翻牌活动*/
        ViewGroup.LayoutParams params1 = ivNewUser.getLayoutParams();
        params1.height = (int) (unitWidth * 0.52);
        ivNewUser.setLayoutParams(params1);

        /*积分商城*/
        ViewGroup.LayoutParams params2 = scoreRoot.getLayoutParams();
        params2.height = params1.height;
        scoreRoot.setLayoutParams(params2);
        tvScore.setPadding(Math.round(unitWidth * 0.08f), Math.round(params1.height * 0.635f), 0, 0);

        /*我的投资*/
        ViewGroup.LayoutParams params3 = ivMyAccount.getLayoutParams();
        params3.height = params1.height;
        ivMyAccount.setLayoutParams(params3);

        ivAiTao.setOnClickListener(this);
        scoreRoot.setOnClickListener(this);
        ivNewUser.setOnClickListener(this);
        ivMyAccount.setOnClickListener(this);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
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
        ImageUtils.display(list.get(2).activityImageUrl, ivMyAccount);
        tvScore.setVisibility(mList.get(1).activityName.contains("积分商城") ? VISIBLE : GONE);
    }

    @Override
    public void bindUserScore(IntegralBean bean) {
        tvScore.setText(bean.myscore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_aitao:
                parseParams(0, 0);
                break;
            case R.id.iv_new_user:
                parseParams(1, 0);
                break;
            case R.id.fl_score_root:
                parseParams(1, 1);
                break;
            case R.id.iv_my_account:
                if (mActivity.login()) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("page", MainActivity.TRADE_PAGE);
                    intent.putExtra("smPage", 1);
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    private void parseParams(int type, int index) {
        String url = null;
        String title = null;
        int isNeedHead = -1;
        boolean isNeedLogin = false;
        switch (type) {
            case 0:
                if (mBean != null) {
                    title = mBean.activityName;
                    url = mBean.activityHtml5Url;
                    isNeedHead = mBean.isNeedHead;
                    isNeedLogin = mBean.isLogin == 1;
                }
                break;
            case 1:
                if (mList != null) {
                    title = mList.get(index).activityName;
                    url = mList.get(index).activityHtml5Url;
                    isNeedHead = mList.get(index).isNeedHead;
                    isNeedLogin = mList.get(index).isLogin == 1;
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

    /***
     * app 登录成功 ，或者刷新用户积分
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppUserAccountEvent event) {
        if (event.code == 0) {
            tvScore.setText("0");
        }
        mPresenter.selectBanner(ViewConstant.HOME_BANNER_BOTTOM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
