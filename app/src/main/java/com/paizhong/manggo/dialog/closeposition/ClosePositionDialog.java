package com.paizhong.manggo.dialog.closeposition;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.paizhong.manggo.widget.DinTextView;

import butterknife.BindView;

/**
 * 平仓
 * Created by zab on 2018/4/12 0012.
 */

public class ClosePositionDialog extends BaseDialog<ClosePositionPresenter> implements ClosePositionContract.View {

    TextView tvTitle;
    TextView tvVarieties;
    DinTextView tvNewPrice;
    DinTextView tvProfitLoss;
    TextView tvCancel;
    @BindView(R.id.tv_determine)
    TextView tvDetermine;

    private int mRedColor;
    private int mGreenColor;
    private int mBlackColor;

    public ClosePositionDialog(@NonNull BaseActivity context) {
        super(context, R.style.center_dialog);
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);
        this.mBlackColor = ContextCompat.getColor(mContext, R.color.color_333333);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_position_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);

        tvTitle = findViewById(R.id.tv_title);
        tvVarieties = findViewById(R.id.tv_varieties);
        tvNewPrice = findViewById(R.id.tv_new_price);
        tvProfitLoss = findViewById(R.id.tv_profit_loss);
        tvCancel =  findViewById(R.id.tv_cancel);
        tvDetermine =  findViewById(R.id.tv_determine);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                mPresenter.getTransfer(AppApplication.getConfig().getAt_token(), mOrderId,AppApplication.getConfig().getAt_secret_access_key());
            }
        });
    }


    private String mProductID;
    private String mOrderId;
    public String getProductID(){
        return  mProductID;
    }

    public String getOrderId(){
        return mOrderId;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        this.mProductID = "";
        this.mOrderId = "";
    }

    public void showDialog(String productID, String orderId, String name, String stockIndex, double floatingPrice){
        this.mProductID = productID;
        this.mOrderId = orderId;
        show();
        tvVarieties.setText(name);
        tvNewPrice.setText(stockIndex);
        tvProfitLoss.setText(String.valueOf(floatingPrice));
        if (floatingPrice > 0){
            tvProfitLoss.setTextColor(mRedColor);
        }else if (floatingPrice < 0){
            tvProfitLoss.setTextColor(mGreenColor);
        }else {
            tvProfitLoss.setTextColor(mBlackColor);
        }
    }

    public void refreshClosePositionDialog(String stockIndex,double floatingPrice){
        tvNewPrice.setText(stockIndex);
        tvProfitLoss.setText(String.valueOf(floatingPrice));
        if (floatingPrice > 0){
            tvProfitLoss.setTextColor(mRedColor);
        }else if (floatingPrice < 0){
            tvProfitLoss.setTextColor(mGreenColor);
        }else {
            tvProfitLoss.setTextColor(mBlackColor);
        }
    }

    @Override
    public void bindTransfer(AtBaseBean atBaseBean) {
        if (atBaseBean !=null && atBaseBean.isSuccess()){
             showErrorMsg("平仓成功",null);
             dismiss();
        }else {
            showErrorMsg(atBaseBean !=null ? atBaseBean.desc : "平仓失败",null);
        }
    }
}
