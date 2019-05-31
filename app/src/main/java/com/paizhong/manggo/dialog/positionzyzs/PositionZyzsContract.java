package com.paizhong.manggo.dialog.positionzyzs;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.AtBaseBean;

/**
 * Created by zab on 2018/4/5 0005.
 */

public interface PositionZyzsContract {
    interface View extends BaseView {
        void bindProfitLossLimit(AtBaseBean atBaseBean);
    }
    interface Presenter {
          void getProfitLossLimit(String token, String secret_access_key, String orderId, int profitLimit, int lossLimit, String ticketId, String orderNo);
    }
}
