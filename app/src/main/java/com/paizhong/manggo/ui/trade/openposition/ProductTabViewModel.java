package com.paizhong.manggo.ui.trade.openposition;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.base.IBaseView;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.DinTextView;
import java.util.List;

/**
 * Created by zab on 2018/8/20 0020.
 */
public class ProductTabViewModel extends IBaseView<List<MarketHQBean>> implements View.OnClickListener{
    private MainActivity mActivity;
    private List<MarketHQBean> mMarketList;
    private String mClosePrice;
    private String mChckProductID = ViewConstant.PRODUCT_ID_AG;
    public boolean mIsProductMsg = false;
    private int mColorRed;
    private int mColorGreen;
    private int mColorNor;

    public HorizontalScrollView hsvScrollView;
    public View mView;
    private LinearLayout llProductLayout;
    private BasePresenter productPresenter;
    private DinTextView tvPriceNumPercenTage;
    private TextView tvProductTime;
    private DinTextView tvPriceThiOpen; //今开
    private DinTextView tvPriceZCollect;//昨收
    private DinTextView tvPriceHighest;//最高
    private DinTextView tvPriceMiniMum;//最低

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

    public String getClosePrice(){
        return mClosePrice;
    }

    public String getProductID() {
        return mChckProductID;
    }

    public ProductTabViewModel(MainActivity mActivity) {
        this.mActivity = mActivity;
        this.mColorRed = ContextCompat.getColor(mActivity,R.color.color_F74F54);
        this.mColorGreen = ContextCompat.getColor(mActivity,R.color.color_1AC47A);
        this.mColorNor = ContextCompat.getColor(mActivity,R.color.color_727272);
        this.mView = LayoutInflater.from(mActivity).inflate(R.layout.trade_view_model_product, null);
        hsvScrollView = mView.findViewById(R.id.hsv_scrollView);
        tvPriceNumPercenTage = mView.findViewById(R.id.tv_price_num_percentage);
        tvProductTime = mView.findViewById(R.id.tv_product_time);
        tvPriceThiOpen = mView.findViewById(R.id.tv_price_thi_open);
        tvPriceZCollect = mView.findViewById(R.id.tv_price_z_collect);
        tvPriceHighest = mView.findViewById(R.id.tv_price_highest);
        tvPriceMiniMum = mView.findViewById(R.id.tv_price_minimum);

        llProductLayout = mView.findViewById(R.id.ll_product_layout);

        llAgLayout = mView.findViewById(R.id.ll_ag_layout);
        //TextView tvAgName = mView.findViewById(R.id.tv_ag_name);
        tvAgXiu = mView.findViewById(R.id.tv_ag_xiu);
        tvAgMarket = mView.findViewById(R.id.tv_ag_market);
        ivAgState = mView.findViewById(R.id.iv_ag_state);
        vAgLine = mView.findViewById(R.id.v_ag_line);

        llCuLayout = mView.findViewById(R.id.ll_cu_layout);
        //TextView tvCuName = mView.findViewById(R.id.tv_cu_name);
        tvCuXiu = mView.findViewById(R.id.tv_cu_xiu);
        tvCuMarket = mView.findViewById(R.id.tv_cu_market);
        ivCuState = mView.findViewById(R.id.iv_cu_state);
        vCuLine = mView.findViewById(R.id.v_cu_line);

        llNiLayout = mView.findViewById(R.id.ll_ni_layout);
        //TextView tvNiName = mView.findViewById(R.id.tv_ni_name);
        tvNiXiu = mView.findViewById(R.id.tv_ni_xiu);
        tvNiMarket = mView.findViewById(R.id.tv_ni_market);
        ivNiState = mView.findViewById(R.id.iv_ni_state);
        vNiLine = mView.findViewById(R.id.v_ni_line);

        llZsLayout = mView.findViewById(R.id.ll_zs_layout);
        //TextView tvZsName = mView.findViewById(R.id.tv_zs_name);
        tvZsXiu = mView.findViewById(R.id.tv_zs_xiu);
        tvZsMarket = mView.findViewById(R.id.tv_zs_market);
        ivZsState = mView.findViewById(R.id.iv_zs_state);
        vZsLine = mView.findViewById(R.id.v_zs_line);

        llZwLayout = mView.findViewById(R.id.ll_zw_layout);
        //TextView tvZwName = mView.findViewById(R.id.tv_zw_name);
        tvZwXiu = mView.findViewById(R.id.tv_zw_xiu);
        tvZwMarket = mView.findViewById(R.id.tv_zw_market);
        ivZwState = mView.findViewById(R.id.iv_zw_state);
        vZwLine = mView.findViewById(R.id.v_zw_line);


        //GL玉米
        llZcLayout = mView.findViewById(R.id.ll_zc_layout);
        //TextView tvZcName = mView.findViewById(R.id.tv_zc_name);
        tvZcXiu = mView.findViewById(R.id.tv_zc_xiu);
        tvZcMarket = mView.findViewById(R.id.tv_zc_market);
        ivZcState = mView.findViewById(R.id.iv_zc_state);

        if (AppApplication.getConfig().mIsAuditing){
            llZsLayout.setVisibility(View.GONE);
            llZwLayout.setVisibility(View.GONE);
            llZcLayout.setVisibility(View.GONE);
            vZsLine.setVisibility(View.GONE);
            vZwLine.setVisibility(View.GONE);
        }
        llAgLayout.setOnClickListener(this);
        llCuLayout.setOnClickListener(this);
        llNiLayout.setOnClickListener(this);
        llZsLayout.setOnClickListener(this);
        llZwLayout.setOnClickListener(this);
        llZcLayout.setOnClickListener(this);
    }


    public void changeProduct(String productID ,boolean scroll){
        updateView(productID);
        if (mProductListener !=null){
            MarketHQBean marketBean = getMarketBean();
            if (marketBean !=null){
                OpenPositionFragment.getInstance().setNewLinePrice(Float.parseFloat(marketBean.point),Float.parseFloat(marketBean.change),marketBean.time);
                mProductListener.onProductLimit(marketBean.buyUp,marketBean.toBuy);
            }
            mProductListener.onProductSelect();
        }
        if (scroll){
            if (TextUtils.equals(productID,ViewConstant.PRODUCT_ID_AG)
                    || TextUtils.equals(productID,ViewConstant.PRODUCT_ID_CU)
                    || TextUtils.equals(productID,ViewConstant.PRODUCT_ID_NL)){
                hsvScrollView.arrowScroll(View.FOCUS_LEFT);
            }else {
                hsvScrollView.arrowScroll(View.FOCUS_RIGHT);
            }
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ll_ag_layout: //银
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_AG,false);
               }
               break;
           case R.id.ll_cu_layout: //铜
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_CU,false);
               }
               break;
           case R.id.ll_ni_layout: //镍
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_NL,false);
               }
               break;
           case R.id.ll_zs_layout: //大豆
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_ZS,false);
               }
               break;
           case R.id.ll_zw_layout: //小麦
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_ZW,false);
               }
               break;
           case R.id.ll_zc_layout: //玉米
               if (!TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,mChckProductID) && !DeviceUtils.isFastDoubleClick()){
                   changeProduct(ViewConstant.PRODUCT_ID_ZC,false);
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
                                 if (mMarketList !=null && mMarketList.size() > 0){
                                     for (MarketHQBean marketBean : mMarketList){

                                         for (MarketHQBean newBean : marketHQBeans){
                                             if (TextUtils.equals(marketBean.remark,newBean.remark)){
                                                 marketBean.point = newBean.point;
                                                 marketBean.change = newBean.change;
                                                 marketBean.changeRate = newBean.changeRate;
                                                 marketBean.open = newBean.open;
                                                 marketBean.close = newBean.close;
                                                 marketBean.high = newBean.high;
                                                 marketBean.low = newBean.low;
                                                 marketBean.time = newBean.time;
                                                 break;
                                             }
                                         }
                                     }
                                 }else {
                                     mMarketList = marketHQBeans;
                                 }
                                 if (!mIsProductMsg){
                                     mIsProductMsg = true;
                                     productPresenter.getDataT();
                                 }
                                 bindData(mMarketList);
                             }
                         }
                     });
                 }

                 @Override
                 public void getDataT() {
                       toSubscribe(HttpManager.getApi().getProductTime(true), new HttpSubscriber<List<ProductTimeLimitBean>>() {
                           @Override
                           protected void _onNext(List<ProductTimeLimitBean> productTimeLimitBeans) {
                                if (productTimeLimitBeans !=null
                                        && productTimeLimitBeans.size() > 0
                                        && mMarketList !=null
                                        && mMarketList.size() > 0){

                                    for (MarketHQBean marketBean : mMarketList){

                                        for (ProductTimeLimitBean timeLimitBean : productTimeLimitBeans){
                                            if (TextUtils.equals(marketBean.remark,timeLimitBean.id)){
                                                marketBean.buyUp = timeLimitBean.rose;
                                                marketBean.toBuy = timeLimitBean.fall;
                                                marketBean.timeStr = getTimsStr(marketBean.remark,timeLimitBean.startTime,timeLimitBean.endTime);
                                                break;
                                            }
                                        }

                                        if (TextUtils.equals(marketBean.remark,mChckProductID) && mProductListener !=null){
                                            mProductListener.onProductLimit(marketBean.buyUp,marketBean.toBuy);
                                        }
                                    }
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

        if (!TextUtils.isEmpty(marketBean.remark) && TextUtils.equals(marketBean.remark,mChckProductID)){
            this.mClosePrice = marketBean.close;
            tvPriceThiOpen.setText(marketBean.open);
            tvPriceZCollect.setText(marketBean.close);
            tvPriceHighest.setText(marketBean.high);
            tvPriceMiniMum.setText(marketBean.low);
            tvProductTime.setText(TextUtils.isEmpty(marketBean.timeStr) ? "---" : marketBean.timeStr);
            OpenPositionFragment.getInstance().setNewLinePrice(Float.parseFloat(marketBean.point),v,marketBean.time);
            
            if (v > 0){
                tvPriceNumPercenTage.setText("+"+marketBean.change +"  +"+marketBean.changeRate);
                tvPriceNumPercenTage.setTextColor(mColorRed);
            }else if (v < 0){
                tvPriceNumPercenTage.setText(marketBean.change +"  "+marketBean.changeRate);
                tvPriceNumPercenTage.setTextColor(mColorGreen);
            }else {
                tvPriceNumPercenTage.setText(marketBean.change +"  "+marketBean.changeRate);
                tvPriceNumPercenTage.setTextColor(mColorNor);
            }
            if (!mActivity.isPlaceOrderEmpty() && mActivity.placeOrder().isShowing()){
                mActivity.placeOrder().refreshPlaceOrderDialog(marketBean.point,marketBean.change);
            }
        }
    }



    private void updateView(String chckProductID){
       if (TextUtils.isEmpty(chckProductID) || TextUtils.equals(chckProductID,mChckProductID)){
           return;
       }

        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,mChckProductID)){//银
            llAgLayout.setBackgroundResource(0);
            vAgLine.setVisibility(View.VISIBLE);
            tvAgMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,mChckProductID)){//铜
            llCuLayout.setBackgroundResource(0);
            vCuLine.setVisibility(View.VISIBLE);
            vAgLine.setVisibility(View.VISIBLE);
            tvCuMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,mChckProductID)) {//镍
            llNiLayout.setBackgroundResource(0);
            vNiLine.setVisibility(View.VISIBLE);
            vCuLine.setVisibility(View.VISIBLE);
            tvNiMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15sp));
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,mChckProductID)){//大豆
            llZsLayout.setBackgroundResource(0);
            vZsLine.setVisibility(View.VISIBLE);
            vNiLine.setVisibility(View.VISIBLE);
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
            tvAgMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvAgMarket,ivAgState);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,chckProductID)){//铜
            llCuLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vCuLine.setVisibility(View.GONE);
            vAgLine.setVisibility(View.GONE);
            tvCuMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvCuMarket,ivCuState);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,chckProductID)) {//镍
            llNiLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vNiLine.setVisibility(View.GONE);
            vCuLine.setVisibility(View.GONE);
            tvNiMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvNiMarket,ivNiState);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,chckProductID)){//大豆
            llZsLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZsLine.setVisibility(View.GONE);
            vNiLine.setVisibility(View.GONE);
            tvZsMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvZsMarket,ivZsState);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,chckProductID)){//小麦
            llZwLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZwLine.setVisibility(View.GONE);
            vZsLine.setVisibility(View.GONE);
            tvZwMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvZwMarket,ivZwState);
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,chckProductID)){//玉米
            llZcLayout.setBackgroundResource(R.drawable.bg_trade_product);
            vZwLine.setVisibility(View.GONE);
            tvZcMarket.setTextSize(TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_24sp));
            this.mChckProductID = chckProductID;
            setData(tvZcMarket,ivZcState);
        }
    }


    private void setData(DinTextView market,ImageView state){
       if (mMarketList !=null && mMarketList.size() > 0){
           for (MarketHQBean marketBean : mMarketList){
               if (TextUtils.equals(mChckProductID,marketBean.remark)){
                   updateData(marketBean,true,market,state);
                   break;
               }
           }
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




    public MarketHQBean getMarketBean(){
        if (mMarketList !=null && mMarketList.size() >0){
            for (MarketHQBean marketHQBean : mMarketList){
                if (TextUtils.equals(mChckProductID,marketHQBean.remark)){
                    return marketHQBean;
                }
            }
        }
        return null;
    }


    public void setOnProductSelectListener(OnProductSelectListener listener){
        this.mProductListener = listener;
    }
    private OnProductSelectListener mProductListener;
    public interface  OnProductSelectListener{
         void onProductSelect();
         void onProductLimit(int profitLimit,int lossLimit);
    }

    public void onDestroy(){
      if (productPresenter !=null){
          productPresenter.onDestroy();
      }
    }



    private String getTimsStr(String id,String startTime,String endTime){
        try {
            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,id)  || TextUtils.equals(ViewConstant.PRODUCT_ID_CU,id) ||TextUtils.equals(ViewConstant.PRODUCT_ID_NL,id) ){
                return startTime + "-次日" + endTime;
            }else {
                return startTime +","+ endTime.substring(0,5) +"-次日" + endTime.substring(6,endTime.length());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
