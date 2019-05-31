package com.paizhong.manggo.ui.follow.module;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.follow.contract.ItemFollowPresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.banner.Banner;
import com.paizhong.manggo.widget.banner.BannerFlingAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Des: 跟买广场-Banner Adapter
 * Created by huang on 2018/8/17 0017 11:26
 */
public class BannerModule extends BaseFollowModule<ItemFollowPresenter> implements BannerFlingAdapter.OnShowPicBannerListener
        , BannerFlingAdapter.OnClickBannerListener {

    @BindView(R.id.follow_banner)
    Banner mBanner;
    private BannerFlingAdapter mAdapter;
    private List<BannerBean> list;

    public BannerModule(Context context) {
        super(context);
        initView();
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.selectBanner(ViewConstant.FOLLOW_BANNER);
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.getRealCount() == 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_follow_banner;
    }

    @Override
    public void bindBanner(List<BannerBean> bannerBeans, String bannerType) {
        this.list = bannerBeans;
        mAdapter.setData(bannerBeans);
        mBanner.start();
    }

    private void initView() {
        mAdapter = new BannerFlingAdapter(mContext);
        mBanner.setAdapter(mAdapter);

        mAdapter.setOnClickBannerListener(this);
        mAdapter.setOnShowPicBannerListener(this);

        mBanner.setBannerRatio(0.28f, 0.057f, 0.24f);
    }

    @Override
    public void itemClick(String url, String circleColumnName, int isNeedHead, int position) {
        if (list == null) {
            return;
        }
        if (list.get(position).isLogin == 1 && !mActivity.login()) {
            return;
        }
        Intent intentAbout = new Intent(mContext, WebViewActivity.class);
        intentAbout.putExtra("url", url);
        intentAbout.putExtra("title", circleColumnName);
        intentAbout.putExtra("noTitle", isNeedHead == 0);
        mContext.startActivity(intentAbout);
    }

    @Override
    public void showPic(ImageView imageView, String url) {
        ImageUtils.display(url, imageView, R.mipmap.other_empty);
    }
}
