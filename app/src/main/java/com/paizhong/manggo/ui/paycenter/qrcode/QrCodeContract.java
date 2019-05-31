package com.paizhong.manggo.ui.paycenter.qrcode;

import android.content.Context;

import com.paizhong.manggo.base.BaseView;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface QrCodeContract {

    interface View extends BaseView {
        void bindQrCode(String path);
        Context getContenx();
    }
    interface Presenter {
       void getQrCode(int amount, String url);
    }
}
