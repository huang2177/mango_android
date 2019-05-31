package com.paizhong.manggo.ui.kchart.fragment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.kchart.bean.KLineEntity;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.ui.kchart.views.BaseKChart;
import com.paizhong.manggo.ui.kchart.views.KChartAdapter;
import com.paizhong.manggo.ui.kchart.views.KChartView;
import com.paizhong.manggo.utils.DeviceUtils;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;



/**
 * 专业 K 线图
 */
public class KLineFragment extends BaseFragment<KLinePresenter> implements KLineContract.View {
    @BindView(R.id.ly_professional_kline)
    KChartView mProfKChartView;
    @BindView(R.id.v_kline_lan)
    View vKlineLan;
    @BindView(R.id.ll_market_kline_layout)
    LinearLayout llMarketKlineLayout;
    @BindView(R.id.tv_sma)
    TextView tvSma;
    @BindView(R.id.tv_ema)
    TextView tvEma;
    @BindView(R.id.tv_boll)
    TextView tvBoll;
    @BindView(R.id.tv_macd)
    TextView tvMacd;
    @BindView(R.id.tv_kdj)
    TextView tvKdj;
    @BindView(R.id.tv_rsi)
    TextView tvRsi;

    private String mLineType; //type 类型
    private String mProductCode; //KLineDataUtils.getProductCode
    private String mTimeCode;//所属分线
    private int mType;
    private boolean mClose = false;
    private boolean mIsCkMarket;//true 参考行情
    private String mProductID;//产品id

    private boolean mIsInitView = false;
    private KChartAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_professional_kline;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    public void setClose(boolean mClose) {
        this.mClose = mClose;
    }


    public void updateStyle(boolean mYeSyle) {
        if (mProfKChartView != null) {
            vKlineLan.setBackgroundColor(mYeSyle ? ContextCompat.getColor(mActivity,R.color.color_1F1C28) : ContextCompat.getColor(mActivity,R.color.line_color));
            mProfKChartView.updateStyle(mYeSyle);
            if (tvSma.isSelected()){
                setTabTopStyle(mYeSyle,tvSma);
            }else if (tvEma.isSelected()){
                setTabTopStyle(mYeSyle,tvEma);
            }else if (tvBoll.isSelected()){
                setTabTopStyle(mYeSyle,tvBoll);
            }

            if (tvMacd.isSelected()){
                setTabMiddleStyle(mYeSyle,tvMacd);
            }else if (tvKdj.isSelected()){
                setTabMiddleStyle(mYeSyle,tvKdj);
            }else if (tvRsi.isSelected()){
                setTabMiddleStyle(mYeSyle,tvRsi);
            }
        }
    }

    public void setIsFirstDraw(boolean land) {
        llMarketKlineLayout.setVisibility(land ? View.VISIBLE : View.GONE);
        if (mProfKChartView != null) {
            mProfKChartView.setIsFirstDraw(land);
        }
    }

    public void setQHProduct(String mProductID,String mProductCode){
        this.mProductID = mProductID;
        this.mProductCode = mProductCode;
    }



    @Override
    public void loadData() {
        mIsCkMarket = getArguments().getBoolean("ckMarket");
        mProductID = getArguments().getString("productID");
        mProductCode = getArguments().getString("productCode");
        mType = getArguments().getInt("type");
        mProfKChartView.setIsNewPrice(!mIsCkMarket);
        if (getArguments().getBoolean("ckLan")){
            setIsFirstDraw(true);
        }
        setTabTopStyle(tvSma);
        setTabMiddleStyle(tvMacd);
        mAdapter = new KChartAdapter();
        mProfKChartView.setAdapter(mAdapter);
        mProfKChartView.setOverScrollRange(DeviceUtils.dip2px(getContext(), 100));
        mIsInitView = true;
        mProfKChartView.setOnTabSelectListener(new BaseKChart.OnTabSelectListener() {
            @Override
            public void onTopTabSelect(int position) {

                if (position == 0){
                    setTabTopStyle(tvSma);
                }else if (position == 1){
                    setTabTopStyle(tvEma);
                }else if (position == 2){
                    setTabTopStyle(tvBoll);
                }
            }

            @Override
            public void onMiddleTabSelect(int position) {

                if (position == 0){
                    setTabMiddleStyle(tvMacd);
                }else if (position == 1){
                    setTabMiddleStyle(tvKdj);
                }else if (position == 2){
                    setTabMiddleStyle(tvRsi);
                }
            }
        });
    }


    @OnClick({R.id.tv_sma, R.id.tv_ema, R.id.tv_boll, R.id.tv_macd, R.id.tv_kdj, R.id.tv_rsi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sma:
                if (!DeviceUtils.isFastDoubleClick() && !tvSma.isSelected()){
                    setTabTopStyle(tvSma);
                    mProfKChartView.setTopTab(0);
                }
                break;
            case R.id.tv_ema:
                if (!DeviceUtils.isFastDoubleClick() && !tvEma.isSelected()){
                    setTabTopStyle(tvEma);
                    mProfKChartView.setTopTab(1);
                }
                break;
            case R.id.tv_boll:
                if (!DeviceUtils.isFastDoubleClick() && !tvBoll.isSelected()){
                    setTabTopStyle(tvBoll);
                    mProfKChartView.setTopTab(2);
                }
                break;
            case R.id.tv_macd:
                if (!DeviceUtils.isFastDoubleClick() && !tvMacd.isSelected()){
                    setTabMiddleStyle(tvMacd);
                    mProfKChartView.setMiddleTab(0);
                }
                break;
            case R.id.tv_kdj:
                if (!DeviceUtils.isFastDoubleClick() && !tvKdj.isSelected()){
                    setTabMiddleStyle(tvKdj);
                    mProfKChartView.setMiddleTab(1);
                }
                break;
            case R.id.tv_rsi:
                if (!DeviceUtils.isFastDoubleClick() && !tvRsi.isSelected()){
                    setTabMiddleStyle(tvRsi);
                    mProfKChartView.setMiddleTab(2);
                }
                break;
        }
    }


    private void setTabTopStyle(TextView txtView){
        setTabTopStyle(AppApplication.getConfig().getYeStyle(),txtView);
    }
    private void setTabTopStyle(boolean mYeSyle,TextView txtView){
        int color_878787 = ContextCompat.getColor(mActivity, R.color.color_878787);
        if (tvSma.isSelected()){
            tvSma.setTextColor(color_878787);
            tvSma.setSelected(false);
            tvSma.setBackground(null);
        }
        if (tvEma.isSelected()){
            tvEma.setTextColor(color_878787);
            tvEma.setSelected(false);
            tvEma.setBackground(null);
        }
        if (tvBoll.isSelected()){
            tvBoll.setTextColor(color_878787);
            tvBoll.setSelected(false);
            tvBoll.setBackground(null);
        }
        txtView.setSelected(true);
        txtView.setTextColor(mYeSyle ? ContextCompat.getColor(mActivity,R.color.color_ffffff) : color_878787);
        txtView.setBackground(mYeSyle ? ContextCompat.getDrawable(mActivity, R.drawable.bg_shape_r3_2a334d) : ContextCompat.getDrawable(mActivity, R.drawable.bg_shape_r4_f0f0f0));
    }


    private void setTabMiddleStyle(TextView txtView){
        setTabMiddleStyle(AppApplication.getConfig().getYeStyle(),txtView);
    }
    private void setTabMiddleStyle(boolean mYeSyle,TextView txtView){
        int color_878787 = ContextCompat.getColor(mActivity, R.color.color_878787);
        if (tvMacd.isSelected()){
            tvMacd.setTextColor(color_878787);
            tvMacd.setSelected(false);
            tvMacd.setBackground(null);
        }
        if (tvKdj.isSelected()){
            tvKdj.setTextColor(color_878787);
            tvKdj.setSelected(false);
            tvKdj.setBackground(null);
        }
        if (tvRsi.isSelected()){
            tvRsi.setTextColor(color_878787);
            tvRsi.setSelected(false);
            tvRsi.setBackground(null);
        }

        txtView.setSelected(true);
        txtView.setTextColor(mYeSyle ? ContextCompat.getColor(mActivity,R.color.color_ffffff) : color_878787);
        txtView.setBackground(mYeSyle ? ContextCompat.getDrawable(mActivity, R.drawable.bg_shape_r3_2a334d) : ContextCompat.getDrawable(mActivity, R.drawable.bg_shape_r4_f0f0f0));
    }

    public void setTabSelect(String lineType, String timeCode) {
        this.mLineType = lineType;
        this.mTimeCode = timeCode;
        mProfKChartView.setIsNewPrice(!mIsCkMarket && !TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode) && !TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode));
        if (mIsInitView && mAdapter.getCount() > 0) {
            mAdapter.clear();
        }
    }

    public void setTabSelectHttp(String lineType, String timeCode) {
        setTabSelect(lineType,timeCode);
        getHttp();
    }

    //timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1H=一个小时 4H=四个小时 1D=日线  week=周线
    private void getHttp() {
        stopRun();
        if (TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode)) {
            mPresenter.getZJKlineQuotation_week(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else {
            if (!mClose) {
                mHandler.post(mRunnable);
            } else {
                getHttpRefresh();
            }
        }
    }


    public void getHttpRefresh() {
        if (TextUtils.equals(ViewConstant.KLINE_TAB_1, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_5, mTimeCode)) {
            mPresenter.getZJKlineQuotation_5(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_15, mTimeCode)) {
            mPresenter.getZJKlineQuotation_15(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_30, mTimeCode)) {
            mPresenter.getZJKlineQuotation_30(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_1H, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1h(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        }else if (TextUtils.equals(ViewConstant.KLINE_TAB_2H, mTimeCode)){
            mPresenter.getZJKlineQuotation_2h(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_4H, mTimeCode)) {
            mPresenter.getZJKlineQuotation_4h(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1d(mType,mIsCkMarket, mLineType, mProductCode, mProductID);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getHttp();
        } else {
            stopRun();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            getHttp();
        } else {
            stopRun();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getHttpRefresh();
            mHandler.postDelayed(this, 1000);
        }
    };


    private void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * @param timeCode  timeCode  1=1分钟  5=5分钟  15=15分钟 30=30分钟 1H=一个小时 4H=四个小时 1D=日线  week=周线
     * @param kLineBean
     */
    @Override
    public void bindZJKlineQuotation(String timeCode, KLineBean kLineBean) {
        if (mClose) {
            stopRun();
        }
        if (TextUtils.equals(timeCode, mTimeCode)) {
            if (!mIsCkMarket) {
                mAdapter.setData(KLineDataUtils.parseKLine(mTimeCode,mIsCkMarket, kLineBean,mProfKChartView.getNewLinePrice()));
            } else {
                List<KLineEntity> kLineEntities = KLineDataUtils.parseKLine(mIsCkMarket, kLineBean);
                mAdapter.setData(kLineEntities);
            }
        }
    }


    public void setNewLinePrice(float newLinePrice ,float changeValue){
        if (mProfKChartView !=null){
            mProfKChartView.setNewLinePrice(newLinePrice,changeValue);
        }
    }
}
