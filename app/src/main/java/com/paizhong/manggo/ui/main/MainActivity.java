package com.paizhong.manggo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.other.ChatMsGuidedDialog;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.dialog.signin.SigninNewDialog;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.events.EventController;
import com.paizhong.manggo.http.Mq;
import com.paizhong.manggo.ui.follow.FollowFragment;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.ui.home.HomeFragment;
import com.paizhong.manggo.ui.market.MarketFragment;
import com.paizhong.manggo.ui.trade.TradeFragment;
import com.paizhong.manggo.ui.trade.openposition.OpenPositionFragment;
import com.paizhong.manggo.ui.user.UserFragment;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.widget.PushTextView;
import com.paizhong.manggo.widget.nav.MenuView;
import com.paizhong.manggo.widget.nav.NavigationView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import ezy.boost.update.AppManager;
import ezy.boost.update.UpdateHelper;

/**
 * Created by zab on 2018/8/15 0015.
 * ppmgtaojin://mgtaojin/mgtaojin/main?page=0&smPage=0&buildUri="xxx Schame"
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View
        , NavigationView.OnNavSelectedListener {

    @BindView(R.id.nav_bar)
    public NavigationView mNavView;
    @BindView(R.id.tv_message)
    PushTextView tvMessage;
    @BindView(R.id.bottom_container)
    public LinearLayout mContainer;

    public int page;
    public int smPage =-1;
    public String mProductID;

    private boolean mMsgGuided =false;

    public static boolean mIsMainStart = false; //mainActivity 是否启动

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(MainActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.mIsMainStart = true;
        super.onCreate(savedInstanceState);
        JPushInterface.setAlias(this, 1, SpUtil.getString(Constant.USER_KEY_PHONE));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        page = intent.getIntExtra("page", -1);
        smPage = intent.getIntExtra("smPage", -1);
        mProductID = intent.getStringExtra("productID");
        selectPage(page, smPage);
    }


    @Override
    public void loadData() {
        if (TextUtils.isEmpty(AppApplication.getConfig().getAppToken())
                && !TextUtils.isEmpty(SpUtil.getString(Constant.USER_KEY_TOKEN))) {
            AppApplication.getConfig().initUserParam();
        }
        if (TextUtils.isEmpty(AppApplication.getConfig().getAppVersion())) {
            AppApplication.getConfig().initParam(AppApplication.getInstance());
        }
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        page = getIntent().getIntExtra("page", 0);
        smPage = getIntent().getIntExtra("smPage", -1);
        mProductID = getIntent().getStringExtra("productID");
        initFragment();
        selectPage(page, smPage);
        if (SpUtil.getBoolean(Constant.CACHE_IS_FIRST_GUIDED)) {
            if (!AppApplication.getConfig().mIsAuditing){
                mPresenter.updateApp();
            }
        }

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login() && !TextUtils.isEmpty(tvMessage.getPhone())) {
                    Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
                    intent.putExtra("phone", tvMessage.getPhone());
                    startActivity(intent);
                    tvMessage.setGone();
                }
            }
        });

        if (!AppApplication.getConfig().mIsAuditing && AppApplication.getConfig().splash) {
            AppApplication.getConfig().splash = false;
            String splashUlr = SpUtil.getString(Constant.USER_KEY_SPLASH_URL);
            if (!TextUtils.isEmpty(splashUlr)) {
                Intent intentWeb = new Intent(MainActivity.this, WebViewActivity.class);
                intentWeb.putExtra("url", splashUlr);
                intentWeb.putExtra("builderPar", false);
                intentWeb.putExtra("noTitle", SpUtil.getBoolean(Constant.USER_KEY_SPLASH_HEAD));
                intentWeb.putExtra("title", SpUtil.getString(Constant.USER_KEY_SPLASH_NAME));
                startActivity(intentWeb);
            }
        }
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, HomeFragment.getInstance());
        transaction.add(R.id.container, MarketFragment.getInstance());
        transaction.add(R.id.container, TradeFragment.getInstance());

        if (AppApplication.getConfig().mIsAuditing){
            if (!AppApplication.getConfig().mIsUserAuditing){
                transaction.add(R.id.container, UserFragment.getInstance());
            }
        }else {
            transaction.add(R.id.container, FollowFragment.getInstance());
            transaction.add(R.id.container, UserFragment.getInstance());
        }
        transaction.hide(HomeFragment.getInstance());
        transaction.hide(MarketFragment.getInstance());
        transaction.hide(TradeFragment.getInstance());

        if (AppApplication.getConfig().mIsAuditing){
            if (!AppApplication.getConfig().mIsUserAuditing){
                transaction.hide(UserFragment.getInstance());
            }
        }else {
            transaction.hide(FollowFragment.getInstance());
            transaction.hide(UserFragment.getInstance());
        }
        transaction.commitNowAllowingStateLoss();
        mNavView.setNavSelectedListener(this);
    }


    public static int HOME_PAGE = 0; //首页
    public static int MARKET_PAGE = 1;//行情
    public static int TRADE_PAGE = 2; //交易
    public static int FOLLOW_PAGE = 3;//跟单
    public static int USER_PAGE = 4; //我的

    /**
     * 底部导航栏选中事件
     *
     * @param page   0 首页  1 行情  2 交易  3跟单  4 我的
     * @param smPage （page 2: 0建仓  1持仓   2 挂单 3 资金）page 0: 0盈利广场 1交易机会 2财经日历 3突发行情 4大事件
     */
    public void selectPage(int page, int smPage) {
        if (page != -1) {
            mNavView.setCurrentNav(page);
            if (smPage !=-1){
                if (page == TRADE_PAGE && mNavView.isCurrentNav(TRADE_PAGE)) {
                    TradeFragment.getInstance().setSmPageCurrentTab(smPage,mProductID,true);
                    this.smPage = -1;
                    this.mProductID = "";
                }else if (page == HOME_PAGE && mNavView.isCurrentNav(HOME_PAGE)){
                    HomeFragment.getInstance().setCurrentTab(smPage);
                    this.smPage = -1;
                }
            }
        }
    }


    @Override
    public void onNavSelected(MenuView menuView, int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!HomeFragment.getInstance().isHidden())
            transaction.hide(HomeFragment.getInstance());
        if (!MarketFragment.getInstance().isHidden())
            transaction.hide(MarketFragment.getInstance());
        if (!TradeFragment.getInstance().isHidden())
            transaction.hide(TradeFragment.getInstance());
        if (!AppApplication.getConfig().mIsAuditing && !FollowFragment.getInstance().isHidden()){
            transaction.hide(FollowFragment.getInstance());
        }
        if (AppApplication.getConfig().mIsAuditing){
            if (!AppApplication.getConfig().mIsUserAuditing && !UserFragment.getInstance().isHidden()){
                transaction.hide(UserFragment.getInstance());
            }
        }else {
            if (!UserFragment.getInstance().isHidden()){
                transaction.hide(UserFragment.getInstance());
            }
        }
        switch (position) {
            case 0: //首页
                transaction.show(HomeFragment.getInstance());
                if (smPage !=-1){
                    HomeFragment.getInstance().setCurrentTab(smPage);
                    this.smPage = -1;
                }
                break;
            case 1: //行情
                transaction.show(MarketFragment.getInstance());
                break;
            case 2: //交易
                transaction.show(TradeFragment.getInstance());
                if (!AppApplication.getConfig().mIsAuditing && smPage !=-1){
                    TradeFragment.getInstance().setSmPageCurrentTab(smPage,mProductID,true);
                    this.smPage = -1;
                    this.mProductID = "";
                }
                if (!AppApplication.getConfig().mIsAuditing && !mMsgGuided && !SpUtil.getBoolean(Constant.CACHE_IS_CHAT_MSG)){
                    mMsgGuided = true;
                    SpUtil.putBoolean(Constant.CACHE_IS_CHAT_MSG,true);
                    new ChatMsGuidedDialog(MainActivity.this).show();
                }
                break;
            case 3: //跟单
                if (AppApplication.getConfig().mIsAuditing){
                    if (!AppApplication.getConfig().mIsUserAuditing){
                        transaction.show(UserFragment.getInstance());
                    }
                }else {
                    transaction.show(FollowFragment.getInstance());
                    mNavView.setNeedRedDot(FOLLOW_PAGE, false);
                }
                break;
            case 4: //我的
                if (!AppApplication.getConfig().mIsAuditing){
                    transaction.show(UserFragment.getInstance());
                }
                break;
        }
        transaction.commitNowAllowingStateLoss();
    }


    /*******
     * 登入登出事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AppAccountEvent event) {
        EventController.getInstance().handleMessage(event);
        OpenPositionFragment.getInstance().queryVipUser();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseUIRefreshEvent event) {
        if (event.mType == 3) {//跟买红点
            if (!AppApplication.getConfig().mIsAuditing){
                if (!mNavView.isCurrentNav(FOLLOW_PAGE)) {
                    mNavView.setNeedRedDot(FOLLOW_PAGE, true);
                }
            }
        } else if (event.mType == 4) {//跟买关注提示
            if (!AppApplication.getConfig().mIsAuditing){
                tvMessage.pushMsg(event.value1, event.value2, event.value3);
            }
        }
    }


    public boolean isPlaceOrderEmpty() {
        return mPlaceOrderDialog == null;
    }

    private PlaceOrderDialog mPlaceOrderDialog;

    public PlaceOrderDialog placeOrder() {
        if (mPlaceOrderDialog == null) {
            mPlaceOrderDialog = new PlaceOrderDialog(MainActivity.this);
        }
        return mPlaceOrderDialog;
    }


    //版本更新

    @Override
    public void bindUpdateApp(UpdateAppBean updateAppBean) {
        if (updateAppBean != null
                && !TextUtils.isEmpty(AppApplication.getConfig().getAppVersion())
                && !TextUtils.isEmpty(updateAppBean.versionNum)
                && !TextUtils.equals(updateAppBean.versionNum, AppApplication.getConfig().getAppVersion())
                && (updateAppBean.isUpdate == 1 || updateAppBean.isUpdate == 0)
                && !TextUtils.isEmpty(updateAppBean.url)) {
            String title = "当前版本:" + AppApplication.getConfig().getAppVersion() + ",最新版本:" + updateAppBean.versionNum;
            UserFragment.getInstance().setVersionNew();
            UpdateHelper.getInstance().update(
                    this,
                    updateAppBean.isUpdate == 1,
                    title,
                    updateAppBean.versionDetail.replace("\\n", "\n"),
                    AndroidUtil.getVersionCode(),
                    updateAppBean.versionNum,
                    updateAppBean.url,
                    updateAppBean.packageMd5);
        } else {
            if (!AppApplication.getConfig().mIsAuditing && AppApplication.getConfig().isLoginStatus()) {
                mPresenter.validIsUpPop();
            }
        }
    }

    //签到

    @Override
    public void bindValidIsUpPop() {
        if (AppApplication.getConfig().isLoginStatus()) {
            new SigninNewDialog(MainActivity.this).httpShowSigninDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPlaceOrderEmpty()) {
            mPlaceOrderDialog.startRunHttp();
        }
        //OpenPositionFragment.getInstance().startMq();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isPlaceOrderEmpty()) {
            mPlaceOrderDialog.stopRun();
        }
    }

    @Override
    protected void onDestroy() {
        //HttpManager.getInstance().zipClose();
        super.onDestroy();
        if (!isPlaceOrderEmpty()) {
            mPlaceOrderDialog.onDestroy();
            mPlaceOrderDialog = null;
        }
        EventBus.getDefault().unregister(this);
        Mq.get().stopRun();
        AppManager.getInstance().AppExit(this);
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showErrorMsg("再按一次退出程序", null);
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
