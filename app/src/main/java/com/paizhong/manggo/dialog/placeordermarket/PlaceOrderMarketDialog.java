package com.paizhong.manggo.dialog.placeordermarket;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.other.PlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProducItemBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.dialog.other.OrderSuccDialog;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderNumLayout;
import com.paizhong.manggo.dialog.recharge.RechargeDialog;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.widget.DinTextView;
import com.paizhong.manggo.widget.VibrateSeekBar;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zab on 2018/8/24 0024.
 */
public class PlaceOrderMarketDialog extends BaseDialog<PlaceOrderMarketPresenter> implements LifecycleObserver,PlaceOrderMarketContract.View, View.OnClickListener {

    //顶部布局
    private TextView tvFollow;
    private View vFollow;
    private LinearLayout llPlaceOrderMarket;

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

    private PlaceOrderBean mPlaceOrderBean;
    private ProductTabViewMarketModel mMarketModel;


    public PlaceOrderMarketDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
        this.mPlaceOrderBean = new PlaceOrderBean();
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void dismiss() {
        onDestroy();
        super.dismiss();
    }


    @Override
    public void onDestroy() {
        stopRun();
        if (mMarketModel !=null){
            mMarketModel.onDestroy();
        }
        if (mContext !=null){
            mContext.getLifecycle().removeObserver(this);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
       if (mMarketModel !=null && isShowing()){
           startRun();
       }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
         stopRun();
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
        setContentView(R.layout.dialog_placeorder_market);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
        mContext.getLifecycle().addObserver(this);

        tvFollow = findViewById(R.id.tv_follow);
        vFollow = findViewById(R.id.v_follow);
        llPlaceOrderMarket = findViewById(R.id.ll_placeOrderMarket);

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

        findViewById(R.id.ll_closed).setOnClickListener(this);
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


        mMarketModel = new ProductTabViewMarketModel((MainActivity) mContext);
        mMarketModel.updateView(mPlaceOrderBean.productID);
        llPlaceOrderMarket.addView(mMarketModel.mView);

        setListener();
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

        mMarketModel.setOnProductSelectListener(new ProductTabViewMarketModel.OnProductSelectListener() {
            @Override
            public void onProductSelect(String productID) {
                ProductListBean singleProduct = getSingleProduct(productID);
                mPlaceOrderBean.productID = productID;
                mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
                if (singleProduct !=null){
                    initSingleProduct(singleProduct);
                }
                ProductTimeLimitBean productBean = getSingleProductLimit(mPlaceOrderBean.productID);
                if (productBean !=null){
                    mPlaceOrderBean.buyUp = productBean.rose;
                    mPlaceOrderBean.toBuy = productBean.fall;
                }
                updateProduct(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_closed: //关闭
                dismiss();
                break;
            case R.id.tv_recharge://充值
                if (!DeviceUtils.isFastDoubleClick()) {
                    new RechargeDialog(mContext).getData();
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
                placeOrderHangUp();
                break;
        }
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
        //
        int newMarket = NumberUtil.getInt(mMarketModel.getStockIndex(), 0);
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

    //show接口出错统一调用
    @Override
    public void bindHttpError() {
        stopLoading();
        onDestroy();
    }


    //产品信息相关数据
    private List<ProductListBean> mProductList;
    private ProductListBean getSingleProduct(String productID){
        if (mProductList !=null && mProductList.size() > 0 && !TextUtils.isEmpty(productID)){
            for (ProductListBean productBean : mProductList){
                if (TextUtils.equals(productID,productBean.id)){
                    return productBean;
                }
            }
        }
        return null;
    }


    //产品做单比例相关数据
    private List<ProductTimeLimitBean> mProductTimeLimit;
    private ProductTimeLimitBean getSingleProductLimit(String productID){
        if (mProductTimeLimit !=null && mProductTimeLimit.size() > 0 && !TextUtils.isEmpty(productID)){
            for (ProductTimeLimitBean productBean : mProductTimeLimit){
                if (TextUtils.equals(productID,productBean.id)){
                    return productBean;
                }
            }
        }
        return null;
    }


    //初始化当前产品信息数据
    private void initSingleProduct(ProductListBean productBean){
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
    }



    //全部产品信息接口
    @Override
    public void bindSingleProduct(List<ProductListBean> productList) {
        if (productList != null && productList.size() > 0) {
            this.mProductList = productList;
            ProductListBean productBean = getSingleProduct(mPlaceOrderBean.productID);
            if (productBean !=null && productBean.items !=null && productBean.items.size() > 0){
                initSingleProduct(productBean);
                mPresenter.getProductTime();
            }else {
                bindHttpError();
            }
        } else {
            bindHttpError();
        }
    }


    //全部做单比例
    @Override
    public void bindProductTime(List<ProductTimeLimitBean> productTimeLimit) {
        if (productTimeLimit !=null && productTimeLimit.size() > 0){
            mProductTimeLimit = productTimeLimit;
            ProductTimeLimitBean productBean = getSingleProductLimit(mPlaceOrderBean.productID);
            if (productBean !=null){
                mPlaceOrderBean.buyUp = productBean.rose;
                mPlaceOrderBean.toBuy = productBean.fall;
                mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
            }else {
                bindHttpError();
            }
        }else {
            bindHttpError();
        }
    }


    //账户信息
    @Override
    public void bindUserInfo(UserZJBean userBean) {
        if (userBean !=null){
            show();
            updateProduct(true);
            stopLoading();
            double ballance = NumberUtil.divide(userBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
            mPlaceOrderBean.ballance = ballance;
            cbPriceCheck.setText(Html.fromHtml("余额 <font color = '#333333'>" + ballance + "</font> 元"));
        }else {
            bindHttpError();
        }
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
        new AppHintDialog(mContext).showAppDialog(type);
    }





    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMarketModel !=null){
                mMarketModel.refreshData();
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



    private void updateProduct(boolean isInit){
        tvUpUserProbability.setText(mPlaceOrderBean.buyUp + "%用户");
        tvDownUserProbability.setText(mPlaceOrderBean.toBuy + "%用户");
        //设置三种规格金额
        tvPriceNum1.setText(mPlaceOrderBean.priceGg1 + "");
        tvPriceNum2.setText(mPlaceOrderBean.priceGg2 + "");
        tvPriceNum3.setText(mPlaceOrderBean.priceGg3 + "");
        if (isInit){
            startRun();
            vFollow.setVisibility(View.GONE);
            tvFollow.setVisibility(View.GONE);
            vsZSeekbar.setProgress(0);
            vsZySeekbar.setProgress(0);
            llHangup.setVisibility(View.VISIBLE);
            plNumLayout.setPNumInitView();
            cbVoucherCheck.setVisibility(View.INVISIBLE);
            cbVoucherCheck.setChecked(false);
            cbYeCheck.setChecked(true);
            cbPriceCheck.setChecked(true);
            etHangupPrice.setText("");
            etHangupNum.setText("");
            if (mPlaceOrderBean.mBuyState) {
                selectUpChange();
            } else {
                selectDownChange();
            }
        }
        selectShouYuanChange(mPlaceOrderBean.selectGgNum);
        setPrice();
        setDwPrice();
        setZsSeekbarProgress(vsZSeekbar.getProgress());
        setZySeekbarProgress(vsZySeekbar.getProgress());
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
//        if (mPlaceOrderBean.selectGgNum == 1) {
//            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum1 + "</font> 张"));
//        } else if (mPlaceOrderBean.selectGgNum == 2) {
//            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum2 + "</font> 张"));
//        } else if (mPlaceOrderBean.selectGgNum == 3) {
//            cbVoucherCheck.setText(Html.fromHtml("代金券 <font color = '#333333'>" + mPlaceOrderBean.voucherGgNum3 + "</font> 张"));
//        }
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
     * 挂单下单
     * @param productID   产品id
     * @param buyState    true 买涨 false 买跌
     */
    public void placeOrderHangUp(String productID, boolean buyState,int selectGgNum) {
        mPlaceOrderBean.placeOrderType = 5;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.mrShouNum = 1;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getProductList();
    }
}
