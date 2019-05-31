package com.paizhong.manggo.ui.paycenter.present;

import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.bean.zj.BankUnbCardZJBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.bean.zj.UserZJSmsBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;

/**
 * Created by zab on 2018/4/4 0004.
 */

public class PresentPresenter extends BasePresenter<PresentContract.View> implements PresentContract.Presenter{


    //获取绑卡列表
    @Override
    public void getBankcardZjList(String token) {
      toSubscribe(HttpManager.getApi().getBankcardZjList(token), new HttpSubscriber<BankCardZjListBean>() {

          @Override
          protected void _onStart() {
              super._onStart();
          }

          @Override
          protected void _onCompleted() {
              super._onCompleted();
          }

          @Override
          protected void _onNext(BankCardZjListBean listBean) {
              super._onNext(listBean);
              mView.bindBankcardZjList(listBean);
          }
      });
    }


    //解绑银行卡
    @Override
    public void unbindingCard(final boolean showError,String token, final String bankCardId) {
       toSubscribe(HttpManager.getApi().unbindingCard(token, bankCardId), new HttpSubscriber<BankUnbCardZJBean>() {

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
               if (showError){
                   mView.showErrorMsg(message,null);
               }
           }

           @Override
           protected void _onNext(BankUnbCardZJBean bankUnbCardZJBean) {
               super._onNext(bankUnbCardZJBean);
               mView.bindUnbindingCard(showError,bankCardId,bankUnbCardZJBean);
           }
       });
    }


    //账户信息
    @Override
    public void getUserZjInfo(String token, String userId) {
        toSubscribe(HttpManager.getApi().getUserZjInfo(token), new HttpSubscriber<UserZJBean>() {
            @Override
            protected void _onNext(UserZJBean userZJBean) {
                super._onNext(userZJBean);
                mView.bindZjUserInfo(userZJBean);
            }
        });
    }


    //获取验证码
    @Override
    public void getUserZjSmsCode(String mobile, String codeType) {
       toSubscribe(HttpManager.getApi().getUserZjSmsCode(mobile, codeType), new HttpSubscriber<UserZJSmsBean>() {
           @Override
           protected void _onNext(UserZJSmsBean userZJSmsBean) {
               super._onNext(userZJSmsBean);
           }
       });
    }


    //校验是否已实名认证
    @Override
    public void getVerifyCard(String token, String realName, String bankCard, String bankMobile, String idCard) {
       toSubscribe(HttpManager.getApi().getVerifyCard(token, realName, bankCard, bankMobile, idCard), new HttpSubscriber<Boolean>() {
           @Override
           protected void _onStart() {
               super._onStart();
               mView.showLoading("");
           }

           @Override
           protected void _onError(String message, int code) {
               super._onError(message, code);
               mView.stopLoading();
           }

           @Override
           protected void _onNext(Boolean aBoolean) {
               super._onNext(aBoolean);
               mView.bindVerifyCard(aBoolean);
           }
       });
    }


    //提现
    @Override
    public void getWithdrawMoney(String token, String smsCode, String password, final String amount, String account_id,String bankNum,String secret_access_key) {
      toSubscribe(HttpManager.getApi().getWithdrawMoney(token, smsCode, password, amount, account_id,bankNum,secret_access_key), new HttpSubscriber<ZJBaseBean>() {
          @Override
          protected void _onCompleted() {
              super._onCompleted();
              mView.stopLoading();
          }

          @Override
          protected void _onError(String message, int code) {
              super._onError(message, code);
              mView.showErrorMsg(message,null);
          }

          @Override
          protected void _onNext(ZJBaseBean zjBaseBean) {
              super._onNext(zjBaseBean);
              mView.bindZJWithdrawMoney(amount,zjBaseBean);
          }
      });
    }
}
