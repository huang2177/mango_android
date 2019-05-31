package com.paizhong.manggo.ui.follow.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.follow.contract.ItemFollowPresenter;
import com.paizhong.manggo.ui.follow.follower.MyFollowerActivity;
import com.paizhong.manggo.ui.follow.module.adapter.IndicatorAdapter;
import com.paizhong.manggo.ui.follow.record.FollowRecordActivity;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.indicatordialog.IndicatorBuilder;
import com.paizhong.manggo.widget.indicatordialog.IndicatorDialog;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import butterknife.BindView;

/**
 * Des: 跟买广场 TitleBar Module
 * Created by huang on 2018/8/17 0017 11:26
 */
public class TitleBarModule extends BaseFollowModule<ItemFollowPresenter> implements View.OnClickListener
        , BaseRecyclerAdapter.OnItemClick {
    @BindView(R.id.iv_dialog)
    ImageView ivDialog;

    private int mValidRole = -1;
    private IndicatorDialog mDialog;


    public TitleBarModule(Context context) {
        this(context, null);
    }

    public TitleBarModule(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setVisibility(VISIBLE);
        setListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_follow_title_bar;
    }

    public void setListener() {
        ivDialog.setOnClickListener(this);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.getValidRole();
    }

    @Override
    public void bindValidRole(Boolean aBoolean) {
        this.mValidRole = aBoolean ? 1 : 2;
    }

    @Override
    public void onClick(View v) {
        if (mDialog == null) {
            mDialog = new IndicatorBuilder((Activity) mContext)
                    .width(DeviceUtils.dip2px(mContext, 100))
                    .height(-1)
                    .arrowDirection(IndicatorBuilder.TOP)
                    .bgColor(Color.parseColor("#90000000"))
                    .gravity(IndicatorBuilder.GRAVITY_RIGHT)
                    .arrowWidth(45)
                    .radius(8)
                    .adapter(new IndicatorAdapter(mContext))
                    .arrowRectage(0.81f)
                    .itemListener(this)
                    .create();
        }
        mDialog.show(v);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {

            case 0: //跟买记录
                if (!(mActivity).login()) {
                    return;
                }
                if (mValidRole == -1) {
                    onRefresh(false);
                    return;
                }
                //1 牛人  2 不是牛人
                Intent intent = new Intent(mContext, FollowRecordActivity.class);
                intent.putExtra("niu", mValidRole == 1);
                mContext.startActivity(intent);
                break;
            case 1: //我的关注
                if (!(mActivity).login()) {
                    return;
                }
                Intent intent1 = new Intent(mContext, MyFollowerActivity.class);
                mContext.startActivity(intent1);
                break;
            case 2: //跟买规则
                Intent intent2 = new Intent(mActivity, WebViewActivity.class);
                intent2.putExtra("url", Constant.H5_BUY_RULE);
                intent2.putExtra("title", "跟买规则");
                intent2.putExtra("noTitle", true);
                mActivity.startActivity(intent2);
                break;
        }
        mDialog.dismiss();
    }
}
