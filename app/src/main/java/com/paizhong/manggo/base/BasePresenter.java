package com.paizhong.manggo.base;

import android.content.Context;

import com.paizhong.manggo.bean.base.BaseResponse;
import com.paizhong.manggo.http.ActivityLifeCycleEvent;
import com.paizhong.manggo.http.HttpSubscriber;
import com.paizhong.manggo.http.RxHelper;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * des:基类presenter
 */
public abstract class BasePresenter<T extends BaseView> {

    protected Context mContext;
    protected T       mView;

    //销毁时退出异步任务
    protected CompositeSubscription mSubscriptions;

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    public BasePresenter(){
    }
    public BasePresenter(T mView){
        init(mView);
    }
    public void init(T v) {
        this.mView = v;
        this.onStart();
    }

    public void onStart() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
    }

    public void onDestroy() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()) {
            mSubscriptions.unsubscribe();
        }
    }

    /**
     * 添加线程管理并订阅
     * @param ob
     * @param subscriber
     */
    public void toSubscribe(Observable ob, HttpSubscriber subscriber) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        //数据预处理
        Observable.Transformer<BaseResponse<Object>, Object> result = RxHelper.handleResult(ActivityLifeCycleEvent
                .DESTROY, lifecycleSubject);
        Subscription subscription = ob.compose(result).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }


    //没有固定code 数据调用
    public void toSimpSubscribe(Observable ob, Subscriber subscriber) {
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        //数据预处理
        Observable.Transformer<Object, Object> result = RxHelper.handleSimplyResult(ActivityLifeCycleEvent
                .DESTROY, lifecycleSubject);
        Subscription subscription = ob.compose(result).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }


    /**
     * 上传图片封装
     * @param key
     * @param fileName
     * @return
     */
    public MultipartBody.Part putFile(String key, String fileName) {
        //构建要上传的文件
        File file = new File(fileName);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        return body;
    }

    /**
     * 默认获取数据的方法.不需要传参数
     */
    public void getData(){}


    /**
     * 默认获取数据的方法.不需要传参数
     */
    public void getDataT(){}
}
