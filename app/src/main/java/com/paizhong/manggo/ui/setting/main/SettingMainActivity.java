package com.paizhong.manggo.ui.setting.main;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.events.UserChangeEvent;
import com.paizhong.manggo.ui.setting.head.SettingHeadActivity;
import com.paizhong.manggo.ui.setting.name.SettingNameActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.SpUtil;
import com.paizhong.manggo.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

public class SettingMainActivity extends BaseActivity<SettingMainPresenter> implements SettingMainContract.View {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.rl_reset_pwd)
    RelativeLayout rlResetPwd;
    @BindView(R.id.tv_out_login)
    TextView tvOutLogin;

    private int mRequestType = 0; //0相册存储权限 1 相机存储权限
    public static final int PHOTO_PICKED_WITH_ALBUM = 10000;
    public static final int PHOTO_PICKED_WITH_CAMERA = 10001;
    public static final int NICKNAME_UPDATE = 10002;
    public static final int IMAGE_HEAD_UPDATE = 10003;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, "设置");
        ImageUtils.display(SpUtil.getString(Constant.USER_KEY_USERPIC),ivHead,R.mipmap.user_head_default,R.mipmap.user_head_default);

        String phone = SpUtil.getString(Constant.USER_KEY_PHONE);
        if (!TextUtils.isEmpty(phone)){
            tvPhoneNum.setText(phone.substring(0,3) +"****"+phone.substring(7,11));
        }
        String nickname = SpUtil.getString(Constant.USER_KEY_NICKNAME);
        if (!TextUtils.isEmpty(nickname)){
            tvNickname.setText(nickname);
        }

    }

    @OnClick({R.id.rl_reset_pwd, R.id.tv_out_login,R.id.rl_head,R.id.rl_nickname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_reset_pwd:
                //startActivity(new Intent(.this, ResetPwdActivity.class));
                break;
            case R.id.tv_out_login:
                if (AppApplication.getConfig().isLoginStatus() && !DeviceUtils.isFastDoubleClick()){
                    mPresenter.outLogin(AppApplication.getConfig().getMobilePhone());
                }
                break;
            case R.id.rl_head: //头像上传
                //0 没请求或者失败 1 牛人  2 不是牛人
                if (!DeviceUtils.isFastDoubleClick()){
                    if (mValidRole == 0){
                        mPresenter.getValidRole(true);
                    }else {
                        Intent intent = new Intent(SettingMainActivity.this,SettingHeadActivity.class);
                        intent.putExtra("validRole",(mValidRole == 1 ? true : false));
                        startActivityForResult(intent,IMAGE_HEAD_UPDATE);
                    }
                }
                break;
            case R.id.rl_nickname: //昵称
                if (!DeviceUtils.isFastDoubleClick()){
                    if (mValidRole == 0) {
                        mPresenter.getValidRole(true);
                    }else {
                        Intent intent = new Intent(SettingMainActivity.this, SettingNameActivity.class);
                        intent.putExtra("validRole",(mValidRole == 1 ? true : false));
                        startActivityForResult(intent,NICKNAME_UPDATE);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NICKNAME_UPDATE: //名字
                if (data !=null){
                    updateName(data.getStringExtra("nickName"));
                }
                break;
            case IMAGE_HEAD_UPDATE: //头像
                if (data !=null){
                    updateHeadPic(data.getStringExtra("headUrl"));
                }
                break;
        }
    }

    @Override
    public void updateHeadPic(String userPic) {
        if (!TextUtils.isEmpty(userPic)){
            SpUtil.putString(Constant.USER_KEY_USERPIC,userPic);
            ImageUtils.display(userPic,ivHead,R.mipmap.ic_user_head);
            EventBus.getDefault().post(new UserChangeEvent(0,AppApplication.getConfig().getMobilePhone(),userPic));
        }
    }

    public void updateName(String name){
        if (!TextUtils.isEmpty(name)){
            SpUtil.putString(Constant.USER_KEY_NICKNAME,name);
            tvNickname.setText(name);
            EventBus.getDefault().post(new UserChangeEvent(1,AppApplication.getConfig().getMobilePhone(),name));
        }
    }

    @Override
    public void outLogin() {
        EventBus.getDefault().post(new AppAccountEvent(0,null));
        finish();
    }

    //判断是不是牛人 0 没请求或者失败 1 牛人  2 不是牛人
    private int mValidRole = 0;
    @Override
    public void bindValidRole(boolean validRole) {
        this.mValidRole = (validRole ? 1 : 2);
    }



}
