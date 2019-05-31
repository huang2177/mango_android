package com.paizhong.manggo.ui.paycenter.qrcode;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.ui.paycenter.recharge.WeiXinDonate;
import com.paizhong.manggo.utils.ImageUtils;

import java.io.File;
import butterknife.BindView;

/**
 * 二维码页面
 * Created by zab on 2018/5/3 0003.
 */

public class QrCodeActivity extends BaseActivity<QrCodePresenter> implements QrCodeContract.View{
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_suc_pay)
    TextView tvSucPay;
    @BindView(R.id.tv_jump_wx)
    TextView tvJumpWx;

    private String mImagPath; //图片路径
    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    public void initPresenter() {
       mPresenter.init(QrCodeActivity.this);
    }

    @Override
    public Context getContenx() {
        return QrCodeActivity.this;
    }

    @Override
    public void loadData() {
        //mTitle.setTitle(false, R.color.color_ffffff, "扫码充值");
        mTitle.setTitle(true, "扫码充值");
        String mQrCode = getIntent().getStringExtra("qrCode");
        int amount = getIntent().getIntExtra("amount",0);
        //申请权限
        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, mListener);
        mPresenter.getQrCode(amount,mQrCode);
        tvSucPay.setEnabled(false);
        tvJumpWx.setEnabled(false);
        tvSucPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvJumpWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiXinDonate.gotoWeChatQrScan(QrCodeActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(mImagPath)){
            File file = new File(mImagPath);
            if (file.exists()){
                file.delete();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
            }
        }
        super.onDestroy();
    }

    @Override
    public void bindQrCode(String path) {
        this.mImagPath = path;
        ///storage/emulated/0/DCIM/Camera/juZiTaoJin_WX_Pay.png
        tvSucPay.setEnabled(true);
        tvJumpWx.setEnabled(true);
        if (!TextUtils.isEmpty(path)){
            File file = new File(path);
            ImageUtils.display(file,ivQrCode,R.mipmap.ic_wx_pay);
            //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
        }
    }
}
