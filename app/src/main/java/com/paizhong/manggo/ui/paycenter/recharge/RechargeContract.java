package com.paizhong.manggo.ui.paycenter.recharge;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.bean.zj.ChannelsZJBean;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.bean.zj.RechargeMoneyZJBean;

import java.util.List;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface RechargeContract {

    interface View extends BaseView {
        void bindPicture(List<PictureZJBean> list);

        void bindRechargeConfig(List<ChannelsZJBean> list);

        void bindRechargeMoney(RechargeMoneyZJBean bean);

        void bindBankcardZjList(String bankCardId);

        void bindAddBank(String account);

        void bindDeleteBank(int position);

        void bindBankList(List<BankListBean> list);

        void onError();

    }

    interface Presenter {
        void getPicture();

        void getCardList();

        void getBankcardZjList(String token);

        void unbindingCard(String token,String bankCardId);

        void getSaveCard(String account);

        void getDeleteCard(String account, int position);

        void getRechargeConfig(boolean showError, String token);

        void getRechargeMoney(String token, String channel_id, String secret_access_key, String amount, String extra,String channel);
    }
}
