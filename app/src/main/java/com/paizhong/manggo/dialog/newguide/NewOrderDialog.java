package com.paizhong.manggo.dialog.newguide;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.dialog.other.OrderSuccDialog;
import com.paizhong.manggo.events.NewGuideEvent;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NetUtil;
import com.paizhong.manggo.utils.ViewHelper;
import com.paizhong.manggo.widget.DinTextView;

import org.greenrobot.eventbus.EventBus;

/**
 * 新用户下单 Dialog
 * Created by zab on 2018/5/24 0024.
 */

public class NewOrderDialog extends BaseDialog<NewGuidePresenter> implements NewGuideContract.View, View.OnClickListener {

    private DinTextView tvDw;
    private View llUpUserView;
    private TextView tvUpUser;
    private TextView tvUpUserText;
    private View llDownUserView;
    private TextView tvDownUser;
    private TextView tvDownUserText;
    private ImageView ivTopImg;

    private int downColor;
    private int upColor;
    private int pColor;

    public NewOrderDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
        this.downColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
        this.upColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.pColor = ContextCompat.getColor(mContext, R.color.color_363030);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        stopRun();
        ViewHelper.recycleImageView(ivTopImg);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_order);
        setCanceledOnTouchOutside(true);

        Window mWindow = getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        ivTopImg = findViewById(R.id.iv_top_img);
        ViewHelper.setImageView(ivTopImg, mContext.getResources(), R.mipmap.ic_new_top_img);
        tvDw = (DinTextView) findViewById(R.id.tv_dw);

        llUpUserView = findViewById(R.id.ll_up_user_probability);
        tvUpUser = (TextView) findViewById(R.id.tv_up_user);
        tvUpUserText = (TextView) findViewById(R.id.tv_up_user_probability);

        llDownUserView = findViewById(R.id.ll_down_user_probability);
        tvDownUser = (TextView) findViewById(R.id.tv_down_user);
        tvDownUserText = (TextView) findViewById(R.id.tv_down_user_probability);

        TextView tvPlaceOrder = (TextView) findViewById(R.id.tv_place_order);
        TextView tvJumpWeb = (TextView) findViewById(R.id.tv_jump_web);

        changeViewState(true);

        tvJumpWeb.setOnClickListener(this);
        llUpUserView.setOnClickListener(this);
        tvPlaceOrder.setOnClickListener(this);
        llDownUserView.setOnClickListener(this);
    }

    private String mVoucherId;//代金券ID
    private int mGgProductId;//产品规格ID
    private String mProductCode; //产品ID

    public void showDialog() {
        mContext.showLoading("");
        mProductCode = ViewConstant.PRODUCT_ID_AG;

        mPresenter.getSingleProduct(mProductCode);
        mPresenter.getNewUserTicket(AppApplication.getConfig().getAt_token());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_up_user_probability:
                changeViewState(true);
                break;
            case R.id.ll_down_user_probability:
                changeViewState(false);
                break;
            case R.id.tv_place_order:
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!NetUtil.isConnected()) {
                    showErrorMsg("网络不可用,请检查网络设置", null);
                    return;
                }
                if (TextUtils.isEmpty(mVoucherId)) {
                    mPresenter.getNewUserTicket(AppApplication.getConfig().getAt_token());
                } else {
                    int flag = llUpUserView.isSelected() ? 0 : 1;
                    String code = KLineDataUtils.getProductCode(mProductCode);
                    String productName = KLineDataUtils.getProductName(ViewConstant.PRODUCT_ID_AG);
                    mPresenter.getVoucherPlaceOrder(AppApplication.getConfig().getAt_token(), mProductCode, AppApplication.getConfig().getAt_secret_access_key()
                            , String.valueOf(mGgProductId), 1, flag, 5, 5, mVoucherId, 100, productName, 0, code);
                }
                break;
        }
    }

    private void changeViewState(boolean buyUp) {
        if (buyUp) {
            llUpUserView.setSelected(true);
            tvUpUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            tvUpUserText.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));

            llDownUserView.setSelected(false);
            tvDownUser.setTextColor(downColor);
            tvDownUserText.setTextColor(downColor);
        } else {
            llUpUserView.setSelected(false);
            tvUpUser.setTextColor(upColor);
            tvUpUserText.setTextColor(upColor);

            llDownUserView.setSelected(true);
            tvDownUser.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            tvDownUserText.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
        }
    }

    /***
     * 单个产品的规格Id
     */
    @Override
    public void bindSingleProduct(int proId) {
        mGgProductId = proId;
    }

    @Override
    public void bindNewUser() {
        show();
        startRun();
    }

    /***
     * 获取新手券Id
     */
    @Override
    public void bindNewUserTicketId(String voucherId) {
        mVoucherId = voucherId;
    }

    /***
     * 单个产品行情信息
     */
    @Override
    public void bindSingleMarket(MarketHQBean marketHQBean) {
        if (!isShowing()) {
            return;
        }
        double v = Double.parseDouble(marketHQBean.change);
        tvDw.setText(marketHQBean.point);
        tvDw.setTextColor(v > 0 ? upColor : v < 0 ? downColor : pColor);

    }

    @Override
    public void bindProductError() {
        mContext.stopLoading();
    }


    /***
     * 代金券建仓成功
     */
    @Override
    public void bindCashPlaceOrder(CashPlaceOrderBean placeOrderBean) {
        new OrderSuccDialog(mContext).showDialog(OrderSuccDialog.Type.TICKET);
        EventBus.getDefault().post(new NewGuideEvent(1));
        dismiss();
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getSingleMarket(KLineDataUtils.getProductCode(mProductCode));
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void startRun() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    private void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }
}
