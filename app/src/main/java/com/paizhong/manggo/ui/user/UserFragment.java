package com.paizhong.manggo.ui.user;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.TextUtils;
import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.ConfigUtil;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.events.AppUserAccountEvent;
import com.paizhong.manggo.events.JysAccountEvent;
import com.paizhong.manggo.events.UserChangeEvent;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeActivity;
import com.paizhong.manggo.ui.setting.head.SettingHeadActivity;
import com.paizhong.manggo.ui.setting.main.SettingMainActivity;
import com.paizhong.manggo.ui.setting.name.SettingNameActivity;
import com.paizhong.manggo.ui.voucher.VoucherZjActivity;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.SobotSdkUtils;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.DinTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.OnClick;
import ezy.boost.update.UpdateHelper;

import static com.paizhong.manggo.ui.setting.main.SettingMainActivity.IMAGE_HEAD_UPDATE;
import static com.paizhong.manggo.ui.setting.main.SettingMainActivity.NICKNAME_UPDATE;

/**
 * Des:
 * Created by huang on 2018/8/16 0016 17:04
 */
public class UserFragment extends BaseFragment<UserPresenter> implements UserContract.View{
    @BindView(R.id.user_nologin_tv_button)
    TextView userNologinTvButton;
    @BindView(R.id.user_login_cl)
    RelativeLayout userLoginCl;
    @BindView(R.id.user_nologin_cl)
    LinearLayout userNologinCl;
    @BindView(R.id.user_nologin_rl)
    RelativeLayout userNologinRl;   //未登录爱淘商城账户
    @BindView(R.id.login_center_cl)
    LinearLayout loginCenterCl;
    @BindView(R.id.user_bottom_ll_quan)
    LinearLayout userBottomLlQuan;
    @BindView(R.id.user_ll_new)
    LinearLayout userLlNew;
    @BindView(R.id.user_ll_service)
    LinearLayout userLlService;
    @BindView(R.id.user_ll_about)
    LinearLayout userLlAbout;
    @BindView(R.id.user_login_name)
    TextView userLoginName;
    @BindView(R.id.user_login_phone)
    TextView userLoginPhone;
    @BindView(R.id.user_bottom_new)
    TextView userBottomNew;
    @BindView(R.id.user_bottom_kefu)
    TextView userBottomKefu;
    @BindView(R.id.user_bottom_about)
    TextView userBottomAbout;
    @BindView(R.id.user_bottom_version)
    TextView userBottomVersion;
    @BindView(R.id.iv_bottom_version_new)
    ImageView userBottomVersionNew;
    @BindView(R.id.user_bottom_ll_update)
    LinearLayout userBottomLlUpdate;
    @BindView(R.id.user_bottom_ll_mall)
    LinearLayout userBottomLlMall;
    @BindView(R.id.user_login_setting)
    ImageView userLoginSetting;
    @BindView(R.id.nologin_center_tv_no)
    TextView nologinCenterTvNo;
    @BindView(R.id.user_bottom_point)
    TextView userBottomPoint;
    @BindView(R.id.user_bottom_quan)
    TextView userBottomQuan;
    @BindView(R.id.tv_price)
    DinTextView tvPrice;
    @BindView(R.id.tv_z_profit_loss)
    DinTextView tvZProfitLoss;
    @BindView(R.id.tv_available_balance)
    DinTextView tvAvailableBalance;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.user_login_head)
    CircleImageView userLoginHead;

    private double mAmount;
    private boolean mIsInitView = false;

    private static UserFragment mUserFragment;
    public static UserFragment getInstance() {
        if (mUserFragment == null) {
            synchronized (UserFragment.class){
                if (mUserFragment == null){
                    mUserFragment = new UserFragment();
                }
            }
        }
        return mUserFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

        userBottomVersion.setText("V"+AppApplication.getConfig().getAppVersion());
        if (AppApplication.getConfig().isLoginStatus()) {
            login();
        } else {
            nologin();
        }

        this.mIsInitView = true;

    }

    public void setVersionNew(){
        if (userBottomVersionNew !=null){
            userBottomVersionNew.setImageResource(R.mipmap.ic_version_new);
        }
    }

    @OnClick({R.id.user_nologin_tv_button, R.id.user_login_name, R.id.user_bottom_new,
            R.id.user_bottom_kefu, R.id.user_bottom_about, R.id.user_bottom_ll_update,
            R.id.user_login_setting,R.id.nologin_center_tv_no,R.id.user_bottom_ll_mall,
            R.id.user_login_head,R.id.tv_recharge,R.id.center_title,R.id.user_bottom_ll_quan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_login_head:
                //0 没请求或者失败 1 牛人  2 不是牛人
                if (!DeviceUtils.isFastDoubleClick()){
                    if (mValidRole == 0){
                        mPresenter.getValidRole(false);
                    }else {
                        Intent intent = new Intent(getActivity(),SettingHeadActivity.class);
                        intent.putExtra("validRole",(mValidRole == 1 ? true : false));
                        startActivityForResult(intent,IMAGE_HEAD_UPDATE);
                    }
                }
                break;
            case R.id.nologin_center_tv_no:
                if (!DeviceUtils.isFastDoubleClick()&& mActivity.login()) {
                }
                break;
            case R.id.user_nologin_tv_button:
                if (!DeviceUtils.isFastDoubleClick()&& mActivity.login()) {
                }
                break;
            case R.id.user_login_name:
                if (!DeviceUtils.isFastDoubleClick()){
                    if (mValidRole == 0) {
                        mPresenter.getValidRole(false);
                    }else {
                        Intent intent = new Intent(mActivity, SettingNameActivity.class);
                        intent.putExtra("validRole",(mValidRole == 1 ? true : false));
                        startActivityForResult(intent,NICKNAME_UPDATE);
                    }
                }
                break;
            case R.id.center_title:
                if (!DeviceUtils.isFastDoubleClick()){
                    Intent intentMoney = new Intent(mActivity, MainActivity.class);
                    intentMoney.putExtra("page",MainActivity.TRADE_PAGE);
                    intentMoney.putExtra("smPage",3);
                    startActivity(intentMoney);
                }
                break;
            case R.id.tv_recharge:
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                }
                break;
            case R.id.user_bottom_ll_mall:
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_SHOP_MALL);
                    intent.putExtra("title", "积分商城");
                    intent.putExtra("noTitle", true);
                    startActivity(intent);
                }
                break;
            case R.id.user_bottom_new:
                if (!DeviceUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_NEWSCHOOL);
                    intent.putExtra("title", "新手教程");
                    intent.putExtra("noTitle", true);
                    intent.putExtra("builderPar",false);
                    startActivity(intent);
                }
                break;
            case R.id.user_bottom_ll_quan:
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()) {
                    startActivity(new Intent(getActivity(), VoucherZjActivity.class));
                }
                break;
            case R.id.user_bottom_kefu:
                if (!DeviceUtils.isFastDoubleClick()) {
                    SobotSdkUtils.startKeFu(getActivity());
                }
                break;
            case R.id.user_bottom_about:
                if (!DeviceUtils.isFastDoubleClick()) {
                    Intent intentWeb = new Intent(getActivity(), WebViewActivity.class);
                    intentWeb.putExtra("url", Constant.H5_ABOUTUS + "?AppVersion=" + AppApplication.getConfig().getAppVersion());
                    intentWeb.putExtra("builderPar", false);
                    intentWeb.putExtra("noTitle", true);
                    startActivity(intentWeb);
                }
                break;
            case R.id.user_bottom_ll_update:
                if (!DeviceUtils.isFastDoubleClick()){
                    if (!AppApplication.getConfig().mIsAuditing){
                        mPresenter.updateApp();
                    }else {
                        showErrorMsg("已是最新版本",null);
                    }
                }
                break;
            case R.id.user_login_setting:
                if (!DeviceUtils.isFastDoubleClick()) {
                    startActivity(new Intent(getActivity(), SettingMainActivity.class));
                }
                break;
        }
    }

    public void login() {
        userNologinCl.setVisibility(View.GONE);
        userNologinRl.setVisibility(View.GONE);
        userLoginCl.setVisibility(View.VISIBLE);
        loginCenterCl.setVisibility(View.VISIBLE);

        ViewHelper.safelySetText(userLoginName, SpUtil.getString(Constant.USER_KEY_NICKNAME));

        if (SpUtil.getString(Constant.USER_KEY_PHONE).length()>10) {
            userLoginPhone.setText(SpUtil.getString(Constant.USER_KEY_PHONE).substring(0, 3) + "****"
                    + SpUtil.getString(Constant.USER_KEY_PHONE).substring(7, 11));
        }

        ImageUtils.display(SpUtil.getString(Constant.USER_KEY_USERPIC),userLoginHead,R.mipmap.user_head_default);

        //审核
        if(AppApplication.getConfig().mIsAuditing){
            loginCenterCl.setVisibility(View.GONE);
            userBottomLlMall.setVisibility(View.GONE);
            userBottomLlQuan.setVisibility(View.GONE);
            userLlNew.setVisibility(View.GONE);
            userLlService.setVisibility(View.GONE);
            userLlAbout.setVisibility(View.GONE);
        }

    }

    public void nologin() {
        userNologinCl.setVisibility(View.VISIBLE);
        userNologinRl.setVisibility(View.VISIBLE);
        userLoginCl.setVisibility(View.GONE);
        loginCenterCl.setVisibility(View.GONE);

        userLoginName.setText("");
        userLoginPhone.setText("");
        userBottomPoint.setText("");
        userBottomQuan.setText("");

        //审核
        if(AppApplication.getConfig().mIsAuditing){
            userNologinRl.setVisibility(View.GONE);
            userBottomLlMall.setVisibility(View.GONE);
            userBottomLlQuan.setVisibility(View.GONE);
            userLlNew.setVisibility(View.GONE);
            userLlService.setVisibility(View.GONE);
            userLlAbout.setVisibility(View.GONE);
        }
        clearCapital();
    }

    public void clearCapital() {
        if (mIsInitView) {
            tvAvailableBalance.setText("-----");
            tvZProfitLoss.setText("-----");
            tvPrice.setText("-----");
            tvRecharge.setText("登录");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NICKNAME_UPDATE: //名字
                if (data !=null){
                    updateName(data.getStringExtra("nickName"));
                }
                break;
            case IMAGE_HEAD_UPDATE: //头像
                if (data !=null){
                    updateHeadPic(data.getStringExtra("headUrl"));
                }
                break;
        }
    }

    @Override
    public void updateHeadPic(String userPic) {
        if (!TextUtils.isEmpty(userPic)){
            SpUtil.putString(Constant.USER_KEY_USERPIC,userPic);
            ImageUtils.display(userPic,userLoginHead,R.mipmap.ic_user_head);
            EventBus.getDefault().post(new UserChangeEvent(0,AppApplication.getConfig().getMobilePhone(),userPic));
        }
    }

    public void updateName(String name){
        if (!TextUtils.isEmpty(name)){
            SpUtil.putString(Constant.USER_KEY_NICKNAME,name);
            userLoginName.setText(name);
            EventBus.getDefault().post(new UserChangeEvent(1,AppApplication.getConfig().getMobilePhone(),name));
        }
    }

    @Override
    public void bindUpdateApp(UpdateAppBean updateAppBean) {
        if (!TextUtils.isEmpty(AppApplication.getConfig().getAppVersion())
                && !TextUtils.isEmpty(updateAppBean.versionNum)
                && !TextUtils.equals(updateAppBean.versionNum, AppApplication.getConfig().getAppVersion())
                && (updateAppBean.isUpdate == 1 || updateAppBean.isUpdate == 0)
                && !TextUtils.isEmpty(updateAppBean.url)) {

            String title = "当前版本:" + AppApplication.getConfig().getAppVersion() + ",最新版本:" + updateAppBean.versionNum;
            UpdateHelper.getInstance().update(
                    getActivity(),
                    false,
                    title,
                    updateAppBean.versionDetail.replace("\\n", "\n"),
                    AndroidUtil.getVersionCode(),
                    updateAppBean.versionNum,
                    updateAppBean.url,
                    updateAppBean.packageMd5);
        } else {
            if (updateAppBean.isUpdate !=2){
                showErrorMsg( "当前已是最新版本", null);
            }
        }
    }

    //判断是不是牛人 0 没请求或者失败 1 牛人  2 不是牛人
    private int mValidRole = 0;
    @Override
    public void bindValidRole(boolean validRole) {
        this.mValidRole = (validRole ? 1 : 2);
    }

    private void getUserZjInfo() {
        if (AppApplication.getConfig().isLoginStatus()) {
            //获取用户积分
            mPresenter.getIntegral(AppApplication.getConfig().getAppToken()
                    , AppApplication.getConfig().getUserId()
                    , AppApplication.getConfig().getMobilePhone());
        }
    }


    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (AppApplication.getConfig().isLoginStatus()) {
                mPresenter.getPositionUserMoney(mAmount, AppApplication.getConfig().getAt_token());
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                stopRun();
            }
        }
    };


    public boolean isInitView() {
        return mIsInitView;
    }

    public void startRun() {
        if (AppApplication.getConfig().isLoginStatus() && !TextUtils.isEmpty(AppApplication.getConfig().getAt_token())) {
            tvRecharge.setText("充值");
            mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
        }else {
            clearCapital();
        }
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    public void startRunUserMoney() {
        if (isHidden()) {
            mPresenter.getPositionUserMoney(mAmount, AppApplication.getConfig().getAt_token());
        } else {
            mHandler.removeCallbacks(mRunnable);
            mHandler.post(mRunnable);
        }
    }

    //获取余额
    @Override
    public void bindUserInfo(UserZJBean userZJBean) {
        if (userZJBean != null && userZJBean.isSuccess() && userZJBean.data != null) {
            mAmount = NumberUtil.divide(userZJBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
            tvAvailableBalance.setText(String.valueOf(mAmount));
            int voucherNum = userZJBean.data.coupons == null ? 0 : userZJBean.data.coupons.size();
            userBottomQuan.setText(""+voucherNum);
            startRunUserMoney();
        }
    }

    @Override
    public void bindPositionUserMoney(Double countAmount, Double countPostAmount ,Double countAmountBj) {
        if (countAmount == null || countPostAmount == null) {
            tvPrice.setText(mAmount + "");
            tvZProfitLoss.setText("0");
            tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_333333));
            stopRun();
        } else {
            tvPrice.setText(countAmount + "");
            tvZProfitLoss.setText(countPostAmount + "");
            if (countPostAmount > countAmountBj){
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_F74F54));
            }else if (countPostAmount < countAmountBj){
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_1AC47A));
            }else {
                tvZProfitLoss.setTextColor(ContextCompat.getColor(mActivity,R.color.color_333333));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isInitView()){
            getUserZjInfo();
            startRun();
        }else{
            stopRun();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isHidden()){
            getUserZjInfo();
            startRun();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    //用户积分
    @Override
    public void bindUserIntegral(IntegralBean bean) {
        userBottomPoint.setText("" + bean.myscore+"积分");
    }

    //app 登录成功、退出
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AppUserAccountEvent event) {
        if (event.code == 1) {
            login();
        } else {
            nologin();
        }
    }

    //交易所登录成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JysAccountEvent event) {
        if (isInitView() && event.code == 1 || event.code == 2) {
            getUserZjInfo();
            startRun();
        }
    }

    //头像名字改变
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UserChangeEvent event) {
        if (event.type == 0) { //头像
            ImageUtils.display(event.content, userLoginHead, R.mipmap.ic_user_head);
        } else if (event.type == 1) { // 名字
            ViewHelper.safelySetText(userLoginName, event.content);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
