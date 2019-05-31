package com.paizhong.manggo.ui.paycenter.addbank;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.zj.BankCardZjBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class AddBankPresenter extends BasePresenter<AddBankContract.View> implements AddBankContract.Presenter{

    @Override
    public void bankCardZj(String token, String real_name, String account, String channel,String idCard,String bankMobile) {

        //channel 提现渠道,alipay支付宝，ebank银行卡 允许值: “alipay”, “ebank” bankCardZj  getVerifyUser
        toSubscribe(HttpManager.getApi().getVerifyUser(token, real_name, account, channel,idCard,bankMobile), new HttpSubscriber<BankCardZjBean>() {
            @Override
            protected void _onStart() {
                super._onStart();
                mView.showLoading("");
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                super._onCompleted();
                mView.stopLoading();
            }

            @Override
            protected void _onNext(BankCardZjBean bankCardZjBean) {
                super._onNext(bankCardZjBean);
                mView.bindBankCardZj(bankCardZjBean);
            }
        });
    }
}
