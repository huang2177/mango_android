package com.paizhong.manggo.ui.follow.record;


import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.follow.FollowRecordBean;
import com.paizhong.manggo.bean.follow.RecordRankBean;

import java.util.List;

/**
 * Created by zab on 2018/7/3 0003.
 */
public interface FollowRecordContract {
    interface View extends BaseView {
        void bindStopShowLoad();

        void bindWeekProfit(RecordRankBean rankBean);

        void bindOrderPosition(int pageIndex, int recordType, int postType, List<FollowRecordBean> recordList);

        void bindOrderEmpty();

        //void refreshDialog(int maxId,String stockIndex,String changeValue,int recordType);
    }

    interface Presenter {

        void getWeekProfit(String customerPhone);

        //发起跟买 持仓接口
        void getOrderPosition();

        //发起跟买 平仓接口
        void getOrderPositionUp(String customerPhone, int pageSize, int pageIndex);

        //我的跟买 持仓接口
        void getMyFellowOrder();

        //我的跟买 平仓接口
        void getMyFellowOrderUp(int pageSize, int pageIndex);
    }
}
