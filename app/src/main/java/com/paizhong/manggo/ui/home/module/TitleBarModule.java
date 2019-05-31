package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SobotSdkUtils;

import butterknife.BindView;

/**
 * Des: 首页TitleBar Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class TitleBarModule extends BaseHomeModule<ItemHomePresenter> implements View.OnClickListener {
    @BindView(R.id.tv_service)
    TextView mTvService;
    @BindView(R.id.tv_course)
    TextView mTvCourse;

    public TitleBarModule(Context context) {
        this(context, null);
    }

    public TitleBarModule(Context context, @Nullable AttributeSet attr) {
        super(context, attr);
        setListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_title_bar;
    }

    public void setListener() {
        mTvCourse.setOnClickListener(this);
        mTvService.setOnClickListener(this);
    }

    @Override
    public void setAuditing(boolean auditing) {
        mTvCourse.setVisibility(auditing ? GONE : VISIBLE);
        mTvService.setVisibility(auditing ? GONE : VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_course:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_NEWSCHOOL);
                intent.putExtra("title", "新手教程");
                intent.putExtra("noTitle", true);
                intent.putExtra("builderPar", false);
                mContext.startActivity(intent);
                break;
            case R.id.tv_service:
                SobotSdkUtils.startKeFu(mContext);
                break;
        }
    }
}
