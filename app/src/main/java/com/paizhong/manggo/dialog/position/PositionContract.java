package com.paizhong.manggo.dialog.position;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;

import java.util.List;

/**
 * Created by zab on 2018/6/19 0019.
 */
public interface PositionContract {
    interface View extends BaseView {
       void bindOrderList(List<PositionListBean> orderZjList);
       boolean getCheck(String key);
       void bindTransferList(ZJBaseBean baseBean);
    }
    interface Presenter {
       void getSignOrderList(String token,String mProductID);

       void getTransferList(String token,String orderids, String secret_access_key);
    }
}
