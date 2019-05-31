package com.paizhong.manggo.dialog.placeordermarket;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.base.IBaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.widget.DinTextView;

import java.util.List;

/**
 * Created by zab on 2018/8/20 0020.
 */
public class ProductTabViewMarketModel extends IBaseView<List<MarketHQBean>> implements View.OnClickListener{
    private MainActivity mActivity;
    private String mChckProductID = ViewConstant.PRODUCT_ID_AG;
    private int mColorRed;
    private int mColorGreen;
    private int mColorNor;

    public View mView;
    private BasePresenter productPresenter;


    //GL银
    private  View llAgLayout;
    private  TextView tvAgXiu;
    private  DinTextView tvAgMarket;
    private  ImageView ivAgState;
    private View vAgLine;

    //GL铜
    private  View llCuLayout;
    private  TextView tvCuXiu;
    private  DinTextView tvCuMarket;
    private  ImageView ivCuState;
    private View vCuLine;

    //GL镍
    private  View llNiLayout;
    private  TextView tvNiXiu;
    private  DinTextView tvNiMarket;
    private  ImageView ivNiState;
    private View vNiLine;

    //GL大豆
    private  View llZsLayout;
    private  TextView tvZsXiu;
    private  DinTextView tvZsMarket;
    private  ImageView ivZsState;
    private View vZsLine;

    //GL小麦
    private  View llZwLayout;
    private  TextView tvZwXiu;
    private  DinTextView tvZwMarket;
    private  ImageView ivZwState;
    private View vZwLine;

    //GL玉米
    private  View llZcLayout;
    private  TextView tvZcXiu;
    private  DinTextView tvZcMarket;
    private  ImageView ivZcState;


    public String getProductID() {
        return mChckProductID;
    }

    public String getStockIndex() {
        String mStockIndex = "0";
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mChckProductID)){//银
            mStockIndex = tvAgMarket.getText().toString();
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mChckProductID)){//铜
            mStockIndex = tvCuMarket.getText().toString();
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mChckProductID)) {//镍
            mStockIndex = tvNiMarket.getText().toString();
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mChckProductID)){//大豆
            mStockIndex = tvZsMarket.getText().toString();
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,mChckProductID)){//小麦
            mStockIndex = tvZwMarket.getText().toString();
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,mChckProductID)){//玉米
            mStockIndex = tvZcMarket.getText().toString();
        }
        return mStockIndex;
    }

    public ProductTabViewMarketModel(MainActivity mActivity) {
        this.mActivity = mActivity;
        this.mColorRed = ContextCompat.getColor(mActivity,R.color.color_F74F54);
        this.mColorGreen = ContextCompat.getColor(mActivity,R.color.color_1AC47A);
        this.mColorNor = ContextCompat.getColor(mActivity,R.color.color_727272);
        this.mView = LayoutInflater.from(mActivity).inflate(R.layout.trade_view_model_product_market, null);

        llAgLayout = mView.findViewById(R.id.ll_ag_layout);
        tvAgXiu = mView.findViewById(R.id.tv_ag_xiu);
        tvAgMarket = mView.findViewById(R.id.tv_ag_market);
        ivAgState = mView.findViewById(R.id.iv_ag_state);
        vAgLine = mView.findViewById(R.id.v_ag_line);

        llCuLayout = mView.findViewById(R.id.ll_cu_layout);
        tvCuXiu = mView.findViewById(R.id.tv_cu_xiu);
        tvCuMarket = mView.findViewById(R.id.tv_cu_market);
        ivCuState = mView.findViewById(R.id.iv_cu_state);
        vCuLine = mView.findViewById(R.id.v_cu_line);

        llNiLayout = mView.findViewById(R.id.ll_ni_layout);
        tvNiXiu = mView.findViewById(R.id.tv_ni_xiu);
        tvNiMarket = mView.findViewById(R.id.tv_ni_market);
        ivNiState = mView.findViewById(R.id.iv_ni_state);
        vNiLine = mView.findViewById(R.id.v_ni_line);

        llZsLayout = mView.findViewById(R.id.ll_zs_layout);
        tvZsXiu = mView.findViewById(R.id.tv_zs_xiu);
        tvZsMarket = mView.findViewById(R.id.tv_zs_market);
        ivZsState = mView.findViewById(R.id.iv_zs_state);
        vZsLine = mView.findViewById(R.id.v_zs_line);

        llZwLayout = mView.findViewById(R.id.ll_zw_layout);
        tvZwXiu = mView.findViewById(R.id.tv_zw_xiu);
        tvZwMarket = mView.findViewById(R.id.tv_zw_market);
        ivZwState = mView.findViewById(R.id.iv_zw_state);
        vZwLine = mView.findViewById(R.id.v_zw_line);


        //GL玉米
        llZcLayout = mView.findViewById(R.id.ll_zc_layout);
        tvZcXiu = mView.findViewById(R.id.tv_zc_xiu);
        tvZcMarket = mView.findViewById(R.id.tv_zc_market);
        ivZcState = mView.findViewById(R.id.iv_zc_state);

        llAgLayout.setOnClickListener(this);
        llCuLayout.setOnClickListener(this);
        llNiLayout.setOnClickListener(this);
        llZsLayout.setOnClickListener(this);
        llZwLayout.setOnClickListener(this);
        llZcLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ll_ag_layout: //银
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mChckProductID)
                       && !DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_AG);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_AG);
                   }
               }
               break;
           case R.id.ll_cu_layout: //铜
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mChckProductID)
                       &&!DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_CU);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_CU);
                   }
               }
               break;
           case R.id.ll_ni_layout: //镍
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mChckProductID)
                       &&!DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_NL);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_NL);
                   }
               }
               break;
           case R.id.ll_zs_layout: //大豆
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mChckProductID)
                       && !DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_ZS);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_ZS);
                   }
               }
               break;
           case R.id.ll_zw_layout: //小麦
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,mChckProductID)
                       &&!DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_ZW);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_ZW);
                   }
               }
               break;
           case R.id.ll_zc_layout: //玉米
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,mChckProductID)
                       &&!DeviceUtils.isFastDoubleClick()){
                   updateView(ViewConstant.PRODUCT_ID_ZC);
                   if (mProductListener !=null){
                       mProductListener.onProductSelect(ViewConstant.PRODUCT_ID_ZC);
                   }
               }
               break;
       }
    }

     public void refreshData(){
         if (productPresenter == null){
             productPresenter = new BasePresenter() {
                 @Override
                 public void getData() {
                     toSubscribe(HttpManager.getApi().getZJMarketList(true), new HttpSubscriber<List<MarketHQBean>>() {
                         @Override
                         protected void _onNext(List<MarketHQBean> marketHQBeans) {
                             super._onNext(marketHQBeans);
                             if (marketHQBeans !=null && marketHQBeans.size() > 0){
                                 bindData(marketHQBeans);
                             }
                         }
                     });
                 }

             };
         }
         productPresenter.getData();
     }

    @Override
    public void bindData(List<MarketHQBean> marketList) {
        for (MarketHQBean marketBean : marketList){
            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,marketBean.remark)){//银
                updateData(marketBean,false,tvAgMarket,ivAgState);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,marketBean.remark)){//铜
                updateData(marketBean,false,tvCuMarket,ivCuState);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,marketBean.remark)) {//镍
                updateData(marketBean,false,tvNiMarket,ivNiState);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,marketBean.remark)){//大豆
                updateData(marketBean,false,tvZsMarket,ivZsState);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,marketBean.remark)){//小麦
                updateData(marketBean,false,tvZwMarket,ivZwState);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,marketBean.remark)){//玉米
                updateData(marketBean,false,tvZcMarket,ivZcState);
            }
        }
    }


    private void updateData(MarketHQBean marketBean ,boolean onClick,DinTextView market,ImageView state){
        float v = Float.parseFloat(marketBean.change);
        if (!onClick){
            market.setText(marketBean.point);
            if (v > 0){
                market.setTextColor(mColorRed);
                state.setVisibility(View.VISIBLE);
                state.setImageResource(R.mipmap.ic_up_red);
            }else if (v < 0){
                market.setTextColor(mColorGreen);
                state.setVisibility(View.VISIBLE);
                state.setImageResource(R.mipmap.ic_down_green);
            }else {
                market.setTextColor(mColorNor);
                state.setVisibility(View.INVISIBLE);
            }
        }
    }



    public void updateView(String chckProductID){
       if (TextUtils.isEmpty(chckProductID) || TextUtils.equals(chckProductID,mChckProductID)){
           return;
       }

        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mChckProductID)){//银
            llAgLayout.setBackgroundResource(0);
            vAgLine.setVisibility(View.VISIBLE);
            vCuLine.setVisibility(View.VISIBLE);
            tvAgMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mChckProductID)){//铜
            llCuLayout.setBackgroundResource(0);
            vCuLine.setVisibility(View.VISIBLE);
            vNiLine.setVisibility(View.VISIBLE);
            tvCuMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mChckProductID)) {//镍
            llNiLayout.setBackgroundResource(0);
            vNiLine.setVisibility(View.VISIBLE);
            tvNiMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mChckProductID)){//大豆
            llZsLayout.setBackgroundResource(0);
            vZsLine.setVisibility(View.VISIBLE);
            vAgLine.setVisibility(View.VISIBLE);
            tvZsMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,mChckProductID)){//小麦
            llZwLayout.setBackgroundResource(0);
            vZwLine.setVisibility(View.VISIBLE);
            vZsLine.setVisibility(View.VISIBLE);
            tvZwMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,mChckProductID)){//玉米
            llZcLayout.setBackgroundResource(0);
            vZsLine.setVisibility(View.VISIBLE);
            tvZcMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }

        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,chckProductID)){//银
            llAgLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vAgLine.setVisibility(View.GONE);
            vCuLine.setVisibility(View.GONE);
            tvAgMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,chckProductID)){//铜
            llCuLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vCuLine.setVisibility(View.GONE);
            vNiLine.setVisibility(View.GONE);
            tvCuMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,chckProductID)) {//镍
            llNiLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vNiLine.setVisibility(View.GONE);
            tvNiMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,chckProductID)){//大豆
            llZsLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZsLine.setVisibility(View.GONE);
            vAgLine.setVisibility(View.GONE);
            tvZsMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,chckProductID)){//小麦
            llZwLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZwLine.setVisibility(View.GONE);
            vZsLine.setVisibility(View.GONE);
            tvZwMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,chckProductID)){//玉米
            llZcLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZwLine.setVisibility(View.GONE);
            tvZcMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
        }
    }




    //显示休市状态
    public void setProductList(List<ProductListBean> productList){
        for (ProductListBean productBean : productList){
            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,productBean.id)){//银
                tvAgXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,productBean.id)){//铜
                tvCuXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,productBean.id)) {//镍
                tvNiXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,productBean.id)){//大豆
                tvZsXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,productBean.id)){//小麦
                tvZwXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,productBean.id)){//玉米
                tvZcXiu.setVisibility(productBean.isClose() ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }

    public void onDestroy(){
      if (productPresenter !=null){
          productPresenter.onDestroy();
      }
    }

    public void setOnProductSelectListener(OnProductSelectListener listener){
        this.mProductListener = listener;
    }
    private OnProductSelectListener mProductListener;
    public interface  OnProductSelectListener{
        void onProductSelect(String productID);
    }
}
