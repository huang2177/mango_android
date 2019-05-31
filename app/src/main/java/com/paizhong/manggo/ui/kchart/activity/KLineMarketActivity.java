package com.paizhong.manggo.ui.kchart.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.ChatMsgBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.market.MarketDialog;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.dialog.placeorderlan.PlaceOrderLanDialog;
import com.paizhong.manggo.dialog.position.PositionDialog;
import com.paizhong.manggo.dialog.sendmsg.SendMsgDialog;
import com.paizhong.manggo.events.JysAccountEvent;
import com.paizhong.manggo.http.Mq;
import com.paizhong.manggo.ui.kchart.bean.KlineCycle;
import com.paizhong.manggo.ui.kchart.fragment.KLineFragment;
import com.paizhong.manggo.ui.kchart.fragment.MinuteHourFragment;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.ui.trade.openposition.ChatAdapter;
import com.paizhong.manggo.ui.trade.openposition.OpenPositionFragment;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.utils.bar.StatusBarUtil;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.tab.KLineCommonNrTabLayout;
import com.paizhong.manggo.widget.tab.KLineCommonTabLayout;
import com.paizhong.manggo.widget.tab.OnMyTabSelectListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zab on 2018/8/16 0016.
 */
public class KLineMarketActivity extends BaseActivity<KLineMarketPresenter> implements KLineMarketContract.View {
    @BindView(R.id.iv_title_arrow)
    ImageView ivTitleArrow;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.tv_title_name_lan)
    TextView tvTitleNameLan;
    @BindView(R.id.tv_xiu)
    TextView tvXiu;
    @BindView(R.id.tv_xiu_lan)
    TextView tvXiuLan;
    @BindView(R.id.iv_title_w)
    ImageView ivTitleW;
    @BindView(R.id.iv_title_full_close)
    ImageView ivTitleFullClose;
    @BindView(R.id.iv_title_w_lan)
    ImageView ivTitleWLan;
    @BindView(R.id.ll_title_layout)
    LinearLayout llTitleLayout;
    @BindView(R.id.tv_new_price)
    DinTextView tvNewPrice;
    @BindView(R.id.tvw_price_num)
    DinTextView tvwPriceNum;
    @BindView(R.id.tvw_price_num_lan)
    DinTextView tvwPriceNumLan;
    @BindView(R.id.tv_price_percentage)
    DinTextView tvPricePercentage;
    @BindView(R.id.tv_price_percentage_lan)
    DinTextView tvPricePercentageLan;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_time_lan)
    TextView tvTimeLan;
    @BindView(R.id.tv_hangup)
    TextView tvHangup;
    @BindView(R.id.ll_title_bottom_layout)
    LinearLayout llTitleBottomLayout;
    @BindView(R.id.ll_product_time_lan)
    LinearLayout llProductTimeLan;
    @BindView(R.id.iv_more_list_lan)
    ImageView ivMoreListLan;
    @BindView(R.id.ll_price_num_per_pro)
    LinearLayout llPriceNumPerPro;
    @BindView(R.id.ll_price_num_per_lan)
    LinearLayout llPriceNumPerLan;

    //竖屏
    @BindView(R.id.ll_pro_layout)
    LinearLayout llProLayout;
    @BindView(R.id.tv_price_highest_hint)
    TextView tvPriceHighestHint;
    @BindView(R.id.tv_price_highest)
    DinTextView tvPriceHighest;
    @BindView(R.id.tv_price_thi_open_hint)
    TextView tvPriceThiOpenHint;
    @BindView(R.id.tv_price_thi_open)
    DinTextView tvPriceThiOpen;
    @BindView(R.id.tv_price_minimum_hint)
    TextView tvPriceMinimumHint;
    @BindView(R.id.tv_price_minimum)
    DinTextView tvPriceMinimum;
    @BindView(R.id.tv_price_z_collect_hint)
    TextView tvPriceZCollectHint;
    @BindView(R.id.tv_price_z_collect)
    DinTextView tvPriceZCollect;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.iv_title_full_open)
    ImageView ivTitleFullOpen;

    //横屏
    @BindView(R.id.ll_lan_layout)
    LinearLayout llLanLayout;
    @BindView(R.id.tv_price_highest_hint_lan)
    TextView tvPriceHighestLanHint;
    @BindView(R.id.tv_price_highest_lan)
    DinTextView tvPriceHighestLan;
    @BindView(R.id.tv_price_thi_open_hint_lan)
    TextView tvPriceThiOpenLanHint;
    @BindView(R.id.tv_price_thi_open_lan)
    DinTextView tvPriceThiOpenLan;
    @BindView(R.id.tv_price_minimum_hint_lan)
    TextView tvPriceMinimumLanHint;
    @BindView(R.id.tv_price_minimum_lan)
    DinTextView tvPriceMinimumLan;
    @BindView(R.id.tv_price_z_collect_hint_lan)
    TextView tvPriceZCollectLanHint;
    @BindView(R.id.tv_price_z_collect_lan)
    DinTextView tvPriceZCollectLan;
    @BindView(R.id.v_title_z_line)
    View vTitleZline;
    @BindView(R.id.tv_buy_up_lan)
    View tvBuyUpLan;
    @BindView(R.id.tv_buy_down_lan)
    View tvBuyDownLan;


    @BindView(R.id.v_bottom_line)
    View vBottomLine;
    @BindView(R.id.v_content_line)
    View vContentLine;
    @BindView(R.id.commonTab)
    KLineCommonTabLayout mCommonTab;
    @BindView(R.id.v_kline_bottom_lan)
    View vKlineBottomLan;
    @BindView(R.id.commonTab_lan)
    KLineCommonNrTabLayout mCommonTabLan;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.ll_kline_bottom)
    LinearLayout llKLineBottom;
    @BindView(R.id.ll_kline_bottom_lan)
    LinearLayout llKLineBottomLan;

    @BindView(R.id.rv_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_msg_layout)
    View llMsgLayout;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;

    private FragmentManager mFragmentManager;
    private MinuteHourFragment mHourFragment;
    private KLineFragment mKLineFragment;


    private boolean mClose;//true 休市 false 沒休市
    private String mProductCode;
    private String mProductID;
    private boolean mLan; //true 交易全屏进入

    private ChatAdapter mChatAdapter;
    private SendMsgDialog mSendMsgDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_kline_market;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(KLineMarketActivity.this);
    }

    @Override
    public void initStatusBar() {
        StatusBarUtil.setStatusBar(KLineMarketActivity.this);
    }

    /**
     * 产品切换
     * @param productID  产品id
     * @param closePrice 收盘价
     * @param productName 产品名称
     */
    public void setQHProduct(String productID,String closePrice,String productName){
        this.mProductID = productID;
        this.mProductCode =  KLineDataUtils.getProductCode(0, mProductID);
        tvPosition.setEnabled(false);
        if (mHourFragment !=null){
            mHourFragment.setQHProduct(mProductID,mProductCode,closePrice);
            mHourFragment.setClose(false);
        }
        if (mKLineFragment !=null){
            mKLineFragment.setQHProduct(mProductID,mProductCode);
            mKLineFragment.setClose(false);
        }
        tvTitleName.setText(!TextUtils.isEmpty(productName) ? productName : "");
        tvTitleNameLan.setText(!TextUtils.isEmpty(productName) ? productName : "");
        mPresenter.getSingleProduct(mProductID);

        if (!TextUtils.isEmpty(AppApplication.getConfig().getAt_token())){
            mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
        }
    }

    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            llTitleLayout.setPadding(0, 0, 0, 0);
            ViewGroup.LayoutParams layoutParams = vTitleZline.getLayoutParams();
            layoutParams.height = 0;
            vTitleZline.setLayoutParams(layoutParams);
        }

        mProductID = getIntent().getStringExtra("productID");
        String mProductName = getIntent().getStringExtra("productName");
        mClose = getIntent().getBooleanExtra("close", false);
        String mClosePrice = getIntent().getStringExtra("closePrice");
        mProductCode = KLineDataUtils.getProductCode(0, mProductID);

        mLan = getIntent().getBooleanExtra("ckLan",false);
        if (mLan){
            updateLandPor(true);
            llMsgLayout.setVisibility(View.VISIBLE);
            boolean eye = getIntent().getBooleanExtra("eye", false);
            ivEye.setSelected(eye);
            if (eye){
                ivEye.setImageResource(R.mipmap.ic_eye_close);
                recyclerView.setVisibility(View.VISIBLE);
            }

            List<ChatMsgBean> chatMsgBeanList = (List<ChatMsgBean>) getIntent().getSerializableExtra("chatList");
            if (chatMsgBeanList !=null && chatMsgBeanList.size() > 0){
                mChatAdapter = new ChatAdapter(mActivity,getIntent().getLongExtra("dateTime",0),chatMsgBeanList);
            }else {
                mChatAdapter = new ChatAdapter(mActivity);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mChatAdapter);
            if (chatMsgBeanList !=null && chatMsgBeanList.size() > 0){
                recyclerView.scrollToPosition(mChatAdapter.getItemCount()-1);
            }
        }
        //tvNewPrice.setTextFonts("fonts/DIN-Medium.ttf");
        tvTitleName.setText(!TextUtils.isEmpty(mProductName) ? mProductName : "");
        tvTitleNameLan.setText(!TextUtils.isEmpty(mProductName) ? mProductName : "");
        tvXiu.setVisibility(mClose ? View.VISIBLE : View.GONE);
        tvPosition.setEnabled(false);

        if (AppApplication.getConfig().mIsAuditing){
            llKLineBottom.setVisibility(View.GONE);
            llKLineBottomLan.setVisibility(View.GONE);
            tvHangup.setVisibility(View.GONE);
            tvBuyUpLan.setVisibility(View.GONE);
            tvBuyDownLan.setVisibility(View.GONE);
            ivTitleFullOpen.setVisibility(View.GONE);
        }

        final List<KlineCycle> klineCycleList = KLineDataUtils.getKlineCycleList(false, mProductID);
        if (TextUtils.isEmpty(mProductCode) || klineCycleList == null || klineCycleList.size() == 0) {
            return;
        }

        mFragmentManager = getSupportFragmentManager();
        mHourFragment = new MinuteHourFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lineType", klineCycleList.get(0).getCode());
        bundle.putString("productCode", mProductCode);
        bundle.putString("closePrice", mClosePrice);
        bundle.putString("productID", mProductID);
        mHourFragment.setArguments(bundle);

        mKLineFragment = new KLineFragment();
        Bundle lineBundle = new Bundle();
        lineBundle.putString("productID", mProductID);
        lineBundle.putString("productCode",mProductCode);
        lineBundle.putBoolean("ckLan",mLan);
        mKLineFragment.setArguments(lineBundle);

        mFragmentManager.beginTransaction().add(R.id.fragment_container, mHourFragment).show(mHourFragment)
                .add(R.id.fragment_container, mKLineFragment)
                .hide(mKLineFragment)
                .commitAllowingStateLoss();

        mCommonTab.setOnMyTabSelectListener(new OnMyTabSelectListener() {
            @Override
            public void onTabSelect(int position, String type) {
                if (AppApplication.getConfig().mIsAuditing){
                    if (Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation){
                        mCommonTabLan.updateTabSelection(position);
                        selectTab(klineCycleList,position);
                    }
                }else {
                    if (llKLineBottom.getVisibility() == View.VISIBLE){
                        mCommonTabLan.updateTabSelection(position);
                        selectTab(klineCycleList,position);
                    }
                }
            }
        });

        mCommonTabLan.setOnMyTabSelectListener(new OnMyTabSelectListener() {
            @Override
            public void onTabSelect(int position, String type) {
                if (AppApplication.getConfig().mIsAuditing){
                    if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation){
                        mCommonTab.updateTabSelection(position);
                        selectTab(klineCycleList,position);
                    }
                }else {
                    if (llKLineBottomLan.getVisibility() == View.VISIBLE){
                        mCommonTab.updateTabSelection(position);
                        selectTab(klineCycleList,position);
                    }
                }
            }
        });

        mCommonTabLan.addTab(klineCycleList);
        mCommonTab.addTab(klineCycleList);

        updateYeStyle(SpUtil.getBoolean(Constant.CACHE_IS_YE_STYLE));

        mPresenter.getSingleProduct(mProductID);

        if (!TextUtils.isEmpty(AppApplication.getConfig().getAt_token())){
            mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
        }
    }


    //行情切换
    private void selectTab(List<KlineCycle> klineCycleList,int position){
        if (position == 0) {
            mFragmentManager.beginTransaction().hide(mKLineFragment) .show(mHourFragment).commitAllowingStateLoss();
        } else {
            if (mKLineFragment.isVisible()) {
                mKLineFragment.setTabSelectHttp(klineCycleList.get(position).getCode() , klineCycleList.get(position).getTimeCode());
            } else {
                mKLineFragment.setTabSelect(klineCycleList.get(position).getCode(), klineCycleList.get(position).getTimeCode());
                mFragmentManager.beginTransaction().hide(mHourFragment).show(mKLineFragment).commitAllowingStateLoss();
            }
        }
    }


    @OnClick({R.id.iv_title_arrow, R.id.iv_title_w,R.id.iv_title_w_lan ,R.id.iv_title_full_open, R.id.iv_title_full_close, R.id.tv_hangup,
            R.id.tv_buy_up,R.id.tv_buy_up_lan,R.id.tv_buy_down,R.id.tv_buy_down_lan,R.id.tv_position,R.id.iv_more_list_lan,R.id.tv_title_name_lan,
            R.id.iv_eye,R.id.iv_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_arrow://返回
                finish();
                break;
            case R.id.iv_title_w://夜间模式切换
            case R.id.iv_title_w_lan :
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (ivTitleW.isSelected()) {
                    updateYeStyle(false);
                } else {
                    updateYeStyle(true);
                }
                break;
            case R.id.iv_title_full_open://切换到横屏

                if (!DeviceUtils.isFastDoubleClick()) {
                    if (AppApplication.getConfig().mIsAuditing){
                        updateLandPor(true);
                    }else {
                        if (login()){
                            updateLandPor(true);
                        }
                    }
                }
                break;
            case R.id.iv_title_full_close://切换到竖屏
                if (mLan){
                    finish();
                }else {
                    if (!DeviceUtils.isFastDoubleClick()) {
                        updateLandPor(false);
                    }
                }
                break;
            case R.id.tv_hangup://挂单
                placeOrder(true,true,llLanLayout.getVisibility()  == View.VISIBLE);
                break;
            case R.id.tv_buy_up: //买涨
                placeOrder(true,false,false);
                break;
            case R.id.tv_buy_up_lan://横屏买涨
                placeOrder(true,false,true);
                break;
            case R.id.tv_buy_down://买跌
                placeOrder(false,false,false);
                break;
            case R.id.tv_buy_down_lan://横屏买跌
                placeOrder(false,false,true);
                break;
            case R.id.tv_position://持仓
                if (!DeviceUtils.isFastDoubleClick()){
                    positionDialog();
                }
                break;
            case R.id.iv_more_list_lan:
            case R.id.tv_title_name_lan://更多
                if (!AppApplication.getConfig().mIsAuditing && !DeviceUtils.isFastDoubleClick()){
                    marketDialog();
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
        }
    }


    //单个产品信息
    @Override
    public void bindSingleProduct(boolean close) {
        this.mClose = close;
        tvXiu.setVisibility(mClose ? View.VISIBLE : View.GONE);
        if (mKLineFragment !=null){
            mKLineFragment.setClose(mClose);
        }
        if (mHourFragment !=null){
            mHourFragment.setClose(mClose);
        }
    }

    //持仓数据量
    @Override
    public void bindOrderList(int positNum) {
        if (positNum > 0){
            tvPosition.setEnabled(true);
            tvPosition.setText(String.format("持仓%d", positNum));
        }else {
            tvPosition.setEnabled(false);
            tvPosition.setText("暂无持仓");
        }
    }


    //行情
    @Override
    public void bindATPriceQuotation(MarketHQBean marketBean) {
        if (mClose){
            stopRun();
        }
        tvNewPrice.setText(marketBean.point);
        Float v = Float.parseFloat(marketBean.change);
        float v1 = Float.parseFloat(marketBean.point);
        if (mHourFragment !=null){
            mHourFragment.setNewLinePrice(v1,v,marketBean.time);
        }
        if (mKLineFragment !=null){
            mKLineFragment.setNewLinePrice(v1,v);
        }
        int color;
        if (v > 0) {
            color = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_F74F54);
            tvwPriceNum.setText("+" + marketBean.change);
            tvwPriceNumLan.setText("+" + marketBean.change);
            tvPricePercentage.setText("+" + marketBean.changeRate);
            tvPricePercentageLan.setText("+" + marketBean.changeRate);
        } else if (v < 0) {
            color = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_1AC47A);
            tvwPriceNum.setText(marketBean.change);
            tvwPriceNumLan.setText(marketBean.change);
            tvPricePercentage.setText(marketBean.changeRate);
            tvPricePercentageLan.setText(marketBean.changeRate);
        } else {
            if (ivTitleW.isSelected()) {
                color = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_7E8595);
            } else {
                color = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_333333);
            }
            tvwPriceNum.setText(marketBean.change);
            tvwPriceNumLan.setText(marketBean.change);
            tvPricePercentage.setText(marketBean.changeRate);
            tvPricePercentageLan.setText(marketBean.changeRate);
        }
        tvNewPrice.setTextColor(color);
        tvwPriceNum.setTextColor(color);
        tvwPriceNumLan.setTextColor(color);
        tvPricePercentage.setTextColor(color);
        tvPricePercentageLan.setTextColor(color);
        String timeStr = TimeUtil.getStringToString(marketBean.time, TimeUtil.dateFormatYMDHMS, TimeUtil.dateFormatMDHMS);
        tvTime.setText(timeStr);
        tvTimeLan.setText(timeStr);
        //竖屏
        tvPriceHighest.setText(marketBean.high);
        tvPriceThiOpen.setText(marketBean.open);
        tvPriceMinimum.setText(marketBean.low);
        tvPriceZCollect.setText(marketBean.close);
        //横屏
        tvPriceHighestLan.setText(marketBean.high);
        tvPriceThiOpenLan.setText(marketBean.open);
        tvPriceMinimumLan.setText(marketBean.low);
        tvPriceZCollectLan.setText(marketBean.close);

        refreshPlaceOrder(marketBean.point,marketBean.change,marketBean.changeRate);
    }




    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(mProductCode)){
                mPresenter.getATPriceQuotation(mProductCode);
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    private void startRuan() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
        if (mMarketDialog !=null && mMarketDialog.isShowing()){
            mMarketDialog.startRuan();
        }
    }

    private void stopRun() {
        mHandler.removeCallbacks(mRunnable);
        if (mMarketDialog !=null && mMarketDialog.isShowing()){
            mMarketDialog.stopRun();
        }
    }


    /**
     * 刷新dialog
     * @param stockIndex
     * @param changeValue
     */
    private void refreshPlaceOrder(String stockIndex, String changeValue ,String changeRange){
        if (placeOrderDialog !=null && placeOrderDialog.isShowing() && TextUtils.equals(mProductID,placeOrderDialog.getProductID())){
            placeOrderDialog.refreshPlaceOrderDialog(stockIndex,changeValue);
        }
        if (placeOrderLanDialog !=null && placeOrderLanDialog.isShowing() && TextUtils.equals(mProductID,placeOrderLanDialog.getProductID())){
            placeOrderLanDialog.refreshPlaceOrderDialog(stockIndex,changeValue);
        }
        if (positionDialog !=null && positionDialog.isShowing() && TextUtils.equals(mProductID,positionDialog.getProductID())){
            positionDialog.refreshDate(stockIndex,changeValue,changeRange);
        }
    }



    //下单弹窗
    private PlaceOrderLanDialog placeOrderLanDialog;
    private PlaceOrderDialog placeOrderDialog;
    private void placeOrder(boolean buyState,boolean hangUp,boolean lan){
        if (DeviceUtils.isFastDoubleClick()){
            return;
        }
        if (login()){
            if (lan){
                if (placeOrderLanDialog == null) {
                    placeOrderLanDialog = new PlaceOrderLanDialog(KLineMarketActivity.this);
                    placeOrderLanDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
                        }
                    });
                }
                if (hangUp){
                    placeOrderLanDialog.placeOrderHangUp(mProductID,buyState,1);
                }else {
                    placeOrderLanDialog.placeOrder(mProductID,buyState,1);
                }
            }else {
                if (placeOrderDialog == null){
                    placeOrderDialog = new PlaceOrderDialog(KLineMarketActivity.this);
                    placeOrderDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
                        }
                    });
                }
                if (hangUp){
                    placeOrderDialog.placeOrderHangUp(mProductID,buyState,1);
                }else {
                    placeOrderDialog.placeOrder(mProductID,buyState,1);
                }
            }
        }
    }


    //持仓单列表
    private PositionDialog positionDialog;
    private void positionDialog(){
        if (positionDialog ==null){
            positionDialog = new PositionDialog(KLineMarketActivity.this);
            positionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
                }
            });
        }
        positionDialog.showDialog(mProductID);
    }


    //行情列表
    private MarketDialog mMarketDialog;
    private void marketDialog(){
        if (mMarketDialog == null){
            mMarketDialog = new MarketDialog(KLineMarketActivity.this);
        }
        mMarketDialog.showMarket(mProductID);
    }



    /**
     * 交易所登录事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JysAccountEvent event) {
        mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
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
    protected void onResume() {
        super.onResume();
        startRuan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRun();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mMarketDialog !=null){
            mMarketDialog.onDestroy();
            mMarketDialog = null;
        }
        if (placeOrderLanDialog !=null){
            placeOrderLanDialog.onDestroy();
            placeOrderLanDialog = null;
        }
        if (placeOrderDialog !=null){
            placeOrderDialog.onDestroy();
            placeOrderDialog = null;
        }
        if (positionDialog !=null){
            positionDialog.onDestroy();
            positionDialog = null;
        }
        if (mSendMsgDialog !=null){
            mSendMsgDialog.onDestroy();
            mSendMsgDialog = null;
        }
    }

    /**
     * 夜间模式切换
     * @param yeStyle
     */
    private void updateYeStyle(boolean yeStyle) {
        if (yeStyle) {
            ivTitleW.setSelected(true);
            ivTitleW.setImageResource(R.mipmap.img_sun);
            ivTitleWLan.setImageResource(R.mipmap.img_sun);
            ivTitleFullClose.setImageResource(R.mipmap.ic_full_screen);
            ivMoreListLan.setImageResource(R.mipmap.ic_more_list);
            vKlineBottomLan.setVisibility(View.GONE);

            int color_27252D = ContextCompat.getColor(mActivity, R.color.color_27252D);
            llTitleBottomLayout.setBackgroundColor(color_27252D);
            llProLayout.setBackgroundColor(color_27252D);
            vTitleZline.setBackgroundColor(color_27252D);
            llTitleLayout.setBackgroundColor(color_27252D);

            mFragmentContainer.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.color_1F1C28));

            int color_2A2732 = ContextCompat.getColor(mActivity, R.color.color_2A2732);
            mCommonTab.setBackgroundColor(color_2A2732);//tab颜色
            mCommonTabLan.setBackgroundColor(color_2A2732);//tab颜色
            vContentLine.setBackgroundColor(color_2A2732);
            vBottomLine.setBackgroundColor(color_2A2732);
            mCommonTab.updateTabStyle(yeStyle);
            mCommonTabLan.updateTabStyle(yeStyle);
            int whiteColor = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_ffffff);
            tvTime.setTextColor(whiteColor);
            tvPriceHighestHint.setTextColor(whiteColor);
            tvPriceHighest.setTextColor(whiteColor);
            tvPriceThiOpenHint.setTextColor(whiteColor);
            tvPriceThiOpen.setTextColor(whiteColor);
            tvPriceMinimumHint.setTextColor(whiteColor);
            tvPriceMinimum.setTextColor(whiteColor);
            tvPriceZCollectHint.setTextColor(whiteColor);
            tvPriceZCollect.setTextColor(whiteColor);

            tvPriceHighestLanHint.setTextColor(whiteColor);
            tvPriceHighestLan.setTextColor(whiteColor);
            tvPriceThiOpenLanHint.setTextColor(whiteColor);
            tvPriceThiOpenLan.setTextColor(whiteColor);
            tvPriceMinimumLanHint.setTextColor(whiteColor);
            tvPriceMinimumLan.setTextColor(whiteColor);
            tvPriceZCollectLanHint.setTextColor(whiteColor);
            tvPriceZCollectLan.setTextColor(whiteColor);
            tvHangup.setTextColor(whiteColor);
            tvTimeLan.setTextColor(whiteColor);
            tvTitleNameLan.setTextColor(whiteColor);
        } else {
            ivTitleW.setSelected(false);
            ivTitleW.setImageResource(R.mipmap.img_night);
            ivTitleWLan.setImageResource(R.mipmap.img_night_gray);
            ivTitleFullClose.setImageResource(R.mipmap.ic_full_screen_gray);
            ivMoreListLan.setImageResource(R.mipmap.ic_more_list_gray);
            vKlineBottomLan.setVisibility(View.VISIBLE);

            int whiteColor = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_ffffff);
            llTitleLayout.setBackgroundColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_333333));
            vTitleZline.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_333333));
            llTitleBottomLayout.setBackgroundColor(whiteColor);
            mFragmentContainer.setBackgroundColor(whiteColor);
            llProLayout.setBackgroundColor(whiteColor);
            mCommonTab.setBackgroundColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_F8F8F8));
            mCommonTab.updateTabStyle(yeStyle);
            mCommonTabLan.setBackgroundColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_F8F8F8));
            mCommonTabLan.updateTabStyle(yeStyle);
            vBottomLine.setBackgroundColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.line_color));
            vContentLine.setBackgroundColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.line_color));

            int color_b2b2b2 = ContextCompat.getColor(KLineMarketActivity.this, R.color.color_b2b2b2);
            tvTime.setTextColor(color_b2b2b2);


            tvPriceHighestHint.setTextColor(color_b2b2b2);
            tvPriceHighest.setTextColor(color_b2b2b2);
            tvPriceThiOpenHint.setTextColor(color_b2b2b2);
            tvPriceThiOpen.setTextColor(color_b2b2b2);
            tvPriceMinimumHint.setTextColor(color_b2b2b2);
            tvPriceMinimum.setTextColor(color_b2b2b2);
            tvPriceZCollectHint.setTextColor(color_b2b2b2);
            tvPriceZCollect.setTextColor(color_b2b2b2);

            tvPriceHighestLanHint.setTextColor(color_b2b2b2);
            tvPriceHighestLan.setTextColor(color_b2b2b2);
            tvPriceThiOpenLanHint.setTextColor(color_b2b2b2);
            tvPriceThiOpenLan.setTextColor(color_b2b2b2);
            tvPriceMinimumLanHint.setTextColor(color_b2b2b2);
            tvPriceMinimumLan.setTextColor(color_b2b2b2);
            tvPriceZCollectLanHint.setTextColor(color_b2b2b2);
            tvPriceZCollectLan.setTextColor(color_b2b2b2);
            tvPriceZCollectLan.setTextColor(color_b2b2b2);
            tvHangup.setTextColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_727272));
            tvTimeLan.setTextColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_727272));
            tvTitleNameLan.setTextColor(ContextCompat.getColor(KLineMarketActivity.this, R.color.color_333333));
        }
        if (mHourFragment != null) {
            mHourFragment.updateStyle(yeStyle);
        }
        if (mKLineFragment != null) {
            mKLineFragment.updateStyle(yeStyle);
        }
        AppApplication.getConfig().setYeStyle(yeStyle);
        SpUtil.putBoolean(Constant.CACHE_IS_YE_STYLE, yeStyle);
    }

    //全屏返回键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
                    if (mLan){
                        finish();
                    }else {
                        updateLandPor(false);
                    }
                } else {
                    finish();
                }
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 横竖屏切换
     * @param land
     */
    private void updateLandPor(boolean land){
        if (land){ //横屏
            if (mKLineFragment !=null){
                mKLineFragment.setIsFirstDraw(land);
            }
            llTitleLayout.setVisibility(View.GONE);
            llProLayout.setVisibility(View.GONE);
            mCommonTab.setVisibility(View.GONE);
            mCommonTabLan.setVisibility(View.VISIBLE);
            vContentLine.setVisibility(View.GONE);
            // vBottomLine.setVisibility(View.GONE);
            if (!AppApplication.getConfig().mIsAuditing){
                llKLineBottom.setVisibility(View.GONE);
            }
            llKLineBottomLan.setVisibility(View.VISIBLE);
            llLanLayout.setVisibility(View.VISIBLE);
            ivTitleFullClose.setVisibility(View.VISIBLE);
            ivTitleWLan.setVisibility(View.VISIBLE);
            vTitleZline.setVisibility(View.VISIBLE);
            llProductTimeLan.setVisibility(View.VISIBLE);
            llPriceNumPerPro.setVisibility(View.GONE);
            llPriceNumPerLan.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {//竖屏
            if (mKLineFragment !=null){
                mKLineFragment.setIsFirstDraw(land);
            }
            llTitleLayout.setVisibility(View.VISIBLE);
            llProLayout.setVisibility(View.VISIBLE);
            mCommonTab.setVisibility(View.VISIBLE);
            mCommonTabLan.setVisibility(View.GONE);
            vContentLine.setVisibility(View.VISIBLE);
            // vBottomLine.setVisibility(View.VISIBLE);
            if (!AppApplication.getConfig().mIsAuditing){
                llKLineBottom.setVisibility(View.VISIBLE);
            }
            llKLineBottomLan.setVisibility(View.GONE);
            llLanLayout.setVisibility(View.GONE);
            ivTitleFullClose.setVisibility(View.GONE);
            ivTitleWLan.setVisibility(View.GONE);
            vTitleZline.setVisibility(View.GONE);
            llProductTimeLan.setVisibility(View.GONE);
            llPriceNumPerPro.setVisibility(View.VISIBLE);
            llPriceNumPerLan.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void updataEye(boolean eye,boolean click){
        if (eye){
            ivEye.setSelected(true);
            ivEye.setImageResource(R.mipmap.ic_eye_close);
            recyclerView.setVisibility(View.VISIBLE);
            OpenPositionFragment.getInstance().updataEye(true);
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
            OpenPositionFragment.getInstance().updataEye(false);
            if (Mq.get().isOpen()){
                Mq.get().stopRun();
            }
            if (click){
                showErrorMsg("弹幕已关闭",null);
            }
        }
    }
}
