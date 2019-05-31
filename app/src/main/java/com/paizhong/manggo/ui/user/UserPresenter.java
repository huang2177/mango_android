package com.paizhong.manggo.ui.user;

import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.bean.zj.UserPositionBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.NumberUtil;

import java.util.List;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class UserPresenter extends BasePresenter<UserContract.View> implements UserContract.Presenter {


    //判断是不是牛人
    @Override
    public void getValidRole(final boolean mIShow) {
        toSubscribe(HttpManager.getApi().getValidRole(), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                if (mIShow){
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }


            @Override
            protected void _onError(String message) {
                super._onError(message);
                if (mIShow){
                    mView.showErrorMsg("初始化数据失败",null);
                }
            }

            @Override
            protected void _onNext(Boolean aBoolean) {
                super._onNext(aBoolean);
                mView.bindValidRole(aBoolean);
            }
        });
    }


    //积分
    @Override
    public void getIntegral(String token, String userId, String phone) {
        toSubscribe(HttpManager.getApi().getUserIntegral(token, userId, phone), new HttpSubscriber<IntegralBean>() {
            @Override
            protected void _onNext(IntegralBean bean) {
                if (bean != null) {
                    mView.bindUserIntegral(bean);
                }
            }
        });
    }


    //版本更新
    @Override
    public void updateApp() {
        toSubscribe(HttpManager.getApi().updateApp(), new HttpSubscriber<UpdateAppBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(UpdateAppBean updateAppBean) {
                super._onNext(updateAppBean);
                if (updateAppBean !=null){
                    mView.bindUpdateApp(updateAppBean);
                }
            }
        });
    }

    //资金信息
    @Override
    public void getUserInfo(String token) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                mView.bindUserInfo(userZJBean);
            }
        });
    }

    //持仓单资金信息
    @Override
    public void getPositionUserMoney(final double amount, String token) {
        toSubscribe(HttpManager.getApi().getUserMoney(token, true), new HttpSubscriber<List<UserPositionBean>>() {

            @Override
            protected void _onNext(List<UserPositionBean> userPositionList) {
                super._onNext(userPositionList);
                if (userPositionList != null && userPositionList.size() > 0){

                    double countPostAmount = 0; //持仓资产
                    double countAmountBj = 0; //本金
                    for (UserPositionBean zjBean : userPositionList){
                        if (!TextUtils.isEmpty(zjBean.coupon_id) && !TextUtils.equals("0",zjBean.coupon_id)){
                            if (zjBean.floatingPrice > 0){
                                countPostAmount += zjBean.floatingPrice;
                            }
                        }else {
                            countPostAmount += (zjBean.amount  + zjBean.floatingPrice);
                            countAmountBj += zjBean.amount;
                        }
                    }
                    mView.bindPositionUserMoney(NumberUtil.addScale(countPostAmount,amount),   NumberUtil.getDouble(countPostAmount),countAmountBj);
                }else {
                    mView.bindPositionUserMoney(null,null,null);
                }
            }
        });
    }
}
