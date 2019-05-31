package com.paizhong.manggo.ui.follow.rank;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.RankListBean;

import java.util.List;

/**
 * Des:
 * Created by hs on 2018/5/31 0031 19:08
 */
public class RankContract {
    public interface View extends BaseView {
        void bindRankList(List<RankListBean> list);
    }

    public interface Presenter {
        void getRankList(boolean showLoading);
    }
}
