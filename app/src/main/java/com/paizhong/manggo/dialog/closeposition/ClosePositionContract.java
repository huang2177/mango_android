package com.paizhong.manggo.dialog.closeposition;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.AtBaseBean;

/**
 * Created by zab on 2018/4/5 0005.
 */

public interface ClosePositionContract {
    interface View extends BaseView {
        void bindTransfer(AtBaseBean atBaseBean);
    }
    interface Presenter {
        void getTransfer(String token, String orderId, String secret_access_key);
    }
}
