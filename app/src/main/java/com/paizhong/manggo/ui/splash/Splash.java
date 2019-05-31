package com.paizhong.manggo.ui.splash;

import com.paizhong.manggo.bean.base.BaseResponse;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.utils.SpUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zab on 2018/11/13 0013.
 */
public class Splash {

    public void getSplash(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Observable<BaseResponse<List<BannerBean>>> baseResponseObservable = HttpManager.getApi().selectBanner("10");
                        baseResponseObservable.subscribe(new Action1<BaseResponse<List<BannerBean>>>() {
                            @Override
                            public void call(BaseResponse<List<BannerBean>> listBase) {
                                if (listBase !=null && listBase.data !=null && listBase.data.size() >0){
                                    SpUtil.putString(Constant.USER_KEY_SPLASH,listBase.data.get(0).activityImageUrl);
                                    SpUtil.putString(Constant.USER_KEY_SPLASH_URL,listBase.data.get(0).activityHtml5Url);
                                    SpUtil.putBoolean(Constant.USER_KEY_SPLASH_HEAD,listBase.data.get(0).isNeedHead == 1);
                                    SpUtil.putString(Constant.USER_KEY_SPLASH_NAME,listBase.data.get(0).activityName);
                                    SpUtil.putString(Constant.USER_KEY_SPLASH_SERVERSION,listBase.data.get(0).detail);
                                }else {
                                    SpUtil.remove(Constant.USER_KEY_SPLASH);
                                    SpUtil.remove(Constant.USER_KEY_SPLASH_URL);
                                    SpUtil.remove(Constant.USER_KEY_SPLASH_HEAD);
                                    SpUtil.remove(Constant.USER_KEY_SPLASH_NAME);
                                    SpUtil.remove(Constant.USER_KEY_SPLASH_SERVERSION);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        try {
                            SpUtil.remove(Constant.USER_KEY_SPLASH);
                            SpUtil.remove(Constant.USER_KEY_SPLASH_URL);
                            SpUtil.remove(Constant.USER_KEY_SPLASH_HEAD);
                            SpUtil.remove(Constant.USER_KEY_SPLASH_NAME);
                            SpUtil.remove(Constant.USER_KEY_SPLASH_SERVERSION);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e2){
            e2.printStackTrace();
        }
    }
}
