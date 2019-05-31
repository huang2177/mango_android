package com.paizhong.manggo.ui.kchart.fragment;
import android.os.Handler;
import android.text.TextUtils;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.views.CrossView;
import com.paizhong.manggo.ui.kchart.views.MinuteHourView;
import java.util.ArrayList;
import butterknife.BindView;


/**
 * 分时fragment
 */
public class MinuteHourFragment extends BaseFragment<MinuteHourPresenter> implements MinuteHourContract.View{
    @BindView(R.id.minute_hour_view)
    MinuteHourView mMinuteHourView;
    @BindView(R.id.cross_view)
    CrossView mCrossView;


    private String mLineType;  //type 类型
    private String mProductCode; //KLineDataUtils.getProductCode
    private int mType;//0 爱淘行情  1-黄金 2-农产品 3-贵金属 4-外汇
    private boolean mClose = false;//是否休市
    private String mYestodayClosePrice;//昨收
    private boolean mIsCkMarket;//true 参考行情
    private String mProductID;//产品id

    private ArrayList<MinuteHourBean> mDataList = new ArrayList<>();
    private ArrayList<String> mDateList = new ArrayList<>();

    private boolean isTouchEnabled = false;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_minute_line;
    }

    @Override
    public void initPresenter() {
         mPresenter.init(this);
    }

    public void setClose(boolean mClose){
        this.mClose = mClose;
    }

    public void updateStyle(boolean mYeSyle){
      if (mMinuteHourView !=null){
          mMinuteHourView.updateStyle(mYeSyle);
      }
    }


    public void setQHProduct(String mProductID,String mProductCode,String closePrice){
        this.mProductID = mProductID;
        this.mProductCode = mProductCode;
        this.mYestodayClosePrice = closePrice;
    }



    @Override
    public void loadData() {
        mLineType = getArguments().getString("lineType");
        mProductCode = getArguments().getString("productCode");
        mYestodayClosePrice = getArguments().getString("closePrice");
        mIsCkMarket = getArguments().getBoolean("ckMarket");
        mProductID = getArguments().getString("productID");
        mType = getArguments().getInt("type");
        mMinuteHourView.setIsCKZS(mIsCkMarket);
        mMinuteHourView.setTouchEnabled(isTouchEnabled);
        mMinuteHourView.setCrossView(mCrossView, "");
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsCkMarket){
                mPresenter.getCKSJQuotation(mType,mProductCode,mYestodayClosePrice,mDataList,mDateList);
            }else {
                getHttpRun();
            }
            mHandler.postDelayed(this, 1000);
        }
    };


    private void getHttpRun(){
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mProductID)){ //银
            mPresenter.getAGTime(mYestodayClosePrice,mDataList,mDateList);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mProductID)){//铜
            mPresenter.getCUTime(mYestodayClosePrice,mDataList,mDateList);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mProductID)){//镍
            mPresenter.getNLTime(mYestodayClosePrice,mDataList,mDateList);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mProductID)){//大豆
            mPresenter.getZSTime(mYestodayClosePrice,mDataList,mDateList);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,mProductID)){//小麦
            mPresenter.getZWTime(mYestodayClosePrice,mDataList,mDateList);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,mProductID)){//玉米
            mPresenter.getZCTime(mYestodayClosePrice,mDataList,mDateList);
        }
    }

    public void startRun() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            startRun();
        }else {
            stopRun();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()){
            startRun();
        }else {
            stopRun();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    @Override
    public void bindZJQuotation() {
        if (mClose){
            stopRun();
        }
        mMinuteHourView.setDataAndInvalidate(mDataList, ""
                , mDateList, KLineUtils.getDouble(mYestodayClosePrice));
    }

    @Override
    public String getTimeStr() {
        return mMinuteHourView !=null? mMinuteHourView.getTimeStr() : "" ;
    }

    @Override
    public float getNewLinePrice() {
        return mMinuteHourView !=null ? mMinuteHourView.getNewLinePrice() : 0;
    }

    public void setNewLinePrice(float newLinePrice ,float changeValue ,String timeStr){
        if (mMinuteHourView !=null){
            mMinuteHourView.setNewLinePrice(newLinePrice,changeValue,timeStr);
        }
    }
}
