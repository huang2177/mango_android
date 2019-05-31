package com.paizhong.manggo.dialog.position;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;
import com.paizhong.manggo.ui.kchart.utils.KLineDataUtils;
import com.paizhong.manggo.utils.DeviceUtils;

import java.util.List;


/**
 * Created by zab on 2018/6/19 0019.
 */
public class PositionDialog extends BaseDialog<PositionPresenter> implements PositionContract.View{

    private View llRootView;
    private TextView tvName;
    private TextView tvDw;
    private TextView tvwPriceNum;
    private TextView tvPricePercentage;
    private TextView tvAllSelect;
    private RecyclerView recyclerView;
    private TextView tvCancel;
    private TextView tvClosePosition;

    private String mProductID;
    private PositionDgAdapter dgAdapter;


    public PositionDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
    }

    @Override
    public void initPresenter() {
       mPresenter.init(this);
    }

    public String getProductID() {
        return mProductID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_position);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        llRootView = findViewById(R.id.ll_root_view);
        tvName = findViewById(R.id.tv_name);
        tvDw = findViewById(R.id.tv_dw);
        tvwPriceNum = findViewById(R.id.tvw_price_num);
        tvPricePercentage = findViewById(R.id.tv_price_percentage);
        tvAllSelect = findViewById(R.id.tv_all_select);
        recyclerView = findViewById(R.id.recyclerView);
        tvCancel = findViewById(R.id.tv_cancel);
        tvClosePosition = findViewById(R.id.tv_close_position);


        tvName.setText(KLineDataUtils.getProductName(mProductID));
        dgAdapter = new PositionDgAdapter(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dgAdapter);

        dgAdapter.setCheckChangeListener(new PositionDgAdapter.OnCheckChangeListener() {
            @Override
            public void onCheckChange() {
                tvAllSelect.setSelected(false);
            }
        });

        tvAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (dgAdapter.getList() !=null && dgAdapter.getList().size() > 0){
                    if (tvAllSelect.isSelected()){
                        tvAllSelect.setSelected(false);
                        dgAdapter.getCheckMap().clear();
                    }else {
                        tvAllSelect.setSelected(true);
                    }
                    boolean tvSelect =tvAllSelect.isSelected();
                    for (PositionListBean zjBean: dgAdapter.getList()){
                        zjBean.check = tvSelect;
                        if (tvSelect){
                            dgAdapter.getCheckMap().put(zjBean.id,true);
                        }
                    }
                    dgAdapter.notifyDataSetChanged();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //快速平仓
        tvClosePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (DeviceUtils.isFastDoubleClick()){
                   return;
               }
               StringBuilder builder = new StringBuilder();
               if (dgAdapter !=null&& dgAdapter.getList() !=null&& dgAdapter.getList().size() > 0){
                   for (PositionListBean zjBean: dgAdapter.getList()){
                       if (zjBean.check){
                           if (builder.length() == 0){
                               builder.append("[");
                           }else {
                               builder.append(",");
                           }
                           builder.append(zjBean.id);
                       }
                   }
                   if (builder.length() > 0){
                       builder.append("]");
                       mPresenter.getTransferList(AppApplication.getConfig().getAt_token(),builder.toString(),AppApplication.getConfig().getAt_secret_access_key());
                   }else {
                       showErrorMsg("请选择需要平仓的订单",null);
                   }
               }
            }
        });
    }


    public void showDialog(String mProductID){
        this.mProductID = mProductID;
        show();
        startRun();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.mProductID = "";
        stopRun();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getSignOrderList(AppApplication.getConfig().getAt_token(),mProductID);
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void startRun(){
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopRun(){
        mHandler.removeCallbacks(mRunnable);
    }


    public void refreshDate(String stockIndex,String changeValue,String changeRange){
        //0 跌  1涨
        double v = Double.parseDouble(changeValue);
        int color ;
        if (v  > 0){
            color = ContextCompat.getColor(mContext,R.color.color_F74F54);
            tvwPriceNum.setText("+"+changeValue);
            tvPricePercentage.setText("+"+changeRange);
        }else if (v < 0){
            color = ContextCompat.getColor(mContext,R.color.color_1AC47A);
            tvwPriceNum.setText(changeValue);
            tvPricePercentage.setText(changeRange);
        }else {
            color = ContextCompat.getColor(mContext,R.color.color_333333);
            tvwPriceNum.setText(changeValue);
            tvPricePercentage.setText(changeRange);
        }
        tvDw.setText(stockIndex);
        tvDw.setTextColor(color);
        tvwPriceNum.setTextColor(color);
        tvPricePercentage.setTextColor(color);
    }



    @Override
    public void bindOrderList(List<PositionListBean> orderZjList) {
        if (orderZjList == null || orderZjList.size() == 0){
            stopRun();
            tvAllSelect.setSelected(false);
        }
        dgAdapter.notifyDataChanged(orderZjList);
    }


    @Override
    public boolean getCheck(String key) {
        return  tvAllSelect.isSelected() ? true :(dgAdapter !=null ? dgAdapter.getCheck(key) : false);
    }

    //批量平仓
    @Override
    public void bindTransferList(ZJBaseBean bean) {
        if (bean !=null && bean.isSuccess()){
            showErrorMsg("平仓成功",null);
            dismiss();
        }else {
            showErrorMsg(bean !=null ? bean.desc : "平仓失败",null);
        }
    }
}
