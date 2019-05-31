package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.newguide.GuideOrderDialog;
import com.paizhong.manggo.dialog.newguide.NewOrderDialog;
import com.paizhong.manggo.dialog.newguide.NewUserDialog;
import com.paizhong.manggo.events.AppUserAccountEvent;
import com.paizhong.manggo.events.JysAccountEvent;
import com.paizhong.manggo.events.NewGuideEvent;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Des: 新手引导模块
 * Created by huang on 2018/9/10 0010 16:32
 */
public class GuideModule extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener {
    private ImageView ivOrder;
    private ImageView ivGuide;
    private ViewGroup container;

    public GuideModule(Context context, ViewGroup parent) {
        super(context, null);
        this.container = parent;
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    /**
     * 处理新手引导
     */
    public void showGuide() {
        if (AppApplication.getConfig().isLoginStatus()) { //已经登录成功
            getInitTicket(false);
            return;
        }
        if (SpUtil.getBoolean(Constant.CACHE_IS_FIRST_GUIDED)) { //是否是首次引导
            visibleGuideIv(true);
        } else {
            new NewUserDialog(mActivity).show();
        }
    }

    /***
     * dialog消失事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewGuideEvent event) {
        // 新人专享dialog消失
        if (event.code == 0) {
            visibleGuideIv(true);
        }
        //新人代金券下单成功
        else if (event.code == 1) {
            visibleOrderIv(false);
        }
        //登录dialog消失
        else if (event.code == 2) {
            visibleGuideIv(true);
        }
    }

    /***
     * app 登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppUserAccountEvent event) {
        if (event.code == 1) {
            visibleGuideIv(false);
        }
    }

    /***
     * 登陆交易所成功(去检查是否是新手)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JysAccountEvent event) {
        if (event.code == 1) {
            getInitTicket(true);
        }
    }

    private void getInitTicket(boolean isLoginSucc) {
        mPresenter.getInitTicket(AppApplication.getConfig().getMobilePhone(), isLoginSucc);
    }

    /**
     * @param isNewUser   是否使用过新手券
     * @param isLoginSucc 是否是登录成功后的回调
     */
    @Override
    public void bindInitTicket(boolean isNewUser, boolean isLoginSucc) {
        visibleOrderIv(isNewUser);
        if (isNewUser && isLoginSucc) {
            new GuideOrderDialog(mActivity).show();
        }
    }


    /***
     * 显示或隐藏底部引导图片
     */
    private void visibleGuideIv(boolean visible) {
        if (SpUtil.getBoolean(Constant.CACHE_IS_GUIDED)) { //是否已经引导
            return;
        }
        if (visible && ivGuide == null) {
            ivGuide = new ImageView(mActivity);
            ivGuide.setTag("guide");
            ivGuide.setImageResource(R.mipmap.image_new_user);
            ivGuide.setOnClickListener(this);
            container.addView(ivGuide, 0);
        } else if (visible) {
            container.removeView(ivGuide);
            container.addView(ivGuide, 0);
        } else {
            container.removeView(ivGuide);
            SpUtil.putBoolean(Constant.CACHE_IS_GUIDED, true);
        }
    }

    /**
     * 显示或隐藏底角的下单图片
     */
    public void visibleOrderIv(boolean visible) {
        if (visible && ivOrder == null) {
            ivOrder = new ImageView(mActivity);
            ivOrder.setTag("order");
            ivOrder.setImageResource(R.mipmap.ic_new_ticket);
            ivOrder.setOnClickListener(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.gravity = Gravity.END;
            container.addView(ivOrder, 0, params);
            SpUtil.putBoolean(Constant.CACHE_IS_NEW_USER, true);
        } else if (visible) {
            container.removeView(ivOrder);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.gravity = Gravity.END;
            container.addView(ivOrder, 0, params);
            SpUtil.putBoolean(Constant.CACHE_IS_NEW_USER, true);
        } else {
            container.removeView(ivOrder);
            SpUtil.putBoolean(Constant.CACHE_IS_NEW_USER, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        switch ((String) v.getTag()) {
            case "guide":
                mActivity.login();
                break;
            case "order":
                if (mActivity.login()) {
                    new NewOrderDialog(mActivity).showDialog();
                }
                break;
        }
    }

    @Override
    public void onVisible(boolean visible) {
        if (ivGuide != null) {
            ivGuide.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
