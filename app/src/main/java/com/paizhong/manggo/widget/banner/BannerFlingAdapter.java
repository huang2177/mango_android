package com.paizhong.manggo.widget.banner;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.bean.other.BannerBean;

/**
 * Des：滚动Banner Adapter
 */
public class BannerFlingAdapter extends BaseAutoFlingAdapter<BannerBean> {

    private OnClickBannerListener mOnClickBannerListener;
    private OnShowPicBannerListener mOnShowPicBannerListener;

    private Context mContext;

    public BannerFlingAdapter(Fragment context) {
        mContext = context.getContext();
    }

    public BannerFlingAdapter(Context activity) {
        mContext = activity;
    }

    public void setOnClickBannerListener(OnClickBannerListener onClickBannerListener) {
        mOnClickBannerListener = onClickBannerListener;
    }

    public void setOnShowPicBannerListener(OnShowPicBannerListener sOnShowPicBannerListener) {
        mOnShowPicBannerListener = sOnShowPicBannerListener;
    }

    @Override
    public View instantiateView(ViewGroup parent, Context context) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_banner, null);
    }

    @Override
    public void bindView(final BannerBean newsSlide, View view, final int position) {
        ImageView imageView = view.findViewById(R.id.banner_img);
        mOnShowPicBannerListener.showPic(imageView, newsSlide.activityImageUrl);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickBannerListener == null) {
                    return;
                }
                mOnClickBannerListener.itemClick(newsSlide.activityHtml5Url
                        , newsSlide.activityName
                        , newsSlide.isNeedHead
                        , position);
            }
        });
    }

    @Override
    public String getTitle(int position) {
        return "";
    }

    public interface OnClickBannerListener {

        void itemClick(String url, String circleColumnName, int isNeedHead, int position);
    }

    public interface OnShowPicBannerListener {

        void showPic(ImageView imageview, String url);
    }
}
