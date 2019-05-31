package com.paizhong.manggo.ui.follow.contract;

import android.content.Context;
import android.util.AttributeSet;

import com.paizhong.manggo.base.BaseModule;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.app.ModuleManager;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/8/27 0027 10:43
 */
public abstract class BaseFollowModule<P extends BasePresenter> extends BaseModule<P>
        implements ItemFollowContract.View {

    public BaseFollowModule(Context context) {
        this(context, null);
    }

    public BaseFollowModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mPresenter != null) mPresenter.init(this);
        ModuleManager.getInstance().addModule(this);
    }

    public void bindOrderEmpty(String proName) {

    }

    public void bindConcern(int position, int isConcern) {

    }

    @Override
    public void bindRankList(List<RankListBean> list) {

    }

    @Override
    public void bindRecordList(List<NoticeListBean> list) {

    }

    @Override
    public void bindBanner(List<BannerBean> bannerBeans, String bannerType) {

    }

    @Override
    public void refreshDialog(String maxId, String stockIndex, String changeValue, String time) {

    }

    @Override
    public void bindBestOrderList(String proName, List<FollowListBean> list) {

    }

    @Override
    public void bindOrderList(String proName, List<FollowListBean> list) {

    }

    @Override
    public void bindValidRole(Boolean aBoolean) {

    }

    @Override
    protected boolean isEmpty() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return -1;
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
