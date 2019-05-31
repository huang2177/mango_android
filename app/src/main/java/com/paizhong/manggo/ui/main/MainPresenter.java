package com.paizhong.manggo.ui.main;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.utils.Logs;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    //app 更新
    @Override
    public void updateApp() {
        toSubscribe(HttpManager.getApi().updateApp(), new HttpSubscriber<UpdateAppBean>() {
            @Override
            protected void _onNext(UpdateAppBean updateAppBean) {
                super._onNext(updateAppBean);
                mView.bindUpdateApp(updateAppBean);
            }
        });
    }


    //是否抽奖
    @Override
    public void validIsUpPop() {
        toSubscribe(HttpManager.getApi().validIsUpPop(), new HttpSubscriber<Boolean>() {
            @Override
            protected void _onNext(Boolean b) {
                super._onNext(b);
                if (b){
                    mView.bindValidIsUpPop();
                }
            }
        });
    }
}
