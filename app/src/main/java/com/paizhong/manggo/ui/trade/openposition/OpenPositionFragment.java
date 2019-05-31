package com.paizhong.manggo.ui.trade.openposition;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseFragment;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.other.ChatMsgBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.dialog.sendmsg.SendMsgDialog;
import com.paizhong.manggo.http.Mq;
import com.paizhong.manggo.ui.kchart.activity.KLineMarketActivity;
import com.paizhong.manggo.ui.kchart.bean.KlineCycle;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.views.CrossView;
import com.paizhong.manggo.ui.kchart.views.KChartAdapter;
import com.paizhong.manggo.ui.kchart.views.TradeKChartView;
import com.paizhong.manggo.ui.kchart.views.TradeMinuteHourView;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.FlickerProgressBar;
import com.paizhong.manggo.widget.tab.KLineCommonTabLayout;
import com.paizhong.manggo.widget.tab.OnMyTabSelectListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 建仓
 * Created by zab on 2018/8/17 0017.
 */
public class OpenPositionFragment extends BaseFragment<OpenPositionPresenter> implements OpenPositionContract.View {
    @BindView(R.id.ll_product_layout)
    LinearLayout llProductLayout;
    @BindView(R.id.commonTab)
    KLineCommonTabLayout commonTab;
    @BindView(R.id.minute_hour_view)
    TradeMinuteHourView mMinuteHourView;
    @BindView(R.id.cross_view)
    CrossView mCrossView;
    @BindView(R.id.ly_professional_kline)
    TradeKChartView mProfKChartView;
    @BindView(R.id.rv_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_start_market)
    ImageView ivStartMarket;
    @BindView(R.id.ll_msg_layout)
    LinearLayout llMsgLayout;
    @BindView(R.id.pb_progress)
    FlickerProgressBar progressBar;
    @BindView(R.id.tv_up_num)
    TextView tvUpNum;
    @BindView(R.id.tv_down_num)
    TextView tvDownNum;
    @BindView(R.id.tv_buy_up)
    TextView tvBuyUp;
    @BindView(R.id.tv_buy_down)
    TextView tvBuyDown;
    @BindView(R.id.tv_hangup)
    TextView tvHangup;
    @BindView(R.id.ll_kline_bottom)
    LinearLayout llKlineBottom;
    @BindView(R.id.ll_progress)
    View llProgress;

    private String mTimeCode;
    private String mLineType;
    private boolean mIsInitView = false;
    private ProductTabViewModel mProductTabViewModel;
    private KChartAdapter mAdapter;
    private ChatAdapter mChatAdapter;
    private SendMsgDialog mSendMsgDialog;

    //分时图数据
    private ArrayList<MinuteHourBean> mDataList = new ArrayList<>();
    private ArrayList<String> mDateList = new ArrayList<>();


    private static OpenPositionFragment mOpenPositionFragment;

    public static OpenPositionFragment getInstance() {
        if (mOpenPositionFragment == null) {
            synchronized (OpenPositionFragment.class) {
                if (mOpenPositionFragment == null) {
                    mOpenPositionFragment = new OpenPositionFragment();
                }
            }
        }
        return mOpenPositionFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_open_position;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(OpenPositionFragment.this);
    }

    public boolean isInitView() {
        return mIsInitView;
    }


    public void setNewLinePrice(float newLinePrice, float changeValue, String timeStr) {
        if (mMinuteHourView != null) {
            mMinuteHourView.setNewLinePrice(newLinePrice, changeValue, timeStr);
            mProfKChartView.setNewLinePrice(newLinePrice, changeValue);
        }
    }


    public void changeProduct(String productID) {
        if (mIsInitView && mProductTabViewModel != null && !TextUtils.equals(productID, mProductTabViewModel.getProductID())) {
            mProductTabViewModel.changeProduct(productID, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final ChatMsgBean event) {
        if (event != null && mChatAdapter != null && ivEye !=null && ivEye.isSelected()) {
            try {
                ivEye.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerView !=null){
                            ChatMsgBean chatMsgBean = mChatAdapter.getChatMsgBean();
                            if (chatMsgBean == null
                                    || !TextUtils.equals(chatMsgBean.phone,event.phone)
                                    || !TextUtils.equals(chatMsgBean.content,event.content)
                                    || chatMsgBean.dateTime == null
                                    || !chatMsgBean.dateTime.equals(event.dateTime)){
                                mChatAdapter.notifyDataChanged(event);
                                recyclerView.scrollToPosition(mChatAdapter.getItemCount()-1);
                            }
                        }
                    }
                }, 50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        llProductLayout.removeAllViews();
        mProductTabViewModel = new ProductTabViewModel(((MainActivity) getActivity()));
        llProductLayout.addView(mProductTabViewModel.mView);
        List<KlineCycle> klineCycleList = KLineDataUtils.getKlineCycleList(false, "");
        mMinuteHourView.setTouchEnabled(false);
        mMinuteHourView.setCrossView(mCrossView, "");
        mAdapter = new KChartAdapter();
        mProfKChartView.setAdapter(mAdapter);
        mProfKChartView.setOverScrollRange(DeviceUtils.dip2px(getContext(), 100));

        ivEye.setSelected(false);
        mChatAdapter = new ChatAdapter(mActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mChatAdapter);

        setListener(klineCycleList);
        commonTab.addTab(klineCycleList);
        mIsInitView = true;
        if (AppApplication.getConfig().mIsAuditing){
            llProgress.setVisibility(View.GONE);
            llKlineBottom.setVisibility(View.GONE);
            ivEye.setVisibility(View.INVISIBLE);
            ivMsg.setVisibility(View.INVISIBLE);
            ivStartMarket.setVisibility(View.INVISIBLE);
        }else {
            queryVipUser();
        }
        setUserVisibleHint(true);
    }


    private void setListener(final List<KlineCycle> klineCycleList) {
        //回调tab切换
        mProductTabViewModel.setOnProductSelectListener(new ProductTabViewModel.OnProductSelectListener() {
            @Override
            public void onProductSelect() {
                tabSelectClear(mProductTabViewModel.getClosePrice());
                startRun();
            }

            @Override
            public void onProductLimit(int buyUp, int toBuy) {
                if (buyUp == 0) {
                    progressBar.setProgress(50);
                    tvUpNum.setText("50%买涨");
                    tvDownNum.setText("50%买跌");
                } else {
                    progressBar.setProgress(buyUp);
                    tvUpNum.setText(buyUp + "%买涨");
                    tvDownNum.setText(toBuy + "%买跌");
                }
            }
        });

        commonTab.setOnMyTabSelectListener(new OnMyTabSelectListener() {
            @Override
            public void onTabSelect(int position, String type) {
                if (position == 0) {
                    mMinuteHourView.setVisibility(View.VISIBLE);
                    mProfKChartView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = llMsgLayout.getLayoutParams();
                    layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dimen_50dp);
                    llMsgLayout.setLayoutParams(layoutParams);
                } else {
                    mTimeCode = klineCycleList.get(position).getTimeCode();
                    mLineType = klineCycleList.get(position).getCode();
                    ViewGroup.LayoutParams layoutParams = llMsgLayout.getLayoutParams();
                    layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dimen_60dp);
                    llMsgLayout.setLayoutParams(layoutParams);
                    mProfKChartView.setIsNewPrice(!TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode) && !TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode));
                    mMinuteHourView.setVisibility(View.GONE);
                    mProfKChartView.setVisibility(View.VISIBLE);
                    tabSelectClear(mProductTabViewModel.getClosePrice());
                    startRun();
                }
            }
        });
    }


    @OnClick({R.id.tv_buy_up, R.id.tv_buy_down, R.id.tv_hangup, R.id.iv_start_market, R.id.iv_eye,R.id.iv_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_start_market: //跳转到k线
                if (!DeviceUtils.isFastDoubleClick()
                        && mProductTabViewModel != null
                        && !TextUtils.isEmpty(mProductTabViewModel.getClosePrice())
                        && mActivity.login()) {
                    startKLineActivity();
                }
                break;
            case R.id.iv_eye: //是否打开消息
                if (!DeviceUtils.isFastDoubleClick()){
                    updataEye(!ivEye.isSelected(),true);
                }
                break;
            case R.id.iv_msg: //发送消息
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login()){
                    if (!ivEye.isSelected()){
                        updataEye(true,false);
                    }
                    if (mSendMsgDialog == null){
                        mSendMsgDialog = new SendMsgDialog(mActivity);
                    }
                    mSendMsgDialog.show();
                }
                break;
            case R.id.tv_buy_up: //买涨下单
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login() && mProductTabViewModel != null) {
                    ((MainActivity) mActivity).placeOrder().placeOrder(mProductTabViewModel.getProductID(), true, 1);
                }
                break;
            case R.id.tv_buy_down:
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login() && mProductTabViewModel != null) {
                    ((MainActivity) mActivity).placeOrder().placeOrder(mProductTabViewModel.getProductID(), false, 1);
                }
                break;
            case R.id.tv_hangup:
                if (!DeviceUtils.isFastDoubleClick() && mActivity.login() && mProductTabViewModel != null) {
                    ((MainActivity) mActivity).placeOrder().placeOrderHangUp(mProductTabViewModel.getProductID(), true, 1);
                }
                break;
        }
    }



    private void startKLineActivity(){
        Intent intent = new Intent(mActivity, KLineMarketActivity.class);
        intent.putExtra("productID", mProductTabViewModel.getProductID());
        intent.putExtra("productName", KLineDataUtils.getProductName(mProductTabViewModel.getProductID()));
        intent.putExtra("close", false);
        intent.putExtra("closePrice", mProductTabViewModel.getClosePrice());
        intent.putExtra("ckLan", true);
        intent.putExtra("eye",ivEye.isSelected());
        if (mChatAdapter.getItemCount() > 0){
            intent.putExtra("chatList", (Serializable) mChatAdapter.getList());
            intent.putExtra("dateTime",mChatAdapter.getmDateTime());
        }
        startActivity(intent);
    }

    private void updataEye(boolean eye,boolean click){
        if (eye){
            ivEye.setSelected(true);
            ivEye.setImageResource(R.mipmap.ic_eye_close);
            recyclerView.setVisibility(View.VISIBLE);
            if (!Mq.get().isOpen()){
                Mq.get().startRun();
            }
            if (click){
                showErrorMsg("弹幕已开启",null);
            }
        }else {
            ivEye.setSelected(false);
            ivEye.setImageResource(R.mipmap.ic_eye_open);
            recyclerView.setVisibility(View.GONE);
            if (Mq.get().isOpen()){
                Mq.get().stopRun();
            }
            if (click){
                showErrorMsg("弹幕已关闭",null);
            }
        }
    }

    public void updataEye(boolean eye){
        if (eye){
            ivEye.setSelected(true);
            ivEye.setImageResource(R.mipmap.ic_eye_close);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            ivEye.setSelected(false);
            ivEye.setImageResource(R.mipmap.ic_eye_open);
            recyclerView.setVisibility(View.GONE);
        }
    }

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mProductTabViewModel != null && mProfKChartView != null) {
                mProductTabViewModel.refreshData();

                if (!TextUtils.isEmpty(mProductTabViewModel.getProductID())) {
                    if (mProfKChartView.getVisibility() == View.VISIBLE) {
                        getKLineRun(mProductTabViewModel.getProductID());
                    } else {
                        getMinuteHourRun(mProductTabViewModel.getProductID(), mProductTabViewModel.getClosePrice());
                    }
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };


    public void stopRun() {
        if (mProductTabViewModel != null) {
            mProductTabViewModel.mIsProductMsg = false;
        }
        mHandler.removeCallbacks(mRunnable);
    }

    public void startRun() {
        mPresenter.getProductList();
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }


    public void startMq(){
        try {
            if (ivEye!=null
                    && ivEye.isSelected()
                    && recyclerView !=null
                    && mChatAdapter !=null
                    && !Mq.get().isOpen()) {
                Mq.get().startRun();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //分时线接口
    private void getMinuteHourRun(String mProductID, String mYestodayClosePrice) {
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG, mProductID)) { //银
            mPresenter.getAGTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU, mProductID)) {//铜
            mPresenter.getCUTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL, mProductID)) {//镍
            mPresenter.getNLTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS, mProductID)) {//大豆
            mPresenter.getZSTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW, mProductID)) {//小麦
            mPresenter.getZWTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        } else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC, mProductID)) {//玉米
            mPresenter.getZCTime(mProductID, mYestodayClosePrice, mDataList, mDateList);
        }
    }


    //k 线
    public void getKLineRun(String mProductID) {
        if (TextUtils.equals(ViewConstant.KLINE_TAB_1, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_5, mTimeCode)) {
            mPresenter.getZJKlineQuotation_5(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_15, mTimeCode)) {
            mPresenter.getZJKlineQuotation_15(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_30, mTimeCode)) {
            mPresenter.getZJKlineQuotation_30(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_1H, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1h(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_4H, mTimeCode)) {
            mPresenter.getZJKlineQuotation_4h(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode)) {
            mPresenter.getZJKlineQuotation_1d(mLineType, mProductID);
        } else if (TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode)) {
            mPresenter.getZJKlineQuotation_week(mLineType, mProductID);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInitView()) {
            if (isVisibleToUser) {
                startRun();
            } else {
                stopRun();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (mProductTabViewModel != null) {
            mProductTabViewModel.onDestroy();
            mProductTabViewModel = null;
        }
        if (mSendMsgDialog !=null){
            mSendMsgDialog.onDestroy();
            mSendMsgDialog = null;
        }
    }


    @Override
    public String getTimeStr() {
        return mMinuteHourView != null ? mMinuteHourView.getTimeStr() : "";
    }

    @Override
    public float getNewLinePrice() {
        return mMinuteHourView != null ? mMinuteHourView.getNewLinePrice() : 0;
    }

    @Override
    public String getClosePrice() {
        return mProductTabViewModel != null ? mProductTabViewModel.getClosePrice() : "";
    }


    //产品信息接口
    @Override
    public void bindProductList(List<ProductListBean> productList) {
        if (mProductTabViewModel != null && productList != null && productList.size() > 0) {
            mProductTabViewModel.setProductList(productList);
        }
    }


    //查询是不是vip
    @Override
    public void bindVipUser(boolean isVipUser) {
        if (mChatAdapter !=null){
            mChatAdapter.setGm(isVipUser);
        }
    }

    //禁言
    public void forbiddenUser(final String name,final String isPhone){
        final AppHintDialog appHintDialog = new AppHintDialog(mActivity);
        appHintDialog.showAppDialog(10, name+","+isPhone, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.forbiddenUser(isPhone);
                appHintDialog.dismiss();
            }
        });
    }


    public void queryVipUser(){
        if (AppApplication.getConfig().isLoginStatus() && mIsInitView && mPresenter !=null){
            mPresenter.queryVipUser();
        }
    }

    //分时线
    @Override
    public void bindZJQuotation(String mProductID, String yestodayClosePrice) {
        if (mProductTabViewModel != null && TextUtils.equals(mProductTabViewModel.getProductID(), mProductID)) {
            mMinuteHourView.setDataAndInvalidate(mDataList, ""
                    , mDateList, KLineUtils.getDouble(yestodayClosePrice));
            if (mMinuteHourView.getVisibility() == View.INVISIBLE) {
                mMinuteHourView.setVisibility(View.VISIBLE);
            }
        }
    }

    //k线
    @Override
    public void bindZJKlineQuotation(String timeCode, String mProductID, KLineBean kLineBean) {
        if (mProductTabViewModel != null
                && TextUtils.equals(mProductTabViewModel.getProductID(), mProductID)
                && TextUtils.equals(timeCode, mTimeCode)) {
            mAdapter.setData(KLineDataUtils.parseKLine(mTimeCode, false, kLineBean, mProfKChartView.getNewLinePrice()));
        }
    }


    //tab 切换清除当前对应的数据
    private void tabSelectClear(String yestodayClosePrice) {
        if (mProfKChartView.getVisibility() == View.VISIBLE) {
            mAdapter.clear();
        } else {
            if (!TextUtils.isEmpty(yestodayClosePrice)) {
                mMinuteHourView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
