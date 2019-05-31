package com.paizhong.manggo.widget.banner;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.utils.DeviceUtils;


public class Banner extends LinearLayout {

    private TextView mTitle;
    private Context mContext;

    private AutoFlingViewPager mPager;
    private IndicatorView mBannerIndicator;
    private BannerFlingAdapter mPagerAdapter;

    public Banner(Context context) {
        super(context);
        initViews(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        this.mContext = context;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_banner, this, true);
        mTitle = findViewById(R.id.tv_title);
        mPager = findViewById(R.id.pager_banner);
        mBannerIndicator = findViewById(R.id.indicator);

        mPager.addOnPageChangeListener(new OnBannerChangeListener());
        mPager.setDuration(1000);
    }

    @Override
    public void setOnClickListener(OnClickListener clickListener) {
        mPager.setOnClickListener(clickListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent viewParent = mPager.getParent();
        while (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(true);
            viewParent = viewParent.getParent();
        }
        return super.onTouchEvent(event);
    }

    /****
     * 设置Banner宽高比例  边距比例
     * @param ratio 宽高比例 （width/height）
     * @param rightRatio
     * @param bottomRatio
     */
    public void setBannerRatio(float ratio, float rightRatio, float bottomRatio) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int height = (int) (DeviceUtils.getScreenWidth(mContext) * ratio);
        params.height = height;
        super.setLayoutParams(params);

        int bottomMargin = (int) (height * bottomRatio);
        int rightMargin = (int) (DeviceUtils.getScreenWidth(mContext) * rightRatio);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBannerIndicator.getLayoutParams();
        lp.setMargins(0, 0, rightMargin, bottomMargin);
        mBannerIndicator.setLayoutParams(lp);
    }

    public void setAdapter(BaseAutoFlingAdapter<?> adapter) {
        mPagerAdapter = (BannerFlingAdapter) adapter;
        mPager.setAdapter(mPagerAdapter);
        mPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                mBannerIndicator.init(mPagerAdapter.getRealCount());
                mTitle.setText(mPagerAdapter.getTitle(mPager.getCurrentItem()));
            }
        });
    }

    public void stop() {
        mPager.stop();
    }

    public void start() {
        mPager.start();
    }

    public void setDuration(int duration) {
        mPager.setDuration(duration);
    }


    class OnBannerChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            if (mPagerAdapter != null) {
                int currentPosition = position % mPagerAdapter.getRealCount();
                mBannerIndicator.currentTab(currentPosition);
               // mTitle.setText(mPagerAdapter.getTitle(currentPosition));
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        // 滑动时候，父控件不拦截事件！解决banner 不好滑动的问题。
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                ViewParent viewParent = mPager.getParent();
                while (viewParent != null) {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                    viewParent = viewParent.getParent();
                }
            }
        }
    }
}
