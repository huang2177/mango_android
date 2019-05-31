package com.paizhong.manggo.dialog.market;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.market.MarketHQBean;
import java.util.List;

/**
 * 行情
 * Created by zab on 2018/9/4 0004.
 */
public class MarketDialog extends BaseDialog<MarketPresenter> implements MarketContract.View{

    private ImageView ivClose;
    private TextView tvClose;
    private View llMarketRootLayout;
    private MarketAdapter marketAdapter;

    public MarketDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme_left);
    }

    @Override
    public void initPresenter() {
       mPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_market_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        setCanceledOnTouchOutside(true);
        DisplayMetrics dm = new DisplayMetrics();
        manage.getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels /3;
        params.height = dm.heightPixels;
        params.gravity = Gravity.LEFT;
        mWindow.setAttributes(params);

        llMarketRootLayout = findViewById(R.id.ll_market_root_layout);
        View llClose = findViewById(R.id.ll_close);
        ivClose = findViewById(R.id.iv_close);
        tvClose = findViewById(R.id.tv_close);
        RecyclerView rvRecyclerView = findViewById(R.id.rv_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecyclerView.setLayoutManager(layoutManager);
        marketAdapter = new MarketAdapter(mContext);
        rvRecyclerView.setAdapter(marketAdapter);

        marketAdapter.setOnItemListener(new MarketAdapter.OnItemListener() {
            @Override
            public void onOnItem() {
                dismiss();
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.getMarketList();
            mHandler.postDelayed(this, 1000);
        }
    };

    public void startRuan() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopRun() {
        mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        stopRun();
    }


    public void showMarket(String productID){
        show();
        marketAdapter.setProductID(productID);
        if (AppApplication.getConfig().getYeStyle()){
            tvClose.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            ivClose.setImageResource(R.mipmap.ic_left_arrow_white);
            llMarketRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_27252D));
        }else {
            tvClose.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            ivClose.setImageResource(R.mipmap.ic_left_arrow);
            llMarketRootLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
        }
        startRuan();
    }


    @Override
    public void bindMarketList(List<MarketHQBean> marketList) {
        marketAdapter.notifyDataChanged(marketList);
    }
}
