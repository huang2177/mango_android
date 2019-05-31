package com.paizhong.manggo.ui.trade.hangup;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.HangUpListBean;
import java.util.List;

/**
 * Created by zab on 2018/4/2 0002.
 */

public interface HangUpContract {

    interface View extends BaseView {
       void bindHangUpOrderList(List<HangUpListBean> hangUpOrderList);
       void bindMarketList(List<MarketHQBean> marketList);
       void bindHangUpHistoryList(int pageIndex,List<HangUpListBean> hangUpOrderList);
       void bindCancelHangUp(String id);
    }
    interface Presenter {
       void getHangUpOrderList(boolean showLoad);
       void getMarketList();
       void getHangUpHistoryList(boolean showLoad,int pageSize,int pageIndex);
       void getCancelHangUp(String id);
    }
}
