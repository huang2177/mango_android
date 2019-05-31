package com.paizhong.manggo.ui.home.contract;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.paizhong.manggo.app.ModuleManager;
import com.paizhong.manggo.base.BaseModule;
import com.paizhong.manggo.base.BasePresenter;
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
import com.paizhong.manggo.config.ViewConstant;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/8/27 0027 10:43
 */
public abstract class BaseHomeModule<P extends BasePresenter> extends BaseModule<P>
        implements ItemHomeContract.View {

    public BaseHomeModule(Context context) {
        this(context, null);
    }

    public BaseHomeModule(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (mPresenter != null) {
            mPresenter.init(this);
        }
        ModuleManager.getInstance().addModule(this);
    }

    @Override
    public void bindReporting(List<ReportBean> list) {

    }

    @Override
    public void bindZJMarket(List<MarketHQBean> list) {

    }

    @Override
    public void bindLastFellowInfo(NewBuyInfoBean bean) {

    }

    @Override
    public void bindInitTicket(boolean isNewUser, boolean isLoginSucc) {

    }

    @Override
    public void bindHotProduct(HotProBean bean) {

    }

    @Override
    public void bindFellowInfoEmpty() {

    }

    @Override
    public void bindProfitRank(List<ProfitBean> bean, int page) {

    }

    @Override
    public void bindProfitEmpty() {

    }

    @Override
    public void bindEmergencyDetail(List<EmergencyBean> bean) {

    }

    @Override
    public void bindZJMarketEmpty() {

    }

    @Override
    public void bindRedPacketInfo(RedPacketBean bean) {

    }

    @Override
    public void bindUserScore(IntegralBean bean) {

    }

    @Override
    public void refreshDialog(String s, String stockIndex, String changeValue, String time) {
    }


    /**
     * 做一层分发
     *
     * @param list
     * @param bannerType
     */
    @Override
    public void bindBanner(List<BannerBean> list, String bannerType) {
        switch (bannerType) {
            case ViewConstant.HOME_BANNER:
                bindBanner(list);
                break;
            case ViewConstant.HOME_ACTIVITY:
                bindActivity1(list.get(0), list.get(0).activityImageUrl.endsWith(".gif"));
                break;
            case ViewConstant.HOME_BANNER_BOTTOM:
                if (list.size() >= 3) {
                    bindActivity2(list);
                }
                break;
        }
    }

    public void bindBanner(List<BannerBean> list) {

    }

    public void bindActivity1(BannerBean bean, boolean isGif) {

    }

    public void bindActivity2(List<BannerBean> list) {

    }

    @Override
    public void bindCalendarEmpty() {

    }

    @Override
    public void bindCalendarList(List<CalendarBean> list) {

    }

    @Override
    public void bindTradeEmpty() {

    }

    @Override
    public void bindTradeChance(List<TradeChanceBean> list, int pageIndex) {

    }

    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void showLoading(String content) {
        mActivity.showLoading(content);
    }

    @Override
    public void stopLoading() {
        mActivity.stopLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        mActivity.showErrorMsg(msg, type);
    }

}
