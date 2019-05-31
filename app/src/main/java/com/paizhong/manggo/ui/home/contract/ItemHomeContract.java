package com.paizhong.manggo.ui.home.contract;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.home.CalendarBean;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.bean.home.HotProBean;
import com.paizhong.manggo.bean.home.NewBuyInfoBean;
import com.paizhong.manggo.bean.home.ProfitBean;
import com.paizhong.manggo.bean.home.RedPacketBean;
import com.paizhong.manggo.bean.home.ReportBean;
import com.paizhong.manggo.bean.home.TradeChanceBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.bean.user.IntegralBean;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/8/28 0028 10:09
 */
public class ItemHomeContract {
    public interface View extends BaseView {
        void bindReporting(List<ReportBean> list);

        void bindZJMarket(List<MarketHQBean> list);

        void bindLastFellowInfo(NewBuyInfoBean bean);

        void bindInitTicket(boolean isNewUser, boolean isLoginSucc);

        void bindBanner(List<BannerBean> list, String bannerType);

        void bindZJMarketEmpty();

        void refreshDialog(String s, String stockIndex, String changeValue, String time);

        void bindFellowInfoEmpty();

        void bindCalendarEmpty();

        void bindCalendarList(List<CalendarBean> list);

        void bindTradeEmpty();

        void bindTradeChance(List<TradeChanceBean> list, int pageIndex);

        void bindRedPacketInfo(RedPacketBean bean);

        void bindUserScore(IntegralBean bean);

        void bindHotProduct(HotProBean bean);

        void bindEmergencyDetail(List<EmergencyBean> bean);

        void bindProfitRank(List<ProfitBean> bean, int page);

        void bindProfitEmpty();
    }

    interface Presenter {
        void getReporting();

        void getZJMarketList();

        void getHotProduct();

        void queryUserScore();

        void getLastFellowInfo();

        void queryRedPacketInfo();

        void queryProfitRank(int page);

        void selectBanner(String bannerType);

        void selectTradeChance(int pageIndex);

        void getInitTicket(String phone, boolean isLoginSucc);

        void getEmergencyDetail(String method, boolean isShowLoading);

        void getCalendarList(String year, String date, final boolean empty);
    }
}
