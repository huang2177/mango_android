package com.paizhong.manggo.ui.follow.detail;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.PersonInfoBean;

/**
 * Des:
 * Created by hs on 2018/5/31 0031 19:08
 */
public class PersonContract {
    public interface View extends BaseView {
        void bindConcern();

        void bindPersonInfo(PersonInfoBean person);
    }

    public interface Presenter {
        void getPersonInfo(String phone);

        void addConcern(String concernPhone, int isConcern);
    }
}
