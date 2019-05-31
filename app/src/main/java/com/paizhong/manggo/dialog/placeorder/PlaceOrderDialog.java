package com.paizhong.manggo.dialog.placeorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.PlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProducItemBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.dialog.other.OrderSuccDialog;
import com.paizhong.manggo.dialog.recharge.RechargeDialog;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.kchart.bean.MinuteHourBean;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.views.CrossView;
import com.paizhong.manggo.ui.kchart.views.KChartAdapter;
import com.paizhong.manggo.ui.kchart.views.KChartView;
import com.paizhong.manggo.ui.kchart.views.MinuteHourView;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.VibrateSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by zab on 2018/8/24 0024.
 */
public class PlaceOrderDialog extends BaseDialog<PlaceOrderPresenter> implements PlaceOrderContract.View, View.OnClickListener {

    private LinearLayout llProductRoot;
    //顶部布局
    private LinearLayout llProductTitle;
    private TextView tvProductName;
    private DinTextView tvProductMarket;
    private ImageView ivDownUp;
    private TextView tvMarketQH;
    private TextView tvFollow;
    private View vFollow;
    private NestedScrollView nsvScrollView;
    //行情与k线布局
    private LinearLayout llMarketRootView;
    private PlaceOrderMarketTabLayout pmtMarketTabLayout;
    private View flMinuteHourView;
    private MinuteHourView minuteHourView;
    private KChartView kChartView;

    //挂单布局
    private View llHangup;
    private EditText etHangupPrice;
    private EditText etHangupNum;

    //买涨买跌布局
    private View buyUpLayout;
    private TextView tvUpUserProbability;
    private TextView tvUpUser;
    private View buyDownLayout;
    private TextView tvDownUserProbability;
    private TextView tvDownUser;

    //元手 布局
    private View llPriceNum1;
    private TextView tvPriceNum1;
    private TextView tvPriceHNum1;
    private View llPriceNum2;
    private TextView tvPriceNum2;
    private TextView tvPriceHNum2;
    private View llPriceNum3;
    private TextView tvPriceNum3;
    private TextView tvPriceHNum3;

    private PlaceOrderNumLayout plNumLayout;
    //底部布局 1
    private CheckBox cbYeCheck;
    private TextView tvMarketPriceChange;
    private CheckBox cbPriceCheck;
    private CheckBox cbVoucherCheck;

    //底部布局 止盈止损
    private TextView tvZsNum;
    private VibrateSeekBar vsZSeekbar;
    private TextView tvZsPrice;
    private TextView tvZyNum;
    private VibrateSeekBar vsZySeekbar;
    private TextView tvZyPrice;

    //底部下单
    private TextView tvPriceHint;
    private DinTextView tvPrice;
    private TextView tvOpenFee;
    private TextView tvPlaceOrder;
    private View llBottomPlaceOrder;

    private PlaceOrderBean mPlaceOrderBean;


    public PlaceOrderDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
        this.mPlaceOrderBean = new PlaceOrderBean();
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        stopRun();
        mPlaceOrderBean.initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopRun();
        super.onDestroy();
    }

    //充值完成刷新金额
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseUIRefreshEvent event) {
        if (event.mType == 2 && isShowing()) {
            mPresenter.getUserInfoRefresh(AppApplication.getConfig().getAt_token());
        }
    }

    public String getProductID() {
        return mPlaceOrderBean != null ? mPlaceOrderBean.productID : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.dialog_placeorder);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        llProductRoot = findViewById(R.id.ll_product_root);
        llProductTitle = findViewById(R.id.ll_product_title);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductMarket = findViewById(R.id.tv_product_market);
        ivDownUp = findViewById(R.id.iv_down_up);
        tvMarketQH = findViewById(R.id.tv_market_qh);
        tvFollow = findViewById(R.id.tv_follow);
        vFollow = findViewById(R.id.v_follow);
        View ivClosed = findViewById(R.id.iv_closed);
        nsvScrollView = findViewById(R.id.nsv_scrollView);

        llMarketRootView = findViewById(R.id.ll_market_root_view);
        pmtMarketTabLayout = findViewById(R.id.pmt_market_tab_layout);
        flMinuteHourView = findViewById(R.id.fl_minute_hour_view);
        minuteHourView = findViewById(R.id.minute_hour_view);
        CrossView crossView = findViewById(R.id.cross_view);
        kChartView = findViewById(R.id.ly_professional_kline);

        llHangup = findViewById(R.id.ll_hangup);
        View ivPriceDel = findViewById(R.id.iv_price_del);
        View ivPriceAdd = findViewById(R.id.iv_price_add);
        etHangupPrice = findViewById(R.id.et_hangup_price);
        View ivNumDel = findViewById(R.id.iv_num_del);
        View ivNumAdd = findViewById(R.id.iv_num_add);
        etHangupNum = findViewById(R.id.et_hangup_num);

        buyUpLayout = findViewById(R.id.ll_up_user_probability);
        tvUpUserProbability = findViewById(R.id.tv_up_user_probability);
        tvUpUser = findViewById(R.id.tv_up_user);
        buyDownLayout = findViewById(R.id.ll_down_user_probability);
        tvDownUserProbability = findViewById(R.id.tv_down_user_probability);
        tvDownUser = findViewById(R.id.tv_down_user);

        llPriceNum1 = findViewById(R.id.ll_price_num1);
        tvPriceNum1 = findViewById(R.id.tv_price_num1);
        tvPriceHNum1 = findViewById(R.id.tv_price_h_num1);
        llPriceNum2 = findViewById(R.id.ll_price_num2);
        tvPriceNum2 = findViewById(R.id.tv_price_num2);
        tvPriceHNum2 = findViewById(R.id.tv_price_h_num2);
        llPriceNum3 = findViewById(R.id.ll_price_num3);
        tvPriceNum3 = findViewById(R.id.tv_price_num3);
        tvPriceHNum3 = findViewById(R.id.tv_price_h_num3);

        plNumLayout = findViewById(R.id.pl_num_layout);
        cbYeCheck = findViewById(R.id.cb_ye_check);
        tvMarketPriceChange = findViewById(R.id.tv_market_price_change);
        cbPriceCheck = findViewById(R.id.cb_price_check);
        TextView tvRecharge = findViewById(R.id.tv_recharge);//充值
        cbVoucherCheck = findViewById(R.id.cb_voucher_check);

        tvZsNum = findViewById(R.id.tv_zs_num);
        vsZSeekbar = findViewById(R.id.vs_zs_seekbar);
        tvZsPrice = findViewById(R.id.tv_zs_price);
        tvZyNum = findViewById(R.id.tv_zy_num);
        vsZySeekbar = findViewById(R.id.vs_zy_seekbar);
        tvZyPrice = findViewById(R.id.tv_zy_price);

        tvPriceHint = findViewById(R.id.tv_price_hint);
        tvPrice = findViewById(R.id.tv_price);
        tvOpenFee = findViewById(R.id.tv_open_fee);
        tvPlaceOrder = findViewById(R.id.tv_place_order);
        llBottomPlaceOrder = findViewById(R.id.ll_bottom_place_order);

        pmtMarketTabLayout.replaceTabCodes(KLineDataUtils.getKlineCycleList(false, ""));
        minuteHourView.setIsCKZS(false);
        minuteHourView.setTouchEnabled(false);
        minuteHourView.setCrossView(crossView, "");

        mAdapter = new KChartAdapter();
        kChartView.setAdapter(mAdapter);
        kChartView.setOverScrollRange(DeviceUtils.dip2px(getContext(), 100));

        ivClosed.setOnClickListener(this);
        tvMarketQH.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        ivPriceDel.setOnClickListener(this);
        ivPriceAdd.setOnClickListener(this);
        ivNumDel.setOnClickListener(this);
        ivNumAdd.setOnClickListener(this);
        buyUpLayout.setOnClickListener(this);
        buyDownLayout.setOnClickListener(this);
        llPriceNum1.setOnClickListener(this);
        llPriceNum2.setOnClickListener(this);
        llPriceNum3.setOnClickListener(this);
        tvPlaceOrder.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);

        setListener();

        minuteHourView.updateStyle(false);
        kChartView.updateStyle(false);
    }

    private void setListener() {
        plNumLayout.setOnNumChangeListener(new PlaceOrderNumLayout.OnNumChangeListener() {
            @Override
            public void onNumChange(int num) {
                mPlaceOrderBean.mrShouNum = num;
                setPrice();
                setDwPrice();
                setZsSeekbarProgress(vsZSeekbar.getProgress());
                setZySeekbarProgress(vsZySeekbar.getProgress());
            }
        });

        pmtMarketTabLayout.setTabSelectListener(new PlaceOrderMarketTabLayout.TabSelectListener() {
            @Override
            public void onTabSelect(String code, String timeCode) {
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(timeCode)) {
                    mLineType = code;
                    mTimeCode = timeCode;
                    kChartView.setIsNewPrice(!TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode) && !TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode));
                    if (TextUtils.equals(ViewConstant.KLINE_TAB_0, mTimeCode)) {
                        kChartView.setVisibility(View.GONE);
                        flMinuteHourView.setVisibility(View.VISIBLE);
                    } else {
                        flMinuteHourView.setVisibility(View.GONE);
                        kChartView.setVisibility(View.VISIBLE);
                        if (mAdapter != null && mAdapter.getCount() > 0) {
                            mAdapter.clear();
                        }
                    }
                    startRun();
                }
            }
        });


        vsZSeekbar.setProgressListener(new VibrateSeekBar.OnProgressListener() {
            @Override
            public void guessNumber(int number) {
            }

            @Override
            public void onStartTrackingTouch() {
            }

            @Override
            public void onStopTrackingTouch() {
            }

            @Override
            public void onProgress(int progress) {
                setZsSeekbarProgress(progress);
            }
        });

        vsZySeekbar.setProgressListener(new VibrateSeekBar.OnProgressListener() {
            @Override
            public void guessNumber(int number) {
            }

            @Override
            public void onStartTrackingTouch() {
            }

            @Override
            public void onStopTrackingTouch() {
            }

            @Override
            public void onProgress(int progress) {
                setZySeekbarProgress(progress);
            }
        });

        //代金券勾选
        cbVoucherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    cbPriceCheck.setChecked(false);
                    cbYeCheck.setChecked(false);
                    setPrice();
                    setZsSeekbarProgress(vsZSeekbar.getProgress());
                } else {
                    if (!cbPriceCheck.isChecked()) {
                        cbVoucherCheck.setChecked(true);
                        setPrice();
                    }
                }
            }
        });

        //现金勾选
        cbPriceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    cbVoucherCheck.setChecked(false);
                    cbYeCheck.setChecked(true);
                    setPrice();
                    setZsSeekbarProgress(vsZSeekbar.getProgress());
                } else {
                    if (!cbVoucherCheck.isChecked()) {
                        cbPriceCheck.setChecked(true);
                        setPrice();
                    }
                }
            }
        });

        //是否开启过夜
        cbYeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cbVoucherCheck.isChecked()) {
                        cbYeCheck.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_closed: //关闭
                dismiss();
                break;
            case R.id.tv_recharge://充值
                if (!DeviceUtils.isFastDoubleClick()) {
                    new RechargeDialog(mContext).getData();
                }
                break;
            case R.id.tv_market_qh://行情切换
                if (!DeviceUtils.isFastDoubleClick()) {
                    if (tvMarketQH.isSelected()) {
                        stopRun();
                        llBottomPlaceOrder.setVisibility(View.VISIBLE);
                        llMarketRootView.setVisibility(View.GONE);
                        nsvScrollView.setVisibility(View.VISIBLE);
                        tvMarketQH.setSelected(false);
                        tvMarketQH.setText("行情");
                    } else {
                        ViewGroup.LayoutParams layoutParams = llMarketRootView.getLayoutParams();
                        layoutParams.height = llProductRoot.getHeight() - llProductTitle.getHeight()
                                - mContext.getResources().getDimensionPixelSize(R.dimen.dimen_29_5dp);
                        llMarketRootView.setLayoutParams(layoutParams);
                        llBottomPlaceOrder.setVisibility(View.GONE);
                        nsvScrollView.setVisibility(View.GONE);
                        llMarketRootView.setVisibility(View.VISIBLE);
                        tvMarketQH.setSelected(true);
                        tvMarketQH.setText("交易");
                        pmtMarketTabLayout.initSelectorTab();
                    }
                }
                break;
            case R.id.tv_follow: //跟买开关
                if (!DeviceUtils.isFastDoubleClick()) {
                    tvFollow.setSelected(!tvFollow.isSelected());
                }
                break;
            case R.id.iv_price_del: //挂单价 -
                int hangUpPriceDel = NumberUtil.getInt(etHangupPrice.getText().toString(), 0);
                if (hangUpPriceDel > 0) {
                    hangUpPriceDel--;
                    etHangupPrice.setText(String.valueOf(hangUpPriceDel));
                    etHangupPrice.setSelection(etHangupPrice.getText().length());
                }
                break;
            case R.id.iv_price_add: //挂单价 +
                int hangUpPriceAdd = NumberUtil.getInt(etHangupPrice.getText().toString(), 0);
                if (hangUpPriceAdd >= 0) {
                    if (hangUpPriceAdd < 99999) {
                        hangUpPriceAdd++;
                        etHangupPrice.setText(String.valueOf((hangUpPriceAdd)));
                        etHangupPrice.setSelection(etHangupPrice.getText().length());
                    }
                }
                break;
            case R.id.iv_num_del://挂单点数 -
                int hangUpNumDel = NumberUtil.getInt(etHangupNum.getText().toString(), 0);
                if (hangUpNumDel > 0) {
                    hangUpNumDel--;
                    etHangupNum.setText(String.valueOf(hangUpNumDel));
                    etHangupNum.setSelection(etHangupNum.getText().length());
                }
                break;
            case R.id.iv_num_add: //挂单点数 +
                int hangUpNumAdd = NumberUtil.getInt(etHangupNum.getText().toString(), 0);
                if (hangUpNumAdd > 0) {
                    if (hangUpNumAdd < 99999) {
                        hangUpNumAdd++;
                        etHangupNum.setText(String.valueOf(hangUpNumAdd));
                        etHangupNum.setSelection(etHangupNum.getText().length());
                    }
                } else {
                    etHangupNum.setText("1");
                    etHangupNum.setSelection(etHangupNum.getText().length());
                }
                break;
            case R.id.ll_up_user_probability: //买涨 按钮
                if (mPlaceOrderBean.placeOrderType == 3 || mPlaceOrderBean.placeOrderType == 4) {
                    return;
                }
                if (!buyUpLayout.isSelected()) {
                    selectUpChange();
                }
                break;
            case R.id.ll_down_user_probability: //买跌按钮
                if (mPlaceOrderBean.placeOrderType == 3 || mPlaceOrderBean.placeOrderType == 4) {
                    return;
                }
                if (!buyDownLayout.isSelected()) {
                    selectDownChange();
                }
                break;
            case R.id.ll_price_num1: //元手 1
                if (!llPriceNum1.isSelected()) {
                    selectShouYuanChange(1);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_price_num2: //元手 2
                if (!llPriceNum2.isSelected()) {
                    selectShouYuanChange(2);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_price_num3: //元手 3
                if (!llPriceNum3.isSelected()) {
                    selectShouYuanChange(3);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.tv_place_order: //下单
                //1 正常下单  2挂单下单 3跟单下单 4跟单行情下单（首先展示行情）
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (mPlaceOrderBean.placeOrderType == 1) {
                    if (cbPriceCheck.isChecked()) {
                        placeOrder();
                    } else {
                        placeOrderVoucher();
                    }
                } else if (mPlaceOrderBean.placeOrderType == 3
                        || mPlaceOrderBean.placeOrderType == 4) {
                    placeOrderFollow();

                } else if (mPlaceOrderBean.placeOrderType == 5) {
                    placeOrderHangUp();
                } else if (mPlaceOrderBean.placeOrderType == 6) {
                    updateHangUp();
                }
                break;
        }
    }


    //现金下单
    private void placeOrder() {
        int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);
        if (NumberUtil.multiply(amount, mPlaceOrderBean.mrShouNum) > mPlaceOrderBean.ballance) {
            new AppHintDialog(mContext).showAppDialog(0);
            return;
        }
        int followType = tvFollow.getVisibility() == View.GONE ? 0 : (tvFollow.isSelected() ? 1 : 0);
        //0买涨，1买跌
        int profitLimit = vsZySeekbar.getProgress() == 0 ? 200 : vsZySeekbar.getProgress() * 5;
        int lossLimit = vsZSeekbar.getProgress() == 0 ? 75 : vsZSeekbar.getProgress() * 5;

        int flag = buyUpLayout.isSelected() ? 0 : 1; //0-买涨，1买跌

        int holdNight = cbYeCheck.isChecked() ? 1 : 0; //0不持仓  1 持仓

        //手续费
        double fee = llPriceNum1.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) : (
                llPriceNum2.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) :
                        NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum));

        mPresenter.getCashPlaceOrder(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, AppApplication.getConfig().getAt_secret_access_key(),
                mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum, flag, profitLimit, lossLimit, amount, holdNight, mPlaceOrderBean.productName, fee,
                KLineDataUtils.getProductCode(mPlaceOrderBean.productID), followType, SpUtil.getString(Constant.USER_KEY_NICKNAME));
    }


    //代金券 下单
    private void placeOrderVoucher() {
        if (mPlaceOrderBean.mrShouNum > 1) {
            showErrorMsg("代金券下单一单只能用一张代金券", null);
            return;
        }

        int voucherGgNum = llPriceNum1.isSelected() ? mPlaceOrderBean.voucherGgNum1 :
                (llPriceNum2.isSelected() ? mPlaceOrderBean.voucherGgNum2 : mPlaceOrderBean.voucherGgNum3);

        if (voucherGgNum >= mPlaceOrderBean.mrShouNum) { //mBindBean.getShouNum() 代金券下单 一单只能用一张
            String voucherGgID = llPriceNum1.isSelected() ? mPlaceOrderBean.voucherGgId1 :
                    (llPriceNum2.isSelected() ? mPlaceOrderBean.voucherGgId2 : mPlaceOrderBean.voucherGgId3);

            //0买涨，1买跌
            int profitLimit = vsZySeekbar.getProgress() == 0 ? 200 : vsZySeekbar.getProgress() * 5;
            int lossLimit = vsZSeekbar.getProgress() == 0 ? 75 : vsZSeekbar.getProgress() * 5;

            int flag = buyUpLayout.isSelected() ? 0 : 1; //0-买涨，1买跌

            int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);

            mPresenter.getVoucherPlaceOrder(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, AppApplication.getConfig().getAt_secret_access_key(),
                    mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum, flag, profitLimit, lossLimit, voucherGgID, amount, mPlaceOrderBean.productName, 0, KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
        } else {
            showErrorMsg((voucherGgNum == 0 ? "当前品种暂无代金券" : "当前下单手数大于代金券数量"), null);
        }
    }


    //跟单下单
    private void placeOrderFollow() {
        int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);
        if (NumberUtil.multiply(amount, mPlaceOrderBean.mrShouNum) > mPlaceOrderBean.ballance) {
            new AppHintDialog(mContext).showAppDialog(0);
            return;
        }
        //0买涨，1买跌
        int profitLimit = vsZySeekbar.getProgress() == 0 ? 200 : vsZySeekbar.getProgress() * 5;
        int lossLimit = vsZSeekbar.getProgress() == 0 ? 75 : vsZSeekbar.getProgress() * 5;

        int flag = buyUpLayout.isSelected() ? 0 : 1; //0-买涨，1买跌

        int holdNight = cbYeCheck.isChecked() ? 1 : 0; //0不持仓  1 持仓

        //手续费
        double fee = llPriceNum1.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) : (
                llPriceNum2.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) :
                        NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum));

        mPresenter.getCashFollow(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, AppApplication.getConfig().getAt_secret_access_key(),
                mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum, flag, profitLimit, lossLimit, amount, holdNight, mPlaceOrderBean.productName, fee,
                KLineDataUtils.getProductCode(mPlaceOrderBean.productID), mPlaceOrderBean.orderNo, mPlaceOrderBean.fellowerPhone, 0);
    }


    //挂单下单
    private void placeOrderHangUp() {
        int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);
        if (NumberUtil.multiply(amount, mPlaceOrderBean.mrShouNum) > mPlaceOrderBean.ballance) {
            new AppHintDialog(mContext).showAppDialog(0);
            return;
        }

        String hangUpNum = etHangupNum.getText().toString();
        if (TextUtils.isEmpty(hangUpNum)) {
            showErrorMsg("误差点位不能为空", null);
            return;
        }
        if (Integer.parseInt(hangUpNum) < 5) {
            showErrorMsg("最小误差点位为5个点", null);
            return;
        }
        String hangUpPrice = etHangupPrice.getText().toString();
        if (TextUtils.isEmpty(hangUpPrice)) {
            showErrorMsg("挂单价不能为空", null);
            return;
        }
        int newMarket = NumberUtil.getInt(tvProductMarket.getText().toString(), 0);
        if (newMarket == 0) {
            newMarket = NumberUtil.getInt(mPlaceOrderBean.stockIndex, 0);
        }
        int priceInt = NumberUtil.getInt(hangUpPrice, 0) - newMarket;
        priceInt = priceInt < 0 ? -priceInt : priceInt;
        if (priceInt > 1000) {
            showErrorMsg("挂单价与最新价相差值为0~1000", null);
            return;
        }
        //0买涨，1买跌
        int profitLimit = vsZySeekbar.getProgress() == 0 ? 200 : vsZySeekbar.getProgress() * 5;
        int lossLimit = vsZSeekbar.getProgress() == 0 ? 75 : vsZSeekbar.getProgress() * 5;

        int flag = buyUpLayout.isSelected() ? 0 : 1; //0-买涨，1买跌

        int holdNight = cbYeCheck.isChecked() ? 1 : 0; //0不持仓  1 持仓

        //手续费
        double fee = llPriceNum1.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) : (
                llPriceNum2.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) :
                        NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum));

        mPresenter.getHangUpOrder(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum,
                flag, profitLimit, lossLimit, amount, holdNight, mPlaceOrderBean.productName, fee, KLineDataUtils.getProductCode(mPlaceOrderBean.productID),
                SpUtil.getString(Constant.USER_KEY_NICKNAME), hangUpNum, hangUpPrice);
    }

    //修改挂单
    public void updateHangUp() {
        int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);
        if (NumberUtil.multiply(amount, mPlaceOrderBean.mrShouNum) > mPlaceOrderBean.ballance) {
            new AppHintDialog(mContext).showAppDialog(0);
            return;
        }

        String hangUpNum = etHangupNum.getText().toString();
        if (TextUtils.isEmpty(hangUpNum)) {
            showErrorMsg("误差点位不能为空", null);
            return;
        }
        if (Integer.parseInt(hangUpNum) < 5) {
            showErrorMsg("最小误差点位为5个点", null);
            return;
        }
        String hangUpPrice = etHangupPrice.getText().toString();
        if (TextUtils.isEmpty(hangUpPrice)) {
            showErrorMsg("挂单价不能为空", null);
            return;
        }
        int newMarket = NumberUtil.getInt(tvProductMarket.getText().toString(), 0);
        if (newMarket == 0) {
            newMarket = NumberUtil.getInt(mPlaceOrderBean.stockIndex, 0);
        }
        int priceInt = NumberUtil.getInt(hangUpPrice, 0) - newMarket;
        priceInt = priceInt < 0 ? -priceInt : priceInt;
        if (priceInt > 1000) {
            showErrorMsg("挂单价与最新价相差值为0~1000", null);
            return;
        }

        //0买涨，1买跌
        int profitLimit = vsZySeekbar.getProgress() == 0 ? 200 : vsZySeekbar.getProgress() * 5;
        int lossLimit = vsZSeekbar.getProgress() == 0 ? 75 : vsZSeekbar.getProgress() * 5;

        int flag = buyUpLayout.isSelected() ? 0 : 1; //0-买涨，1买跌

        int holdNight = cbYeCheck.isChecked() ? 1 : 0; //0不持仓  1 持仓

        //手续费
        double fee = llPriceNum1.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) : (
                llPriceNum2.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) :
                        NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum));

        mPresenter.getUpdateHangUp(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.hangUpID, hangUpPrice, profitLimit, lossLimit, hangUpNum,
                mPlaceOrderBean.mrShouNum, fee, amount, flag, holdNight,mPlaceOrderBean.productIdSelect);
    }


    //show接口出错统一调用
    @Override
    public void bindHttpError() {
        stopLoading();
    }


    //单个产品信息接口
    @Override
    public void bindSingleProduct(ProductListBean productBean) {
        if (productBean != null && productBean.items != null && productBean.items.size() >= 2) {
            ProducItemBean itemsBean1 = productBean.items.get(0);
            ProducItemBean itemsBean2 = productBean.items.get(1);
            ProducItemBean itemsBean3 = productBean.items.get(2);

            mPlaceOrderBean.priceGg1 = itemsBean1.price / Constant.ZJ_PRICE_COMPANY;
            mPlaceOrderBean.priceGg2 = itemsBean2.price / Constant.ZJ_PRICE_COMPANY;
            mPlaceOrderBean.priceGg3 = itemsBean3.price / Constant.ZJ_PRICE_COMPANY;

            mPlaceOrderBean.feeGg1 = NumberUtil.divide(itemsBean1.fee, Constant.ZJ_PRICE_COMPANY);
            mPlaceOrderBean.feeGg2 = NumberUtil.divide(itemsBean2.fee, Constant.ZJ_PRICE_COMPANY);
            mPlaceOrderBean.feeGg3 = NumberUtil.divide(itemsBean3.fee, Constant.ZJ_PRICE_COMPANY);

            mPlaceOrderBean.productIdGg1 = String.valueOf(itemsBean1.id);
            mPlaceOrderBean.productIdGg2 = String.valueOf(itemsBean2.id);
            mPlaceOrderBean.productIdGg3 = String.valueOf(itemsBean3.id);

            mPlaceOrderBean.validRole = false;
            if (TextUtils.isEmpty(mPlaceOrderBean.stockIndex) || TextUtils.isEmpty(mPlaceOrderBean.changeValue)) {
                mPresenter.getReal(KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
            } else {
                if (mPlaceOrderBean.buyUp == 0 || mPlaceOrderBean.toBuy == 0) {
                    mPresenter.getSingleTime(KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
                } else {
                    //2挂单下单 3跟单下单 4跟单行情下单（首先展示行情）
                    if (mPlaceOrderBean.placeOrderType == 1) {
                        mPresenter.getValidRole();
                    } else {
                        mPlaceOrderBean.validRole = false;
                        mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
                    }
                }
            }
        } else {
            bindHttpError();
        }
    }


    //单个行情
    @Override
    public void bindATPriceQuotation(MarketHQBean marketBean) {
        if (marketBean != null) {
            mPlaceOrderBean.changeValue = marketBean.change;
            mPlaceOrderBean.stockIndex = marketBean.point;
            mPlaceOrderBean.closingPrice = marketBean.close;

            if (mPlaceOrderBean.buyUp == 0 || mPlaceOrderBean.toBuy == 0) {
                mPresenter.getSingleTime(KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
            } else {
                //2挂单下单 3跟单下单 4跟单行情下单（首先展示行情）
                if (mPlaceOrderBean.placeOrderType == 1) {
                    mPresenter.getValidRole();
                } else {
                    mPlaceOrderBean.validRole = false;
                    mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
                }
            }
        } else {
            bindHttpError();
        }
    }


    //单个时间和做单比例
    @Override
    public void bindSingleTime(ProductTimeLimitBean timeLimitBean) {
        if (timeLimitBean != null) {
            mPlaceOrderBean.toBuy = timeLimitBean.fall;
            mPlaceOrderBean.buyUp = timeLimitBean.rose;
        } else {
            mPlaceOrderBean.toBuy = 49;
            mPlaceOrderBean.buyUp = 51;
        }

        //2挂单下单 3跟单下单 4跟单行情下单（首先展示行情）
        if (mPlaceOrderBean.placeOrderType == 1) {
            mPresenter.getValidRole();
        } else {
            mPlaceOrderBean.validRole = false;
            mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
        }
    }


    //判断是不是牛人
    @Override
    public void bindValidRole(Boolean validRole) {
        mPlaceOrderBean.validRole = validRole;
        mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
    }


    //账户信息
    @Override
    public void bindUserInfo(UserZJBean userBean) {
        if (userBean.data.coupons != null && userBean.data.coupons.size() > 0) {
            for (UserZJBean.DataBean.CouponsBean couponsBean: userBean.data.coupons) {
                int voucherAmount = NumberUtil.divideInt(couponsBean.amount, Constant.ZJ_PRICE_COMPANY);
                if (mPlaceOrderBean.priceGg1 == voucherAmount) {
                    mPlaceOrderBean.voucherGgNum1 = mPlaceOrderBean.voucherGgNum1 + 1;
                    mPlaceOrderBean.voucherGgId1 = couponsBean.id;
                } else if (mPlaceOrderBean.priceGg2 == voucherAmount) {
                    mPlaceOrderBean.voucherGgNum2 = mPlaceOrderBean.voucherGgNum2 + 1;
                    mPlaceOrderBean.voucherGgId2 = couponsBean.id;
                } else if (mPlaceOrderBean.priceGg3 == voucherAmount) {
                    mPlaceOrderBean.voucherGgNum3 = mPlaceOrderBean.voucherGgNum3 + 1;
                    mPlaceOrderBean.voucherGgId3 = couponsBean.id;
                }
            }
        }
        showPlaceOrder();
        double ballance = NumberUtil.divide(userBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
        mPlaceOrderBean.ballance = ballance;
        cbPriceCheck.setText(Html.fromHtml("余额 <font color = '#333333'>" + ballance + "</font> 元"));
    }


    //刷新资金
    @Override
    public void bindUserInfoRefresh(UserZJBean userBean) {
        double ballance = NumberUtil.divide(userBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
        mPlaceOrderBean.ballance = ballance;
        cbPriceCheck.setText(Html.fromHtml("余额 <font color = '#333333'>" + ballance + "</font> 元"));
    }


    /**
     * @param type  0 现金建仓  1 代金券建仓 6挂单 7修改挂单
     * @param score
     */
    @Override
    public void bindPlaceOrder(int type, int score, String couScore) {
        dismiss();
        if (type == 6 || type == 7) {
            if (type == 7) { EventBus.getDefault().post(new BaseUIRefreshEvent(1));}
            new AppHintDialog(mContext).showAppDialog(type);
        } else if (type == 0) {
            new OrderSuccDialog(mContext).showDialog(OrderSuccDialog.Type.CASH, score, couScore);
        } else {
            new OrderSuccDialog(mContext).showDialog(OrderSuccDialog.Type.TICKET);
        }
    }


    //----------行情与k线相关--------------------


    @Override
    public String getTimeStr() {
        return minuteHourView != null ? minuteHourView.getTimeStr() : "";
    }

    @Override
    public float getNewLinePrice() {
        return minuteHourView != null ? minuteHourView.getNewLinePrice() : 0;
    }


    //分时线
    @Override
    public void bindZJQuotation(String productID, String yestodayClosePrice) {
        if (!TextUtils.isEmpty(productID) && TextUtils.equals(productID, mPlaceOrderBean.productID)) {
            minuteHourView.setDataAndInvalidate(mDataList, ""
                    , mDateList, KLineUtils.getDouble(yestodayClosePrice));
        }
    }

    //k线
    @Override
    public void bindZJKlineQuotation(String timeCode, String productID, KLineBean kLineBean) {
        if (!TextUtils.isEmpty(timeCode) && TextUtils.equals(timeCode, mTimeCode)
                && !TextUtils.isEmpty(productID) && TextUtils.equals(productID, mPlaceOrderBean.productID)) {
            mAdapter.setData(KLineDataUtils.parseKLine(mTimeCode, false, kLineBean, kChartView.getNewLinePrice()));
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


    private ArrayList<MinuteHourBean> mDataList = new ArrayList<>();
    private ArrayList<String> mDateList = new ArrayList<>();
    private KChartAdapter mAdapter;
    private String mLineType;
    private String mTimeCode;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(mPlaceOrderBean.productID)) {
                if (flMinuteHourView != null && flMinuteHourView.getVisibility() == View.VISIBLE) {
                    getMinuteHourRun(mPlaceOrderBean.productID, mPlaceOrderBean.closingPrice);
                } else {
                    getKLineRun(mPlaceOrderBean.productID);
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }

    private void startRun() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void startRunHttp() {
        if (isShowing()
                && tvMarketQH != null
                && tvMarketQH.getVisibility() == View.VISIBLE
                && tvMarketQH.isSelected()) {
            startRun();
        }
    }


    /**
     * 展示显示方法
     */
    private void showPlaceOrder() {
        show();
        tvProductName.setText(mPlaceOrderBean.productName);

        tvUpUserProbability.setText(mPlaceOrderBean.buyUp + "%用户");
        tvDownUserProbability.setText(mPlaceOrderBean.toBuy + "%用户");


        if (mPlaceOrderBean.placeOrderType == 6) {
            plNumLayout.setPNumInitView(mPlaceOrderBean.mrShouNum);
            cbYeCheck.setChecked(mPlaceOrderBean.holdNight == 1);
            if (!TextUtils.isEmpty(mPlaceOrderBean.hangUpPrice) && !TextUtils.isEmpty(mPlaceOrderBean.hangUpNum)) {
                etHangupPrice.setText(mPlaceOrderBean.hangUpPrice);
                etHangupPrice.setSelection(mPlaceOrderBean.hangUpPrice.length());
                etHangupNum.setText(mPlaceOrderBean.hangUpNum);
                etHangupNum.setSelection(mPlaceOrderBean.hangUpNum.length());
            }

        } else {
            //初始化手数
            plNumLayout.setPNumInitView();
            cbYeCheck.setChecked(true);
            etHangupPrice.setText("");
            etHangupNum.setText("");
        }
        cbVoucherCheck.setVisibility(mPlaceOrderBean.placeOrderType == 1 ? View.VISIBLE : View.INVISIBLE);
        cbVoucherCheck.setChecked(false);
        cbPriceCheck.setChecked(true);

        if (mPlaceOrderBean.mBuyState) {
            selectUpChange();
        } else {
            selectDownChange();
        }

        //设置三种规格金额
        tvPriceNum1.setText(mPlaceOrderBean.priceGg1 + "");
        tvPriceNum2.setText(mPlaceOrderBean.priceGg2 + "");
        tvPriceNum3.setText(mPlaceOrderBean.priceGg3 + "");
        selectShouYuanChange(mPlaceOrderBean.selectGgNum);

        //初始化涨跌幅
        refreshPlaceOrderDialog(mPlaceOrderBean.stockIndex, mPlaceOrderBean.buyUp, mPlaceOrderBean.toBuy, mPlaceOrderBean.changeValue);

        setPrice();
        setDwPrice();

        //是否显示挂单布局
        llHangup.setVisibility(mPlaceOrderBean.placeOrderType == 5 || mPlaceOrderBean.placeOrderType == 6 ? View.VISIBLE : View.GONE);

        //设置止盈止损 百分比
        if (mPlaceOrderBean.placeOrderType == 3 || mPlaceOrderBean.placeOrderType == 4) {
            vsZSeekbar.setProgress((mPlaceOrderBean.lossLimit / 5));
            vsZySeekbar.setProgress((mPlaceOrderBean.profitLimit / 5));
            setZsSeekbarProgress(vsZSeekbar.getProgress());
            setZySeekbarProgress(vsZySeekbar.getProgress());

            nsvScrollView.setVisibility(View.VISIBLE);
            llMarketRootView.setVisibility(View.GONE);
            tvMarketQH.setVisibility(View.VISIBLE);
            tvMarketQH.setSelected(false);
            tvMarketQH.setText("行情");
            vFollow.setVisibility(View.GONE);
            tvFollow.setVisibility(View.GONE);
            tvFollow.setSelected(false);
        } else {
            if (mPlaceOrderBean.placeOrderType == 6) {
                vsZSeekbar.setProgress(mPlaceOrderBean.lossLimit / 5);
                vsZySeekbar.setProgress(mPlaceOrderBean.profitLimit / 5);
            } else {
                vsZSeekbar.setProgress(0);
                vsZySeekbar.setProgress(0);
            }
            setZsSeekbarProgress(vsZSeekbar.getProgress());
            setZySeekbarProgress(vsZySeekbar.getProgress());
            nsvScrollView.setVisibility(View.VISIBLE);
            llMarketRootView.setVisibility(View.GONE);
            tvMarketQH.setVisibility(View.INVISIBLE);
            if (mPlaceOrderBean.validRole) {
                vFollow.setVisibility(View.VISIBLE);
                tvFollow.setVisibility(View.VISIBLE);
                tvFollow.setSelected(true);
            } else {
                vFollow.setVisibility(View.GONE);
                tvFollow.setVisibility(View.GONE);
            }
        }


        if (mPlaceOrderBean.placeOrderType == 4) {
            nsvScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = llMarketRootView.getLayoutParams();
                    layoutParams.height = llProductRoot.getHeight() - llProductTitle.getHeight()
                            - mContext.getResources().getDimensionPixelSize(R.dimen.dimen_29_5dp);
                    llMarketRootView.setLayoutParams(layoutParams);

                    llBottomPlaceOrder.setVisibility(View.GONE);
                    llMarketRootView.setVisibility(View.VISIBLE);
                    nsvScrollView.setVisibility(View.GONE);
                    tvMarketQH.setVisibility(View.VISIBLE);
                    tvMarketQH.setSelected(true);
                    tvMarketQH.setText("跟买");
                    mDataList.clear();
                    flMinuteHourView.setVisibility(View.VISIBLE);
                    kChartView.setVisibility(View.GONE);
                    minuteHourView.setDataAndInvalidate(mDataList, ""
                            , mDateList, KLineUtils.getDouble(mPlaceOrderBean.closingPrice));
                    pmtMarketTabLayout.initSelectorTab();
                }
            }, 20);
        } else {
            llBottomPlaceOrder.setVisibility(View.VISIBLE);
        }
    }


    //选择 买涨 状态改变
    private void selectUpChange() {
        plNumLayout.setBuyViewState(true);
        tvPlaceOrder.setText("买涨下单");
        buyUpLayout.setSelected(true);
        tvUpUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.white_10));
        tvUpUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));

        buyDownLayout.setSelected(false);
        tvDownUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        tvDownUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));

        //手数、元
        llPriceNum1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));
        llPriceNum2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));
        llPriceNum3.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));

        //浮动盈亏
        tvMarketPriceChange.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
        tvPlaceOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
    }

    //选择 买跌 状态改变
    private void selectDownChange() {
        plNumLayout.setBuyViewState(false);
        tvPlaceOrder.setText("买跌下单");
        buyUpLayout.setSelected(false);
        tvUpUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        tvUpUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));

        buyDownLayout.setSelected(true);
        tvDownUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.white_10));
        tvDownUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));

        //手数、元
        llPriceNum1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));
        llPriceNum2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));
        llPriceNum3.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));

        //浮动盈亏
        tvMarketPriceChange.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
        tvPlaceOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
    }


    //手、元选择
    private void selectShouYuanChange(int num) {
        if (llPriceNum1.isSelected()) {
            llPriceNum1.setSelected(false);
            tvPriceNum1.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            tvPriceHNum1.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        }
        if (llPriceNum2.isSelected()) {
            llPriceNum2.setSelected(false);
            tvPriceNum2.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            tvPriceHNum2.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        }
        if (llPriceNum3.isSelected()) {
            llPriceNum3.setSelected(false);
            tvPriceNum3.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            tvPriceHNum3.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        }

        if (num == 1) {
            llPriceNum1.setSelected(true);
            tvPriceNum1.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));
            tvPriceHNum1.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            mPlaceOrderBean.selectPrice = mPlaceOrderBean.priceGg1;
            mPlaceOrderBean.selectGgNum = 1;
            setVoucherNum();
        } else if (num == 2) {
            llPriceNum2.setSelected(true);
            tvPriceNum2.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));
            tvPriceHNum2.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            mPlaceOrderBean.selectPrice = mPlaceOrderBean.priceGg2;
            mPlaceOrderBean.selectGgNum = 2;
            setVoucherNum();
        } else {
            llPriceNum3.setSelected(true);
            tvPriceNum3.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));
            tvPriceHNum3.setTextColor(ContextCompat.getColor(mContext, R.color.color_828282));
            mPlaceOrderBean.selectPrice = mPlaceOrderBean.priceGg3;
            mPlaceOrderBean.selectGgNum = 3;
            setVoucherNum();
        }
    }

    //设置对应规格的代金券数量
    private void setVoucherNum() {
        if (mPlaceOrderBean.selectGgNum == 1) {
            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum1 + "</font> 张"));
        } else if (mPlaceOrderBean.selectGgNum == 2) {
            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum2 + "</font> 张"));
        } else if (mPlaceOrderBean.selectGgNum == 3) {
            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum3 + "</font> 张"));
        }
    }


    /**
     * 设置总计金额
     */
    private void setPrice() {
        if (llPriceNum1.isSelected()) {
            mPlaceOrderBean.productIdSelect = mPlaceOrderBean.productIdGg1;
            if (cbPriceCheck.isChecked()) {
                tvPrice.setText(NumberUtil.multiply(mPlaceOrderBean.priceGg1, mPlaceOrderBean.mrShouNum) + "");
                tvOpenFee.setText("(解约手续费:" + NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) + "元)");
                tvPriceHint.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setText(mPlaceOrderBean.mrShouNum + "张代金券");
                tvOpenFee.setText("(解约手续费:0元)");
                tvPriceHint.setVisibility(View.GONE);
            }
        } else if (llPriceNum2.isSelected()) {
            mPlaceOrderBean.productIdSelect = mPlaceOrderBean.productIdGg2;
            if (cbPriceCheck.isChecked()) {
                tvPrice.setText(NumberUtil.multiply(mPlaceOrderBean.priceGg2, mPlaceOrderBean.mrShouNum) + "");
                tvOpenFee.setText("(解约手续费:" + NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) + "元)");
                tvPriceHint.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setText(mPlaceOrderBean.mrShouNum + "张代金券");
                tvOpenFee.setText("(解约手续费:0元)");
                tvPriceHint.setVisibility(View.GONE);
            }
        } else {
            mPlaceOrderBean.productIdSelect = mPlaceOrderBean.productIdGg3;
            if (cbPriceCheck.isChecked()) {
                tvPrice.setText(NumberUtil.multiply(mPlaceOrderBean.priceGg3, mPlaceOrderBean.mrShouNum) + "");
                tvOpenFee.setText("(解约手续费:" + NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum) + "元)");
                tvPriceHint.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setText(mPlaceOrderBean.mrShouNum + "张代金券");
                tvOpenFee.setText("(解约手续费:0元)");
                tvPriceHint.setVisibility(View.GONE);
            }
        }
    }


    private void setZsSeekbarProgress(int zsProgress) {
        int zsNum = zsProgress * 5;
        tvZsNum.setText((zsNum == 0 ? "不限" : zsNum + "%"));
        if (cbPriceCheck.isChecked()) {
            double price = NumberUtil.multiply(llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3), mPlaceOrderBean.mrShouNum);
            tvZsPrice.setText((zsNum == 0 ? NumberUtil.multiply(price, 0.75) + "元" : NumberUtil.multiply(price, NumberUtil.divide(zsNum, 100)) + "元"));
        } else {
            tvZsPrice.setText("0.0元");
        }
    }

    private void setZySeekbarProgress(int zyProgress) {
        int zyNum = zyProgress * 5;
        tvZyNum.setText((zyNum == 0 ? "不限" : zyNum + "%"));
        double price = NumberUtil.multiply(llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3), mPlaceOrderBean.mrShouNum);
        tvZyPrice.setText((zyNum == 0 ? NumberUtil.multiply(price, 2) + "元" : NumberUtil.multiply(price, NumberUtil.divide(zyNum, 100)) + "元"));
    }


    //设置每个点位浮动盈亏多少
    private void setDwPrice() {
        double dPrice = TradeUtils.getProductPrice(mPlaceOrderBean.productID, (int) mPlaceOrderBean.selectPrice);
        tvMarketPriceChange.setText("每浮动1点，盈亏" + NumberUtil.multiply(dPrice, mPlaceOrderBean.mrShouNum) + "元");
    }

    /**
     * 刷新数据
     *
     * @param stockIndex
     * @param buyUp
     * @param toBuy
     * @param changeValue
     */
    public void refreshPlaceOrderDialog(String stockIndex, int buyUp, int toBuy, String changeValue) {
        tvProductMarket.setText(stockIndex);
        tvUpUserProbability.setText(buyUp + "%用户");
        tvDownUserProbability.setText(toBuy + "%用户");
        float v = Float.parseFloat(changeValue);
        if (mPlaceOrderBean.placeOrderType == 3 || mPlaceOrderBean.placeOrderType == 4) {
            minuteHourView.setNewLinePrice(0, 0, "");
            kChartView.setNewLinePrice(0, 0);
        }
        if (v > 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(true);
        } else if (v < 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(false);
        } else {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            ivDownUp.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 刷新数据
     *
     * @param stockIndex
     * @param changeValue
     */
    public void refreshPlaceOrderDialog(String stockIndex, String changeValue, String mTimeStr) {

        tvProductMarket.setText(stockIndex);
        float v = Float.parseFloat(changeValue);
        if (mPlaceOrderBean.placeOrderType == 3 || mPlaceOrderBean.placeOrderType == 4) {
            float v1 = Float.parseFloat(stockIndex);
            minuteHourView.setNewLinePrice(v1, v, mTimeStr);
            kChartView.setNewLinePrice(v1, v);
        }
        if (v > 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(true);
        } else if (v < 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(false);
        } else {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            ivDownUp.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshPlaceOrderDialog(String stockIndex, String changeValue) {

        tvProductMarket.setText(stockIndex);
        float v = Float.parseFloat(changeValue);
        if (v > 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(true);
        } else if (v < 0) {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
            ivDownUp.setVisibility(View.VISIBLE);
            ivDownUp.setSelected(false);
        } else {
            tvProductMarket.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            ivDownUp.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 普通下单
     *
     * @param productID   产品id
     * @param buyState    true 买涨 false 买跌
     * @param selectGgNum 购买规格 1 2 3 （默认传1）
     * @param stockIndex  当前点位
     * @param
     */
    public void placeOrder(String productID, boolean buyState, int selectGgNum, String stockIndex, String changeValue, int buyUp, int toBuy) {
        mPlaceOrderBean.placeOrderType = 1;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.stockIndex = stockIndex;
        mPlaceOrderBean.changeValue = changeValue;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.mrShouNum = 1;
        mPlaceOrderBean.buyUp = buyUp;
        mPlaceOrderBean.toBuy = toBuy;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getSingleProduct(productID);
    }


    public void placeOrder(String productID, boolean buyState, int selectGgNum) {
        placeOrder(productID, buyState, selectGgNum, "", "", 0, 0);
    }

    /**
     * 挂单下单
     *
     * @param productID   产品id
     * @param buyState    true 买涨 false 买跌
     * @param selectGgNum 购买规格 1 2 3 （默认传1）
     * @param stockIndex  当前点位
     * @param changeValue
     */
    public void placeOrderHangUp(String productID, boolean buyState, int selectGgNum, String stockIndex, String changeValue) {
        mPlaceOrderBean.placeOrderType = 5;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.stockIndex = stockIndex;
        mPlaceOrderBean.changeValue = changeValue;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.mrShouNum = 1;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getSingleProduct(productID);
    }

    public void placeOrderHangUp(String productID, boolean buyState, int selectGgNum) {
        placeOrderHangUp(productID, buyState, selectGgNum, "", "");
    }


    /**
     * 修改挂单
     *
     * @param productID   产品id
     * @param stockIndex  最新价
     * @param changeValue 涨跌值
     * @param buyState    true 买涨 false 买跌
     * @param selectGgNum 购买规格 1 2 3
     * @param quantity    手数
     * @param holdNight   是否持仓过夜 0不持仓 1持仓
     * @param profitLimit 止盈
     * @param lossLimit   止损
     */
    public void placeOrderUpdateHangUp(String hangUpID, String productID, String stockIndex, String changeValue, boolean buyState, int selectGgNum,
                                       int quantity, int holdNight, int profitLimit, int lossLimit, String hangUpPrice, String hangUpNum) {
        mPlaceOrderBean.placeOrderType = 6;
        mPlaceOrderBean.hangUpID = hangUpID;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.stockIndex = stockIndex;
        mPlaceOrderBean.changeValue = changeValue;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.mrShouNum = quantity;
        mPlaceOrderBean.holdNight = holdNight;
        mPlaceOrderBean.profitLimit = profitLimit;
        mPlaceOrderBean.lossLimit = lossLimit;
        mPlaceOrderBean.hangUpPrice = hangUpPrice;
        mPlaceOrderBean.hangUpNum = hangUpNum;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getSingleProduct(productID);
    }


    /**
     * 跟单下单(行情点击)
     *
     * @param isMarketKLine 3跟单下单 4跟单行情下单（首先展示行情）
     * @param productID     产品id
     * @param buyState      true 买涨 false 买跌
     * @param selectGgNum   购买规格 1 2 3 （默认传1）
     * @param stockIndex    当前点位
     * @param closingPrice  收盘价
     * @param fellowerPhone 被跟单人手机号
     * @param orderNo       订单号
     */
    public void placeOrderFollow(int isMarketKLine, String productID, boolean buyState, int selectGgNum, String stockIndex,
                                 String changeValue, String closingPrice, int profitLimit, int lossLimit,
                                 String fellowerPhone, String orderNo) {
        mPlaceOrderBean.placeOrderType = isMarketKLine;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.stockIndex = stockIndex;
        mPlaceOrderBean.changeValue = changeValue;
        mPlaceOrderBean.closingPrice = closingPrice;
        mPlaceOrderBean.profitLimit = profitLimit;
        mPlaceOrderBean.lossLimit = lossLimit;
        mPlaceOrderBean.fellowerPhone = fellowerPhone;
        mPlaceOrderBean.orderNo = orderNo;
        mPlaceOrderBean.mrShouNum = 1;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getSingleProduct(productID);
    }

}
