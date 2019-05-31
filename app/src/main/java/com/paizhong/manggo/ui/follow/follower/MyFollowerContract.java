package com.paizhong.manggo.ui.follow.follower;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.MyFollowerBean;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/5/31 0031 19:08
 */
public class MyFollowerContract {
    public interface View extends BaseView {
        void bindFollowerList(int pageIndex, List<MyFollowerBean> list);

        void bindFollowerEmpty();
    }

    public interface Presenter {
        void getFollowerList(int pageIndex, int pageSize, boolean showLoading);
    }
}
