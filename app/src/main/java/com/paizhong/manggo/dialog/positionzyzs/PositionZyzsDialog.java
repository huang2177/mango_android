package com.paizhong.manggo.dialog.positionzyzs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.trade.AtBaseBean;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.widget.VibrateSeekBar;

/**
 * 交易 - 持仓 - 设置止盈止损
 * Created by zab on 2018/4/5 0005.
 */

public class PositionZyzsDialog extends BaseDialog<PositionZyzsPresenter> implements PositionZyzsContract.View{

    private View llZsNum;
    private View rlXsDianWei;
    private VibrateSeekBar mVsZsSeekbar;
    private TextView mTvZsNum;
    private TextView mTvZsDianWei;
    private TextView mTvZyNum;
    private VibrateSeekBar mVsZySeekbar;
    private TextView mTvZyDianWei;
    private TextView mTvCancel;
    private TextView mTvDetermine;
    private View llZyNum;
    private View rlZyDianWei;
    private TextView tvZyPrice;
    private TextView tvZsPrice;


    private String mOrderId;
    private String mVoucherID;
    private String mOrderNoID;
    private int mAmount;
    public PositionZyzsDialog(@NonNull BaseActivity context) {
        super(context, R.style.center_dialog);
    }

    @Override
    public void initPresenter() {
       mPresenter.init(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_position_zyzs_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);

        llZsNum = findViewById(R.id.ll_zs_num);
        rlXsDianWei = findViewById(R.id.rl_zs_dianwei);
        mTvZsNum = findViewById(R.id.tv_zs_num);
        mVsZsSeekbar = findViewById(R.id.vs_zs_seekbar);
        mTvZsDianWei = findViewById(R.id.tv_zs_dianwei);

        llZyNum = findViewById(R.id.ll_zy_num);
        rlZyDianWei = findViewById(R.id.rl_zy_dianwei);
        mTvZyNum = findViewById(R.id.tv_zy_num);
        mVsZySeekbar = findViewById(R.id.vs_zy_seekbar);
        mTvZyDianWei = findViewById(R.id.tv_zy_dianwei);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvDetermine = findViewById(R.id.tv_determine);

        tvZyPrice = (TextView)findViewById(R.id.tv_zy_price);
        tvZsPrice = (TextView)findViewById(R.id.tv_zs_price);

        mVsZsSeekbar.setProgressListener(new VibrateSeekBar.OnProgressListener() {
            @Override
            public void onStartTrackingTouch() {}
            @Override
            public void onStopTrackingTouch() {}
            @Override
            public void guessNumber(int number) {}

            @Override
            public void onProgress(int progress) {
                setZsSeekbarProgress(progress);
            }
        });

        mVsZySeekbar.setProgressListener(new VibrateSeekBar.OnProgressListener() {
            @Override
            public void onStartTrackingTouch() {}
            @Override
            public void onStopTrackingTouch() {}
            @Override
            public void guessNumber(int number) {}

            @Override
            public void onProgress(int progress) {
                setZySeekbarProgress(progress);
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mTvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                int profitLimit = mVsZySeekbar.getProgress() == 0 ? 200 :  mVsZySeekbar.getProgress() * 5;
                int lossLimit = mVsZsSeekbar.getProgress() == 0 ? 75 : mVsZsSeekbar.getProgress() * 5;
                mPresenter.getProfitLossLimit(AppApplication.getConfig().getAt_token(),AppApplication.getConfig().getAt_secret_access_key(),
                        mOrderId,profitLimit,lossLimit,mVoucherID,mOrderNoID);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.mOrderId = "";
        this.mVoucherID = "";
        this.mOrderNoID = "";
        this.mAmount = 0;
        onDestroy();
    }

    public void showPositionZyzsDialog(String orderId, String voucherID, String orderNoID ,int amount,int loss_limit ,int profit_limit){
        this.mOrderId = orderId;
        this.mVoucherID = !TextUtils.isEmpty(voucherID) ? voucherID : "";
        this.mOrderNoID = orderNoID;
        this.mAmount = amount;
        show();
        mVsZsSeekbar.setProgress(loss_limit / 5);
        mVsZySeekbar.setProgress(profit_limit /5);
        setZsSeekbarProgress(mVsZsSeekbar.getProgress());
        setZySeekbarProgress(mVsZySeekbar.getProgress());
    }


    private void setZsSeekbarProgress(int zsProgress){
        int zsNum = zsProgress * 5;
        mTvZsNum.setText((zsNum == 0 ? "不限" :zsNum+"%"));
        if (mAmount !=0){
            if (TextUtils.isEmpty(mVoucherID)){
                tvZsPrice.setText((zsNum == 0 ? "（"+ NumberUtil.multiply(mAmount,0.75)+"元）" : "（"+NumberUtil.multiply(mAmount, +NumberUtil.divide(zsNum,100))+"元）"));
            }else {
                tvZsPrice.setText("（0.0元）");
            }
        }else {
            tvZsPrice.setText("--元");
        }
    }

    private void setZySeekbarProgress(int zyProgress){
        int zyNum = zyProgress * 5;
        mTvZyNum.setText((zyNum == 0 ? "不限" : zyNum+"%"));
        if (mAmount !=0){
            tvZyPrice.setText((zyNum == 0 ? "（"+NumberUtil.multiply(mAmount, 2)+"元）" : "（"+NumberUtil.multiply(mAmount, NumberUtil.divide(zyNum,100))+"元）"));
        }else {
            tvZyPrice.setText("--元");
        }
    }

    @Override
    public void bindProfitLossLimit(AtBaseBean atBaseBean) {
        if (atBaseBean !=null && atBaseBean.isSuccess()){
             showErrorMsg("设置止盈止损成功",null);
             stopLoading();
             dismiss();
        }else {
             showErrorMsg(atBaseBean !=null ? atBaseBean.desc : "设置止盈止损失败",null);
        }
    }
}
