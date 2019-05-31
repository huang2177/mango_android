package com.paizhong.manggo.ui.setting.head;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.other.HeadImageBean;
import com.paizhong.manggo.ui.setting.main.SettingMainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.DrawableCenterTextView;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 更改头像
 * Created by zab on 2018/6/11 0011.
 */

public class SettingHeadActivity extends BaseActivity<SettingHeadPresenter> implements SettingHeadContract.View {
    @BindView(R.id.tv_left)
    DrawableCenterTextView mLeft;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_save)
    TextView mSave;
    @BindView(R.id.rv_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh)
    FixRefreshLayout refreshLayout;


    private SetHeadAdapter mSetHeadAdapter;
    private Boolean mValidRole;

    @Override
    public int getLayoutId() {
        return R.layout.activity_settinghead;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mValidRole = getIntent().getBooleanExtra("validRole",false);
        Drawable left = ContextCompat.getDrawable(this, R.mipmap.ic_left_arrow);
        left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        mLeft.setCompoundDrawables(left, null, null, null);

        GridLayoutManager layoutManager = new GridLayoutManager(SettingHeadActivity.this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mSetHeadAdapter = new SetHeadAdapter(SettingHeadActivity.this);
        mRecyclerView.setAdapter(mSetHeadAdapter);

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(mSetHeadAdapter.mImageUrl)) {
                    showErrorMsg("请选择头像", null);
                    return;
                }
                //1：牛人   0：不是牛人
                mPresenter.updateHeadPic(AppApplication.getConfig().getUserId(), mSetHeadAdapter.mImageUrl,mValidRole ? 1 : 0,AppApplication.getConfig().getMobilePhone());
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getImageList(false, AppApplication.getConfig().getMobilePhone());
                refreshLayout.finishRefresh();
            }
        });

        mPresenter.getImageList(true, AppApplication.getConfig().getMobilePhone());
    }


    @Override
    public void bindImageList(List<HeadImageBean> images) {
        mSetHeadAdapter.notifyDataChanged(images);
    }


    @Override
    public void bindUpdateHeadPic(String userPic) {
        if (!TextUtils.isEmpty(userPic)){
            Intent intent = new Intent();
            intent.putExtra("headUrl",userPic);
            setResult(SettingMainActivity.IMAGE_HEAD_UPDATE,intent);
            finish();
        }
    }
}
