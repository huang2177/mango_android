package com.paizhong.manggo.ui.trade.capital;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.trade.CapitalListBean;
import com.paizhong.manggo.bean.zj.UserZJBean;

import java.util.List;

/**
 * Created by zab on 2018/3/28 0028.
 */

public interface CapitalContract {
    interface View extends BaseView {
        int getTabSelect();

        void bindUserInfo(UserZJBean userZJBean);

        void bindTodayOrderBill(int tabType, List<CapitalListBean> listBeans);

        void bindPositionUserMoney(Double countAmount, Double countPostAmount , Double countAmountBj);
    }

    interface Presenter {
        void getUserInfo(String token);

        void getTodayOrderBill(boolean showLoad, String token, int page, int page_size, long open_time, long close_time);

        void getWithdrawalList(boolean showLoad, String token, int page, int page_size);

        void getRechargeList(boolean showLoad, String token, int page, int page_size);

        void getPositionUserMoney(double amount, String token);
    }
}
