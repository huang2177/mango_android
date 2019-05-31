package com.paizhong.manggo.dialog.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Des:建仓成功 Dialog
 * Created by huang on 2018/5/10 0010.
 */

public class OrderSuccDialog extends Dialog implements View.OnClickListener {
    private BaseActivity mActivity;

    private TextView tvStrategy;
    private TextView tvPosition;
    private LinearLayout llScore;

    private int score;
    private String allScore;
    private Type mType;

    public enum Type {
        CASH,
        TICKET,
        NEW_USER
    }

    public OrderSuccDialog(@NonNull Activity context) {
        super(context, R.style.translucent_theme);
        this.mActivity = (BaseActivity) context;
    }

    public void showDialog(Type type) {
        showDialog(type, 0, null);
    }

    public void showDialog(Type type, String allScore) {
        showDialog(type, 0, allScore);
    }

    public void showDialog(Type type, int score, String allScore) {
        this.mType = type;
        this.score = score;
        this.allScore = allScore;
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_succ);

        initWindow();
        initView();
        setListener();
    }

    private void initWindow() {
        setCanceledOnTouchOutside(true);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        if (mActivity.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            params.width = DeviceUtils.getScreenHeight(mActivity);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);
    }

    private void initView() {
        llScore = findViewById(R.id.ll_score);
        tvPosition = findViewById(R.id.tv_position);
        tvStrategy = findViewById(R.id.tv_strategy);

        switch (mType) {
            case CASH: //现金建仓
                llScore.setVisibility(View.VISIBLE);

                TextView title = findViewById(R.id.title);
                title.setPadding(0, 0, 0, 0);

                TextView tvGetScore = findViewById(R.id.tv_get_score);
                TextView tvAllScore = findViewById(R.id.tv_all_score);
                tvGetScore.setText(Html.fromHtml("交易获得  <font color = '#008EFF'>" + score + "</font>积分"));
                tvAllScore.setText(Html.fromHtml("现有积分  <font color = '#008EFF'>" + allScore + "</font>积分"));
                break;
            case TICKET: //代金券建仓
                break;
            case NEW_USER: //新用户代金券建仓
                tvStrategy.setText("100%赚钱攻略");
                tvStrategy.setBackgroundResource(R.drawable.bg_shape_r3_86a2b0);
                tvStrategy.setTextColor(ContextCompat.getColor(mActivity, R.color.color_ffffff));

                findViewById(R.id.tv_promise).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setListener() {
        llScore.setOnClickListener(this);
        tvStrategy.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_position:
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("page", MainActivity.TRADE_PAGE);
                intent.putExtra("smPage", 1);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_strategy:
                if (mType == Type.NEW_USER) {
                    Intent intentAbout = new Intent(mActivity, WebViewActivity.class);
                    intentAbout.putExtra("url", Constant.MAKE_MONEY);
                    intentAbout.putExtra("title", "新人1000元亏损包赔");
                    mActivity.startActivity(intentAbout);
                }
                break;
            case R.id.ll_score:
                Intent intent2 = new Intent(mActivity, WebViewActivity.class);
                intent2.putExtra("url", Constant.H5_SHOP_MALL);
                intent2.putExtra("title", "积分商城");
                intent2.putExtra("noTitle", true);
                mActivity.startActivity(intent2);
                break;
        }
        dismiss();
    }
}
