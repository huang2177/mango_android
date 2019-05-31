package com.paizhong.manggo.ui.paycenter.qrcode;

import android.graphics.BitmapFactory;
import android.os.Environment;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.utils.TimeUtil;
import com.paizhong.manggo.widget.zxing.MatrixToImageWriter;

import java.io.File;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by zab on 2018/4/4 0004.
 */

public class QrCodePresenter extends BasePresenter<QrCodeContract.View> implements QrCodeContract.Presenter{

    @Override
    public void getQrCode(final int amount,final String url) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //String pngName = "juZiTaoJin_WX_Pay.png";
                String pngName = TimeUtil.getStringByFormat(System.currentTimeMillis(),TimeUtil.dateFormat_path) + amount+"_Pay.png";
                final String galleryPath= Environment.getExternalStorageDirectory()
                        + File.separator + Environment.DIRECTORY_DCIM
                        +File.separator+"Camera"+File.separator;

                File file = new File(galleryPath);
                if (!file.exists()){
                    file.mkdirs();
                }
                int dimensionPixelSize = mView.getContenx().getResources().getDimensionPixelSize(R.dimen.dimen_150dp);
                boolean qrImage = MatrixToImageWriter.createQRImage(url, dimensionPixelSize, dimensionPixelSize
                        , BitmapFactory.decodeResource(mView.getContenx().getResources(), R.mipmap.ic_wx_pay), galleryPath+pngName);

                subscriber.onNext(galleryPath+pngName);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mView.showLoading("");
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopLoading();
                        mView.showErrorMsg(e.getMessage(),null);
                    }

                    @Override
                    public void onNext(String path) {
                        mView.stopLoading();
                        mView.bindQrCode(path);
                    }
                });
    }
}
