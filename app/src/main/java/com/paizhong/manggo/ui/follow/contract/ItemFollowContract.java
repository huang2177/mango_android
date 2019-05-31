package com.paizhong.manggo.ui.follow.contract;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.bean.other.BannerBean;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/8/28 0028 10:09
 */
public class ItemFollowContract {
    public interface View extends BaseView {
        void bindOrderEmpty(String proName);

        void bindConcern(int position, int isConcern);

        void bindRankList(List<RankListBean> list);

        void bindRecordList(List<NoticeListBean> list);

        void refreshDialog(String maxId, String stockIndex, String changeValue, String time);

        void bindBanner(List<BannerBean> bannerBeans, String bannerType);

        void bindValidRole(Boolean aBoolean);

        void bindBestOrderList(String proName, List<FollowListBean> list);

        void bindOrderList(String proName, List<FollowListBean> list);
    }

    interface Presenter {
        void getValidRole();

        void getRankList();

        void getRecordList();

        void getBestOrderList(String name);

        void selectBanner(String bannerType);

        void getOrderList(boolean empty, String proName);

        void addConcern(String concernPhone, int isConcern, int position);
    }
}
