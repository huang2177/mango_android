package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.home.ReportBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.widget.banner.Banner;
import com.paizhong.manggo.widget.banner.BannerFlingAdapter;
import com.paizhong.manggo.widget.switcher.AutoViewSwitcher;
import com.paizhong.manggo.widget.switcher.SwitcherHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Des: 首页Banner Adapter
 * Created by huang on 2018/8/17 0017 11:26
 */
public class BannerModule extends BaseHomeModule<ItemHomePresenter>
        implements BannerFlingAdapter.OnClickBannerListener, BannerFlingAdapter.OnShowPicBannerListener {
    @BindView(R.id.home_banner)
    Banner mBanner;
    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(R.id.root)
    LinearLayout mRoot;
    @BindView(R.id.home_switcher)
    AutoViewSwitcher mSwitcher;

    private SwitcherHelper mHelper;
    private BannerFlingAdapter mAdapter;
    private List<BannerBean> list;

    public BannerModule(Context context) {
        super(context);
        initBanner();
        onRefresh(false);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.getReporting();
        mPresenter.selectBanner(ViewConstant.HOME_BANNER);
    }

    @Override
    public void setAuditing(boolean auditing) {
        super.setAuditing(auditing);
        mRoot.setVisibility(auditing ? GONE : VISIBLE);

        showAuditingData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_banner;
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.getRealCount() == 0;
    }

    private void initBanner() {
        mAdapter = new BannerFlingAdapter(mContext);
        mBanner.setAdapter(mAdapter);

        mBanner.setBannerRatio(0.40f, 0.08f, 0.20f);

        mImageView.setImageResource(R.mipmap.ic_notify);
        mHelper = new SwitcherHelper(mSwitcher);

        mAdapter.setOnClickBannerListener(this);
        mAdapter.setOnShowPicBannerListener(this);
    }


    @Override
    public void bindBanner(List<BannerBean> list) {
        if (mIsAuditing) {
            return;
        }
        this.list = list;
        mBanner.setBackground(null);
        mAdapter.setData(list);
        mBanner.start();
    }

    @Override
    public void bindReporting(List<ReportBean> list) {
        mHelper.setReportBeans(1, list);
        mHelper.start();
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

    /**
     * 展示屏蔽中的banner
     */
    private void showAuditingData() {
        if (mIsAuditing) {
            list = Arrays.asList(new BannerBean("http://mangoo.oss-cn-shanghai.aliyuncs.com/banner/1545387228076-20181221181348.jpg"
                            , "https://m.313bcmj.com/banner/banner48")
                    , new BannerBean("http://mangoo.oss-cn-shanghai.aliyuncs.com/banner/1543574397487-20181130183957.jpg"
                            , "https://m.313bcmj.com/banner/banner36"));
            mBanner.setBackground(null);
            mAdapter.setData(list);
            mBanner.start();
        }
    }
}
