package com.paizhong.manggo.ui.follow.detail.newbuy;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.PersonOrderBean;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/5/31 0031 19:08
 */
public class NewBuyContract {
    public interface View extends BaseView {

        void bindOrderEmpty();

        void bindOrderList(List<PersonOrderBean> list);

        void refreshDialog(String maxId, String stockIndex, String changeValue, String time);
    }

    public interface Presenter {
        void getOrderList(String phone, boolean showLoading);
    }
}
