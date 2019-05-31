package com.paizhong.manggo.ui.paycenter.addbank;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.zj.BankCardZjBean;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface AddBankContract {

    interface View extends BaseView {
        void bindBankCardZj(BankCardZjBean bankCardZjBean);
    }
    interface Presenter {
        void bankCardZj(String token, String real_name, String account, String channel, String idCard,String bankMobile);
    }
}
