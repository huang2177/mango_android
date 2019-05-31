package com.paizhong.manggo.ui.paycenter.present;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.bean.zj.BankUnbCardZJBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface PresentContract {

    interface View extends BaseView {
        void bindBankcardZjList(BankCardZjListBean listBean);

        void bindUnbindingCard(boolean showError,String bankCardId, BankUnbCardZJBean bankUnbCardZJBean);

        void bindZjUserInfo(UserZJBean userZJBean);

        void bindVerifyCard(boolean verifyCard);

        void bindZJWithdrawMoney(String amount, ZJBaseBean baseBean);

    }
    interface Presenter {
       void getBankcardZjList(String token);

       void unbindingCard(boolean showError,String token, String bankCardId);

        void getUserZjInfo(String token, String userId);

        void getUserZjSmsCode(String mobile, String codeType);

        void getVerifyCard(String token,String realName,String bankCard,String bankMobile,String idCard);

        void getWithdrawMoney(String token, String smsCode, String password, String amount, String account_id ,String bankNum , String secret_access_key);
    }
}
