package com.paizhong.manggo.dialog.placeorderlan;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.PlaceOrderBean;
import com.paizhong.manggo.bean.trade.ProducItemBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.dialog.other.OrderSuccDialog;
import com.paizhong.manggo.dialog.recharge.RechargeDialog;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.widget.VibrateSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 横屏下单
 * Created by zab on 2018/9/5 0005.
 */
public class PlaceOrderLanDialog extends BaseDialog<PlaceOrderLanPresenter> implements PlaceOrderLanContract.View, View.OnClickListener {
    private TextView tvProductName;


    private PlaceOrderBean mPlaceOrderBean;
    private TextView tvProductMarket;
    private ImageView ivDownUp;

    private View llUpUserProbability;
    private TextView tvUpUserProbability;
    private TextView tvUpUser;
    private View llDownUserProbability;
    private TextView tvDownUserProbability;
    private TextView tvDownUser;

    private View llHangupLayout;
    private EditText etHangupPrice;
    private EditText etHangupNum;

    private View llPriceNum1, llPriceNum2, llPriceNum3;
    private TextView tvPriceNum1, tvPriceNum2, tvPriceNum3;
    private TextView tvPriceHNum1, tvPriceHNum2, tvPriceHNum3;

    private View llExShou1, llExShou2, llExShou3, llExShou4, llExShou5, llExShou6, llExShou7, llExShou8, llExShou9, llExShou10;
    private TextView tvExShou1, tvExShou2, tvExShou3, tvExShou4, tvExShou5, tvExShou6, tvExShou7, tvExShou8, tvExShou9, tvExShou10;
    private TextView tvExShouHint1, tvExShouHint2, tvExShouHint3, tvExShouHint4, tvExShouHint5, tvExShouHint6, tvExShouHint7, tvExShouHint8, tvExShouHint9, tvExShouHint10;

    private TextView tvZsNum, tvZyNum;
    private TextView tvZsPrice, tvZyPrice;
    private VibrateSeekBar vsZsSeekbar, vsZySeekbar;

    private CheckBox cbPriceCheck, cbVoucherCheck, cbYeCheck;

    private View vFollow;
    private TextView tvFollow;
    private TextView tvPriceHint;
    private TextView tvPrice;
    private TextView tvOpenFee;
    private TextView tvPriceChange;
    private TextView tvPlaceOrder;


    public PlaceOrderLanDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
        this.mPlaceOrderBean = new PlaceOrderBean();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        mPlaceOrderBean.initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
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
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.dialog_placeorder_lan);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);


        tvProductName = findViewById(R.id.tv_product_name);
        tvProductMarket = findViewById(R.id.tv_product_market);
        ivDownUp = findViewById(R.id.iv_down_up);

        //涨跌  名称
        View ivClosed = findViewById(R.id.iv_closed);
        llUpUserProbability = findViewById(R.id.ll_up_user_probability);
        tvUpUserProbability = findViewById(R.id.tv_up_user_probability);
        tvUpUser = findViewById(R.id.tv_up_user);
        llDownUserProbability = findViewById(R.id.ll_down_user_probability);
        tvDownUserProbability = findViewById(R.id.tv_down_user_probability);
        tvDownUser = findViewById(R.id.tv_down_user);

        //元 手
        llHangupLayout = findViewById(R.id.ll_hangup);
        etHangupPrice = findViewById(R.id.et_hangup_price);
        etHangupNum = findViewById(R.id.et_hangup_num);
        llPriceNum1 = findViewById(R.id.ll_price_num1);
        tvPriceNum1 = findViewById(R.id.tv_price_num1);
        tvPriceHNum1 = findViewById(R.id.tv_price_h_num1);
        llPriceNum2 = findViewById(R.id.ll_price_num2);
        tvPriceNum2 = findViewById(R.id.tv_price_num2);
        tvPriceHNum2 = findViewById(R.id.tv_price_h_num2);
        llPriceNum3 = findViewById(R.id.ll_price_num3);
        tvPriceNum3 = findViewById(R.id.tv_price_num3);
        tvPriceHNum3 = findViewById(R.id.tv_price_h_num3);

        //手数
        llExShou1 = findViewById(R.id.ll_ex_shou_1);
        tvExShou1 = findViewById(R.id.tv_ex_shou_1);
        tvExShouHint1 = findViewById(R.id.tv_ex_shou_hint_1);
        llExShou2 = findViewById(R.id.ll_ex_shou_2);
        tvExShou2 = findViewById(R.id.tv_ex_shou_2);
        tvExShouHint2 = findViewById(R.id.tv_ex_shou_hint_2);

        llExShou3 = findViewById(R.id.ll_ex_shou_3);
        tvExShou3 = findViewById(R.id.tv_ex_shou_3);
        tvExShouHint3 = findViewById(R.id.tv_ex_shou_hint_3);

        llExShou4 = findViewById(R.id.ll_ex_shou_4);
        tvExShou4 = findViewById(R.id.tv_ex_shou_4);
        tvExShouHint4 = findViewById(R.id.tv_ex_shou_hint_4);

        llExShou5 = findViewById(R.id.ll_ex_shou_5);
        tvExShou5 = findViewById(R.id.tv_ex_shou_5);
        tvExShouHint5 = findViewById(R.id.tv_ex_shou_hint_5);

        llExShou6 = findViewById(R.id.ll_ex_shou_6);
        tvExShou6 = findViewById(R.id.tv_ex_shou_6);
        tvExShouHint6 = findViewById(R.id.tv_ex_shou_hint_6);

        llExShou7 = findViewById(R.id.ll_ex_shou_7);
        tvExShou7 = findViewById(R.id.tv_ex_shou_7);
        tvExShouHint7 = findViewById(R.id.tv_ex_shou_hint_7);

        llExShou8 = findViewById(R.id.ll_ex_shou_8);
        tvExShou8 = findViewById(R.id.tv_ex_shou_8);
        tvExShouHint8 = findViewById(R.id.tv_ex_shou_hint_8);

        llExShou9 = findViewById(R.id.ll_ex_shou_9);
        tvExShou9 = findViewById(R.id.tv_ex_shou_9);
        tvExShouHint9 = findViewById(R.id.tv_ex_shou_hint_9);

        llExShou10 = findViewById(R.id.ll_ex_shou_10);
        tvExShou10 = findViewById(R.id.tv_ex_shou_10);
        tvExShouHint10 = findViewById(R.id.tv_ex_shou_hint_10);

        //止盈 止损
        tvZsNum = findViewById(R.id.tv_zs_num);
        tvZsPrice = findViewById(R.id.tv_zs_price);
        vsZsSeekbar = findViewById(R.id.vs_zs_seekbar);
        tvZyNum = findViewById(R.id.tv_zy_num);
        tvZyPrice = findViewById(R.id.tv_zy_price);
        vsZySeekbar = findViewById(R.id.vs_zy_seekbar);

        cbPriceCheck = findViewById(R.id.cb_price_check);
        TextView tvRecharge = findViewById(R.id.tv_recharge);
        cbVoucherCheck = findViewById(R.id.cb_voucher_check);
        cbYeCheck = findViewById(R.id.cb_ye_check);


        vFollow = findViewById(R.id.v_follow);
        tvFollow = findViewById(R.id.tv_follow);
        tvPriceHint = findViewById(R.id.tv_price_hint);
        tvPrice = findViewById(R.id.tv_price);
        tvOpenFee = findViewById(R.id.tv_open_fee);
        tvPriceChange = findViewById(R.id.tv_market_price_change);
        tvPlaceOrder = findViewById(R.id.tv_place_order);


        ivClosed.setOnClickListener(this);
        llUpUserProbability.setOnClickListener(this);
        llDownUserProbability.setOnClickListener(this);
        llPriceNum1.setOnClickListener(this);
        llPriceNum2.setOnClickListener(this);
        llPriceNum3.setOnClickListener(this);

        llExShou1.setOnClickListener(this);
        llExShou2.setOnClickListener(this);
        llExShou3.setOnClickListener(this);
        llExShou4.setOnClickListener(this);
        llExShou5.setOnClickListener(this);
        llExShou6.setOnClickListener(this);
        llExShou7.setOnClickListener(this);
        llExShou8.setOnClickListener(this);
        llExShou9.setOnClickListener(this);
        llExShou10.setOnClickListener(this);
        tvPlaceOrder.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
        setListener();
    }

    private void setListener() {
        vsZsSeekbar.setProgressListener(new VibrateSeekBar.OnProgressListener() {
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
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
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
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
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
            case R.id.ll_ex_shou_1://手数1
                if (!llExShou1.isSelected()) {
                    selectNumChange(1);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_2://手数2
                if (!llExShou2.isSelected()) {
                    selectNumChange(2);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_3://手数3
                if (!llExShou3.isSelected()) {
                    selectNumChange(3);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_4://手数4
                if (!llExShou4.isSelected()) {
                    selectNumChange(4);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_5://手数5
                if (!llExShou5.isSelected()) {
                    selectNumChange(5);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_6://手数6
                if (!llExShou6.isSelected()) {
                    selectNumChange(6);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_7://手数7
                if (!llExShou7.isSelected()) {
                    selectNumChange(7);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_8://手数8
                if (!llExShou8.isSelected()) {
                    selectNumChange(8);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_9://手数9
                if (!llExShou9.isSelected()) {
                    selectNumChange(9);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_ex_shou_10://手数10
                if (!llExShou10.isSelected()) {
                    selectNumChange(10);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.iv_closed: //关闭
                dismiss();
                break;
            case R.id.tv_recharge://充值
                if (!DeviceUtils.isFastDoubleClick()) {
                    new RechargeDialog(mContext).getData();
                }
                break;
            case R.id.ll_up_user_probability: //买涨
                if (!llUpUserProbability.isSelected()) {
                    selectUpChange();
                }
                break;
            case R.id.ll_down_user_probability: //买跌
                if (!llDownUserProbability.isSelected()) {
                    selectDownChange();
                }
                break;
            case R.id.ll_price_num1: //元手 1
                if (!llPriceNum1.isSelected()) {
                    selectShouYuanChange(1);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_price_num2: //元手 2
                if (!llPriceNum2.isSelected()) {
                    selectShouYuanChange(2);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.ll_price_num3: //元手 3
                if (!llPriceNum3.isSelected()) {
                    selectShouYuanChange(3);
                    setPrice();
                    setDwPrice();
                    setZsSeekbarProgress(vsZsSeekbar.getProgress());
                    setZySeekbarProgress(vsZySeekbar.getProgress());
                }
                break;
            case R.id.tv_follow: //跟买开关
                if (!DeviceUtils.isFastDoubleClick()) {
                    tvFollow.setSelected(!tvFollow.isSelected());
                }
                break;
            case R.id.tv_place_order: //下单
                //1 正常下单  2无 3跟单下单 4跟单行情下单（首先展示行情）5挂单下单 6修改挂单
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (mPlaceOrderBean.placeOrderType == 1) {
                    if (cbPriceCheck.isChecked()) {
                        placeOrder();
                    } else {
                        placeOrderVoucher();
                    }
                } else if (mPlaceOrderBean.placeOrderType == 5) {
                    placeOrderHangUp();
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
        int lossLimit = vsZsSeekbar.getProgress() == 0 ? 75 : vsZsSeekbar.getProgress() * 5;

        int flag = llUpUserProbability.isSelected() ? 0 : 1; //0-买涨，1买跌

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
            int lossLimit = vsZsSeekbar.getProgress() == 0 ? 75 : vsZsSeekbar.getProgress() * 5;

            int flag = llUpUserProbability.isSelected() ? 0 : 1; //0-买涨，1买跌

            int amount = llPriceNum1.isSelected() ? mPlaceOrderBean.priceGg1 : (llPriceNum2.isSelected() ? mPlaceOrderBean.priceGg2 : mPlaceOrderBean.priceGg3);

            mPresenter.getVoucherPlaceOrder(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, AppApplication.getConfig().getAt_secret_access_key(),
                    mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum, flag, profitLimit, lossLimit, voucherGgID, amount, mPlaceOrderBean.productName, 0, KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
        } else {
            showErrorMsg((voucherGgNum == 0 ? "当前品种暂无代金券" : "当前下单手数大于代金券数量"), null);
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
        int lossLimit = vsZsSeekbar.getProgress() == 0 ? 75 : vsZsSeekbar.getProgress() * 5;

        int flag = llUpUserProbability.isSelected() ? 0 : 1; //0-买涨，1买跌

        int holdNight = cbYeCheck.isChecked() ? 1 : 0; //0不持仓  1 持仓

        //手续费
        double fee = llPriceNum1.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg1, mPlaceOrderBean.mrShouNum) : (
                llPriceNum2.isSelected() ? NumberUtil.multiply(mPlaceOrderBean.feeGg2, mPlaceOrderBean.mrShouNum) :
                        NumberUtil.multiply(mPlaceOrderBean.feeGg3, mPlaceOrderBean.mrShouNum));

        mPresenter.getHangUpOrder(AppApplication.getConfig().getAt_token(), mPlaceOrderBean.productID, mPlaceOrderBean.productIdSelect, mPlaceOrderBean.mrShouNum,
                flag, profitLimit, lossLimit, amount, holdNight, mPlaceOrderBean.productName, fee, KLineDataUtils.getProductCode(mPlaceOrderBean.productID),
                SpUtil.getString(Constant.USER_KEY_NICKNAME), hangUpNum, hangUpPrice);
    }


    public void showPlaceOrder() {
        show();
        tvProductName.setText(mPlaceOrderBean.productName);
        tvUpUserProbability.setText(mPlaceOrderBean.buyUp + "%用户");
        tvDownUserProbability.setText(mPlaceOrderBean.toBuy + "%用户");

        selectNumChange(1);

        cbVoucherCheck.setVisibility(mPlaceOrderBean.placeOrderType == 1 ? View.VISIBLE : View.INVISIBLE);
        cbYeCheck.setChecked(true);
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

        refreshPlaceOrderDialog(mPlaceOrderBean.stockIndex, mPlaceOrderBean.changeValue);

        setPrice();
        setDwPrice();
        setZsSeekbarProgress(vsZsSeekbar.getProgress());
        setZySeekbarProgress(vsZySeekbar.getProgress());

        //是否显示挂单布局
        llHangupLayout.setVisibility(mPlaceOrderBean.placeOrderType == 5 || mPlaceOrderBean.placeOrderType == 6 ? View.VISIBLE : View.GONE);

        if (mPlaceOrderBean.validRole) {
            vFollow.setVisibility(View.VISIBLE);
            tvFollow.setVisibility(View.VISIBLE);
            tvFollow.setSelected(true);
        } else {
            vFollow.setVisibility(View.GONE);
            tvFollow.setVisibility(View.GONE);
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
        tvPriceChange.setText("每浮动1点，盈亏" + NumberUtil.multiply(dPrice, mPlaceOrderBean.mrShouNum) + "元");
    }


    /**
     * 刷新数据
     *
     * @param stockIndex
     * @param changeValue
     */
    public void refreshPlaceOrderDialog(String stockIndex, String changeValue) {
        tvProductMarket.setText(stockIndex);
        double v = Double.parseDouble(changeValue);
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


    //选择 买涨 状态改变
    private void selectUpChange() {
        setBuyViewState(true);
        tvPlaceOrder.setText("买涨下单");
        llUpUserProbability.setSelected(true);
        tvUpUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.white_10));
        tvUpUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));

        llDownUserProbability.setSelected(false);
        tvDownUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        tvDownUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));

        //手数、元
        llPriceNum1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));
        llPriceNum2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));
        llPriceNum3.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_up_num));

        //浮动盈亏
        tvPriceChange.setTextColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
        tvPlaceOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_F74F54));
    }

    //选择 买跌 状态改变
    private void selectDownChange() {
        setBuyViewState(false);
        tvPlaceOrder.setText("买跌下单");
        llUpUserProbability.setSelected(false);
        tvUpUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
        tvUpUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_363030));

        llDownUserProbability.setSelected(true);
        tvDownUserProbability.setTextColor(ContextCompat.getColor(mContext, R.color.white_10));
        tvDownUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));

        //手数、元
        llPriceNum1.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));
        llPriceNum2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));
        llPriceNum3.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_place_order_down_num));

        //浮动盈亏
        tvPriceChange.setTextColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
        tvPlaceOrder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_1AC47A));
    }


    /**
     * 数量涨跌切换样式
     *
     * @param mBuyUp
     */
    public void setBuyViewState(boolean mBuyUp) {
        llExShou1.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou2.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou3.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou4.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou5.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou6.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou7.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou8.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou9.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
        llExShou10.setBackground(mBuyUp ? ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_up_num) : ContextCompat.getDrawable(getContext(), R.drawable.bg_place_order_down_num));
    }


    //默认 布局手数选择
    public void selectNumChange(int num) {
        mPlaceOrderBean.mrShouNum = num;
        if (llExShou1.isSelected()) {
            llExShou1.setSelected(false);
            tvExShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou2.isSelected()) {
            llExShou2.setSelected(false);
            tvExShou2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou3.isSelected()) {
            llExShou3.setSelected(false);
            tvExShou3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou4.isSelected()) {
            llExShou4.setSelected(false);
            tvExShou4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou5.isSelected()) {
            llExShou5.setSelected(false);
            tvExShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou6.isSelected()) {
            llExShou6.setSelected(false);
            tvExShou6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        if (llExShou7.isSelected()) {
            llExShou7.setSelected(false);
            tvExShou7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou8.isSelected()) {
            llExShou8.setSelected(false);
            tvExShou8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou9.isSelected()) {
            llExShou9.setSelected(false);
            tvExShou9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }
        if (llExShou10.isSelected()) {
            llExShou10.setSelected(false);
            tvExShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
            tvExShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b2b2b2));
        }

        switch (num) {
            case 1:
                llExShou1.setSelected(true);
                tvExShou1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint1.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 2:
                llExShou2.setSelected(true);
                tvExShou2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint2.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 3:
                llExShou3.setSelected(true);
                tvExShou3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint3.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 4:
                llExShou4.setSelected(true);
                tvExShou4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint4.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 5:
                llExShou5.setSelected(true);
                tvExShou5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint5.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 6:
                llExShou6.setSelected(true);
                tvExShou6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint6.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 7:
                llExShou7.setSelected(true);
                tvExShou7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint7.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 8:
                llExShou8.setSelected(true);
                tvExShou8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint8.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 9:
                llExShou9.setSelected(true);
                tvExShou9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint9.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
            case 10:
                llExShou10.setSelected(true);
                tvExShou10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363030));
                tvExShouHint10.setTextColor(ContextCompat.getColor(getContext(), R.color.color_828282));
                break;
        }
    }


    @Override
    public void bindHttpError() {
        stopLoading();
    }

    @Override
    public void bindSingleProduct(ProductListBean productBean) {
        if (productBean != null && productBean.items != null && productBean.items.size() >= 2) {
            mPlaceOrderBean.validRole = false;
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

            mPresenter.getReal(KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
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
            mPresenter.getSingleTime(KLineDataUtils.getProductCode(mPlaceOrderBean.productID));
        } else {
            bindHttpError();
        }
    }


    @Override
    public void bindSingleTime(ProductTimeLimitBean timeLimitBean) {
        if (timeLimitBean != null) {
            mPlaceOrderBean.toBuy = timeLimitBean.fall;
            mPlaceOrderBean.buyUp = timeLimitBean.rose;
        } else {
            mPlaceOrderBean.toBuy = 49;
            mPlaceOrderBean.buyUp = 51;
        }
        if (mPlaceOrderBean.placeOrderType == 1) {
            mPresenter.getValidRole();
        } else {
            mPresenter.getUserInfo(AppApplication.getConfig().getAt_token());
        }
    }


    //是不是牛人
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
            new AppHintDialog(mContext).showAppDialog(type);
        } else if (type == 0) {
            new OrderSuccDialog(mContext).showDialog(OrderSuccDialog.Type.CASH, score, couScore);
        } else {
            new OrderSuccDialog(mContext).showDialog(OrderSuccDialog.Type.TICKET);
        }
    }

    /**
     * 普通下单
     *
     * @param productID   产品id
     * @param buyState    true 买涨 false 买跌
     * @param selectGgNum 购买规格 1 2 3 （默认传1）
     * @param
     */
    public void placeOrder(String productID, boolean buyState, int selectGgNum) {
        mPlaceOrderBean.placeOrderType = 1;
        mPlaceOrderBean.mBuyState = buyState;
        mPlaceOrderBean.selectGgNum = selectGgNum;
        mPlaceOrderBean.productID = productID;
        mPlaceOrderBean.mrShouNum = 1;
        mPlaceOrderBean.productName = KLineDataUtils.getProductName(productID);
        mPresenter.getSingleProduct(productID);
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

}
