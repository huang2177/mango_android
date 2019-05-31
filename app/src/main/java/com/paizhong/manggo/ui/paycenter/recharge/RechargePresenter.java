package com.paizhong.manggo.ui.paycenter.recharge;

import android.text.TextUtils;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.bean.zj.BankUnbCardZJBean;
import com.paizhong.manggo.bean.zj.ChannelsZJBean;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.bean.zj.RechargeConfigZJBean;
import com.paizhong.manggo.bean.zj.RechargeMoneyZJBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.CheckUtils;
import com.google.gson.JsonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class RechargePresenter extends BasePresenter<RechargeContract.View> implements RechargeContract.Presenter {

    //获取资金配置信息
    @Override
    public void getRechargeConfig(final boolean showError, String token) {
        toSubscribe(HttpManager.getApi().getRechargeConfig(token), new HttpSubscriber<RechargeConfigZJBean>() {

            @Override
            protected void _onStart() {
                super._onStart();
                if (!showError) {
                    mView.showLoading("");
                }
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                if (showError) {
                    mView.showErrorMsg(message, null);
                }
                mView.onError();
            }

            @Override
            public void onNext(RechargeConfigZJBean configZJBean) {
                super.onNext(configZJBean);
                if (configZJBean != null
                        && configZJBean.isSuccess()
                        && configZJBean.data != null
                        && configZJBean.data.channels != null
                        && configZJBean.data.channels.size() > 0) {

                    Collections.sort(configZJBean.data.channels, new Comparator<ChannelsZJBean>() {
                        @Override
                        public int compare(ChannelsZJBean o1, ChannelsZJBean o2) {
                            // o1 - o2 升序（从小到大） o2 - 01 降序（从大到小）
                            int compareIndex_1 = TextUtils.equals("alipay_qr", o1.channel) ? 0 : (TextUtils.equals("wx_h5", o1.channel) ? 2 : 1);
                            int compareIndex_2 = TextUtils.equals("alipay_qr", o2.channel) ? 0 : (TextUtils.equals("wx_h5", o2.channel) ? 2 : 1);
                            return compareIndex_1 - compareIndex_2;
                        }
                    });

                    Iterator<ChannelsZJBean> iterator = configZJBean.data.channels.iterator();
                    while (iterator.hasNext()) {
                        ChannelsZJBean bean = iterator.next();
                        String number = !TextUtils.isEmpty(bean.name) ? String.valueOf(bean.name.charAt(bean.name.length() - 1)) : "";
                        //去掉name后面的数字
                        if (!TextUtils.isEmpty(number) && CheckUtils.isInt(number)) {
                            bean.name = bean.name.replace(number, "");
                        }
                        if (bean.is_disable != 0) {
                            iterator.remove();
                        }
                    }
                    mView.bindRechargeConfig(configZJBean.data.channels);
                }
            }
        });
    }


    //充值金额列表
    @Override
    public void getPicture() {
        toSubscribe(HttpManager.getApi().getPicture(), new HttpSubscriber<List<PictureZJBean>>() {
            @Override
            protected void _onNext(List<PictureZJBean> beanList) {
                super._onNext(beanList);
                if (beanList != null && beanList.size() > 0) {
                    mView.bindPicture(beanList);
                }
            }
        });
    }


    //绑卡列表 （用于验证）
    @Override
    public void getBankcardZjList(String token) {
        toSubscribe(HttpManager.getApi().getBankcardZjList(token), new HttpSubscriber<BankCardZjListBean>() {
            @Override
            protected void _onNext(BankCardZjListBean listBean) {
                super._onNext(listBean);
                if (listBean !=null && listBean.data !=null && listBean.data.size() > 0){
                    mView.bindBankcardZjList(listBean.data.get(0).id);
                }
            }
        });
    }


    //解绑银行卡
    @Override
    public void unbindingCard(String token, String bankCardId) {
        toSubscribe(HttpManager.getApi().unbindingCard(token, bankCardId), new HttpSubscriber<BankUnbCardZJBean>() {
            @Override
            public void onNext(BankUnbCardZJBean bankUnbCardZJBean) {
                super.onNext(bankUnbCardZJBean);
            }
        });
    }


    //充值
    @Override
    public void getRechargeMoney(String token, String channel_id, String secret_access_key, String amount, String extra,String channel) {
        toSubscribe(HttpManager.getApi().getRechargeMoney(token, channel_id, secret_access_key, amount, extra,channel), new HttpSubscriber<RechargeMoneyZJBean>() {

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
            protected void _onError(String message) {
                super._onError(message);
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(RechargeMoneyZJBean bean) {
                super._onNext(bean);
                mView.bindRechargeMoney(bean);
            }
        });
    }


    /**
     * 获取平台绑卡列表
     */
    @Override
    public void getCardList() {
        toSubscribe(HttpManager.getApi().getCardList(), new HttpSubscriber<List<BankListBean>>() {
            @Override
            protected void _onNext(List<BankListBean> list) {
                if (list != null) {
                    mView.bindBankList(list);
                }
            }
        });
    }


    /**
     * 平台绑卡
     */
    @Override
    public void getSaveCard(final String account) {
        toSubscribe(HttpManager.getApi().getSaveCard(account), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.bindAddBank(account);
            }
        });
    }

    /**
     * 平台解绑
     *
     * @param account
     * @param position
     */
    @Override
    public void getDeleteCard(String account, final int position) {
        toSubscribe(HttpManager.getApi().getDeleteCard(account), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.bindDeleteBank(position);
            }
        });
    }
}
